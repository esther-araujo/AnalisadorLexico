package util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
public class ReconhecedorCaracteresTest {
    
    @Before
    public void setUp() {
        
    }
    @Test
    public void testIsSpace() {
        assertEquals(false, ReconhecedorCaracteres.isSpace('j'));
        assertEquals(true, ReconhecedorCaracteres.isSpace(' '));
        assertEquals(true, ReconhecedorCaracteres.isSpace('\n'));
        assertEquals(false, ReconhecedorCaracteres.isSpace('b'));
    }

    @Test
    public void testIsChar() {
        assertEquals(true, ReconhecedorCaracteres.isChar('A'));
        assertEquals(true, ReconhecedorCaracteres.isChar('a'));
        assertEquals(true, ReconhecedorCaracteres.isChar('B'));
        assertEquals(true, ReconhecedorCaracteres.isChar('b'));
        assertEquals(true, ReconhecedorCaracteres.isChar('Z'));
        assertEquals(true, ReconhecedorCaracteres.isChar('z'));
        assertEquals(false, ReconhecedorCaracteres.isChar('5'));
        assertEquals(false, ReconhecedorCaracteres.isChar('9'));
    }

    @Test
    public void testIsDigit() {
        assertEquals(true, ReconhecedorCaracteres.isDigit('1'));
        assertEquals(true, ReconhecedorCaracteres.isDigit('2'));
        assertEquals(true, ReconhecedorCaracteres.isDigit('3'));
        assertEquals(true, ReconhecedorCaracteres.isDigit('4'));
        assertEquals(true, ReconhecedorCaracteres.isDigit('5'));
        assertEquals(true, ReconhecedorCaracteres.isDigit('6'));
        assertEquals(true, ReconhecedorCaracteres.isDigit('7'));
        assertEquals(true, ReconhecedorCaracteres.isDigit('8'));
        assertEquals(true, ReconhecedorCaracteres.isDigit('9'));
        assertEquals(false, ReconhecedorCaracteres.isDigit('a'));
        assertEquals(false, ReconhecedorCaracteres.isDigit('c'));
    }
    
}
