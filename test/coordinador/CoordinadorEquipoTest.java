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

    @Test
    public void agregarPersonaLaIncluyeEnLosCandidatos() {
        coordinador.agregarPersona("Ana", Rol.PROGRAMADOR, 4);

        assertEquals(1, coordinador.getCandidatos().size());
        assertTrue(coordinador.getNombresCandidatos().contains("Ana"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void agregarPersonaDuplicadaPorNombreLanzaExcepcion() {
        coordinador.agregarPersona("Ana", Rol.PROGRAMADOR, 4);
        coordinador.agregarPersona("Ana", Rol.TESTER, 2);
    }

    @Test
    public void getCandidatosEsInmodificable() {
        coordinador.agregarPersona("Ana", Rol.PROGRAMADOR, 4);
        try {
            coordinador.getCandidatos().add(new Persona("Beto", Rol.TESTER, 3));
            org.junit.Assert.fail("La lista expuesta no debe poder modificarse desde afuera");
        } catch (UnsupportedOperationException esperada) {
        }
    }

    @Test
    public void hayCandidatosReflejaSiHayAlguno() {
        assertFalse(coordinador.hayCandidatos());
        coordinador.agregarPersona("Ana", Rol.PROGRAMADOR, 4);
        assertTrue(coordinador.hayCandidatos());
    }

    @Test
    public void puedeAgregarIncompatibilidadesRequiereDosCandidatos() {
        coordinador.agregarPersona("Ana", Rol.PROGRAMADOR, 4);
        assertFalse(coordinador.puedeAgregarIncompatibilidades());
        coordinador.agregarPersona("Beto", Rol.PROGRAMADOR, 5);
        assertTrue(coordinador.puedeAgregarIncompatibilidades());
    }

    @Test
    public void getNombresCandidatosDevuelveLosNombresEnOrden() {
        coordinador.agregarPersona("Ana", Rol.PROGRAMADOR, 4);
        coordinador.agregarPersona("Beto", Rol.TESTER, 3);

        assertEquals(2, coordinador.getNombresCandidatos().size());
        assertEquals("Ana", coordinador.getNombresCandidatos().get(0));
        assertEquals("Beto", coordinador.getNombresCandidatos().get(1));
    }

    @Test
    public void agregarIncompatibilidadLaIncluye() {
        coordinador.agregarPersona("Ana", Rol.PROGRAMADOR, 4);
        coordinador.agregarPersona("Beto", Rol.PROGRAMADOR, 5);

        coordinador.agregarIncompatibilidad(0, 1);

        assertEquals(1, coordinador.getIncompatibilidades().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getIncompatibilidadesEsInmodificable() {
        Persona ana  = new Persona("Ana",  Rol.PROGRAMADOR, 4);
        Persona beto = new Persona("Beto", Rol.PROGRAMADOR, 5);
        coordinador.getIncompatibilidades().add(new Incompatibilidad(ana, beto));
    }

    @Test
    public void prepararBusquedaEligeElMejorCandidato() {
        coordinador.agregarPersona("Ana",  Rol.PROGRAMADOR, 3);
        coordinador.agregarPersona("Beto", Rol.PROGRAMADOR, 5);
        coordinador.setCupoRequerido(Rol.PROGRAMADOR, 1);

        ResultadoEquipo resultado = coordinador.prepararBusqueda().get();

        assertFalse(resultado.esSinSolucion());
        assertEquals(5, resultado.getCalificacionTotal());
        assertTrue(resultado.getIntegrantes().stream().anyMatch(p -> "Beto".equals(p.getNombre())));
    }

    @Test
    public void prepararBusquedaCongelaLosCandidatosEnElMomento() {
        coordinador.agregarPersona("Ana", Rol.PROGRAMADOR, 3);
        coordinador.setCupoRequerido(Rol.PROGRAMADOR, 1);

        Supplier<ResultadoEquipo> busqueda = coordinador.prepararBusqueda();
        coordinador.agregarPersona("Beto", Rol.PROGRAMADOR, 5);

        ResultadoEquipo resultado = busqueda.get();

        assertEquals(3, resultado.getCalificacionTotal());
    }

    @Test
    public void prepararBusquedaCongelaLosRequerimientosEnElMomento() {
        coordinador.agregarPersona("Ana", Rol.PROGRAMADOR, 3);
        coordinador.agregarPersona("Tom", Rol.TESTER, 4);
        coordinador.setCupoRequerido(Rol.PROGRAMADOR, 1);

        Supplier<ResultadoEquipo> busqueda = coordinador.prepararBusqueda();
        coordinador.setCupoRequerido(Rol.TESTER, 1);

        ResultadoEquipo resultado = busqueda.get();

        assertEquals(1, resultado.getIntegrantes().size());
        assertEquals(3, resultado.getCalificacionTotal());
    }

    @Test
    public void tieneDatosGuardadosDetectaElArchivoDePersonas() {
        assertFalse(coordinador.tieneDatosGuardados(directorio));
        coordinador.agregarPersona("Ana", Rol.PROGRAMADOR, 4);
        coordinador.guardar(directorio);
        assertTrue(coordinador.tieneDatosGuardados(directorio));
    }

    @Test
    public void guardarYCargarPreservaPersonasEIncompatibilidades() {
        coordinador.agregarPersona("Ana",  Rol.PROGRAMADOR, 4);
        coordinador.agregarPersona("Beto", Rol.TESTER, 3);
        coordinador.agregarIncompatibilidad(0, 1);

        coordinador.guardar(directorio);

        CoordinadorEquipo recargado = new CoordinadorEquipo();
        recargado.cargar(directorio);

        assertEquals(2, recargado.getCandidatos().size());
        assertEquals(1, recargado.getIncompatibilidades().size());
        assertTrue(recargado.getNombresCandidatos().contains("Ana"));
        assertTrue(recargado.getNombresCandidatos().contains("Beto"));
    }

    @Test
    public void cargarDirectorioSinIncompatibilidadesDejaListaVacia() {
        coordinador.agregarPersona("Ana", Rol.PROGRAMADOR, 4);
        coordinador.guardar(directorio);
        borrar(new File(directorio, "incompatibilidades.json"));

        CoordinadorEquipo recargado = new CoordinadorEquipo();
        recargado.cargar(directorio);

        assertEquals(1, recargado.getCandidatos().size());
        assertTrue(recargado.getIncompatibilidades().isEmpty());
    }
}
