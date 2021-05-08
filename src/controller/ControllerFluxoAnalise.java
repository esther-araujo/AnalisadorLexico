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
import java.util.ArrayList;
import java.util.LinkedList;
import javafx.util.Pair;
import model.Arquivo;
import util.CaminhoInvalidoException;
import util.SemEntradasException;

public class ControllerFluxoAnalise {
    ControllerArquivos arquivoController;
    LinkedList<Arquivo> arquivos;
    AnalisadorLexico analisadorLexico;
    String outputPath;
    
    /**
     * Esta classe irá controlar o fluxo da análise. Futuramente, ela controlará o fluxo de compilação.
     * @param inputPath Caminho para os arquivos de entrada.
     * @param outputPath Caminho para os arquivos de saída.
     * @throws SemEntradasException Caso não haja arquivos de entrada.
     * @throws CaminhoInvalidoException caso o caminho seja inválido.
     */
    public ControllerFluxoAnalise(String inputPath,String outputPath ) throws SemEntradasException, CaminhoInvalidoException{
        this.arquivoController = ControllerArquivos.getInstance(inputPath);
        this.arquivos = arquivoController.getArquivos();//Pega todos os arquivos com nome apropriado na pasta de entrada.
        this.outputPath=outputPath;
        analisadorLexico = new AnalisadorLexico();
        if(!arquivoController.isFolder(outputPath)||!arquivoController.isFolder(inputPath)){
            throw new CaminhoInvalidoException();
        }
    }
    
    public void comecarAnalise(){
        arquivos.forEach((Arquivo arq)->{//Itera os arquivos de entrada passando-os pela análise léxica.
            Pair<ArrayList,String> par = analisadorLexico.analise(arq);//Pega a o conjunto de tokens gerados pelo conteúdo do arquivo.
            ArrayList tokens = par.getKey();
            String ret = par.getValue();
            String outputFile = "saida";
            String inputFile = arq.getNome();
            int i;
            for(i = inputFile.length()-1;i>=0;i--){//Encontra a parte "/entradaX.txt" do nome.
                if(inputFile.charAt(i)=='/')
                    break;
            }
            for(i += 8;inputFile.charAt(i) !='.';i++){//Adiciona o X que indica o número da entrada ao nome do arquivo de saída.
                outputFile+=inputFile.charAt(i);
            }
            try {
                arquivoController.escreverArquivo(outputPath+outputFile+".txt", ret);//Concluída a análise, cria o arquivo de saída correspondente.
            } catch (IOException ex) {
                System.out.println("Não foi possível criar o arquivo "+outputPath+outputFile+".txt");
            }
        });
    }
    
}