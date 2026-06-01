# Guía de Arquitectura y Estilo — Aucorsa

## Por qué este documento existe

Cuando alguien llega a un proyecto por primera vez, el código le dice el *qué* pero raramente el *por qué*. Este fichero cubre esa parte: las decisiones de diseño, los patrones que se usaron y cómo encaja todo.
 
---

## Patrón general: MVC (Modelo - Vista - Controlador)

El proyecto sigue MVC de forma bastante estricta. Cada entidad de negocio tiene su propia carpeta dentro de `controller/` y `view/`, lo que hace que añadir una entidad nueva sea mecánico: copias la estructura de, por ejemplo, `bus/` y adaptas los nombres.

```
models/       →  Datos puros, sin lógica de presentación
view/         →  Swing: JPanel, JFrame, JTable, etc. Sin lógica de negocio
controller/   →  Une vista y modelo. Llama al DAO, actualiza la vista
```

La vista nunca llama directamente al DAO. Siempre pasa por el controlador. Esto es importante si en el futuro se quiere cambiar la fuente de datos (por ejemplo, pasar de MySQL a una API REST) sin tocar las pantallas.
 
---

## Acceso a datos: patrón DAO

Cada entidad tiene su propia clase DAO:

| Clase       | Tabla MySQL |
|-------------|------------|
| `BusDAO`    | `Bus`      |
| `ConductorDAO` | `Conductor` |
| `LugarDAO`  | `Place`    |
| `BCLDAO`    | `BCL`      |

Todos extienden la clase abstracta `DAO`, que centraliza la conexión a la base de datos. Los métodos usan `PreparedStatement` en lugar de concatenación de strings, lo que evita inyección SQL.

Cada DAO tiene operaciones básicas: `get*`, `insertar*`, `editar*`, `eliminar*`. El nombre en español facilita la lectura para el equipo original, aunque los nombres de columna en la BD están en inglés (decisión de diseño mixto, a tener en cuenta si se amplía).
 
---

## Conexión a la base de datos: Singleton parcial

`ConexionBBDD` implementa un patrón Singleton incompleto: crea una nueva conexión en cada llamada a `getConexion()` en lugar de reutilizar la existente. Funciona para un uso de un solo usuario, pero si se escala a múltiples hilos o usuarios simultáneos habría que añadir un pool de conexiones (HikariCP es la opción más habitual en proyectos Maven).
 
---

## Relación ternaria BCL

La entidad más compleja es `BCL` (Bus-Conductor-Lugar), que representa la asignación de un conductor a un autobús en una parada concreta un día de la semana. Es una tabla de relación ternaria con clave primaria compuesta por los cuatro campos. Esto implica que un conductor puede cubrir la misma parada en días distintos, y un bus puede pasar por la misma parada con distintos conductores.

Si se quiere añadir, por ejemplo, una hora de paso, simplemente se añade una columna a `BCL` y se actualiza el modelo y el DAO correspondiente.
 
---

## Organización de carpetas

```
controller/
├── bus/            → BusController (lista, formulario) + BusInfoController (detalle)
├── conductor/      → ídem para conductores
├── lugar/          → ídem para lugares
├── ruta/           → gestión de la relación BCL desde la vista
├── connection/     → ConexionBBDD
├── dao/            → clases de acceso a datos
└── MainController  → controlador raíz, inicializa los subcontroladores
 
view/
├── bus/            → BusPanel (tabla) + BusInfoView (formulario)
├── conductor/      → ídem
├── lugar/          → ídem
├── ruta/           → ídem
└── MainView        → ventana principal con pestañas o navegación
```

Cada módulo (bus, conductor, lugar) es autónomo: su panel, su vista de detalle y su controlador no dependen de los otros módulos directamente.
 
---

## Estilo de código

- **Idioma:** comentarios y nombres de métodos en español, nombres de columnas de BD en inglés. Mantener esa convención al añadir código nuevo para no romper la coherencia interna.
- **Javadoc:** los modelos y DAOs están bien comentados con `@param` y `@return`. Los controladores tienen menos documentación. Recomendamos añadir Javadoc a cualquier método público nuevo.
- **Manejo de errores:** los errores de BD se capturan con `try-catch` y se imprime el mensaje por consola. Para producción convendría usar un logger (SLF4J + Logback) en lugar de `System.out.println`.
- **Tests:** hay tests unitarios en `src/test/` para `BCLDAOTest`. Se pueden ejecutar con `mvn test`.
---

## Cómo añadir una entidad nueva (ejemplo: Turno)

1. Crear `models/Turno.java` con los campos y getters/setters.
2. Crear `controller/dao/TurnoDAO.java` extendiendo `DAO` con los métodos CRUD.
3. Crear `view/turno/TurnoPanel.java` y `TurnoInfoView.java` siguiendo la misma estructura que `BusPanel` y `BusInfoView`.
4. Crear `controller/turno/TurnoController.java` y `TurnoInfoController.java`.
5. Registrar el nuevo panel en `MainView` y el nuevo controlador en `MainController`.
6. Crear la tabla en MySQL con la misma nomenclatura en inglés.
   No hace falta tocar nada más.