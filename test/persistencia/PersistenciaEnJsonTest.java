package persistencia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import logica.modelo.Incompatibilidad;
import logica.modelo.Persona;
import logica.modelo.Rol;
import persistencia.PersistenciaEnJson;
import persistencia.PersonaDatos;

public class PersistenciaEnJsonTest {

	    private static final String ARCHIVO_PERSONA = "persona_test.json";
	    private static final String ARCHIVO_EQUIPO = "equipo_test.json";

	    @Before
	    public void setUp() {
	        borrarArchivo(ARCHIVO_PERSONA);
	        borrarArchivo(ARCHIVO_EQUIPO);
	    }

	    @After
	    public void tearDown() {
	        borrarArchivo(ARCHIVO_PERSONA);
	        borrarArchivo(ARCHIVO_EQUIPO);
	    }

	    private void borrarArchivo(String nombreArchivo) {
	        File archivo = new File(nombreArchivo);
	        if (archivo.exists()) {
	            archivo.delete();
	        }
	    }

	    @Test
	    public void testConvertirPersonaADatos() {

	        Persona persona = new Persona("Juan", Rol.PROGRAMADOR, 3);

	        PersonaDatos dto = PersistenciaEnJson.convertirPersonaADatos(persona);

	        assertEquals("Juan", dto._nombre);
	        assertEquals(Rol.PROGRAMADOR, dto._rol);
	        assertEquals(3, dto._calificacion);
	    }

	    @Test
	    public void testConvertirAObjeto() {

	        PersonaDatos dto = new PersonaDatos();
	        dto._nombre = "Ana";
	        dto._rol = Rol.TESTER;
	        dto._calificacion = 4;

	        Persona persona = PersistenciaEnJson.convertirAObjeto(dto);

	        assertEquals("Ana", persona.getNombre());
	        assertEquals(Rol.TESTER, persona.getRol());
	        assertEquals(4, persona.getCalificacion());
	    }

	    @Test
	    public void testGuardarYCargarEquipo() {

	        Set<Persona> equipo = new HashSet<>();

	        equipo.add(new Persona("Juan", Rol.PROGRAMADOR, 3));
	        equipo.add(new Persona("Ana", Rol.TESTER, 4));

	        PersistenciaEnJson.guardarEquipo(equipo, ARCHIVO_EQUIPO);

	        Set<Persona> resultado = PersistenciaEnJson.cargarPersonal(ARCHIVO_EQUIPO);

	        assertEquals(2, resultado.size());

	        assertTrue(resultado.contains(
	                new Persona("Juan", Rol.PROGRAMADOR, 3)));

	        assertTrue(resultado.contains(
	                new Persona("Ana", Rol.TESTER, 4)));
	    }
	@Test
    public void buscarPersonaPorNombreExistente() {

        Set<Persona> personal = new HashSet<>();

        Persona juan = new Persona("Juan", Rol.PROGRAMADOR, 3);
        Persona ana = new Persona("Ana", Rol.TESTER, 4);

        personal.add(juan);
        personal.add(ana);

        Persona encontrada =
                PersistenciaEnJson.buscarPersonaPorNombre(personal, "Juan");

        assertNotNull(encontrada);
        assertEquals("Juan", encontrada.getNombre());
    }

    @Test(expected = IllegalArgumentException.class)
    public void buscarPersonaPorNombreInexistente() {

        Set<Persona> personal = new HashSet<>();

        personal.add(new Persona("Juan", Rol.PROGRAMADOR, 3));

        PersistenciaEnJson.buscarPersonaPorNombre(personal, "Pedro");
   		}
	@Test
	public void crearIncompatibilidadCorrectamente() {

   		 Set<Persona> personal = new HashSet<>();

    	Persona juan = new Persona("Juan", Rol.PROGRAMADOR, 3);
    	Persona ana = new Persona("Ana", Rol.TESTER, 4);

    	personal.add(juan);
    	personal.add(ana);

    	Incompatibilidad incompatibilidad =new Incompatibilidad(
                    PersistenciaEnJson.buscarPersonaPorNombre(personal, "Juan"),
                    PersistenciaEnJson.buscarPersonaPorNombre(personal, "Ana"));

    assertEquals(juan, incompatibilidad.getPersona1());
    assertEquals(ana, incompatibilidad.getPersona2());
}
	}
