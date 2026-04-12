# ADR-001: Menú basado en Roles de Usuario

## Estado
**Aceptado** - En implementación

## Fecha
2026-04-11

## Contexto
El sistema DocuFISE requiere que cada rol de usuario vea únicamente los menús correspondientes a sus permisos. Originalmente:
- CTD debía ver Expedientes, Firmas (lectura), Reportes (lectura)
- Firmante debía ver solo Firmas
- Administrador debía ver todo

Se identificó que el sistema original:
1. No utilizaba el campo `permiso` de la tabla `rol_menu`
2. Retornaba todos los menús asignados al rol sin filtrar
3. No discriminaba entre escritura y lectura
4. **Cargaba todos los catálogos al hacer login** innecesariamente

## Decisión

### 1. Estructura de Base de Datos
- Mantener tabla `rol_menu` con clave compuesta (`ide_rol`, `ide_menu`)
- Agregar columna `txt_permiso` para definir tipo de acceso
- Agregar columna `fec_registro` para auditoría

### 2. Asignación de Menús por Rol

| Rol | Menú(s) Asignado(s) |
|-----|---------------------|
| CTD (1) | Expedientes (ESCRITURA) |
| Firmante (2) | Firmas (ESCRITURA) |
| Administrador (3) | Todos (ESCRITURA) |

### 3. Backend - Cambios en Entidades

**RolMenu.java**
- Entity simple con columnas básicas (rolId, menuId, permiso)

**MenuService.java**
- Nuevo método `listarPorRolConPermiso()` que retorna menú + permiso
- Query JPQL con JOIN directo para evitar problemas N+1

**MenuController.java**
- Endpoint `GET /api/v1/menus/rol/{rolId}` ahora retorna `MenuResponse` con campo `permiso`

### 4. Frontend - Carga Lazy de Catálogos

**catalog.service.ts**
- Eliminar método `loadAll()` que cargaba todo al inicio
- Crear métodos individuales por tipo de catálogo:
  - `loadMenusByRol(rolId)` - Solo menús al login
  - `loadAreas()` - Solo cuando se necesita
  - `loadRoles()` - Solo cuando se necesita (admin)
  - `loadTiposDocumento()` - Solo cuando se necesita
  - `loadEstados()` - Solo cuando se necesita
- Cada método verifica si ya tiene datos antes de llamar a la API

**auth.service.ts**
- En login: solo llamar a `loadMenusByRol()`
- En restoreSession: solo cargar menús

**Pages (expedientes, firmas, etc.)**
- Cada página carga los catálogos que necesita en su `ngOnInit()`

## Consecuencias

### Positivas
- ✅ Seguridad mejorada: usuarios ven solo lo que necesitan
- ✅ Mantenimiento simplificado: permisos centralizados en BD
- ✅ Extensibilidad: fácil agregar nuevos permisos (SOLO_LECTURA, ESCRITURA, ADMIN)
- ✅ **Rendimiento mejorado**: menos llamadas al hacer login
- ✅ **UX mejorada**: login más rápido

### Negativas
- ⚠️ Requiere migración de BD
- ⚠️ Primera carga de cada página será levemente más lenta (carga catálogos)
- ⚠️ Cada formulario debe invocar carga de sus catálogos

## Notas
- El campo `permiso` está definido pero no se usa aún para lógica de negocio
- Future extensión: implementar validación de permisos en backend antes de ejecutar operaciones CRUD
- Los catálogos se cargan una sola vez y se reutilizan (no re-cargan)