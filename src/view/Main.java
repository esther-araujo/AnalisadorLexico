package view;
import controller.AnalisadorLexico;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.SemEntradasException;

public class Main {

    public static void main(String[] args) {
        AnalisadorLexico analisador;
        try {
            analisador = new AnalisadorLexico("entrada/");
            analisador.analise();
        } catch (SemEntradasException ex) {
            System.out.println("Não há arquivos de entrada.");
        }
        catch (IOException ex) {
            System.out.println("Falha ao ler o arquivo.");
        }
    }
    
}
