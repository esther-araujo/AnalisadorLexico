package model;

public class Token {
    private final String lexema;
    private final int linha;
    private final String id;

    public Token(String id, String lexema, int linha) {
        this.lexema = lexema;
        this.linha = linha;
        this.id = id;
    }

    public String getLexema() {
        return lexema;
    }

    public int getLinha() {
        return linha;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%04d", linha)+" "+id+" "+lexema;
    }
}
