package view;
import controller.AnalisadorLexico;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
       AnalisadorLexico analisador = new AnalisadorLexico("entrada1.txt");
       
        try {
            analisador.analise();

        } catch (IOException ex) {
            System.out.println("Falha ao ler o arquivo.");
        }
    }
    
}
