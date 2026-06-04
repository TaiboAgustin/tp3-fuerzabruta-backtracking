package logica.algoritmo;

import logica.modelo.*;

import java.util.List;

public class AlgoritmoBacktracking {
    private final List<Persona>          candidatos;
    private final List<Incompatibilidad> incompatibilidades;
    private final Requerimiento          requerimiento;

    public AlgoritmoBacktracking(List<Persona> candidatos,
                                  List<Incompatibilidad> incompatibilidades,
                                  Requerimiento requerimiento) {
        this.candidatos         = candidatos;
        this.incompatibilidades = incompatibilidades;
        this.requerimiento      = requerimiento;
    }

    // implementar en Módulo 3
    public ResultadoEquipo buscar() {
        return null;
    }
}
