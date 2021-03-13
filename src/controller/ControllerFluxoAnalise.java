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

package controller;

import java.io.IOException;
import java.util.LinkedList;
import model.Arquivo;
import util.CaminhoInvalidoException;
import util.SemEntradasException;

public class ControllerFluxoAnalise {
    ControllerArquivos arquivoController;
    LinkedList<Arquivo> arquivos;
    AnalisadorLexico analisadorLexico;
    String outputPath;
    
    public ControllerFluxoAnalise(String inputPath,String outputPath ) throws SemEntradasException, CaminhoInvalidoException{
        this.arquivoController = ControllerArquivos.getInstance(inputPath);
        this.arquivos = arquivoController.getArquivos();
        this.outputPath=outputPath;
        analisadorLexico = new AnalisadorLexico();
        if(!arquivoController.isFolder(outputPath)||!arquivoController.isFolder(inputPath)){
            throw new CaminhoInvalidoException();
        }
    }
    
    public void comecarAnalise(){
        arquivos.forEach((Arquivo arq)->{
            String ret = "";
            ret=analisadorLexico.analise(arq.getConteudo());
            String outputFile = "saida";
            String inputFile = arq.getNome();
            for(int i = 13;inputFile.charAt(i) !='.';i++){
                outputFile+=inputFile.charAt(i);
            }
            try {
                arquivoController.escreverArquivo(outputPath+outputFile+".txt", ret);
            } catch (IOException ex) {
                System.out.println("Não foi possível criar o arquivo "+outputPath+outputFile+".txt");
            }
        });
    }
    
}