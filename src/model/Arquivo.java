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

package model;

import java.util.Iterator;

public class Arquivo {
    private final String nome;
    private final Iterator conteudo;
    int errosLexicos;

    public Arquivo(String nome, Iterator conteudo) {
        this.nome = nome;
        this.conteudo = conteudo;
        errosLexicos=0;
    }

    public String getNome() {
        return nome;
    }

    public Iterator getConteudo() {
        return conteudo;
    }

    public int getErrosLexicos() {
        return errosLexicos;
    }

    public void setErrosLexicos(int errosLexicos) {
        this.errosLexicos = errosLexicos;
    }
    
}
