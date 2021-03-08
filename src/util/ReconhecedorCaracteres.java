package util;

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
}
