package logica.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class PersonaTest {

    @Test
    public void constructorValido() {
        Persona p = new Persona("Ana", Rol.PROGRAMADOR, 3);
        assertEquals("Ana", p.getNombre());
        assertEquals(Rol.PROGRAMADOR, p.getRol());
        assertEquals(3, p.getCalificacion());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nombreNuloLanzaExcepcion() {
        new Persona(null, Rol.PROGRAMADOR, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nombreBlankLanzaExcepcion() {
        new Persona("   ", Rol.PROGRAMADOR, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rolNuloLanzaExcepcion() {
        new Persona("Ana", null, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void calificacionMenorAUnoLanzaExcepcion() {
        new Persona("Ana", Rol.PROGRAMADOR, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void calificacionMayorACincoLanzaExcepcion() {
        new Persona("Ana", Rol.PROGRAMADOR, 6);
    }

    @Test
    public void calificacionEnLimitesValidos() {
        new Persona("Ana", Rol.PROGRAMADOR, 1);
        new Persona("Beto", Rol.PROGRAMADOR, 5);
    }

    @Test
    public void equalsBasadoEnNombre() {
        Persona p1 = new Persona("Ana", Rol.PROGRAMADOR, 5);
        Persona p2 = new Persona("Ana", Rol.TESTER, 1);
        assertEquals(p1, p2);
    }

    @Test
    public void equalsDistintoNombre() {
        Persona p1 = new Persona("Ana", Rol.PROGRAMADOR, 3);
        Persona p2 = new Persona("Beto", Rol.PROGRAMADOR, 3);
        assertNotEquals(p1, p2);
    }
}
