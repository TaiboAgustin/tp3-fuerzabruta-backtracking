package logica.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class RequerimientoTest {

    private Requerimiento requerimiento;

    @Before
    public void setUp() {
        requerimiento = new Requerimiento();
    }

    @Test
    public void getCupoDevuelveCeroParaRolNoConfigurado() {
        assertEquals(0, requerimiento.getCupo(Rol.PROGRAMADOR));
    }

    @Test
    public void setCupoYGetCupo() {
        requerimiento.setCupo(Rol.PROGRAMADOR, 3);
        assertEquals(3, requerimiento.getCupo(Rol.PROGRAMADOR));
    }

    @Test
    public void setCupoPisaValorAnterior() {
        requerimiento.setCupo(Rol.TESTER, 2);
        requerimiento.setCupo(Rol.TESTER, 4);
        assertEquals(4, requerimiento.getCupo(Rol.TESTER));
    }

    @Test
    public void cadaRolEsIndependiente() {
        requerimiento.setCupo(Rol.PROGRAMADOR, 2);
        requerimiento.setCupo(Rol.TESTER, 1);
        assertEquals(2, requerimiento.getCupo(Rol.PROGRAMADOR));
        assertEquals(1, requerimiento.getCupo(Rol.TESTER));
        assertEquals(0, requerimiento.getCupo(Rol.ARQUITECTO));
    }

    @Test
    public void equipoCompletoRetornaTrueCuandoCuadran() {
        requerimiento.setCupo(Rol.PROGRAMADOR, 1);

        ResultadoEquipo equipo = new ResultadoEquipo();
        equipo.agregarPersona(new Persona("Ana", Rol.PROGRAMADOR, 4));

        assertTrue(requerimiento.equipoCompleto(equipo));
    }

    @Test
    public void equipoCompletoRetornaFalseCuandoFaltanIntegrantes() {
        requerimiento.setCupo(Rol.PROGRAMADOR, 2);

        ResultadoEquipo equipo = new ResultadoEquipo();
        equipo.agregarPersona(new Persona("Ana", Rol.PROGRAMADOR, 4));

        assertFalse(requerimiento.equipoCompleto(equipo));
    }

    @Test
    public void equipoCompletoRetornaFalseCuandoSobranIntegrantes() {
        requerimiento.setCupo(Rol.PROGRAMADOR, 1);

        ResultadoEquipo equipo = new ResultadoEquipo();
        equipo.agregarPersona(new Persona("Ana",  Rol.PROGRAMADOR, 4));
        equipo.agregarPersona(new Persona("Beto", Rol.PROGRAMADOR, 3));

        assertFalse(requerimiento.equipoCompleto(equipo));
    }
}
