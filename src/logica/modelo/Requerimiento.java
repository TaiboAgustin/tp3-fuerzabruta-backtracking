package logica.modelo;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class Requerimiento {
    private final Map<Rol, Integer> cantidadesPorRol;

    public Requerimiento(Map<Rol, Integer> cantidadesPorRol) {
        if (cantidadesPorRol == null) {
            throw new IllegalArgumentException("El mapa de cantidades no puede ser nulo.");
        }
        EnumMap<Rol, Integer> copia = new EnumMap<>(Rol.class);
        for (Rol rol : Rol.values()) {
            int cantidad = cantidadesPorRol.getOrDefault(rol, 0);
            if (cantidad < 0) {
                throw new IllegalArgumentException("La cantidad para " + rol + " no puede ser negativa.");
            }
            copia.put(rol, cantidad);
        }
        this.cantidadesPorRol = Collections.unmodifiableMap(copia);
    }

    public int getCantidad(Rol rol) {
        return cantidadesPorRol.getOrDefault(rol, 0);
    }

    public Map<Rol, Integer> getCantidadesPorRol() {
        return cantidadesPorRol;
    }

}
