package logica.modelo;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ResultadoEquipo {
    public static final ResultadoEquipo SIN_SOLUCION =
            new ResultadoEquipo(Collections.emptySet());

    private final Set<Persona> integrantes;

    public ResultadoEquipo(Set<Persona> integrantes) {
        if (integrantes == null) {
            throw new IllegalArgumentException("La lista de equipo no puede ser nula.");
        }
        this.integrantes = Collections.unmodifiableSet(integrantes);
    }

    public ResultadoEquipo() {
        this.integrantes = new HashSet<>();
    }

    public Set<Persona> getIntegrantes() { return integrantes; }

    public int getCalificacionTotal() {
        return integrantes.stream()
                .mapToInt(Persona::getCalificacion)
                .sum();
    }

    public void agregarPersona(Persona p) {
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
