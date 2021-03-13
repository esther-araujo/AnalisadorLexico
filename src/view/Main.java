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
