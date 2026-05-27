# TP3 - El equipo ideal

Trabajo Práctico 3 de Programación III — Universidad Nacional de General Sarmiento, Comisión 01.

Aplicación para armar el equipo de desarrollo más calificado posible a partir de un pool de candidatos, respetando requerimientos por rol y evitando incompatibilidades entre personas. El problema se resuelve mediante un algoritmo de backtracking que corre en un thread separado.

## Integrantes

- Marcelo Agustín Gomez Rodríguez ([@MarceloAgustindev](https://github.com/MarceloAgustindev))
- Nicolás Rocha ([@AgusNico-java](https://github.com/AgusNico-java))
- Florencia Sangueso ([@FlorenciaSangueso](https://github.com/FlorenciaSangueso))
- Agustín Taibo Cruz ([@TaiboAgustin](https://github.com/TaiboAgustin))

## Estructura del proyecto

```
tp3-fuerzabruta-backtracking/
├── src/
│   ├── UI/                  # Módulo 4 — Interfaz gráfica
│   ├── logica/
│   │   ├── modelo/          # Módulo 1 — Clases del dominio
│   │   └── algoritmo/       # Módulo 3 — Backtracking + thread
│   └── persistencia/        # Módulo 2 — Carga y guardado de datos
├── test/                    # Tests unitarios (espejo de src/)
└── lib/                     # Dependencias externas (JARs)
```

## Módulos

**Módulo 1 — Modelo** (`src/logica/modelo/`)
Clases del dominio: `Rol`, `Persona`, `Incompatibilidad`, `Requerimiento`, `ResultadoEquipo`.

**Módulo 2 — Persistencia** (`src/persistencia/`)
Carga y guardado de personas, incompatibilidades y requerimientos desde archivos.

**Módulo 3 — Algoritmo** (`src/logica/algoritmo/`)
`AlgoritmoBacktracking` con ejecución en thread separado (`SwingWorker`).

**Módulo 4 — UI** (`src/UI/`)
Pantallas para cargar datos, lanzar el algoritmo y visualizar el equipo resultante.
