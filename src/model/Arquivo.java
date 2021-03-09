package model;

import java.util.Iterator;

public class Arquivo {
    private final String nome;
    private final Iterator conteudo;

    public Arquivo(String nome, Iterator conteudo) {
        this.nome = nome;
        this.conteudo = conteudo;
    }

    public String getNome() {
        return nome;
    }

    public Iterator getConteudo() {
        return conteudo;
    }
    
}
