# Plan de Desacoplamiento de Datos Hardcoded y Variables de Entorno

Este plan detalla las acciones necesarias para eliminar valores quemados en el código ("hardcoded") y centralizarlos en variables de entorno o archivos de configuración, facilitando la portabilidad y seguridad del sistema DOCUFISE.

## Casos Identificados

### Fase 1: Estandarización del Backend (Configuraciones Externas y .env)

#### 1.1 Soporte para Archivos `.env`
- Se creará un archivo `.env.example` con todas las variables necesarias.
- Se configurará el proyecto para leer el archivo `.env` local (usando `io.github.cdimascio:dotenv-java` o configuración de IDE recomendada).
- **Archivo**: [NEW] `backend_docufise/.env.example`.

#### 1.2 Migración de `application.yml` a Placeholders
- Reemplazar valores literales por `${NOM_VAR:default}` para permitir la inyección desde el entorno.

#### 1.3 Externalización de CORS y Seguridad
- Orígenes permitidos y constantes de seguridad (`MAX_INTENTOS`, `MINUTOS_BLOQUEO`) se moverán a propiedades en `application.yml`.

### Fase 2: Configuración Dinámica en Frontend (Runtime Config)

#### 2.1 Implementación de `config.json`
- Crear un archivo de configuración en `src/assets/config/config.json`.
- Este archivo contendrá la `apiUrl` y otras configuraciones que puedan cambiar sin recompilar.

#### 2.2 Servicio de Carga en Runtime (APP_INITIALIZER)
- Crear un `ConfigService` que lea el JSON antes de que la aplicación inicie.
- Reemplazar el uso de `environment.apiUrl` por `configService.get('apiUrl')`.

### Fase 3: Validación e Integración

#### 3.1 Pruebas de Carga de Configuración
- Verificar que el backend inicie correctamente si faltan variables de entorno (usando valores por defecto).
- Verificar el funcionamiento de CORS con orígenes configurados externamente.

#### 3.2 Documentación de Variables (Archivo `.env.example`)
- [NEW] Crear un archivo `.env.example` en la raíz de cada proyecto para guiar a otros desarrolladores.

---

## Decisiones Técnicas Tomadas

1. **Backend**: Uso de **archivo `.env`** para desarrollo y `${VAR}` para producción.
2. **Frontend**: Migración a **Pattern Runtime Config** (`config.json`) para permitir cambios de API sin recompilación.

## Plan de Verificación

### Pruebas Automatizadas
- Ejecutar `npm run build` en frontend.
- Ejecutar `./gradlew build` en backend.

### Verificación Manual
- Crear un `.env` local y cambiar el puerto del servidor (`SERVER_PORT`) o la base de datos para confirmar que toma los valores.
- Modificar `assets/config/config.json` con una URL falsa y verificar que el frontend intente conectar a ella.
