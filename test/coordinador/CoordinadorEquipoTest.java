package coordinador;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Supplier;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import logica.modelo.Incompatibilidad;
import logica.modelo.Persona;
import logica.modelo.ResultadoEquipo;
import logica.modelo.Rol;

public class CoordinadorEquipoTest {

    private CoordinadorEquipo coordinador;
    private File directorio;

    @Before
    public void setUp() throws IOException {
        coordinador = new CoordinadorEquipo();
        directorio = Files.createTempDirectory("coordinador_test").toFile();
    }

    @After
    public void tearDown() {
        borrar(new File(directorio, "personas.json"));
        borrar(new File(directorio, "incompatibilidades.json"));
        borrar(directorio);
    }

    private void borrar(File archivo) {
        if (archivo.exists()) {
            archivo.delete();
        }
    }

    // ─── Alta de personas ────────────────────────────────────────────────────

    @Test
    public void agregarPersonaLaIncluyeEnLosCandidatos() {
        Persona ana = new Persona("Ana", Rol.PROGRAMADOR, 4);

        coordinador.agregarPersona(ana);

        assertEquals(1, coordinador.getCandidatos().size());
        assertTrue(coordinador.getCandidatos().contains(ana));
    }

    @Test(expected = IllegalArgumentException.class)
    public void agregarPersonaDuplicadaPorNombreLanzaExcepcion() {
        coordinador.agregarPersona(new Persona("Ana", Rol.PROGRAMADOR, 4));
        coordinador.agregarPersona(new Persona("Ana", Rol.TESTER, 2));
    }

    @Test
    public void getCandidatosEsInmodificable() {
        coordinador.agregarPersona(new Persona("Ana", Rol.PROGRAMADOR, 4));
        try {
            coordinador.getCandidatos().add(new Persona("Beto", Rol.TESTER, 3));
            org.junit.Assert.fail("La lista expuesta no debe poder modificarse desde afuera");
        } catch (UnsupportedOperationException esperada) {
            // El coordinador es el único dueño de la lista (encapsulamiento).
        }
    }

    // ─── Incompatibilidades ──────────────────────────────────────────────────

    @Test
    public void agregarIncompatibilidadLaIncluye() {
        Persona ana  = new Persona("Ana",  Rol.PROGRAMADOR, 4);
        Persona beto = new Persona("Beto", Rol.PROGRAMADOR, 5);
        coordinador.agregarPersona(ana);
        coordinador.agregarPersona(beto);

        coordinador.agregarIncompatibilidad(new Incompatibilidad(ana, beto));

        assertEquals(1, coordinador.getIncompatibilidades().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getIncompatibilidadesEsInmodificable() {
        Persona ana  = new Persona("Ana",  Rol.PROGRAMADOR, 4);
        Persona beto = new Persona("Beto", Rol.PROGRAMADOR, 5);
        coordinador.getIncompatibilidades().add(new Incompatibilidad(ana, beto));
    }

    // ─── Búsqueda ────────────────────────────────────────────────────────────

    @Test
    public void prepararBusquedaEligeElMejorCandidato() {
        coordinador.agregarPersona(new Persona("Ana",  Rol.PROGRAMADOR, 3));
        Persona beto = new Persona("Beto", Rol.PROGRAMADOR, 5);
        coordinador.agregarPersona(beto);
        coordinador.setCupoRequerido(Rol.PROGRAMADOR, 1);

        ResultadoEquipo resultado = coordinador.prepararBusqueda().get();

        assertFalse(resultado.esSinSolucion());
        assertEquals(5, resultado.getCalificacionTotal());
        assertTrue(resultado.getIntegrantes().contains(beto));
    }

    // Regresión: las personas agregadas DESPUÉS de prepararBusqueda no deben
    // afectar el cómputo. Prueba la copia defensiva de candidatos (race fix).
    @Test
    public void prepararBusquedaCongelaLosCandidatosEnElMomento() {
        coordinador.agregarPersona(new Persona("Ana", Rol.PROGRAMADOR, 3));
        coordinador.setCupoRequerido(Rol.PROGRAMADOR, 1);

        Supplier<ResultadoEquipo> busqueda = coordinador.prepararBusqueda();
        coordinador.agregarPersona(new Persona("Beto", Rol.PROGRAMADOR, 5));

        ResultadoEquipo resultado = busqueda.get();

        assertEquals(3, resultado.getCalificacionTotal());
    }

    // Regresión: cambiar los cupos DESPUÉS de prepararBusqueda no debe afectar
    // el cómputo. Prueba la copia defensiva del Requerimiento.
    @Test
    public void prepararBusquedaCongelaLosRequerimientosEnElMomento() {
        coordinador.agregarPersona(new Persona("Ana", Rol.PROGRAMADOR, 3));
        coordinador.agregarPersona(new Persona("Tom", Rol.TESTER, 4));
        coordinador.setCupoRequerido(Rol.PROGRAMADOR, 1);

        Supplier<ResultadoEquipo> busqueda = coordinador.prepararBusqueda();
        coordinador.setCupoRequerido(Rol.TESTER, 1);

        ResultadoEquipo resultado = busqueda.get();

        assertEquals(1, resultado.getIntegrantes().size());
        assertEquals(3, resultado.getCalificacionTotal());
    }

    // ─── Persistencia ────────────────────────────────────────────────────────

    @Test
    public void guardarYCargarPreservaPersonasEIncompatibilidades() {
        Persona ana  = new Persona("Ana",  Rol.PROGRAMADOR, 4);
        Persona beto = new Persona("Beto", Rol.TESTER, 3);
        coordinador.agregarPersona(ana);
        coordinador.agregarPersona(beto);
        coordinador.agregarIncompatibilidad(new Incompatibilidad(ana, beto));

        coordinador.guardar(directorio);

        CoordinadorEquipo recargado = new CoordinadorEquipo();
        recargado.cargar(directorio);

        assertEquals(2, recargado.getCandidatos().size());
        assertEquals(1, recargado.getIncompatibilidades().size());
        assertTrue(recargado.getCandidatos().contains(ana));
        assertTrue(recargado.getCandidatos().contains(beto));
    }

    @Test
    public void cargarDirectorioSinIncompatibilidadesDejaListaVacia() {
        coordinador.agregarPersona(new Persona("Ana", Rol.PROGRAMADOR, 4));
        coordinador.guardar(directorio);
        borrar(new File(directorio, "incompatibilidades.json"));

        CoordinadorEquipo recargado = new CoordinadorEquipo();
        recargado.cargar(directorio);

        assertEquals(1, recargado.getCandidatos().size());
        assertTrue(recargado.getIncompatibilidades().isEmpty());
    }
}
