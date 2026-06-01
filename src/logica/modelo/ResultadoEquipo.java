package logica.modelo;

import java.util.HashSet;
import java.util.Set;

public class ResultadoEquipo {
    private Set<Persona> integrantes;

    public ResultadoEquipo() {
        this.integrantes = new HashSet<>();
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

    public int getCalificacionTotal() {
        int total = 0;
        for (Persona p : integrantes) {
            total += p.getCalificacion();
        }
        return total;
    }

    public long contarPorRol(Rol rol) {
        return integrantes.stream()
            .filter(p -> p.getRol() == rol)
            .count();
    }
    
    public Set<Persona> getIntegrantes() {
        return new HashSet<>(integrantes); 
    }
}