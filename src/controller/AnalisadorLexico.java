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

/**
 *
 * @author esther
 */
public class AnalisadorLexico {

    private BufferedReader codigo;
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

            codigo = new BufferedReader(new FileReader(pathFile));
        } catch (FileNotFoundException ex) {
            System.out.println("Arquivo não encontrado");
        }
    }

    public void analise() throws IOException {
        int line = 0;
        while (true) {
            line++;
            linha = codigo.readLine();
            if (linha == null) {
                break;
            }
            lexema ="";
            int size = linha.length();
            String caractere = "";
            for (int i = 0; i < size; i++) {
                
                caractere = linha.substring(i, i + 1);
               
                //remover comentarios
                if(caractere.equals(" ")){ //deixei espaço como delimitador por enquanto
                    this.addToken(lexema,line);
                    lexema ="";
                }
                else{
                    lexema = lexema + caractere;
                }

            }

        }

    }

    public void addToken(String palavra, int line) {

        if (palavrasReservadas.contains(palavra)) {
            System.out.println(String.format("%04d", line)+" KEY "+palavra);
            return;
        } 
        else {
                System.out.println(String.format("%04d", line)+" IDE "+palavra);
                return;
        }

    }
}
