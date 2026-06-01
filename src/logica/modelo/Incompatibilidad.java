package logica.modelo;

import java.util.Objects;

public class Incompatibilidad {
    private Persona persona1;
    private Persona persona2;

    public Incompatibilidad(Persona persona1, Persona persona2) {
        this.persona1 = persona1;
        this.persona2 = persona2;
    }

    public Persona getPersona1() { return persona1; }
    public Persona getPersona2() { return persona2; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Incompatibilidad that = (Incompatibilidad) o;
        
        return (Objects.equals(persona1, that.persona1) && Objects.equals(persona2, that.persona2)) ||
               (Objects.equals(persona1, that.persona2) && Objects.equals(persona2, that.persona1));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(persona1) + Objects.hashCode(persona2);
    }
}