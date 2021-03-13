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

package util;

/**
 * Reconhece tipos de caracteres 
 */
public class ReconhecedorCaracteres {

    
    public static boolean isSpace(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

    public static boolean isChar(char c) {
        return (c > 64 && c < 91) || (c > 96 && c < 123);
    }

    public static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    public static boolean isUnderline(char c) {
        return c == 95;
    }
    
    public static boolean isValidSymbol(char c) {
        return c!=34 && c >=32 && c<=126;
    }

    public static boolean isDot(char c) {
        return c == 46;
    }

}
