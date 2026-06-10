package logica.modelo;

import java.util.Objects;

public class Persona {
    private String nombre;
    private Rol rol;
    private int calificacion;

    public Persona(String nombre, Rol rol, int calificacion) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede ser vacío.");
        }
        if (rol == null) {
            throw new IllegalArgumentException("El rol no puede ser nulo.");
        }
        if (calificacion < 1 || calificacion > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5.");
        }
        this.nombre = nombre;
        this.rol = rol;
        this.calificacion = calificacion;
    }

    public String getNombre() { return nombre; }
    public Rol getRol() { return rol; }
    public int getCalificacion() { return calificacion; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Persona persona = (Persona) o;
        return Objects.equals(nombre, persona.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}
