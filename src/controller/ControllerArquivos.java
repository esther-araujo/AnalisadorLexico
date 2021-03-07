package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Stream;

public class ControllerArquivos {
    private static ControllerArquivos arquivoController;
    
    private ControllerArquivos(){
    }
    
   
    public static synchronized ControllerArquivos getInstance() throws IOException{
        if(arquivoController == null){
            arquivoController = new ControllerArquivos();
        }
        return arquivoController;
    }
        
    public Iterator lerArquivo(String nome) throws FileNotFoundException{
        String result = new String();
        BufferedReader buffReader = new BufferedReader(new FileReader(nome));
        return buffReader.lines().iterator();
    }

    public long escreverArquivo(String nome, String data) throws IOException{
        if(!inicializarArquivo(nome)){
            System.out.println("Falha ao inicializar arquivo "+nome);
            throw new IOException();
        }
        try (BufferedWriter buffWriter = new BufferedWriter(new FileWriter(nome))) {
            buffWriter.write(data);
            //buffWriter.close();
        }
        return (new File(nome)).lastModified();
    }

    public long lastModify(String nome) throws FileNotFoundException{
        File arq = new File(nome);
        if (!arq.exists())
            throw new FileNotFoundException();
        return arq.lastModified();
    }
        
    public boolean existeArquivo(String nome){
        return new File(nome).exists();
    }

    public boolean inicializarArquivo(String nome) throws IOException{
        File arq = new File(nome);
        if(arq.exists()) return true;
        return arq.createNewFile();
    }
    
}
