package logica.algoritmo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import logica.modelo.Incompatibilidad;
import logica.modelo.Persona;
import logica.modelo.Requerimiento;
import logica.modelo.ResultadoEquipo;
import logica.modelo.Rol;

public class AlgoritmoBacktrackingTest {

    private Requerimiento req(Object... pares) {
        Map<Rol, Integer> mapa = new EnumMap<>(Rol.class);
        for (int i = 0; i < pares.length; i += 2) {
            mapa.put((Rol) pares[i], (Integer) pares[i + 1]);
        }
        return new Requerimiento(mapa);
    }

    private List<Incompatibilidad> sinIncompatibilidades() {
        return Collections.emptyList();
    }

    @Test
    public void requerimientoTodosCeroRetornaSinSolucion() {
        Requerimiento r = req(
                Rol.LIDER_DE_PROYECTO, 0,
                Rol.ARQUITECTO, 0,
                Rol.PROGRAMADOR, 0,
                Rol.TESTER, 0);

        ResultadoEquipo resultado = new AlgoritmoBacktracking(
                Collections.emptyList(), sinIncompatibilidades(), r).buscar();

        assertTrue(resultado.esSinSolucion());
    }

    @Test
    public void dosCandidatosDelMismoRolIncompatiblesRetornaSinSolucion() {
        Persona p1 = new Persona("Ana",   Rol.PROGRAMADOR, 4);
        Persona p2 = new Persona("Bruno", Rol.PROGRAMADOR, 5);
        List<Incompatibilidad> incs = List.of(new Incompatibilidad(p1, p2));

        ResultadoEquipo resultado = new AlgoritmoBacktracking(
                List.of(p1, p2), incs, req(Rol.PROGRAMADOR, 2)).buscar();

        assertTrue(resultado.esSinSolucion());
    }

    @Test
    public void liderIncompatibleConUnicoArquitectoRetornaSinSolucion() {
        Persona lider = new Persona("Carlos", Rol.LIDER_DE_PROYECTO, 5);
        Persona arq   = new Persona("Diana",  Rol.ARQUITECTO,        5);
        List<Incompatibilidad> incs = List.of(new Incompatibilidad(lider, arq));

        ResultadoEquipo resultado = new AlgoritmoBacktracking(
                List.of(lider, arq), incs,
                req(Rol.LIDER_DE_PROYECTO, 1, Rol.ARQUITECTO, 1)).buscar();

        assertTrue(resultado.esSinSolucion());
    }

    @Test
    public void candidatosInsuficientesParaUnRolRetornaSinSolucion() {
        Persona p1 = new Persona("Eva",    Rol.PROGRAMADOR, 3);
        Persona p2 = new Persona("Felipe", Rol.PROGRAMADOR, 4);

        ResultadoEquipo resultado = new AlgoritmoBacktracking(
                List.of(p1, p2), sinIncompatibilidades(), req(Rol.PROGRAMADOR, 3)).buscar();

        assertTrue(resultado.esSinSolucion());
    }

    @Test
    public void unSoloCandidatoDisponibleYRequeridoFormaEquipo() {
        Persona prog = new Persona("Gina", Rol.PROGRAMADOR, 3);

        ResultadoEquipo resultado = new AlgoritmoBacktracking(
                List.of(prog), sinIncompatibilidades(), req(Rol.PROGRAMADOR, 1)).buscar();

        assertFalse(resultado.esSinSolucion());
        assertEquals(1, resultado.getIntegrantes().size());
        assertTrue(resultado.getIntegrantes().contains(prog));
        assertEquals(3, resultado.getCalificacionTotal());
    }

    @Test
    public void unCandidatoPorRolTodosCompatiblesFormaEquipo() {
        Persona lider = new Persona("Hugo",  Rol.LIDER_DE_PROYECTO, 4);
        Persona arq   = new Persona("Irene", Rol.ARQUITECTO,        3);

        ResultadoEquipo resultado = new AlgoritmoBacktracking(
                List.of(lider, arq), sinIncompatibilidades(),
                req(Rol.LIDER_DE_PROYECTO, 1, Rol.ARQUITECTO, 1)).buscar();

        assertFalse(resultado.esSinSolucion());
        assertEquals(2, resultado.getIntegrantes().size());
        assertEquals(7, resultado.getCalificacionTotal());
    }

