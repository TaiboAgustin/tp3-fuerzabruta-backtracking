package logica.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ResultadoEquipoTest {

    private ResultadoEquipo equipo;
    private Persona p1;
    private Persona p2;
    private Persona p3;
    
    @Before
    public void setUp() {
        equipo = new ResultadoEquipo();
        p1 = new Persona("Ana", Rol.PROGRAMADOR, 5);
        p2 = new Persona("Beto", Rol.TESTER, 3);
        p3 = new Persona("Carlos", Rol.PROGRAMADOR, 4);
    }

    @Test
    public void testAgregarYContienePersona() {
        equipo.agregarPersona(p1);
        assertTrue("El equipo debería contener a Ana", equipo.contiene(p1));
        assertFalse("El equipo NO debería contener a Beto", equipo.contiene(p2));
    }

    @Test
    public void testRemoverPersona() {
        equipo.agregarPersona(p1);
        equipo.removerPersona(p1);
        
        assertFalse("El equipo ya no debería contener a Ana tras ser removida", equipo.contiene(p1));
    }

    @Test
    public void testGetCalificacionTotal() {
        equipo.agregarPersona(p1); // Calificación: 5
        equipo.agregarPersona(p2); // Calificación: 3
        equipo.agregarPersona(p3); // Calificación: 4

        int totalEsperado = 12; // 5 + 3 + 4
        
        assertEquals("La calificación total debe ser la suma exacta de sus integrantes", totalEsperado, equipo.getCalificacionTotal());
    }

    @Test
    public void testContarPorRol() {
        equipo.agregarPersona(p1); // PROGRAMADOR
        equipo.agregarPersona(p2); // TESTER
        equipo.agregarPersona(p3); // PROGRAMADOR

        assertEquals("Debería haber exactamente 2 programadores", 2L, equipo.contarPorRol(Rol.PROGRAMADOR));
        assertEquals("Debería haber exactamente 1 tester", 1L, equipo.contarPorRol(Rol.TESTER));
        assertEquals("No debería haber líderes de proyecto", 0L, equipo.contarPorRol(Rol.LIDER_DE_PROYECTO));
    }

    @Test
    public void testRemoverPersonaInexistente() {
        equipo.removerPersona(p1);
        
        assertFalse("Ana no debería estar en el equipo", equipo.contiene(p1));
        assertEquals("La calificación total no debe alterarse al intentar borrar a alguien que no existe", 0, equipo.getCalificacionTotal());
    }

    @Test
    public void testAgregarPersonaDuplicada() {
        equipo.agregarPersona(p1); // Agregamos a Ana
        equipo.agregarPersona(p1); // Intentamos agregar a Ana de nuevo
        
        assertEquals("La calificación no debe sumar a la misma persona dos veces", 5, equipo.getCalificacionTotal());
        assertEquals("Solo debería contabilizarse a la persona una vez en su rol", 1L, equipo.contarPorRol(Rol.PROGRAMADOR));
    }

    @Test
    public void testEquipoVacio() {
        assertEquals("La calificación de un equipo vacío debe ser 0", 0, equipo.getCalificacionTotal());
        assertEquals("No debe haber integrantes contados por rol en un equipo vacío", 0L, equipo.contarPorRol(Rol.TESTER));
    }
}