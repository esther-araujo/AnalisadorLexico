/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.HashSet;
import java.util.Iterator;
import model.Token;
import util.ReconhecedorCaracteres;
import util.SemEntradasException;

/**
 *
 * @author esther
 */
public class AnalisadorLexico {

    private Iterator linhas;
    private String linha;
    private String lexema;
    private static final HashSet<String> palavrasReservadas = new HashSet();
    private static final HashSet<String> delimitadores = new HashSet();
    private boolean inComment = false;

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

    }

    public String analise(Iterator linhas) {
        analiseRet="";
        this.linhas = linhas;
        int line = 0;
        int lineComment = 0;
        inComment= false;
        while (linhas.hasNext()) {
            line++;
            linha = (String) linhas.next();
            if (inComment) {
                estado = 12;
            } else {
                estado = 0;
                lexema = "";
            }

            int size = linha.length();
            char caractere = ' ';

            boolean caractereExcedente = false;
            for (int i = 0; i < size || caractereExcedente; i++) {
                if (!caractereExcedente)
                    caractere = linha.charAt(i);
                else
                    i--;
                switch (estado) {
                    case 0:
                        caractereExcedente = false;
                        if (ReconhecedorCaracteres.isSpace(caractere)) {
                            estado = 0;
                        } else if (ReconhecedorCaracteres.isChar(caractere)) {
                            lexema += caractere;
                            if(i==size-1){
                                this.addToken("IDE", lexema, line);
                            }else
                                estado = 1;
                        } else if (ReconhecedorCaracteres.isDigit(caractere)) {
                            lexema += caractere;
                            
                            if(i==size-1){
                                this.addToken("NRO", lexema, line);
                            }else
                                estado = 3;
                        } else if (delimitadores.contains("" + caractere)) {
                            lexema += caractere;
                            this.addToken("DEL", lexema, line);
                        } else if (caractere == '|') {
                            lexema += caractere;
                            if(i==size-1){
                                this.addToken("OpMF", lexema, line);
                            }else
                                estado = 7;
                        } else if (caractere == '&') {
                            lexema += caractere;
                            if(i==size-1){
                                this.addToken("OpMF", lexema, line);
                            }else
                                estado = 8;
                        } else if (caractere == '!') {
                            lexema += caractere;
                            if(i==size-1){
                                this.addToken("LOG", lexema, line);
                            }else
                                estado = 9;
                        } else if (caractere == '<' || caractere == '>') {
                            lexema += caractere;
                            if(i==size-1){
                                this.addToken("REL", lexema, line);
                            }else
                                estado = 10;
                        } else if (caractere == '=') {
                            lexema += caractere;
                            if(i==size-1){
                                this.addToken("REL", lexema, line);
                            }else
                                estado = 10;
                        } else if (caractere == '/') {
                            if(i==size-1){
                                this.addToken("ART", lexema, line);
                            }else
                                estado = 11;
                        }else if(caractere == '*'){
                            this.addToken("ART", lexema, line);
                        } else if (caractere == '+') {
                            lexema += caractere;
                            if(i==size-1){
                                this.addToken("ART", lexema, line);
                            }else
                                estado = 14;
                        } else if (caractere == '-') {
                            lexema += caractere;
                            if(i==size-1){
                                this.addToken("ART", lexema, line);
                            }else
                                estado = 15;
                        } else if (caractere == '"'){
                            lexema+=caractere;
                            if(i==size-1)
                                this.addToken("CMF", lexema, line);
                            else
                                estado = 16;
                        } else {
                            this.addToken("SIB",""+caractere, line);
                        }
                        break;
                    case 1:
                        if (ReconhecedorCaracteres.isChar(caractere) || ReconhecedorCaracteres.isDigit(caractere) || caractere == 95) {
                            lexema += caractere;
                            if(i==size-1){
                                if (palavrasReservadas.contains(lexema)) {
                                    this.addToken("PRE", lexema, line);
                                } else {
                                    this.addToken("IDE", lexema, line);
                                }
                                estado = 0;
                            }
                        } else {
                            estado = 2;
                            caractereExcedente = true;
                        }
                        break;
                    case 2:
                        estado = 0;
                        if (palavrasReservadas.contains(lexema)) {
                            this.addToken("PRE", lexema, line);
                        } else {
                            this.addToken("IDE", lexema, line);
                        }
                        break;
                    case 3:
                        if (ReconhecedorCaracteres.isChar(caractere)) {
                            lexema += caractere;
                            if(size-1==i)
                                this.addToken("NMF", lexema, line);
                            else
                                estado = 6;
                        } else if (ReconhecedorCaracteres.isDigit(caractere)) {
                            lexema += caractere;
                            if(size-1==i)
                                this.addToken("NRO", lexema, line);
                        } else if (ReconhecedorCaracteres.isDot(caractere)) {
                            lexema += caractere;
                            if(size-1==i)
                                this.addToken("NMF", lexema, line);
                            else
                                estado = 4;
                        } else {
                            estado = 0;
                            caractereExcedente = true;
                            this.addToken("NRO", lexema, line);
                        }
                        break;
                    case 4:
                        lexema+=caractere;
                        if (ReconhecedorCaracteres.isDigit(caractere)) {
                            if(size-1==i)
                                this.addToken("NRO", lexema, line);
                            else
                                estado = 5;
                        }else if(ReconhecedorCaracteres.isChar(caractere)){
                            if(size-1==i)
                                this.addToken("NMF", lexema, line);
                            else
                                estado = 6;
                        }else{
                            this.addToken("NRO", lexema, line);
                            caractereExcedente = true;
                            estado = 0;
                        }
                        break;
                    case 5:
                        if (ReconhecedorCaracteres.isDigit(caractere)) {
                            lexema += caractere;
                            if(size-1==i)
                                this.addToken("NRO", lexema, line);
                        } else if(ReconhecedorCaracteres.isChar(caractere)){
                            lexema += caractere;
                            if(size-1==i)
                                this.addToken("NMF", lexema, line);
                            else
                                estado = 6;
                        }else{
                            caractereExcedente = true;
                            this.addToken("NRO", lexema, line);
                            estado = 0;
                        }
                        break;
                    case 6:
                        if (ReconhecedorCaracteres.isChar(caractere)) {
                            lexema += caractere;
                            if(size-1==i)
                                this.addToken("NMF", lexema, line);
                        } else {
                            this.addToken("NMF", lexema, line);
                            estado = 0;
                            caractereExcedente=true;
                        }
                        break;
                    case 7:
                        if (caractere == '|') {
                            lexema += caractere;
                            this.addToken("LOG", lexema, line);
                            estado = 0;
                        } else {
                            lexema += caractere;
                            this.addToken("OpMF", lexema, line);
                            estado = 0;
                        }
                        break;
                    case 8:
                        if (caractere == '&') {
                            lexema += caractere;
                            this.addToken("LOG", lexema, line);
                            estado = 0;
                        } else {
                            lexema += caractere;
                            this.addToken("OpMF", lexema, line);
                            estado = 0;
                        }
                        break;
                    case 9:
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
                    case 10:
                        if (caractere == '=') {
                            lexema += caractere;
                            this.addToken("REL", lexema, line);
                            estado = 0;
                        } else {
                            caractereExcedente = true;
                            this.addToken("REL", lexema, line);
                            estado = 0;
                        }
                        break;
                    case 11:
                        if (caractere == '/') {
                            i = size + 1;//saindo do laço que percorre a linha
                        } else if (caractere == '*') {
                            estado = 12;
                            inComment = true;
                            lineComment = line;
                        }else{
                            lexema+='/';
                            this.addToken("ART", lexema, line);
                            estado = 0;
                            caractereExcedente = true;
                        }
                        break;
                    case 12:
                        if (caractere == '*') {
                            estado = 13;
                        } else {
                            estado = 12;
                        }
                        break;
                    case 13:
                        if (caractere == '/') {
                            estado = 0;
                            inComment = false;
                        }
                        break;
                    case 14:
                        if (caractere == '+') {
                            lexema += caractere;
                            this.addToken("ART", lexema, line);
                            estado = 0;
                        } else {
                            caractereExcedente = true;
                            this.addToken("ART", lexema, line);
                            estado = 0;
                        }
                        break;
                    case 15:
                        if (caractere == '-') {
                            lexema += caractere;
                            this.addToken("ART", lexema, line);
                            estado = 0;
                        } else {
                            caractereExcedente = true;
                            this.addToken("ART", lexema, line);
                            estado = 0;
                        }
                        break;
                    case 16:
                        if(caractere == '\\') {
                            if(i==size-1){
                                this.addToken("CMF", lexema, line);
                            }else
                                estado = 17;
                        }else if (ReconhecedorCaracteres.isValidSymbol(caractere)) {
                            lexema += caractere;
                            if(i==size-1)
                                this.addToken("CMF", lexema, line);
                        }else if(caractere=='"'){
                            lexema+=caractere;
                            this.addToken("CAD", lexema, line);
                        }else{
                            String temp = lexema;
                            this.addToken("SIB", ""+caractere, line);
                            lexema = temp;
                            if(i==size-1)
                                this.addToken("CMF", lexema, line);
                        }
                        break;
                    case 17:
                        if(caractere == '"'){
                            lexema+='"';
                        }else if(ReconhecedorCaracteres.isValidSymbol(caractere)){
                            lexema+='\\';
                            lexema+=caractere;
                        }else{
                            String temp = lexema +'\\';
                            this.addToken("SIB", ""+caractere, line);
                            lexema = temp;
                        }
                        if(i==size-1){
                            this.addToken("CMF", lexema, line);
                        }else
                            estado = 16;
                        break;
                    default:
                        lexema = "";
                        caractereExcedente = false;
                }
            }
        }
        //se quando a leitura terminar o comentario ainda estiver aberto
        if (inComment) {
            this.addToken("CoMF", "", lineComment);//comentario nao terminado/ mal formado
        }
        return analiseRet;
    }

    public void addToken(String id, String lexema, int line) {
        Token t = new Token(id, lexema, line);
        analiseRet += t + "\n";
        this.lexema = "";
    }
}
