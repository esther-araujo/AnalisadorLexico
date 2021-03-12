package view;
import controller.ControllerFluxoAnalise;
import util.CaminhoInvalidoException;
import util.SemEntradasException;

public class Main {

    public static void main(String[] args) {
        try {
            ControllerFluxoAnalise controllerFluxoAnalise= new ControllerFluxoAnalise("input/", "output/");
            controllerFluxoAnalise.comecarAnalise();
        } catch (SemEntradasException ex) {
            System.out.println("Não há arquivos de entrada.");
        } catch (CaminhoInvalidoException ex) {
            System.out.println("O caminho informado para a pasta de entrada e/ou saída é inválido");
        }
    }
    
}
