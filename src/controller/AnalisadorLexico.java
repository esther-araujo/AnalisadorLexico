/*
Autores: Cleyton Almeida da Silva e Esther de Santana Araújo
Componente Curricular: MI - Processadores de Linguagens de Programação
Concluido em: 12/03/2021
Declaramos que este código foi elaborado por nós de forma "individual" e não contém nenhum
trecho de código de outro colega ou de outro autor, tais como provindos de livros e
apostilas, e páginas ou documentos eletrônicos da Internet. Qualquer trecho de código
de outra autoria que não a nossa está destacado com uma citação para o autor e a fonte
do código, e estamos ciente que estes trechos não serão considerados para fins de avaliação.
 */

package controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import javafx.util.Pair;
import model.Arquivo;
import model.Token;
import util.ReconhecedorCaracteres;
import util.SemEntradasException;

/**
 *
 * @author esther
 */
public class AnalisadorLexico {
    private String linha; //Guarda a linha atual.
    private String lexema; //Guarda o lexema atual, antes de gerado um token.
    //
    private static final HashSet<String> palavrasReservadas = new HashSet();
    private static final HashSet<String> delimitadores = new HashSet();
	private final ArrayList<Token> tokens;
    
	int estado = 0;
    int token = 0;
    boolean fimCodigo = false;
    private String analiseRet = "";

    public AnalisadorLexico() throws SemEntradasException {
        palavrasReservadas.add("var");
        palavrasReservadas.add("const");
        palavrasReservadas.add("typedef");
        palavrasReservadas.add("struct");
        palavrasReservadas.add("extends");
        palavrasReservadas.add("procedure");
        palavrasReservadas.add("function");
        palavrasReservadas.add("start");
        palavrasReservadas.add("return");
        palavrasReservadas.add("if");
        palavrasReservadas.add("else");
        palavrasReservadas.add("then");
        palavrasReservadas.add("while");
        palavrasReservadas.add("read");
        palavrasReservadas.add("print");
        palavrasReservadas.add("int");
        palavrasReservadas.add("real");
        palavrasReservadas.add("boolean");
        palavrasReservadas.add("string");
        palavrasReservadas.add("true");
        palavrasReservadas.add("false");
        palavrasReservadas.add("global");
        palavrasReservadas.add("local");
        delimitadores.add(";");
        delimitadores.add(",");
        delimitadores.add("(");
        delimitadores.add(")");
        delimitadores.add("[");
        delimitadores.add("]");
        delimitadores.add("{");
        delimitadores.add("}");
        delimitadores.add(".");
        tokens = new ArrayList();
    }

