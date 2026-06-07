package logica.modelo;

import java.util.Collections;
import java.util.List;

public class ResultadoEquipo {
    public static final ResultadoEquipo SIN_SOLUCION =
            new ResultadoEquipo(Collections.emptyList());

    private final List<Persona> equipo;
    private final int calificacionTotal;

    public ResultadoEquipo(List<Persona> equipo) {
        if (equipo == null) {
            throw new IllegalArgumentException("La lista de equipo no puede ser nula.");
        }
        this.equipo = Collections.unmodifiableList(equipo);
        this.calificacionTotal = equipo.stream()
                .mapToInt(Persona::getCalificacion)
                .sum();
    }

    public List<Persona> getEquipo() { return equipo; }

    public int getCalificacionTotal() { return calificacionTotal; }

    public boolean esSinSolucion() { return equipo.isEmpty(); }

}
