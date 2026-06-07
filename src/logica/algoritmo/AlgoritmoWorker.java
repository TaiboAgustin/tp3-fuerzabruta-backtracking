package logica.algoritmo;

import java.util.List;
import java.util.function.Consumer;

import javax.swing.SwingWorker;

import logica.modelo.Incompatibilidad;
import logica.modelo.Persona;
import logica.modelo.Requerimiento;
import logica.modelo.ResultadoEquipo;

public class AlgoritmoWorker extends SwingWorker<ResultadoEquipo, Void> {
    private final List<Persona>          personas;
    private final List<Incompatibilidad> incompatibilidades;
    private final Requerimiento          requerimiento;
    private final Consumer<ResultadoEquipo> onCompletado;
    private final Consumer<Exception>       onError;

    public AlgoritmoWorker(List<Persona>             personas,
                           List<Incompatibilidad>    incompatibilidades,
                           Requerimiento             requerimiento,
                           Consumer<ResultadoEquipo> onCompletado,
                           Consumer<Exception>       onError) {
        this.personas           = personas;
        this.incompatibilidades = incompatibilidades;
        this.requerimiento      = requerimiento;
        this.onCompletado       = onCompletado;
        this.onError            = onError;
    }

    @Override
    protected ResultadoEquipo doInBackground() {
        AlgoritmoBacktracking algoritmo = new AlgoritmoBacktracking();
        return algoritmo.resolver(personas, incompatibilidades, requerimiento);
    }

    @Override
    protected void done() {
        try {
            ResultadoEquipo resultado = get();
            onCompletado.accept(resultado);
        } catch (Exception e) {
            onError.accept(e);
        }
    }


}
