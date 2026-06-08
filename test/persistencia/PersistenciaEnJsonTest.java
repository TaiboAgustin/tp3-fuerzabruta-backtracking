package persistencia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

	        List<Persona> equipo = new ArrayList<>();

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
	}
