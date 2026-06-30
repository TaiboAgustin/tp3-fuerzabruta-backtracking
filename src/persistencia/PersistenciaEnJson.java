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

import logica.modelo.Incompatibilidad;
import logica.modelo.Persona;

public class PersistenciaEnJson {
private static final Gson gson=new GsonBuilder().setPrettyPrinting().create();
		
		
		public static void guardarPersona(Persona persona,String archivo) {
				Set<Persona> personal = cargarPersonal(archivo);
				personal.add(persona);
				guardarEquipo(personal, archivo);
			 }
			
		public static Set<Persona> cargarPersonal(String archivo){
			try (FileReader reader = new FileReader(archivo)){
				Type tipoLista= new TypeToken<Set<Persona>>() {}.getType();
				Set<Persona>dtos = gson.fromJson(reader, tipoLista);
				Set<Persona>Personal = new HashSet<>();
				if(dtos!= null) {
					for(Persona dto : dtos) {
						Personal.add(dto);
					}
				}
				return Personal;
			}
			catch(IOException e) {
				throw new RuntimeException("Error en la carga de datos",e);
			}
		}
		public static void guardarEquipo(Set<Persona>equipo , String archivo) {
			List<Persona>dtos = new ArrayList<Persona>();
			for(Persona persona : equipo) {
				dtos.add(persona);
			}
			try(FileWriter writer = new FileWriter(archivo)){
				gson.toJson(dtos,writer);
			}
			catch(IOException e){
				throw new RuntimeException("Error a la carga de Datos",e);
			}
		}
		public static void guardarIncompatibilidad(Incompatibilidad incompatibles,String archivo){
			IncompatibilidadDato dto= convertirAIncompatibilidad(incompatibles);
			try(FileWriter writer = new FileWriter(archivo)){
				gson.toJson(dto,writer);
			}
			catch(IOException e){
				throw new RuntimeException("Error al cargar el dato",e);
			}
		}
		public static void guardarIncompatibilidades(List<Incompatibilidad> incompatibilidades, String archivo){
			List<IncompatibilidadDato> dtos = new ArrayList<>();
			for(Incompatibilidad inc : incompatibilidades){
				dtos.add(convertirAIncompatibilidad(inc));
			}
			try(FileWriter writer = new FileWriter(archivo)){
				gson.toJson(dtos,writer);
			}
			catch(IOException e){
				throw new RuntimeException("Error al guardar las incompatibilidades",e);
			}
		}
		public static List<Incompatibilidad> cargaIncompatibilidad(Set<Persona> personal , String archivo){
			List<Incompatibilidad> listaIncompatibilidad =new ArrayList<Incompatibilidad>();
			try (FileReader reader = new FileReader(archivo)){
				Type tipoLista = new TypeToken<Set<IncompatibilidadDato>>(){}.getType();
				Set<IncompatibilidadDato> dtos = gson.fromJson(reader, tipoLista);
				if(dtos != null){
					for (IncompatibilidadDato dto : dtos) {
						String primeraPersona= dto.getPrimeraPersona();
						String segundaPersona= dto.getSegundaPersona();
						listaIncompatibilidad.add(buscaYcreaIncompatibilidad(personal, primeraPersona, segundaPersona));
					}
				}
				return listaIncompatibilidad;
			} catch (Exception e) {
				throw new RuntimeException("Error a la hora de cargar",e);
			}
		}

		private static Incompatibilidad buscaYcreaIncompatibilidad(Set<Persona> personal, String primeraPersona, String segundaPersona) {
			return new Incompatibilidad(buscarPersonaPorNombre(personal, primeraPersona),buscarPersonaPorNombre(personal,segundaPersona));
		}
		public static Persona buscarPersonaPorNombre(Set<Persona> personas, String nombre) {
    	for (Persona persona : personas) {
        if (persona.getNombre().equals(nombre)) {
            return persona;
        	}
    	}
		throw new IllegalArgumentException( "No existe una persona llamada" + nombre);
	}
		private static IncompatibilidadDato convertirAIncompatibilidad(Incompatibilidad incompatibles) {
			IncompatibilidadDato dto= new IncompatibilidadDato();
			dto.setPrimeraNombre(incompatibles.getPersona1().getNombre());
			dto.setSegundoNombre(incompatibles.getPersona2().getNombre()); 
			return dto;
		}

}
