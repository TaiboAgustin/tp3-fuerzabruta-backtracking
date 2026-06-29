package logica.algoritmo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private Map<Rol, List<Persona>> elegidosEnProcesoOrdenadosPorRol;
    private int calParcial;
    private List<Persona> equipoParcial;
    private final Requerimiento requerimiento;
    private final Rol[] rolesRequeridos;
    Map<Rol, List<Persona>> candidatosOrdenadosPorRol = new EnumMap<>(Rol.class);

    public AlgoritmoBacktracking(List<Persona> candidatos, List<Incompatibilidad> incompatibilidades, Requerimiento requerimiento) {
        this.incompatibilidades         = incompatibilidades;
        this.requerimiento              = requerimiento;
        this.rolesRequeridos             = seleccionarRolesRequeridos(requerimiento);
        this.candidatosOrdenadosPorRol  = agruparCandidatosPorRol(candidatos);
        this.elegidosEnProcesoOrdenadosPorRol = new HashMap<>();
        this.equipoParcial = new ArrayList<>();
    }


	public ResultadoEquipo buscar() {

		if(!verificarSolucionPosible()) {
			return ResultadoEquipo.SIN_SOLUCION;
		}
		
        this.mejorEquipo       = null;
        this.mejorCalificacion = Integer.MIN_VALUE;
        this.maxCalificacionRestante = calcularMaxCalificacionRestantePosible();

        backtrack(0);

        return mejorEquipo == null
                ? ResultadoEquipo.SIN_SOLUCION
                : new ResultadoEquipo(mejorEquipo);	
       }

	private int[] calcularMaxCalificacionRestantePosible() {
        int n = rolesRequeridos.length;
        int[] maxCalPorRol = new int[n];
        for (int i = 0; i < n; i++) {
            Rol rol = rolesRequeridos[i];
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

	private boolean verificarSolucionPosible() {
		for (Rol rol : rolesRequeridos) {
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
		for (Rol rol : Rol.values()) {
			List<Persona> grupo = new ArrayList<>();
			for (Persona persona : personas) {
				if (persona.getRol() == rol)
					grupo.add(persona);
			}
			grupo.sort(Comparator.comparingInt(Persona::getCalificacion).reversed());
            candidatosOrdenadosPorRol.put(rol, grupo);
		}
		return candidatosOrdenadosPorRol;
	}

	private void backtrack(int indiceRol) {
        if (indiceRol == rolesRequeridos.length) {
            if (calParcial > mejorCalificacion) {
                mejorCalificacion = calParcial;
                mejorEquipo = new HashSet<>(equipoParcial);
            }
            return;
        }

        int calMaxPosible = calParcial + maxCalificacionRestante[indiceRol];
        if (calMaxPosible <= mejorCalificacion) {
            return;
        }

        Rol rolActual = rolesRequeridos[indiceRol];
        this.elegidosEnProcesoOrdenadosPorRol.put(rolActual, new ArrayList<Persona>());

        elegirCombinacion(rolActual, 0, indiceRol);
	}

	private void elegirCombinacion(Rol rolActual, int inicio, int indiceRol) {
        List<Persona> candidatos = candidatosOrdenadosPorRol.get(rolActual);
        int candidatosNecesariosParaRolActual = requerimiento.getCupo(rolActual);
        List<Persona> elegidosParaRolActual = this.elegidosEnProcesoOrdenadosPorRol.get(rolActual);

        if (elegidosParaRolActual.size() == candidatosNecesariosParaRolActual) {
            backtrack(indiceRol + 1);
            return;
        }

        int faltanElegir = candidatosNecesariosParaRolActual - elegidosParaRolActual.size();
        int disponibles  = candidatos.size() - inicio;

        if (disponibles < faltanElegir) return;

        for (int i = inicio; i < candidatos.size(); i++) {
            Persona candidato = candidatos.get(i);

            if (esIncompatibleConEquipo(candidato, equipoParcial)) continue;
            if (esIncompatibleConEquipo(candidato, elegidosParaRolActual)) continue;

            elegidosParaRolActual.add(candidato);
            equipoParcial.add(candidato);
            calParcial = calParcial + candidato.getCalificacion();

            elegirCombinacion(rolActual, i + 1,
                    indiceRol);

            elegidosParaRolActual.remove(elegidosParaRolActual.size() - 1);
            this.elegidosEnProcesoOrdenadosPorRol.put(rolActual, elegidosParaRolActual);
            equipoParcial.remove(equipoParcial.size() - 1);
            calParcial -= candidato.getCalificacion();
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
