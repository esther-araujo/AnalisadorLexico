/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.HashSet;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import model.Arquivo;
import model.Token;
import util.ReconhecedorCaracteres;
import util.SemEntradasException;

/**
 *
 * @author esther
 */
public class AnalisadorLexico {

    private final Iterator linhas;
    private String linha;
    private String lexema;
    private static final HashSet<String> palavrasReservadas = new HashSet();
    private static final HashSet<String> delimitadores = new HashSet();
    private boolean inComment = false;

    int estado = 0;
    int token = 0;
    boolean fimCodigo = false;

    public AnalisadorLexico(String pathFile) throws SemEntradasException {
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
        
        ControllerArquivos arquivoController = ControllerArquivos.getInstance(pathFile);
        LinkedList<Arquivo> arquivos = arquivoController.getArquivos();
        Arquivo arquivo1 = arquivos.iterator().next();
        linhas = arquivo1.getConteudo();
    }

    public void analise() throws IOException {
        int line = 0;
        while (linhas.hasNext()) {
            line++;
            linha = (String) linhas.next();
            if(inComment){
               estado = 13;
            }
            else{
                estado = 0;
                lexema = "";
            }
            
            int size = linha.length();
            char caractere = ' ';
            
            boolean caractereExcedente = false;
            for (int i = 0; i <=size||caractereExcedente; i++) {
                //System.out.print("i: "+i+"; ");
                if(!caractereExcedente){
                    if(i!=size)
                        caractere = linha.charAt(i);
                }else
                    i--;
                
                switch (estado) {
                    case 0:
                        caractereExcedente=false;
                        if (ReconhecedorCaracteres.isSpace(caractere)) {
                            estado = 0;
                            caractereExcedente = false;
                        }
                        else if (ReconhecedorCaracteres.isChar(caractere)) {
                            lexema += caractere;
                            estado = 1;
                        }else if(ReconhecedorCaracteres.isDigit(caractere)){
                            lexema += caractere;
                            estado = 3;
                        }else if(delimitadores.contains(""+caractere)&&size!=i){
                            lexema+=caractere;
                            this.addToken("DEL", lexema, line);
                        }else if (caractere == '|'){
                            estado = 7;
                            lexema+=caractere;
                        }else if (caractere == '&'){
                            estado = 8;
                            lexema+=caractere;
                        }else if(caractere == '!'){
                            estado = 9;
                            lexema+=caractere;
                        }else if(caractere == '<' || caractere=='>'){
                            estado = 10;
                            lexema+=caractere;
                        }else if(caractere == '='){
                            estado = 10;
                            lexema+=caractere;
                        }else if(caractere == '/'){
                            estado = 11;
                            lexema+=caractere;
                        }else{
                            lexema = "";
                        }
                        break;
                    case 1:
                        if ((ReconhecedorCaracteres.isChar(caractere) || ReconhecedorCaracteres.isDigit(caractere) || caractere == 95)&&i!=size){
                            lexema += caractere;
                            //System.out.println("AQUI GARAI: "+caractere+" i: "+i+" size: "+size+" Excedente: "+caractereExcedente);
                        }else{
                            estado = 2;
                            caractereExcedente = true;
                        }
                        break;
                    case 2:
                        estado = 0;
                        if(palavrasReservadas.contains(lexema))
                            this.addToken("PRE",lexema, line);
                        else
                            this.addToken("IDE",lexema, line);
                        //caractereExcedente = false;
                        break;
                    case 3:
                        if (ReconhecedorCaracteres.isChar(caractere)){
                            lexema+=caractere;
                            estado = 6;
                        }else if (ReconhecedorCaracteres.isDigit(caractere)) {
                            lexema += caractere;
                        } else if (ReconhecedorCaracteres.isDot(caractere)) {
                            lexema += caractere;
                            estado = 4;
                        }else{
                            estado = 0;
                            caractereExcedente=true;
                            this.addToken("NRO",lexema, line);
                        }
                        break;
                    case 4:
                        if(ReconhecedorCaracteres.isDigit(caractere)){
                            lexema+=caractere;
                            estado = 5;
                        }else{
                            caractereExcedente=true;
                            this.addToken("NMF",lexema, line);
                            estado = 0;
                        }
                        break;
                    case 5:
                        if(ReconhecedorCaracteres.isDigit(caractere)){
                            lexema+=caractere;
                        }else{
                            caractereExcedente=true;
                            this.addToken("NRO",lexema, line);
                            estado=0;
                        }
                        break;
                    case 6:
                        if(ReconhecedorCaracteres.isChar(caractere)){
                            lexema+=caractere;
                        }else{
                            this.addToken("NMF",lexema, line);
                            estado = 0;
                        }
                        break;
                    case 7:
                        if(caractere=='|'){
                            lexema+=caractere;
                            this.addToken("LOG", lexema, line);
                            estado=0;
                        }else{
                            lexema+=caractere;
                            this.addToken("OpMF", lexema, line);
                            estado=0;
                        }
                        break;
                    case 8:
                        if(caractere=='&'){
                            lexema+=caractere;
                            this.addToken("LOG", lexema, line);
                            estado=0;
                        }else{
                            lexema+=caractere;
                            this.addToken("OpMF", lexema, line);
                            estado=0;
                        }
                        break;
                    case 9:
                        if(caractere=='='){
                            lexema+=caractere;
                            this.addToken("REL", lexema, line);
                            estado=0;
                        }else{
                            caractereExcedente=true;
                            this.addToken("LOG", lexema, line);
                            estado=0;
                        }
                        break;
                    case 10:
                        if(caractere=='='){
                            lexema+=caractere;
                            this.addToken("REL", lexema, line);
                            estado=0;
                        }else{
                            caractereExcedente=true;
                            this.addToken("REL", lexema, line);
                            estado=0;
                        }
                        break;
                    case 11:
                        if(caractere=='/'){
                            estado = 12;
                            lexema+=caractere;
                            this.addToken("COM", lexema, line);
                            lexema="";
                        }else if(caractere=='*'){
                            lexema+=caractere;
                            estado=13;
                            inComment=true;
                        }
                        break;
                    case 12:
                        estado = 12;
                        break;
                    case 13:
                        if(caractere=='*'){
                            estado = 14;
                            lexema+=caractere;
                            inComment=false;
                        }
                        else{
                            estado=13;
                        }
                        break;
                    case 14:
                        if(caractere=='/'){
                            lexema+=caractere;
                            estado=0;
                            inComment=false;
                            this.addToken("COM", lexema, line);
                        }
                        else{
                            lexema+=caractere;
                            this.addToken("CoMF", lexema, line);
                            inComment=false;
                        }
                        break;
                    default:
                        lexema = "";
                        caractereExcedente = false;
                }
            }
        }
        //se quando a leitura terminar o comentario ainda estiver aberto
        if(inComment){
            this.addToken("CoNT", lexema, line);//comentario nao terminado
        }
    }

    public void addToken(String id, String lexema, int line) {
            Token t = new Token(id, lexema, line);
            System.out.println(t);
            this.lexema="";
    }
}