/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.HashSet;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import model.Arquivo;
import model.Lexema;
import util.ReconhecedorCaracteres;

/**
 *
 * @author esther
 */
public class AnalisadorLexico {

    private Iterator linhas;
    private String linha;
    private String lexema;
    private String id;
    private static final HashSet<String> palavrasReservadas = new HashSet();
    private static final HashSet delimitadores = new HashSet();
    private static final HashSet operadoresLog = new HashSet();
    private static final HashSet operadoresRel = new HashSet();
    private String identificador = "[a-z]|[A-Z])(([a-z]|[A-Z])|[0-9]|_";

    int estado = 0;
    int token = 0;
    boolean fimCodigo = false;

    public AnalisadorLexico(String pathFile) {
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
        operadoresRel.add("<");
        operadoresRel.add(">");
        operadoresRel.add("==");
        operadoresRel.add("!=");
        operadoresRel.add(">=");
        operadoresRel.add("<=");
        operadoresRel.add("=");
        operadoresLog.add("&&");
        operadoresLog.add("!");
        operadoresLog.add("||");
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
            lexema = "";
            id = "";
            int size = linha.length();
            char caractere;
            int estado = 0;
            int estadoAnterior = 0;
            for (int i = 0; i < size; i++) {

                caractere = linha.charAt(i);

                switch (estado) {
                    case 0:
                        if (ReconhecedorCaracteres.isChar(caractere)) {
                            lexema += caractere;
                            estado = 1;

                        } else if (ReconhecedorCaracteres.isDigit(caractere)) {
                            lexema += caractere;
                            id = "NRO";
                            estado = 2;
                            estadoAnterior = 2;
                        } else if (ReconhecedorCaracteres.isSpace(caractere)) {
                            estado = 0;   
                        } else {
                            lexema+=caractere;
                            id = "ERRO";
                            Lexema token = new Lexema(line,id,lexema ); 
                            this.showToken(token);
                        }
                        break;

                    case 1:
                        if (ReconhecedorCaracteres.isChar(caractere) || ReconhecedorCaracteres.isDigit(caractere) || ReconhecedorCaracteres.isUnderline(caractere)) {
                            lexema += caractere;
                            estadoAnterior=1;
                        } else if (ReconhecedorCaracteres.isSpace(caractere)) {
                            estado = 0;
                            this.addToken(lexema, line);
                        } else {
                            lexema+=caractere;
                            id = "ERRO";
                            Lexema token = new Lexema(line,id,lexema ); 
                            this.showToken(token);
                            estado=0;
                            lexema="";
                        }
                        break;
                    case 2:
                        if (ReconhecedorCaracteres.isDigit(caractere)) {
                            lexema += caractere;
                            id = "NRO";
                            estado = 2;
                            estadoAnterior = 2;
                        } else if (ReconhecedorCaracteres.isSpace(caractere)) {
                            estado = 0;
                            id = "NRO";
                            Lexema token = new Lexema(line,id,lexema );
                            this.showToken(token);
                            lexema = "";
                        } else if (ReconhecedorCaracteres.isDot(caractere)) {
                            lexema += caractere;
                            estado = 3;
                        }
                        break;

                    case 3:
                        if (ReconhecedorCaracteres.isDigit(caractere)) {
                            lexema += caractere;
                            estado = 3;
                        } else if (ReconhecedorCaracteres.isSpace(caractere)) {
                            estado = 0;
                            id = "NRO";
                            Lexema token = new Lexema(line,id ,lexema );
                            this.showToken(token);
                            estadoAnterior = 3;
                            lexema = "";
                        } else if (ReconhecedorCaracteres.isDot(caractere)) {
                            lexema += caractere;
                            id = "NMF";
                            Lexema token = new Lexema(line,id ,lexema );
                            this.showToken(token);
                            lexema="";
                            estadoAnterior = 3;
                            estado = 0;
                        }
                        break;

                }

            }
            if(estadoAnterior==1 && id!="ERRO"){
                this.addToken(lexema, line);
            }
            if((estadoAnterior==2 || estadoAnterior==3) && id!="ERRO"){
                Lexema token = new Lexema(line,id ,lexema );
            }
            
            
            

        }
    }

    public void addToken(String palavra, int line) {

        if (palavrasReservadas.contains(palavra)) {
            lexema = "";
            System.out.println(String.format("%01d", line) + " PRE " + palavra);
            return;
        } else {
            lexema = "";
            System.out.println(String.format("%01d", line) + " IDE " + palavra);
            return;
        }

    }

    //para testar
    public void showToken(Lexema token) {
        System.out.println(token.getLinha() + " " + token.getLexema() + " " + token.getId());
    }
}
