package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Arquivo;
import util.ReconhecedorCaracteres;
import util.SemEntradasException;

public class ControllerArquivos {
    private static ControllerArquivos arquivoController;
    private final String path;
    private LinkedList<String> arquivosDeEntrada;
    
    private ControllerArquivos(String path) throws SemEntradasException{
        this.path = path;
        arquivosEntrada();
    }
    
    /**
     * Aplicação do padrão Singleton, a fim de manter possíveis variáveis de estado iguais para chamadas em esocpos diferentes.
     * @param path Caminho para a pasta com os arquivos de entrada. Caso seja passado o nome de um arquivo, apenas ele será tratado.
     * @return instância única do ControllerArquivos.
     */
    public static synchronized ControllerArquivos getInstance(String path) throws SemEntradasException {
        if(arquivoController == null){
            arquivoController = new ControllerArquivos(path);
        }
        return arquivoController;
    }
    
    /**
     * Encontra todos os arquivos na pasta apontada como origem de entradas
     * @return Lista com os nomes dos arquivos
     */
    private LinkedList<String> arquivosPastaEntrada() throws SemEntradasException{
        LinkedList<String> ret = new LinkedList<>();
        File pasta = new File(path);
        if (pasta.isFile()){
            ret.add(pasta.getName());
            return ret;
        }
        File[] listaDeArquivos = pasta.listFiles();
        if(listaDeArquivos==null){
            throw new SemEntradasException();
        }
        for(File file : listaDeArquivos){
            ret.add(file.getName());
        }
        return ret;
    }
    
    /**
     * Encontra os arquivos com nome do tipo "entradaX.txt".
     * Observe que não é permitido a entrada com caixa alta.
     * @return Lista com os nomes dos arquivos com o nome no formato exigido
     */
    private void arquivosEntrada() throws SemEntradasException{
        LinkedList<String> ret = new LinkedList<>();
        LinkedList<String> arquivos = arquivosPastaEntrada();
        arquivos.forEach((nome) -> {
            int i = 0;
            if (nome.length()>11&&nome.charAt(i)=='e'){
                i++;
                if (nome.charAt(i)=='n'){
                    i++;
                    if (nome.charAt(i)=='t'){ 
                        i++;
                        if (nome.charAt(i)=='r'){ 
                            i++;
                            if (nome.charAt(i)=='a'){ 
                                i++;
                                if (nome.charAt(i)=='d'){ 
                                    i++;
                                    if (nome.charAt(i)=='a') {
                                        //Esperando dígitos a partir do próximo caractere (i+1).
                                        //Para também se o tamanho do nome não possibilitar  a ocorrência de .txt;
                                        for(i++;nome.length()>i+3&&ReconhecedorCaracteres.isDigit(nome.charAt(i));i++){
                                        }
                                        if (nome.charAt(i)=='.') {
                                            i++;
                                            if (nome.charAt(i)=='t') {
                                                i++;
                                                if (nome.charAt(i)=='x') {
                                                    i++;
                                                    if (nome.charAt(i)=='t') 
                                                        ret.add(path+nome);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
        arquivosDeEntrada = ret;
    }
    
    /**
     * Recupera o conteúdo de um arquivo dado.
     * @param nome nome do arquivo a ser lido.
     * @return Iterador para o conjunto de linhas do arquivo.
     * @throws FileNotFoundException Caso seja impossibilitada a leitura do arquivo.
     */
    private Iterator lerArquivo(String nome) throws FileNotFoundException, IOException{
        String result = new String();
        BufferedReader buffReader = new BufferedReader(new FileReader(nome));
//        LinkedList<String> linhas = new LinkedList<>();
//        String linha = buffReader.readLine();
//        do{
//            linhas.add(linha);
//            linha = buffReader.readLine();
//        }while(linha!=null);
//        buffReader.close();
        return buffReader.lines().iterator();//linhas.iterator();
    }
    
    /**
     * Busca os nomes dos arquivos e seus respectivos conteúdos presentes na pasta de entrada.
     * @return Lista de instâncias da classe Arquivo gerada a partir dos arquivos de entrada.
     * @throws util.SemEntradasException Caso não haja arquivos de entrada que possam ser lidos.
     */
    public LinkedList<Arquivo> getArquivos() throws SemEntradasException{
        LinkedList<Arquivo> ret = new LinkedList<>();
        arquivosDeEntrada.forEach((String nome)->{
            try{
                ret.add(new Arquivo(nome, lerArquivo(nome)));
            }catch(FileNotFoundException E){
                System.out.println("O arquivo "+nome+" não pôde ser lido.");
            } catch (IOException ex) {
                System.out.println("O arquivo "+nome+" não foi encontrado.");;
            }
        });
        if(ret.isEmpty())
            throw new SemEntradasException();
        return ret;
    }
    
    /**
     * Verifica se um dado caminho é uma pasta ou não.
     * @param path caminho recebido.
     * @return true caso seja pasta, false caso não seja.
     */
    public boolean isFolder(String path){
        File folder = new File(path);
        return folder.isDirectory();
    }
    
    /**
     * Escreve uma mensagem de texto em um arquivo dado. O conteúdo anterior é perdido.
     * @param nome nome do arquivo em que será feita a escrita.
     * @param data conteúdo (textual) a ser escrito no arquivo.
     * @return long contendo informações do momento de modificação.
     * @throws IOException caso não consiga realizar a escrita.
     */
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

    /**
     * Busca o instante da última modificação em um arquivo dado
     * @param nome nome do arquivo
     * @return long correspondente à data desejada
     * @throws FileNotFoundException
     * @deprecated não será utilizado neste problema
     */
    @Deprecated
    public long lastModify(String nome) throws FileNotFoundException{
        File arq = new File(nome);
        if (!arq.exists())
            throw new FileNotFoundException();
        return arq.lastModified();
    }
     
    /**
     * Verifica se um arquivo existe.
     * @param nome nome do arquivo.
     * @return true se existe, false se não.
     * @deprecated não será utilizado neste problema.
     */
    @Deprecated
    public boolean existeArquivo(String nome){
        return new File(nome).exists();
    }
    
    /**
     * Inicializa um arquivo (pode ser utilizao em rotinas de escrita).
     * @param nome nomde do arquivo.
     * @return true se o arquivo existe ou foi criado com sucesso, false caso contrário.
     * @throws IOException caso o arquivo não exista e não tenha sido possível criá-lo.
     * @deprecated não será utilizado neste problema.
     */
    @Deprecated
    public boolean inicializarArquivo(String nome) throws IOException{
        File arq = new File(nome);
        if(arq.exists()) return true;
        return arq.createNewFile();
    }
    
}