    @Test
    public void variosCandidatosParaUnRolEligeMejorCalificado() {
        Persona p1 = new Persona("Juan",  Rol.TESTER, 2);
        Persona p2 = new Persona("Karla", Rol.TESTER, 5);
        Persona p3 = new Persona("Luis",  Rol.TESTER, 3);

        ResultadoEquipo resultado = new AlgoritmoBacktracking(
                List.of(p1, p2, p3), sinIncompatibilidades(), req(Rol.TESTER, 1)).buscar();

        assertFalse(resultado.esSinSolucion());
        assertEquals(5, resultado.getCalificacionTotal());
        assertTrue(resultado.getIntegrantes().contains(p2));
    }

    @Test
    public void cuatroCandidatosParaDosPuestosEligeMejorPar() {
        Persona a = new Persona("María",   Rol.PROGRAMADOR, 5);
        Persona b = new Persona("Nicolás", Rol.PROGRAMADOR, 4);
        Persona c = new Persona("Olga",    Rol.PROGRAMADOR, 3);
        Persona d = new Persona("Pablo",   Rol.PROGRAMADOR, 2);

        ResultadoEquipo resultado = new AlgoritmoBacktracking(
                List.of(a, b, c, d), sinIncompatibilidades(), req(Rol.PROGRAMADOR, 2)).buscar();

        assertFalse(resultado.esSinSolucion());
        assertEquals(9, resultado.getCalificacionTotal());
        assertTrue(resultado.getIntegrantes().containsAll(List.of(a, b)));
    }

    @Test
    public void incompatibilidadEntreLosDosMaximosEligeTercero() {
        Persona a = new Persona("Rebeca", Rol.PROGRAMADOR, 5);
        Persona b = new Persona("Sergio", Rol.PROGRAMADOR, 4);
        Persona c = new Persona("Tania",  Rol.PROGRAMADOR, 3);
        List<Incompatibilidad> incs = List.of(new Incompatibilidad(a, b));

        ResultadoEquipo resultado = new AlgoritmoBacktracking(
                List.of(a, b, c), incs, req(Rol.PROGRAMADOR, 2)).buscar();

        assertFalse(resultado.esSinSolucion());
        assertEquals(8, resultado.getCalificacionTotal()); // a(5)+c(3)
        assertTrue(resultado.getIntegrantes().containsAll(List.of(a, c)));
    }

    @Test
    public void multiplesRolesMaximizaCalificacionTotal() {
        Persona u = new Persona("Ulises", Rol.LIDER_DE_PROYECTO, 5);
        Persona v = new Persona("Vera",   Rol.LIDER_DE_PROYECTO, 3);
        Persona w = new Persona("Walter", Rol.ARQUITECTO, 4);
        Persona x = new Persona("Ximena", Rol.ARQUITECTO, 2);

        ResultadoEquipo resultado = new AlgoritmoBacktracking(
                List.of(u, v, w, x), sinIncompatibilidades(),
                req(Rol.LIDER_DE_PROYECTO, 1, Rol.ARQUITECTO, 1)).buscar();

        assertFalse(resultado.esSinSolucion());
        assertEquals(9, resultado.getCalificacionTotal()); // 5+4
        assertTrue(resultado.getIntegrantes().containsAll(List.of(u, w)));
    }

    @Test
    public void mejorLiderIncompatibleConMejorArquitectoAjustaSeleccion() {
        Persona l1 = new Persona("Yael",    Rol.LIDER_DE_PROYECTO, 5);
        Persona l2 = new Persona("Zoe",     Rol.LIDER_DE_PROYECTO, 3);
        Persona a1 = new Persona("Alfredo", Rol.ARQUITECTO,        5);
        Persona a2 = new Persona("Beatriz", Rol.ARQUITECTO,        4);
        List<Incompatibilidad> incs = List.of(new Incompatibilidad(l1, a1));

        ResultadoEquipo resultado = new AlgoritmoBacktracking(
                List.of(l1, l2, a1, a2), incs,
                req(Rol.LIDER_DE_PROYECTO, 1, Rol.ARQUITECTO, 1)).buscar();

        assertFalse(resultado.esSinSolucion());
        assertEquals(9, resultado.getCalificacionTotal()); // l1(5)+a2(4)
        assertTrue(resultado.getIntegrantes().containsAll(List.of(l1, a2)));
    }

