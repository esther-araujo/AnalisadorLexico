package view;
import controller.AnalisadorLexico;
import java.io.IOException;
import util.SemEntradasException;

public class Main {

    public static void main(String[] args) {
        AnalisadorLexico analisador;
        try {
            analisador = new AnalisadorLexico("entrada1.txt");
            analisador.analise();
        } catch (SemEntradasException ex) {
            System.out.println("Não há arquivos de entrada.");
        }
        catch (IOException ex) {
            System.out.println("Falha ao ler o arquivo.");
        }
    }
    
}