    /**
     * Efetua a análise de arquivo que contém o código fonte de entrada.
     * @param arquivo arquivo a ser analisado.
     * @return 
     */
	public Pair<ArrayList, String> analise(Arquivo arquivo) {
        tokens.clear();
        Iterator linhas = arquivo.getConteudo();
        boolean inComment = false;//flag para indicar se a leitura está dentro de um comentario de bloco
        analiseRet="";
        int line = 0;//Carrega o número da linha do arquivo em que está a análise.
        int lineComment = 0;//Guarda a linha em que há um comentário de bloco (necessário caso seja mal formado).
        //percorrendo as linhas
        while (linhas.hasNext()) {//Enquanto houver linhas para serem analisadas.
            line++;//A primeira linha é a de número 1 e não 0.
            linha = (String) linhas.next();//Pega a linha atual da leitura.
            
            if (inComment) {//Caso esteja em um comentário, vai para o estado 12 a despeito do caractere inicial.
                estado = 12;//Estado de leitura de um comentário de bloco.
            } else {
                estado = 0;//Caso não esteja em comentário de bloco, vai ao estado inicial (0).
                lexema = "";//E reseta o lexema (além de CMF, nenhum token possui quebra de linha).
            }

            int size = linha.length();
            char caractere = ' ';

            boolean caractereExcedente = false;//Flag utilizada para saber se o caractere lido foi utilizado no token formado.
            for (int i = 0; i < size || caractereExcedente; i++) {
                //Percorre todos os caracteres da linha. Caso haja caractere excedente, há mais uma execução.
                if (!caractereExcedente)//Pega o próximo caractere caso não haja caractere excedente.
                    caractere = linha.charAt(i);
                else//Caso haja caractere excedente, diminui 1 na contagem de caracteres passados.
                    i--;
                switch (estado) {//Switch entre os estado do autômato.
                    case 0://Formado um token ou lida uma nova linha (exceto Comentário de bloco), o estado inicial será este.
                        caractereExcedente = false;//Estando no estado 0, o caractereExcedente será tratado como atual.
                        if (ReconhecedorCaracteres.isSpace(caractere)) {
                            estado = 0;//Nada acontece, caracteere é ignorado.
                        } else if (ReconhecedorCaracteres.isChar(caractere)) {
                            //Caso seja uma letra válida.
                            lexema += caractere;//Adiciona o caractere ao lexema.
                            if(i==size-1){//Caso seja o último caractere da linha.
                                this.addToken("IDE", lexema, line);//Gera um token IDE, com o lexema e a linha atuais. 
                                //Gerado o token, o lexema volta a ser vazio.
                            }else
                                estado = 1;//Não sendo o último caractere da linha, tentar-se-á formar um token Identificador
                                            //ou palavra reservada.
                        } else if (ReconhecedorCaracteres.isDigit(caractere)) {//Caso receba um dígito.
                            lexema += caractere;
                            if(i==size-1){//Último caractere da linha
                                this.addToken("NRO", lexema, line);//Gera um token Numérico.
                            }else
                                estado = 3;//Tenta formar um token Númérico.
                        } else if (delimitadores.contains("" + caractere)) {
                            lexema += caractere;//Delimitadores possuem um único caractere.
                            this.addToken("DEL", lexema, line);//Gera um token Delimitador.
                        } else if (caractere == '|') {
                            lexema += caractere;
                            if(i==size-1){
                                this.addToken("OpMF", lexema, line);//Fim de linha => Apenas 1 | => Operador mal formado
                                arquivo.setErrosLexicos(arquivo.getErrosLexicos()+1);//Incrementa o número de erros léxicos no arquivo.
                            }else
                                estado = 7;//Tenta formar token Operador Lógico.
                        } else if (caractere == '&') {
                            lexema += caractere;
                            if(i==size-1){//Equivalente ao Operador ||
                                this.addToken("OpMF", lexema, line);
                                arquivo.setErrosLexicos(arquivo.getErrosLexicos()+1);
                            }else
                                estado = 8;
                        } else if (caractere == '!') {
                            lexema += caractere;
                            if(i==size-1){//Caso fim de linha, o operador formado é '!'
                                this.addToken("LOG", lexema, line);
                            }else//Não sendo fim de linha, distinguir-se-á entre '!' e "!="
                                estado = 9;
                        } else if (caractere == '<' || caractere == '>') {
                            lexema += caractere;
                            if(i==size-1){//Semelhante ao caso do '!'. Mas distinguir-se-á entre '<', '>', "<=" e ">=".
                                this.addToken("REL", lexema, line);
                            }else
                                estado = 10;
                        } else if (caractere == '=') {//Semelhante ao caso anterior, dintinguindo entre '=' ou "==".
                            lexema += caractere;
                            if(i==size-1){
                                this.addToken("ATRIB", lexema, line);
                            }else
                                estado = 18;//O tratamento é igual ao do caso anterior, pode-se ir ao mesmo estado.
                        } else if (caractere == '/') {
                            if(i==size-1){
                                this.addToken("ART", lexema, line);//Operador Aritmético, caso seja apenas '/'.
                            }else//Distinguir, no estado 11, entre "/**/", "//" e '/'
                                estado = 11;
                        }else if(caractere == '*'){//Estando no estado 0, '*' forma Operador Aritmético.
                            lexema += caractere;
                            this.addToken("ART", lexema, line);
                        } else if (caractere == '+') {//Distinguir-se-á entre '+' e "++".
                            lexema += caractere;
                            if(i==size-1){
                                this.addToken("ART", lexema, line);
                            }else
                                estado = 14;
                        } else if (caractere == '-') {//Distinguir-se-á entre '-' e "--".
                            lexema += caractere;
                            if(i==size-1){
                                this.addToken("ART", lexema, line);
                            }else
                                estado = 15;
                        } else if (caractere == '"'){//'"' indica formação de cadeia de caracteres..
                            lexema+=caractere;
                            if(i==size-1){//Havendo apenas a abertuda da cadeia, ela é mal formada.
                                this.addToken("CMF", lexema, line);
                                arquivo.setErrosLexicos(arquivo.getErrosLexicos()+1);
                            }else
                                estado = 16;//Reconhecimento de cadeia.
                        } else {//Caso não seja início de token válido, diz-se um Símbolo inválido, código SIB.
                            this.addToken("SIB",""+caractere, line);
                            arquivo.setErrosLexicos(arquivo.getErrosLexicos()+1);
                        }
                        break;
                    case 1:
                        if (ReconhecedorCaracteres.isChar(caractere) || ReconhecedorCaracteres.isDigit(caractere) || caractere == 95) {
                            //Caso seja letra, digito ou '_'.
                            lexema += caractere;
                            if(i==size-1){//Caso seja fim de linha, verifica se a palavra é reservada, se não, é identificador.
                                if (palavrasReservadas.contains(lexema)) {
                                    this.addToken("PRE", lexema, line);
                                } else {
                                    this.addToken("IDE", lexema, line);
                                }
                                estado = 0;
                            }//Não sendo fim de linnha, continua no estado 1.
                        } else {//Não sendo caracter permitido em identificadores, determinar-se-á o token no estado 2.
                            estado = 2;
                            caractereExcedente = true;//O caractere atual não pertence ao IDE ou à PRE, dizemos que é excedente.
                            //Na próxima execução do for, o estado será o 2 e caractereExcedente ainda será true.
                        }
                        break;
                    case 2:
                        estado = 0;//Próximo estado será o 0 e o símbolo excedente será tratano nele.
                        if (palavrasReservadas.contains(lexema)) {
                            this.addToken("PRE", lexema, line);
                        } else {
                            this.addToken("IDE", lexema, line);
                        }
                        break;
                    case 3://Já recebeu um caractere do tipo Digito.
                        if (ReconhecedorCaracteres.isChar(caractere)) {//Caso receba uma letra, irá gerar token Número Mal Formado.
                            lexema += caractere;
                            if(size-1==i){
                                this.addToken("NMF", lexema, line);
                                arquivo.setErrosLexicos(arquivo.getErrosLexicos()+1);
                            }else
                                estado = 6;//Caso não seja fim de linha, aguardar-se-á o fim das letras a fim de gerar o token de erro.
                        } else if (ReconhecedorCaracteres.isDigit(caractere)) {//Caso receba um digito, concatena-o.
                            lexema += caractere;
                            if(size-1==i)
                                this.addToken("NRO", lexema, line);
                        } else if (ReconhecedorCaracteres.isDot(caractere)) {//Recebendo um ponto, concatena-o e vai para o estado 4.
                            lexema += caractere;
                            if(size-1==i){//Um fim de linha do tipo "digito*." indica erro.
                                this.addToken("NMF", lexema, line);
                                arquivo.setErrosLexicos(arquivo.getErrosLexicos()+1);
                            }else
                                estado = 4;
                        } else {//Recebendo qualquer outra coisa, volta ao estado 0, gera o token Numérico.
                            estado = 0;
                            caractereExcedente = true;//Caractere Excedente tratado no estado 0.
                            this.addToken("NRO", lexema, line);
                        }
                        break;
                    case 4://REcebeu digitos e 1 '.'. Verifica se receberá um digito depois do ponto.
                        lexema+=caractere;
                        if (ReconhecedorCaracteres.isDigit(caractere)) {
                            if(size-1==i)
                                this.addToken("NRO", lexema, line);
                            else
                                estado = 5;//Sendo digito, vaipara o estado 5.
                        }else if(ReconhecedorCaracteres.isChar(caractere)){
                            if(size-1==i){
                                this.addToken("NMF", lexema, line);
                                arquivo.setErrosLexicos(arquivo.getErrosLexicos()+1);
                            }else
                                estado = 6;//Sendo letra, vai para estado 6 e concatena próximas letras.
                        }else{
                            this.addToken("NMF", lexema, line);
                            arquivo.setErrosLexicos(arquivo.getErrosLexicos()+1);
                            caractereExcedente = true; //Recebendo qualquer outro caractere após o ponto, gera token NMF.
                            estado = 0;
                        }
                        break;
                    case 5:
                        if (ReconhecedorCaracteres.isDigit(caractere)) {
                        //Fica nesse estado enquanto receber digitos ou chegar o fim da linha, recebendo letra vai para o estado 6.
                            lexema += caractere;
                            if(size-1==i)
                                this.addToken("NRO", lexema, line);
                        } else if(ReconhecedorCaracteres.isChar(caractere)){
                            lexema += caractere;
                            if(size-1==i){
                                this.addToken("NMF", lexema, line);
                                arquivo.setErrosLexicos(arquivo.getErrosLexicos()+1);
                            }else
                                estado = 6;
                        }else{
                            caractereExcedente = true;
                            this.addToken("NRO", lexema, line);
                            estado = 0;
                        }
                        break;
                    case 6://Enquanto receber letras, permanece concatenando. Ao final, gera NMF.
                        if (ReconhecedorCaracteres.isChar(caractere)) {
                            lexema += caractere;
                            if(size-1==i){
                                this.addToken("NMF", lexema, line);
                                arquivo.setErrosLexicos(arquivo.getErrosLexicos()+1);
                            }
                        } else {
                            this.addToken("NMF", lexema, line);
                            arquivo.setErrosLexicos(arquivo.getErrosLexicos()+1);
                            estado = 0;
                            caractereExcedente=true;
                        }
                        break;
                    case 7://Rebeu um |. Recebendo outro, LOG de valor "||", caso contrário, gera OPMF.
                        if (caractere == '|') {
                            lexema += caractere;
                            this.addToken("LOG", lexema, line);
                            estado = 0;
                        } else {
                            lexema += caractere;
                            this.addToken("OpMF", lexema, line);
                            arquivo.setErrosLexicos(arquivo.getErrosLexicos()+1);
                            estado = 0;
                        }
                        break;
                    case 8://Lógica semelhante ao caso anterior.
                        if (caractere == '&') {
                            lexema += caractere;
                            this.addToken("LOG", lexema, line);
                            estado = 0;
                        } else {
                            lexema += caractere;
                            this.addToken("OpMF", lexema, line);
                            arquivo.setErrosLexicos(arquivo.getErrosLexicos()+1);
                            estado = 0;
                        }
                        break;
                    case 9://Recebeu um '!'. Caso receba '=', gera Operador Relacionar de valor "!=", caso contrário, LOG valor '!'.
                        if (caractere == '=') {
                            lexema += caractere;
                            this.addToken("REL", lexema, line);
                            estado = 0;
                        } else {
                            caractereExcedente = true;
                            this.addToken("LOG", lexema, line);
                            estado = 0;
                        }
                        break;
                    case 10://Recebeu '<' ou '>', em qualquer caso, geral REL, mas não recebendo '=', há um caractere excedente.
                        if (caractere == '=') {
                            lexema += caractere;//Lexema possui "<=" ou ">="
                            this.addToken("REL", lexema, line);
                            estado = 0;
                        } else {
                            caractereExcedente = true;//Caractere excedente tratado em estado 0.
                            this.addToken("REL", lexema, line);//Lexema possui '<' ou '>'.
                            estado = 0;
                        }
                        break;
                    case 11://Recebeu 1 '/'
                        if (caractere == '/') {
                            //Reber um segundo '/' indica comentário de linha, sugerindo ignorar o resto da linha atual.
                            i = size + 1;//saindo do laço que percorre a linha (comentário de linha é identificado)
                        } else if (caractere == '*') {
                            //Receber um '*' indica comentário de bloco. Sugere ignorar caracteres até receber "*/".
                            estado = 12;
                            inComment = true;//comentário de bloco esta aberto
                            lineComment = line;//Guarda a informação da linha do comentário. Sendo mal formado, gerará token CoMF.
                        }else{//Recebendo outra coisa, forma-se um Operador Aritmético de valor '/'.
                            lexema+='/';
                            this.addToken("ART", lexema, line);
                            estado = 0;
                            caractereExcedente = true;//O caractere atual é excedente.
                        }
                        break;
                    case 12://Comentário de bloco foi iniciado.
                        if (caractere == '*')//Verificar-se-á a formação de "*/"
                            estado = 13;
                        break;
                    case 13:
                        switch (caractere) {
                            case '/':
                                estado = 0;
                                inComment = false;//comentário de bloco foi fechado
                                break;
                            case '*':
                                estado = 13;//Não encontrada a sequência */ mas o caractere atual pode formá-la;
                                break;
                            default:
                                estado = 12;
                                break;
                        }
                        break;
                    case 14:
                        if (caractere == '+') {//Recebeu "++".
                            lexema += caractere;
                            this.addToken("ART", lexema, line);
                            estado = 0;
                        } else {//Recebeu '+' e caractere excedente.
                            caractereExcedente = true;
                            this.addToken("ART", lexema, line);
                            estado = 0;
                        }
                        break;
                    case 15:
                        if (caractere == '-') {//Recebeu "--".
                            lexema += caractere;
                            this.addToken("ART", lexema, line);
                            estado = 0;
                        } else {
                            caractereExcedente = true;//Recebeu '-' e caractere excedente.
                            this.addToken("ART", lexema, line);
                            estado = 0;
                        }
                        break;
                    case 16://Recebeu ", sugerindo início de Cadeia de Caracteres.
                        if(caractere == '\\') {//Caso receba '\', a próxima '"' não fechará a cadeia.
                            if(i==size-1){//Finalizando a linha sem terminar a cadeia de caracteres é uma má formação de Cadeia.
                                this.addToken("CMF", lexema, line);
                                arquivo.setErrosLexicos(arquivo.getErrosLexicos()+1);
                            }else{//No estado 17 irá ignorar a próxima '"'.
                                lexema += caractere;
                                estado = 17;
                            }
                        }else if (ReconhecedorCaracteres.isValidSymbol(caractere)) {//Verifica se o caractere é válido na cadeia.   
                            lexema += caractere;
                            if(i==size-1){
                                this.addToken("CMF", lexema, line);
                                arquivo.setErrosLexicos(arquivo.getErrosLexicos()+1);
                            }
                        }else if(caractere=='"'){//Fim de cadeia.
                            lexema+=caractere;
                            this.addToken("CAD", lexema, line);
                        }else{//Símbolo inválido dentro da cadeia. Será removido como SIB e a cadeia será formada sem ele.
                            String temp = lexema;
                            this.addToken("SIB", ""+caractere, line);
                            arquivo.setErrosLexicos(arquivo.getErrosLexicos()+1);
                            lexema = temp;
                            if(i==size-1){
                                this.addToken("CMF", lexema, line);
                                arquivo.setErrosLexicos(arquivo.getErrosLexicos()+1);
                            }
                        }
                        break;
                    case 17://Recebida uma '\' no meio da cadeia.
                        if(caractere == '"'){
                            lexema+='"';
                        }else if(ReconhecedorCaracteres.isValidSymbol(caractere)){
                            lexema+=caractere;
                        }else{
                            String temp = lexema;
                            this.addToken("SIB", ""+caractere, line);
                            arquivo.setErrosLexicos(arquivo.getErrosLexicos()+1);
                            lexema = temp;
                        }
                        if(i==size-1){//Caso tenha acabado a linha, gera CMF.
                            this.addToken("CMF", lexema, line);
                            arquivo.setErrosLexicos(arquivo.getErrosLexicos()+1);
                        }else//Caso contrário, volta para aguardar o encerramenteo da cadeia.
                            estado = 16;
                        break;
                    case 18: 
                        if (caractere == '=') {
                            lexema += caractere;//Lexema possui "=="
                            this.addToken("REL", lexema, line);
                            estado = 0;
                        } else {
                            caractereExcedente = true;//Caractere excedente tratado em estado 0.
                            this.addToken("ATRIB", lexema, line); // Lexema possui "="
                            estado = 0;
                        }
                        break;
                    default://Caso caia em um estado não determinístico, volta para 
                        lexema = "";
                        caractereExcedente = false;
                }
            }
        }
        //se quando a leitura terminar o comentario ainda estiver aberto
        if (inComment) {
            this.addToken("CoMF", "", lineComment);//comentario nao terminado/ mal formado.
            arquivo.setErrosLexicos(arquivo.getErrosLexicos()+1);
        }
        System.out.println("Análise léxica realizada "+(arquivo.getErrosLexicos()==0?"com":"sem")+" sucesso ("+String.format("%03d", arquivo.getErrosLexicos())+" erros léxicos encontrados) "+" no arquivos "+arquivo.getNome());
        return new Pair<>(tokens, analiseRet);
    }
	
	public Iterator getTokens(){
        return tokens.iterator();
    }

    /*
    Método que adiciona os Tokens a variável de retorno
    */
    private void addToken(String id, String lexema, int line) {
        Token t = new Token(id, lexema, line);
        tokens.add(t);
        analiseRet += t + "\n";
        
        this.lexema = "";
    }
}