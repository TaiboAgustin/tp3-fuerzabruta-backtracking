package logica.algoritmo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.SwingWorker;

import logica.modelo.Incompatibilidad;
import logica.modelo.Persona;
import logica.modelo.Requerimiento;
import logica.modelo.ResultadoEquipo;
import logica.modelo.Rol;

public class AlgoritmoBacktracking {
	
    private Set<Persona> mejorEquipo;
    private int mejorCalificacion;
    private int[] maxCalificacionRestante;
    private List<Incompatibilidad> incompatibilidades;
    private final List<Persona> candidatos;
    private final Requerimiento requerimiento;

    public AlgoritmoBacktracking(List<Persona> candidatos, List<Incompatibilidad> incompatibilidades, Requerimiento requerimiento) {
        this.candidatos         = candidatos;
        this.incompatibilidades = incompatibilidades;
        this.requerimiento      = requerimiento;
    }


	public ResultadoEquipo buscar() {

		Map<Rol, List<Persona>> candidatosOrdenadosPorRol = agruparCandidatosPorRol(candidatos);
		Rol[] rolesAUtilizar = seleccionarRolesRequeridos(requerimiento);

		if(!verificarSolucionPosible(candidatosOrdenadosPorRol, rolesAUtilizar, requerimiento)) {
			return ResultadoEquipo.SIN_SOLUCION;
		}
		
        this.mejorEquipo       = null;
        this.mejorCalificacion = Integer.MIN_VALUE;
        this.incompatibilidades = incompatibilidades;
        this.maxCalificacionRestante = calcularMaxCalificacionRestantePosible(candidatosOrdenadosPorRol, rolesAUtilizar, requerimiento);

        backtrack(candidatosOrdenadosPorRol, rolesAUtilizar, requerimiento, 0, new ArrayList<>(), 0);

        return mejorEquipo == null
                ? ResultadoEquipo.SIN_SOLUCION
                : new ResultadoEquipo(mejorEquipo);	
       }

	private int[] calcularMaxCalificacionRestantePosible(Map<Rol, List<Persona>> candidatosOrdenadosPorRol, Rol[] rolesAUtilizar, Requerimiento requerimiento) {
        int n = rolesAUtilizar.length;
        int[] maxCalPorRol = new int[n];
        for (int i = 0; i < n; i++) {
            Rol rol = rolesAUtilizar[i];
            List<Persona> candidatos = candidatosOrdenadosPorRol.get(rol);
            int k = requerimiento.getCupo(rol);
            int suma = 0;
            for (int j = 0; j < k && j < candidatos.size(); j++) {
                suma += candidatos.get(j).getCalificacion();
            }
            maxCalPorRol[i] = suma;
        }

        int[] cantidadRestantePorRol = new int[n + 1];
        for (int i = n - 1; i >= 0; i--) {
            cantidadRestantePorRol[i] = cantidadRestantePorRol[i + 1] + maxCalPorRol[i];
        }
        return cantidadRestantePorRol;
	}

	private boolean verificarSolucionPosible(Map<Rol, List<Persona>> candidatosOrdenadosPorRol, Rol[] rolesAUtilizar, Requerimiento requerimiento) {
		for (Rol rol : rolesAUtilizar) {
			if (candidatosOrdenadosPorRol.get(rol).size() < requerimiento.getCupo(rol)) {
				return false;
			}
		}
		return true;
	}

	private Rol[] seleccionarRolesRequeridos(Requerimiento requerimiento) {
		List<Rol> rolesTemp = new ArrayList<>();
		for (Rol r : Rol.values()) {
			if (requerimiento.getCupo(r) > 0) {
				rolesTemp.add(r);
			}
		}
		return rolesTemp.toArray(new Rol[0]);
	}

	private Map<Rol, List<Persona>> agruparCandidatosPorRol(List<Persona> personas) {
		Map<Rol, List<Persona>> candidatosPorRol = new EnumMap<>(Rol.class);
		for (Rol rol : Rol.values()) {
			List<Persona> grupo = new ArrayList<>();
			for (Persona persona : personas) {
				if (persona.getRol() == rol)
					grupo.add(persona);
			}
			grupo.sort(Comparator.comparingInt(Persona::getCalificacion).reversed());
			candidatosPorRol.put(rol, grupo);
		}

		return candidatosPorRol;
	}

	private void backtrack(Map<Rol, List<Persona>> candidatosPorRol, Rol[] rolesRequeridos, Requerimiento requerimiento, int indiceRol, List<Persona> equipo, int calParcial) {

        if (indiceRol == rolesRequeridos.length) {
            if (calParcial > mejorCalificacion) {
                mejorCalificacion = calParcial;
                mejorEquipo = new HashSet<>(equipo);
            }
            return;
        }

        int calMaxPosible = calParcial + maxCalificacionRestante[indiceRol];
        if (calMaxPosible <= mejorCalificacion) {
            return;
        }

        Rol rolActual = rolesRequeridos[indiceRol];
        List<Persona> candidatos = candidatosPorRol.get(rolActual);
        int necesarios = requerimiento.getCupo(rolActual);

        elegirCombinacion(candidatos, necesarios, 0, new ArrayList<>(),
                candidatosPorRol, rolesRequeridos, requerimiento,
                indiceRol, equipo, calParcial);
	}

	private void elegirCombinacion(List<Persona> candidatos, int candidatosNecesariosParaRolActual, int inicio, List<Persona> elegidos, Map<Rol, List<Persona>> candidatosPorRol, Rol[] rolesRequeridos, Requerimiento requerimiento, int indiceRol, List<Persona> equipoParcial, int calParcial) {

        if (elegidos.size() == candidatosNecesariosParaRolActual) {
            backtrack(candidatosPorRol, rolesRequeridos, requerimiento, indiceRol + 1, equipoParcial, calParcial);
            return;
        }

        int faltanElegir = candidatosNecesariosParaRolActual - elegidos.size();
        int disponibles  = candidatos.size() - inicio;

        if (disponibles < faltanElegir) return;

        for (int i = inicio; i < candidatos.size(); i++) {
            Persona candidato = candidatos.get(i);

            if (esIncompatibleConEquipo(candidato, equipoParcial)) continue;
            if (esIncompatibleConEquipo(candidato, elegidos)) continue;

            elegidos.add(candidato);
            equipoParcial.add(candidato);
            int nuevaCal = calParcial + candidato.getCalificacion();

            elegirCombinacion(candidatos, candidatosNecesariosParaRolActual, i + 1, elegidos,
                    candidatosPorRol, rolesRequeridos, requerimiento,
                    indiceRol, equipoParcial, nuevaCal);

            elegidos.remove(elegidos.size() - 1);
            equipoParcial.remove(equipoParcial.size() - 1);
        }
	}

	private boolean esIncompatibleConEquipo(Persona persona, List<Persona> grupo) {
		for (Persona miembro : grupo) {
			for (Incompatibilidad inc : incompatibilidades) {
				if (inc.involucra(persona, miembro))
					return true;
			}
		}
		return false;
	}
}
