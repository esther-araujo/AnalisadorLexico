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