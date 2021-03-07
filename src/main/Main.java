/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;
import classes.AnalisadorLexico;
import java.io.IOException;
/**
 *
 * @author esther
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       AnalisadorLexico analisador = new AnalisadorLexico("entrada1.txt");
        try {
            analisador.analise();

        } catch (IOException ex) {
            System.out.println("Falha ao ler o arquivo.");
        }
    }
    
}
