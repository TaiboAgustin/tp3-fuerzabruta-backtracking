# El equipo ideal

Aplicación de escritorio que arma el equipo de desarrollo **más calificado posible**
a partir de un pool de candidatos, respetando los cupos requeridos por rol y
evitando pares de personas incompatibles. El problema se resuelve con un algoritmo
de **backtracking con poda** que se ejecuta en un thread separado de la interfaz.

> Trabajo Práctico 3 — Programación III, Universidad Nacional de General Sarmiento (Comisión 01).

---

## Tabla de contenidos

- [El problema](#el-problema)
- [Funcionalidad](#funcionalidad)
- [Arquitectura](#arquitectura)
- [Modelo de dominio](#modelo-de-dominio)
- [El algoritmo](#el-algoritmo)
- [Persistencia](#persistencia)
- [Requisitos](#requisitos)
- [Cómo compilar y ejecutar](#cómo-compilar-y-ejecutar)
- [Tests e integración continua](#tests-e-integración-continua)
- [Decisiones de diseño](#decisiones-de-diseño)
- [Integrantes](#integrantes)

---

## El problema

Estamos en el departamento de RRHH de una *software factory* y tenemos que diseñar
el grupo de desarrollo para un nuevo proyecto.

- Cada **persona** tiene un **rol** posible (líder de proyecto, arquitecto, programador
  o tester) y una **calificación histórica** de su desempeño, un número entre **1 y 5**
  (cuanto más grande, mejor).
- Existe una lista de **incompatibilidades**: pares de personas que en el pasado
  tuvieron inconvenientes trabajando juntas y no deben quedar en el mismo equipo.
- Se define un **requerimiento**: cuántas personas se necesitan en cada rol
  (por ejemplo, 1 líder, 2 arquitectos, 4 programadores y 5 testers).

El objetivo es encontrar el conjunto de personas **de mayor calificación total**
que cumpla los cupos por rol y no contenga ningún par incompatible.

## Funcionalidad

- ✅ Cargar y visualizar las personas disponibles.
- ✅ Cargar y visualizar las incompatibilidades.
- ✅ Cargar y visualizar los requerimientos del equipo.
- ✅ Resolver el problema y visualizar el equipo resultante.
- ✅ Guardar y cargar la sesión (personas e incompatibilidades) en disco como JSON.
- ✅ Cómputo del algoritmo en un thread separado para no bloquear la interfaz.

## Arquitectura

El código de negocio está **claramente separado** del código de interfaz: toda la
lógica vive bajo `logica/` y `persistencia/`, sin ninguna dependencia de Swing,
mientras que `UI/` consume esas clases.

```
tp3-fuerzabruta-backtracking/
├── src/
│   ├── UI/                  # Módulo 4 — Interfaz gráfica (Swing)
│   │   ├── Main.java                       # Punto de entrada / pantalla de inicio
│   │   ├── PantallaEquipo.java             # Pantalla principal con pestañas
│   │   ├── AgregarPersonaDialog.java       # Alta de candidatos
│   │   └── AgregarIncompatibilidadDialog.java
│   ├── logica/
│   │   ├── modelo/          # Módulo 1 — Clases del dominio
│   │   │   ├── Rol.java
│   │   │   ├── Persona.java
│   │   │   ├── Incompatibilidad.java
│   │   │   ├── Requerimiento.java
│   │   │   └── ResultadoEquipo.java
│   │   └── algoritmo/       # Módulo 3 — Backtracking + thread
│   │       └── AlgoritmoBacktracking.java
│   └── persistencia/        # Módulo 2 — Carga y guardado en JSON
│       ├── PersistenciaEnJson.java
│       ├── PersonaDatos.java
│       └── IncompatibilidadDato.java
├── test/                    # Tests unitarios (espejo de src/)
├── lib/                     # Dependencias externas (JARs)
└── .github/workflows/       # CI (GitHub Actions)
```

### Módulos

| # | Módulo | Paquete | Responsabilidad |
|---|--------|---------|-----------------|
| 1 | Modelo | `logica.modelo` | Clases del dominio y sus invariantes |
| 2 | Persistencia | `persistencia` | Serialización JSON de personas e incompatibilidades |
| 3 | Algoritmo | `logica.algoritmo` | Backtracking con poda, ejecutado en thread aparte |
| 4 | UI | `UI` | Pantallas Swing para cargar datos, ejecutar y visualizar |

## Modelo de dominio

- **`Rol`** — enum: `LIDER_DE_PROYECTO`, `ARQUITECTO`, `PROGRAMADOR`, `TESTER`.
- **`Persona`** — nombre, rol y calificación (1–5). Valida sus argumentos en el
  constructor (nombre no vacío, rol no nulo, calificación en rango). Su identidad
  (`equals`/`hashCode`) se basa en el **nombre**.
- **`Incompatibilidad`** — par no ordenado de personas que no pueden coincidir.
  Es **simétrica**: `(A, B)` equivale a `(B, A)`. Valida que ninguna persona sea
  nula y que no se declare a alguien incompatible consigo mismo.
- **`Requerimiento`** — mapa de rol → cantidad requerida. Sabe decidir si un
  `ResultadoEquipo` cumple exactamente los cupos.
- **`ResultadoEquipo`** — conjunto de integrantes y su calificación total acumulada.
  Mantiene el invariante de que la calificación total siempre refleja a los
  integrantes presentes. Expone la constante `SIN_SOLUCION` para representar la
  ausencia de equipo válido.

## El algoritmo

`AlgoritmoBacktracking` resuelve el problema explorando combinaciones de candidatos
por rol y quedándose con la de mayor calificación total. Para evitar recorrer todo
el espacio de soluciones aplica **poda (branch and bound)**:

1. **Agrupa** los candidatos por rol y los **ordena** de mayor a menor calificación.
2. **Descarta de entrada** los casos sin solución posible (cuando algún rol no tiene
   candidatos suficientes para su cupo).
3. Precalcula, para cada rol, la **máxima calificación restante alcanzable** (sumas
   sufijas con los mejores candidatos de cada rol).
4. **Backtracking** rol por rol: en cada paso, si la calificación parcial más la
   máxima restante no puede superar a la mejor solución encontrada, **poda** esa
   rama. Al elegir candidatos para un rol verifica que no se introduzcan
   incompatibilidades con el equipo parcial.
5. Devuelve el mejor `ResultadoEquipo` encontrado, o `ResultadoEquipo.SIN_SOLUCION`.

El cómputo corre dentro de un `SwingWorker` (`doInBackground`), de modo que la
interfaz permanece responsiva mientras se busca el equipo.

## Persistencia

`PersistenciaEnJson` serializa el estado a JSON usando **Gson**:

- `personas.json` — el pool de candidatos.
- `incompatibilidades.json` — los pares incompatibles (referenciando a las personas
  por nombre, que se reconectan contra el pool al cargar).

Desde la pantalla principal, los botones **Guardar** y **Cargar** escriben/leen
estos archivos en una carpeta elegida por el usuario. Los cupos por rol se ingresan
manualmente en cada sesión.

## Requisitos

- **Java 21** (JDK).
- Dependencias incluidas en `lib/`:
  - `gson-2.14.0.jar` — serialización JSON.
  - `junit-4.13.2.jar` y `hamcrest-core-1.3.jar` — tests.

## Cómo compilar y ejecutar

Desde la raíz del proyecto.

**Linux / macOS (bash):**

```bash
# Compilar
javac -cp "src:lib/gson-2.14.0.jar" -d bin $(find src -name "*.java")

# Ejecutar
java -cp "bin:lib/gson-2.14.0.jar" UI.Main
```

**Windows (PowerShell):**

```powershell
# Compilar
javac -cp "src;lib/gson-2.14.0.jar" -d bin (Get-ChildItem src -Recurse -Filter *.java).FullName

# Ejecutar
java -cp "bin;lib/gson-2.14.0.jar" UI.Main
```

Al iniciar se muestra la pantalla de bienvenida; el botón **Iniciar planificación**
abre la pantalla principal con las pestañas de Personas, Incompatibilidades,
Requerimientos y Resultado.

## Tests e integración continua

Todas las clases de negocio tienen **tests unitarios** con **JUnit 4**.

**Linux / macOS (bash):**

```bash
javac -cp "lib/*:src" -d bin $(find src test -name "*.java")
java -cp "bin:lib/*" org.junit.runner.JUnitCore \
  $(find bin -name "*Test.class" | sed 's|bin/||;s|/|.|g;s|\.class$||')
```

**Windows (PowerShell):**

```powershell
javac -cp "lib/*;src" -d bin (Get-ChildItem src,test -Recurse -Filter *.java).FullName
$tests = (Get-ChildItem bin -Recurse -Filter *Test.class | ForEach-Object {
  $_.FullName.Replace((Resolve-Path bin).Path + '\','').Replace('\','.').Replace('.class','') })
java -cp "bin;lib/*" org.junit.runner.JUnitCore $tests
```

El pipeline de **CI (GitHub Actions)** compila el proyecto con Java 21 (Temurin),
corre la suite de tests y mide cobertura con **JaCoCo**, exigiendo un mínimo del
**80 %** de líneas cubiertas. El módulo de UI se excluye de la medición de cobertura.

## Decisiones de diseño

- **Separación negocio / UI:** ni `logica` ni `persistencia` dependen de Swing; la
  interfaz se apoya sobre ellos. Esto permite testear el dominio y el algoritmo de
  forma aislada.
- **Cómputo fuera del hilo de UI:** el algoritmo se ejecuta en un `SwingWorker` y los
  datos se copian de forma defensiva antes de pasarlos al thread de fondo, evitando
  condiciones de carrera sobre las listas de la UI.
- **Poda en el backtracking:** el ordenamiento por calificación y las cotas de máxima
  calificación restante reducen drásticamente el espacio de búsqueda frente a una
  fuerza bruta pura.
- **Identidad por nombre:** `Persona` se identifica por su nombre, lo que simplifica
  la reconexión de incompatibilidades al deserializar desde JSON.

## Integrantes

- Marcelo Agustín Gomez Rodríguez ([@MarceloAgustindev](https://github.com/MarceloAgustindev)) — Persistencia
- Nicolás Rocha ([@AgusNico-java](https://github.com/AgusNico-java)) — Algoritmo
- Florencia Sangueso ([@FlorenciaSangueso](https://github.com/FlorenciaSangueso)) — Modelo
- Agustín Taibo Cruz ([@TaiboAgustin](https://github.com/TaiboAgustin)) — Interfaz gráfica
