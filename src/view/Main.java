/*
Autores: Cleyton Almeida da Silva e Esther de Santana Araújo
Componente Curricular: MI - Processadores de Linguagens de Programação
Concluido em: 12/03/2021
Declaramos que este código foi elaborado por nós de forma "individual" e não contém nenhum
trecho de código de outro colega ou de outro autor, tais como provindos de livros e
apostilas, e páginas ou documentos eletrônicos da Internet. Qualquer trecho de código
de outra autoria que não a nossa está destacado com uma citação para o autor e a fonte
do código, e estamos ciente que estes trechos não serão considerados para fins de avaliação.
 */

package view;
import controller.ControllerFluxoAnalise;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.CaminhoInvalidoException;
import util.SemEntradasException;

public class Main{

    public static void main(String[] args){
        try {
            String path = new File(".").getCanonicalPath().endsWith("dist") ? "../" : "";
            ControllerFluxoAnalise controllerFluxoAnalise= new ControllerFluxoAnalise(path+"input/", path+"output/");
            
            //Possibilita executar o programa pela raiz ou pela pasta dist (Executando pelo .jar)
            controllerFluxoAnalise.comecarAnalise();
            System.out.println("Executado com sucesso");
        } catch (SemEntradasException ex) {
            System.out.println("Não há arquivos de entrada.");
        } catch (CaminhoInvalidoException ex) {
            System.out.println("O caminho informado para a pasta de entrada e/ou saída é inválido");
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
}
