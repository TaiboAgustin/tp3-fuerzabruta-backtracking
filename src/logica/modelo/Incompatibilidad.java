package logica.modelo;

public class Incompatibilidad {
    private final Persona persona1;
    private final Persona persona2;

    public Incompatibilidad(Persona persona1, Persona persona2) {
        if (persona1 == null || persona2 == null) {
            throw new IllegalArgumentException("Ninguna persona puede ser nula.");
        }
        if (persona1.equals(persona2)) {
            throw new IllegalArgumentException("Una persona no puede ser incompatible con sí misma.");
        }
        this.persona1 = persona1;
        this.persona2 = persona2;
    }

    public Persona getPersona1() { return persona1; }
    public Persona getPersona2() { return persona2; }

    /** Devuelve true si este par involucra a ambas personas indicadas (en cualquier orden). */
    public boolean involucra(Persona a, Persona b) {
        return (persona1.equals(a) && persona2.equals(b))
            || (persona1.equals(b) && persona2.equals(a));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Incompatibilidad)) return false;
        Incompatibilidad inc = (Incompatibilidad) o;
        return involucra(inc.persona1, inc.persona2);
    }

}
