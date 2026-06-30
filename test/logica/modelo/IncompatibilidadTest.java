package logica.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class IncompatibilidadTest {

    private Persona p1;
    private Persona p2;
    private Persona p3;

    @Before
    public void setUp() {
        p1 = new Persona("Ana",    Rol.PROGRAMADOR, 4);
        p2 = new Persona("Beto",   Rol.TESTER,      3);
        p3 = new Persona("Carlos", Rol.ARQUITECTO,  5);
    }

    @Test
    public void involucraEnOrdenDirecto() {
        Incompatibilidad inc = new Incompatibilidad(p1, p2);
        assertTrue(inc.involucra(p1, p2));
    }

    @Test
    public void involucraEsSimetrico() {
        Incompatibilidad inc = new Incompatibilidad(p1, p2);
        assertTrue(inc.involucra(p2, p1));
    }

    @Test
    public void involucraDevuelveFalseParaPersonasDistintas() {
        Incompatibilidad inc = new Incompatibilidad(p1, p2);
        assertFalse(inc.involucra(p1, p3));
        assertFalse(inc.involucra(p3, p2));
    }

    @Test
    public void equalsEsSimetrico() {
        Incompatibilidad inc1 = new Incompatibilidad(p1, p2);
        Incompatibilidad inc2 = new Incompatibilidad(p2, p1);
        assertEquals(inc1, inc2);
    }

    @Test
    public void equalsDevuelveFalseParaParDistinto() {
        Incompatibilidad inc1 = new Incompatibilidad(p1, p2);
        Incompatibilidad inc2 = new Incompatibilidad(p1, p3);
        assertNotEquals(inc1, inc2);
    }

    @Test
    public void hashCodeConsistenteConEquals() {
        Incompatibilidad inc1 = new Incompatibilidad(p1, p2);
        Incompatibilidad inc2 = new Incompatibilidad(p2, p1);
        assertEquals(inc1.hashCode(), inc2.hashCode());
    }
}
