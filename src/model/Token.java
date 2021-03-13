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
