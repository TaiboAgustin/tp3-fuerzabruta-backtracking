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

    // @Before hace que este método se ejecute antes de CADA test, 
    // asegurando que siempre arranquemos con un equipo vacío y limpio.
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
        
        assertEquals("La calificación total debe ser la suma exacta de sus integrantes", 
                     totalEsperado, equipo.getCalificacionTotal());
    }

    @Test
    public void testContarPorRol() {
        equipo.agregarPersona(p1); // PROGRAMADOR
        equipo.agregarPersona(p2); // TESTER
        equipo.agregarPersona(p3); // PROGRAMADOR

        assertEquals("Debería haber exactamente 2 programadores", 
                     2, equipo.contarPorRol(Rol.PROGRAMADOR));
        
        assertEquals("Debería haber exactamente 1 tester", 
                     1, equipo.contarPorRol(Rol.TESTER));
        
        assertEquals("No debería haber líderes de proyecto", 
                     0, equipo.contarPorRol(Rol.LIDER_DE_PROYECTO));
    }
}
