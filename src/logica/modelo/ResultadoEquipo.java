package logica.modelo;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ResultadoEquipo {
    public static final ResultadoEquipo SIN_SOLUCION =
            new ResultadoEquipo(Collections.emptySet());

    private final Set<Persona> integrantes;
    private int calificacionTotal;

    public ResultadoEquipo(Set<Persona> integrantes) {
        if (integrantes == null) {
            throw new IllegalArgumentException("La lista de equipo no puede ser nula.");
        }
        this.integrantes = Collections.unmodifiableSet(integrantes);
        this.calificacionTotal = integrantes.stream()
                .mapToInt(Persona::getCalificacion)
                .sum();
    }
    
    public ResultadoEquipo() {
        this.integrantes = new HashSet<>();
        this.calificacionTotal = 0;
    }

	public Set<Persona> getIntegrantes() { return integrantes; }

    public int getCalificacionTotal() { return calificacionTotal; }
    
    public void agregarPersona(Persona p) {
    	calificacionTotal = calificacionTotal + p.getCalificacion();
        integrantes.add(p);
    }

    public void removerPersona(Persona p) {
        integrantes.remove(p);
    }

    public boolean contiene(Persona p) {
        return integrantes.contains(p);
    }

    public boolean esSinSolucion() { return integrantes.isEmpty(); }
    
    public long contarPorRol(Rol rol) {
        return integrantes.stream()
            .filter(p -> p.getRol() == rol)
            .count();
    }


}
