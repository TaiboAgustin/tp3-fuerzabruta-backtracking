package persistencia;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import logica.modelo.Persona;

public class PersistenciaEnJson {
private static final Gson gson=new GsonBuilder().setPrettyPrinting().create();
		
		
		public static void guardarPersona(Persona persona,String archivo) {
				PersonaDatos dto = convertirPersonaADatos(persona);
				try(FileWriter writer =new FileWriter(archivo)){
					gson.toJson(dto,writer);
				}
				catch(IOException e) {
					throw new RuntimeException("Error en la carga del Dato",e);
				}
						
		}
		public static Set<Persona> cargarPersonal(String archivo){
			try (FileReader reader = new FileReader(archivo)){
				Type tipoLista= new TypeToken<Set<PersonaDatos>>() {}.getType();
				Set<PersonaDatos>dtos = gson.fromJson(reader, tipoLista);
				Set<Persona>Personal = new HashSet<>();
				if(dtos!= null) {
					for(PersonaDatos dto : dtos) {
						Personal.add(convertirAObjeto(dto));
					}
				}
				return Personal;
			}
			catch(IOException e) {
				throw new RuntimeException("Error en la carga de datos",e);
			}
		}
		public static void guardarEquipo(List<Persona>equipo , String archivo) {
			List<PersonaDatos>dtos = new ArrayList<PersonaDatos>();
			for(Persona persona : equipo) {
				dtos.add(convertirPersonaADatos(persona));
			}
			try(FileWriter writer = new FileWriter(archivo)){
				gson.toJson(dtos,writer);
			}
			catch(IOException e){
				throw new RuntimeException("Error a la carga de Datos",e);
			}
		}

		public static PersonaDatos convertirPersonaADatos(Persona persona) {
			PersonaDatos dto= new PersonaDatos();
			dto._nombre=persona.getNombre();
			dto._rol= persona.getRol();
			dto._calificacion= persona.getCalificacion();
			return dto;
		}
		public static Persona convertirAObjeto(PersonaDatos dto) {
			return new Persona(dto._nombre,dto._rol,dto._calificacion);
		}

}
