package logica.modelo;

import java.util.HashMap;
import java.util.Map;

public class Requerimiento {
    private Map<Rol, Integer> cuposPorRol;

    public Requerimiento() {
        this.cuposPorRol = new HashMap<>();
    }

    public void setCupo(Rol rol, int cantidad) {
        this.cuposPorRol.put(rol, cantidad);
    }

    public int getCupo(Rol rol) {
        return this.cuposPorRol.getOrDefault(rol, 0);
    }
    
    public boolean equipoCompleto(ResultadoEquipo equipo) {
        for (Rol rol : Rol.values()) {
            if (equipo.contarPorRol(rol) != getCupo(rol)) {
                return false;
            }
        }
        return true;
    }
}