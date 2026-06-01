# 🚀 El Relevo del Desarrollador: Gestión del Proyecto

Documentación técnica prioritaria orientada a la transferencia de conocimiento exprés (puesta en marcha en < 2 horas).

## 🗺️ Índice de Contenidos
* [1. Requisitos del Sistema](#1-requisitos-del-sistema)
* [2. Guía de Instalación y Despliegue](#2-guía-de-instalación-y-despliegue)
* [3. Documentación de la Arquitectura Interna](#3-documentación-de-la-arquitectura-interna)
* [4. Contrato de Operaciones de Datos (API Interna)](#4-contrato-de-operaciones-de-datos-api-interna)
* [5. Historial de Versiones (Changelog)](#5-historial-de-versiones-changelog)
* [6. Mantenibilidad, Testing y Extensibilidad](#6-mantenibilidad-testing-y-extensibilidad)


# Aucorsa — Sistema de Gestión de Flotas

Aplicación de escritorio para gestionar autobuses, conductores, lugares y rutas de una empresa de transporte urbano.
Tiene dos modos de uso: interfaz gráfica (Swing) y consola interactiva.
 
---

## Requisitos de software

| Herramienta | Versión mínima | Notas |
|-------------|---------------|-------|
| Java JDK    | 17            | El proyecto compila con `--release 17` |
| Maven       | 3.8           | Gestión de dependencias y build |
| MySQL       | 8.0           | Base de datos relacional |
| IntelliJ IDEA / Eclipse | cualquiera | Opcional, se puede lanzar desde terminal |

> **Nota:** el `pom.xml` declara `source/target 25` en una sección pero la configuración real del compilador usa la `21`. Para evitar confusiones, instala JDK 21 o superior.
 
---

## Variables de entorno / configuración

El proyecto **no usa variables de entorno** en este momento. La cadena de conexión está directamente en `ConexionBBDD.java`:

```
URL:      jdbc:mysql://127.0.0.1:3306/AUCORSA
Usuario:  root
Password: 1234
```

Antes de ejecutar, asegúrate de que MySQL esté corriendo con esas credenciales, o edita ese fichero con los tuyos:

```
src/main/java/controller/connection/ConexionBBDD.java
```

Si quieres externalizarlo sin tocar código, puedes crear un fichero `.env` en la raíz y leerlo con una librería como `dotenv-java` (no incluida, modificación opcional).
 
---

## Pasos para el despliegue en local

### 1. Clonar o descomprimir el proyecto

```bash
unzip proyecto_final_progr.zip
cd proyecto_final_progr
```

### 2. Crear la base de datos

Abre MySQL y ejecuta:

```sql
CREATE DATABASE AUCORSA CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE AUCORSA;
 
CREATE TABLE Bus (
    register VARCHAR(20) PRIMARY KEY,
    type     VARCHAR(50) NOT NULL,
    license  VARCHAR(20) NOT NULL,
    imagen   VARCHAR(255)
);
 
CREATE TABLE Conductor (
    numeroConductor   INT PRIMARY KEY AUTO_INCREMENT,
    nombreConductor   VARCHAR(100) NOT NULL,
    apellidoConductor VARCHAR(100) NOT NULL,
    imagen            VARCHAR(255)
);
 
CREATE TABLE Place (
    idplace INT PRIMARY KEY AUTO_INCREMENT,
    cp      VARCHAR(10)  NOT NULL,
    city    VARCHAR(100) NOT NULL,
    site    VARCHAR(150) NOT NULL,
    imagen  VARCHAR(255)
);
 
-- Relación ternaria Bus-Conductor-Lugar
CREATE TABLE BCL (
    idBus           VARCHAR(20) NOT NULL,
    numeroConductor INT         NOT NULL,
    idLugar         INT         NOT NULL,
    diaSemana       VARCHAR(20) NOT NULL,
    PRIMARY KEY (idBus, numeroConductor, idLugar, diaSemana),
    FOREIGN KEY (idBus)           REFERENCES Bus(register),
    FOREIGN KEY (numeroConductor) REFERENCES Conductor(numeroConductor),
    FOREIGN KEY (idLugar)         REFERENCES Place(idplace)
);
```

### 3. Ajustar credenciales (si es necesario)

Edita `src/main/java/controller/connection/ConexionBBDD.java` si tu MySQL no usa `root/1234`.

### 4. Compilar con Maven

```bash
mvn clean package -DskipTests
```

El `.jar` resultante aparece en `target/Aucorsa-1.0-SNAPSHOT.jar`.

### 5. Ejecutar

**Modo gráfico (recomendado):**

```bash
java -cp target/Aucorsa-1.0-SNAPSHOT.jar app.MainGUI
```

**Modo consola:**

```bash
java -cp target/Aucorsa-1.0-SNAPSHOT.jar app.Main
```

### 6. Imágenes de conductores

Las fotos de los conductores se cargan desde `src/main/resources/images/`. Los ficheros incluidos son `driver101.jpg` hasta `driver110.jpg` y una imagen por defecto `default.jpeg`. Al empaquetar con Maven, quedan dentro del `.jar`; si añades imágenes nuevas, tendrás que recompilar o ajustar la ruta de carga.
 
---

## Solución de problemas rápida

| Síntoma | Causa probable | Solución |
|---------|---------------|----------|
| `Communications link failure` | MySQL no está corriendo | `sudo systemctl start mysql` |
| `Access denied for user 'root'` | Credenciales incorrectas | Editar `ConexionBBDD.java` |
| `Table 'AUCORSA.Bus' doesn't exist` | BD no inicializada | Ejecutar el SQL del paso 2 |
| Pantalla en blanco al arrancar | JDK < 17 | Actualizar JDK |
 
---

## Estructura de carpetas (resumen)

```
proyecto_final_progr/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/
    │   │   ├── app/           → Puntos de entrada (Main, MainGUI)
    │   │   ├── controller/    → Lógica de negocio y acceso a datos
    │   │   │   ├── bus/
    │   │   │   ├── conductor/
    │   │   │   ├── lugar/
    │   │   │   ├── ruta/
    │   │   │   ├── connection/ → Singleton de conexión MySQL
    │   │   │   └── dao/        → CRUD por entidad
    │   │   ├── models/        → Entidades (Bus, Conductor, Lugar, BCL)
    │   │   ├── view/          → Paneles e interfaces Swing
    │   │   └── utils/         → Menú consola y helpers
    │   └── resources/
    │       └── images/        → Imágenes de conductores y buses
    └── test/
        └── java/controller/dao/ → Tests unitarios DAO
```