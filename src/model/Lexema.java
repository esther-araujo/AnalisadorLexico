package model;

public class Lexema {
    private final String lexema;
    private final int linha;
    private final String id;

    public Lexema(String lexema, int linha, String id) {
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
}