    @Test
    public void dosTestersIncompatiblesEntreSimismoRolTerceroEntraAlEquipo() {
        Persona t1 = new Persona("Carlos2", Rol.TESTER, 5);
        Persona t2 = new Persona("Diana2",  Rol.TESTER, 5);
        Persona t3 = new Persona("Elena",   Rol.TESTER, 3);
        List<Incompatibilidad> incs = List.of(new Incompatibilidad(t1, t2));

        ResultadoEquipo resultado = new AlgoritmoBacktracking(
                List.of(t1, t2, t3), incs, req(Rol.TESTER, 2)).buscar();

        assertFalse(resultado.esSinSolucion());
        assertEquals(2, resultado.getIntegrantes().size());
        assertEquals(8, resultado.getCalificacionTotal()); // 5+3
        assertTrue(resultado.getIntegrantes().contains(t3));
    }

    @Test
    public void equipoCompletoSinIncompatibilidadesRetornaOptimoGlobal() {
        Persona lider1 = new Persona("Franco", Rol.LIDER_DE_PROYECTO, 5);
        Persona lider2 = new Persona("Gabi",   Rol.LIDER_DE_PROYECTO, 2);
        Persona arq1   = new Persona("Hana",   Rol.ARQUITECTO,        4);
        Persona arq2   = new Persona("Iván",   Rol.ARQUITECTO,        3);
        Persona prog1  = new Persona("Javi",   Rol.PROGRAMADOR,       5);
        Persona prog2  = new Persona("Kira",   Rol.PROGRAMADOR,       4);
        Persona prog3  = new Persona("Luca",   Rol.PROGRAMADOR,       2);
        Persona test1  = new Persona("Mila",   Rol.TESTER,            5);
        Persona test2  = new Persona("Noel",   Rol.TESTER,            3);

        ResultadoEquipo resultado = new AlgoritmoBacktracking(
                List.of(lider1, lider2, arq1, arq2, prog1, prog2, prog3, test1, test2),
                sinIncompatibilidades(),
                req(Rol.LIDER_DE_PROYECTO, 1, Rol.ARQUITECTO, 1,
                    Rol.PROGRAMADOR, 2, Rol.TESTER, 1)).buscar();

        assertFalse(resultado.esSinSolucion());
        assertEquals(5, resultado.getIntegrantes().size());
        assertEquals(23, resultado.getCalificacionTotal()); // 5+4+5+4+5
        assertTrue(resultado.getIntegrantes().containsAll(
                List.of(lider1, arq1, prog1, prog2, test1)));
    }

    @Test
    public void equipoCompletoConIncompatibilidadesRetornaOptimoValido() {
        Persona lider = new Persona("Omar",   Rol.LIDER_DE_PROYECTO, 5);
        Persona arq   = new Persona("Paula",  Rol.ARQUITECTO,        5);
        Persona prog1 = new Persona("Quino",  Rol.PROGRAMADOR,       5);
        Persona prog2 = new Persona("Romina", Rol.PROGRAMADOR,       4);
        Persona prog3 = new Persona("Samuel", Rol.PROGRAMADOR,       3);
        Persona test  = new Persona("Teresa", Rol.TESTER,            5);
        List<Incompatibilidad> incs = List.of(new Incompatibilidad(lider, prog1));

        ResultadoEquipo resultado = new AlgoritmoBacktracking(
                List.of(lider, arq, prog1, prog2, prog3, test), incs,
                req(Rol.LIDER_DE_PROYECTO, 1, Rol.ARQUITECTO, 1,
                    Rol.PROGRAMADOR, 2, Rol.TESTER, 1)).buscar();

        assertFalse(resultado.esSinSolucion());
        assertEquals(22, resultado.getCalificacionTotal()); // 5+5+4+3+5
        assertTrue(resultado.getIntegrantes().contains(lider));
        assertFalse(resultado.getIntegrantes().contains(prog1));
    }

}