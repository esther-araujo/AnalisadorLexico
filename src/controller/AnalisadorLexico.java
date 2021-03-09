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
    private static final HashSet delimitadores = new HashSet();
    private static final HashSet operadoresLog = new HashSet();
    private static final HashSet operadoresRel = new HashSet();
    private String identificador = "[a-z]|[A-Z])(([a-z]|[A-Z])|[0-9]|_";

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
            int size = linha.length();
            char caractere = ' ';
            int estado = 0;
            boolean caractereExcedente = false;
            for (int i = 0; i <=size; i++) {
                System.out.print("i: "+i+"; ");
                if(!caractereExcedente)
                    if(size!=i)
                    caractere = linha.charAt(i);
                else
                    i--;
                switch (estado) {
                    case 0:
                        if (ReconhecedorCaracteres.isSpace(caractere)) {
                            estado = 0;
                            caractereExcedente = false;
                        }
                        else if (ReconhecedorCaracteres.isChar(caractere)) {
                            lexema += caractere;
                            estado = 1;
                        }else {
                            lexema="ERRO";
                            caractereExcedente = false;
                        }
                        break;
                    case 1:
                        if ((ReconhecedorCaracteres.isChar(caractere) || ReconhecedorCaracteres.isDigit(caractere) || caractere == 95)&&i<size){
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
                            this.addToken("KEY",lexema, line);
                        else
                            this.addToken("IDE",lexema, line);
                        caractereExcedente = false;
                        System.out.println("acabou a palavra "+lexema);
                        break;
                    default:
                        caractereExcedente = false;
                }

            }
        }
    }

    public void addToken(String id, String lexema, int line) {
            Token t = new Token(id, lexema, line);
            System.out.println(t);
            this.lexema="";
            return;
    }
}