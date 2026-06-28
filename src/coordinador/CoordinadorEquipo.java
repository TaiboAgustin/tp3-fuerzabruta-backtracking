package coordinador;

import logica.algoritmo.AlgoritmoBacktracking;
import logica.modelo.Incompatibilidad;
import logica.modelo.Persona;
import logica.modelo.Requerimiento;
import logica.modelo.ResultadoEquipo;
import logica.modelo.Rol;
import persistencia.PersistenciaEnJson;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class CoordinadorEquipo {

    private final List<Persona>          candidatos         = new ArrayList<>();
    private final List<Incompatibilidad> incompatibilidades = new ArrayList<>();
    private final Requerimiento          requerimiento      = new Requerimiento();

    public void agregarPersona(Persona persona) {
        if (candidatos.contains(persona)) {
            throw new IllegalArgumentException("Ya existe una persona con el nombre \"" + persona.getNombre() + "\".");
        }
        candidatos.add(persona);
    }

    public void agregarIncompatibilidad(Incompatibilidad incompatibilidad) {
        incompatibilidades.add(incompatibilidad);
    }

    public void setCupoRequerido(Rol rol, int cantidad) {
        requerimiento.setCupo(rol, cantidad);
    }

    public List<Persona> getCandidatos() {
        return Collections.unmodifiableList(candidatos);
    }

    public List<Incompatibilidad> getIncompatibilidades() {
        return Collections.unmodifiableList(incompatibilidades);
    }

    // Toma una foto inmutable del estado actual y devuelve el cómputo diferido.
    // Debe invocarse en el hilo de UI: así las copias se hacen sin competir con
    // otras mutaciones (agregar, cargar) y el Supplier corre seguro en otro thread.
    public Supplier<ResultadoEquipo> prepararBusqueda() {
        AlgoritmoBacktracking algoritmo = new AlgoritmoBacktracking(
            new ArrayList<>(candidatos),
            new ArrayList<>(incompatibilidades),
            copiarRequerimiento()
        );
        return algoritmo::buscar;
    }

    public void guardar(File directorio) {
        PersistenciaEnJson.guardarEquipo(
            new HashSet<>(candidatos),
            new File(directorio, "personas.json").getPath()
        );
        PersistenciaEnJson.guardarIncompatibilidades(
            incompatibilidades,
            new File(directorio, "incompatibilidades.json").getPath()
        );
    }

    public void cargar(File directorio) {
        Set<Persona> personal = PersistenciaEnJson.cargarPersonal(
            new File(directorio, "personas.json").getPath()
        );

        File incFile = new File(directorio, "incompatibilidades.json");
        List<Incompatibilidad> incs = incFile.exists()
            ? PersistenciaEnJson.cargaIncompatibilidad(personal, incFile.getPath())
            : new ArrayList<>();

        candidatos.clear();
        candidatos.addAll(personal);
        incompatibilidades.clear();
        incompatibilidades.addAll(incs);
    }

    private Requerimiento copiarRequerimiento() {
        Map<Rol, Integer> copia = new EnumMap<>(Rol.class);
        for (Rol rol : Rol.values()) {
            copia.put(rol, requerimiento.getCupo(rol));
        }
        return new Requerimiento(copia);
    }
}
