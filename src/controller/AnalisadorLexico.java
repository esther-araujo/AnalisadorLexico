/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.HashSet;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

/**
 *
 * @author esther
 */
public class AnalisadorLexico {

    private Iterator linhas;
    private String linha;
    private String lexema;
    private static HashSet<String> palavrasReservadas = new HashSet();
    private static HashSet delimitadores = new HashSet();
    private static HashSet operadoresLog = new HashSet();
    private static HashSet operadoresRel = new HashSet();
    private String identificador = "[a-z]|[A-Z])(([a-z]|[A-Z])|[0-9]|_";

    int estado = 0;
    int token = 0;
    boolean fimCodigo = false;

    public AnalisadorLexico(String pathFile) {
        try {

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

            ControllerArquivos codigo = new ControllerArquivos();

            linhas = codigo.lerArquivo(pathFile);
        } catch (FileNotFoundException ex) {
            System.out.println("Arquivo não encontrado");
        }
    }

    public void analise() throws IOException {
        int line = 0;
        while (linhas.hasNext()) {
            line++;
            linha = (String) linhas.next();
            lexema = "";
            int size = linha.length();
            char caractere;
            int estado = 0;
            for (int i = 0; i <size; i++) {

                caractere = linha.charAt(i);

                switch (estado) {
                    case 0:
                        if (isChar(caractere)) {
                            lexema += caractere;
                            estado = 1;

                        } else if (isSpace(caractere)) {
                            estado = 0;
                            this.addToken(lexema, line);
                        }
                        else {
                            lexema="ERRO";
                        }
                        break;

                    case 1:
                        if (isChar(caractere) || isDigit(caractere) || caractere == 95) {
                            lexema += caractere;
                        } else if (isSpace(caractere)) {
                            estado = 0;
                            this.addToken(lexema, line);
                        } else {
                            lexema="ERRO";
                        }
                        break;

                }

            }
            this.addToken(lexema, line);

        }
    }

    public void addToken(String palavra, int line) {

        if (palavrasReservadas.contains(palavra)) {
            lexema = "";
            System.out.println(String.format("%01d", line) + " KEY " + palavra);
            return;
        } 
        else if(palavra=="ERRO"){
            System.out.println(String.format("%01d ", line) + palavra);
        }
        else {
            lexema = "";
            System.out.println(String.format("%01d", line) + " IDE " + palavra);
            return;
        }

    }

    public boolean isChar(char c) {
        return (c > 64 && c < 91) || (c > 96 && c < 123);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isSpace(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }
}
