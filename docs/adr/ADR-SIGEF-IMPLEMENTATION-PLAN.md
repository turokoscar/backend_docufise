# ADR - Plan de Implementación SIGEF

## Estado: EN PROGRESO

---

## 1. Contexto

Este ADR documenta el estado de cumplimiento del sistema SIGEF (Sistema Integrado de Gestión de Expedientes y Firmas) respecto al ADR de "Estándares de Diseño Visual, UI y Flujos de Trabajo" v2.0.

Después de un análisis profundo del código, se identificaron gaps entre la implementación actual y los requisitos del ADR.

---

## 2. Gaps Identificados

### 2.1 Gaps de UI/Visual (ADR Sección 2)

| Prioridad | Gap | Archivo | Estado |
|-----------|-----|--------|--------|
| ~~ALTA~~ | ~~Color botón destructivo: usa `#EF4444`~~ | ~~`button.component.ts:48`~~ | ✅ **CORREGIDO** |
| ~~MEDIA~~ | ~~Label "Eliminar expediente" vs "Eliminar"~~ | ~~`expedientes.page.html:153-156`~~ | ✅ **CORREGIDO** |

### 2.2 Gaps de Flujo de Expedientes (ADR Sección 3 y HU 1.1, 1.2)

| Prioridad | Gap | Archivo | Estado |
|-----------|-----|--------|--------|
| ~~**CRÍTICA**~~ | ~~Bug: botón Derivar aparece en estado INGRESADO en lugar de REGISTRADO~~ | ~~`expedientes.page.html:143`~~ | ✅ **CORREGIDO** |
| BAJA | Consistencia de constantes: front usa 'Registrados', backend 'REGISTRADO' | `states.constants.ts` | **PENDIENTE** |

### 2.3 Gaps de Reportes (ADR HU 3.1)

| Prioridad | Gap | Archivo | Estado |
|-----------|-----|--------|--------|
| ALTA | Gráfico de barras usa datos hardcoded `[15, 28, 42...]` | `reportes.page.ts:132-150` | **PENDIENTE** |

---

## 3. Arquitectura

### 3.1 Backend - Arquitectura Hexagonal ✅ CUMPLE

```
domain/
├── model/           # Entidades de dominio
│   ├── Documento.java
│   ├── Firma.java
│   └── ...
├── ports/
│   ├── input/       # Casos de uso
│   └── output/     # Interfaces de salida
├── repository/      # Interfaces de repositorio
└── exception/       # Excepciones de dominio

application/
├── service/         # Implementación de servicios
└── mapper/         # Mapeadores DTO

infrastructure/
├── controller/      # Adaptadores HTTP
└── repository/      # Implementaciones JPA
```

### 3.2 Frontend - Componentes Reutilizables ✅ CUMPLE

```
src/app/
├── pages/           # Páginas de características
│   ├── login/
│   ├── expedientes/
│   ├── firmas/
│   └── reportes/
├── shared/
│   └── components/
│       └── ui/     # Componentes UI reutilizables
│           ├── button/
│           ├── badge/
│           ├── table/
│           └── ...
└── core/
    ├── services/    # Servicios (API, Auth, Catalog)
    ├── models/      # Interfaces TypeScript
    └── constants/   # Constantes de la app
```

---

## 4. Plan de Implementación por Fases

### Fase 1: Corrección de Bugs Críticos

| # | Tarea | Archivo | Cambio | Estado |
|---|-------|---------|--------|--------|
| 1.1 | Corregir bug botón Derivar | `expedientes.page.html` | Cambiar `'INGRESADO'` por `'REGISTRADO'` | ✅ COMPLETADO |
| 1.2 | Bug inconsistencia de estados | `states.constants.ts` | Constantes ahora usan mayúsculas (`REGISTRADO`) | ✅ COMPLETADO |

**Causa raíz**: DB tiene `txt_nombre = 'REGISTRADO'` (mayúsculas) pero frontend `ESTADOS_EXPEDIENTE.REGISTRADO = 'Registrado'` (capitalizado)

**Solución propuesta**: Unificar constantes frontend a mayúsculas para que coincidan con la DB

**Responsable**: Backend/Frontend  
**Tiempo estimado**: 15 minutos

---

### Fase 2: Ajustes de UI/Visual

| # | Tarea | Archivo | Cambio | Estado |
|---|-------|---------|--------|--------|
| 2.1 | Corregir color botón destructivo | `button.component.ts:48` | Cambiar `#EF4444` por `#AB2741` | ✅ COMPLETADO |
| 2.2 | Simplificar label "Eliminar" | `expedientes.page.html:155` | Cambiar "Eliminar expediente" por "Eliminar" | ✅ COMPLETADO |
| 2.3 | Unificar constantes de estados | `states.constants.ts` | Usar valores en mayúsculas del backend (`REGISTRADO`) | ✅ COMPLETADO |

**Responsable**: Frontend  
**Tiempo estimado**: 30 minutos

---

### Fase 3: Reportes Dinámicos

| # | Tarea | Archivo | Cambio | Estado |
|---|-------|---------|--------|--------|
| 3.1 | Implementar datos dinámicos para gráfico de barras | `reportes.page.ts` | Reemplazar datos hardcoded con datos reales filtrados por mes | ✅ COMPLETADO |
| 3.2 | Crear endpoint backend para estadísticas mensuales | `DocumentoController.java` | Agregar endpoint GET `/documentos/estadisticas` | ✅ COMPLETADO |

**Responsable**: Backend + Frontend  
**Tiempo estimado**: 1-2 horas

---

### Fase 4: Validación y Pruebas

| # | Tarea | Verificación |
|---|-------|--------------|
| 4.1 | Verificar flujo completo REGISTRADO → INGRESADO | Crear expediente → Derivar → Verificar estado cambia |
| 4.2 | Verificar flujo firma (HU 2.1, 2.2) | Login como firmante → Descargar → Rechazar → Verificar OBSERVADO |
| 4.3 | Verificar colores según ADR | Comparar implementación con valores hex del ADR |
| 4.4 | Verificar reportes dinámicos | Crear expedientes → Verificar gráfico refleja datos reales |

---

## 5. Resumen de Cambios Realizados

### Archivos Backend Creados/Modificados

| Archivo | Tipo | Descripción |
|---------|------|-------------|
| `shared/dto/EstadisticaResponse.java` | **CREADO** | DTO para respuesta de estadísticas |
| `domain/ports/input/DocumentoInputPort.java` | **MODIFICADO** | Agregado método `getEstadisticas()` |
| `application/service/DocumentoService.java` | **MODIFICADO** | Implementación de `getEstadisticas()` y `calcularTendenciaMensual()` |
| `infrastructure/controller/DocumentoController.java` | **MODIFICADO** | Endpoint GET `/documentos/estadisticas` |

### Archivos Frontend Creados/Modificados

| Archivo | Tipo | Descripción |
|---------|------|-------------|
| `core/services/api.service.ts` | **MODIFICADO** | Método `getEstadisticas()` |
| `pages/reportes/reportes.page.ts` | **MODIFICADO** | Uso del endpoint de estadísticas para gráfico de barras |

### Tiempo Total Estimado

- **Fase 1**: 15 min
- **Fase 2**: 30 min
- **Fase 3**: 1-2 horas
- **Fase 4**: 1 hora
- **Total**: ~3-4 horas

---

## 6. Decisiones Técnicas

1. **Constantes de estados**: Se unificarán usando el formato del backend (REGISTRADO, no 'Registrados')
2. **Gráfico de barras**: Se implementará con agregación mensual desde el frontend usando los datos ya cargados
3. **Endpoint de estadísticas**: Se evaluará si es necesario endpoint dedicado o se puede resolver en frontend

---

## 7. Estado de Implementación

| Fase | Estado | Observaciones |
|------|--------|---------------|
| Fase 1: Bugs Críticos | ✅ COMPLETADO | Bug corregido + labels simplificados |
| Fase 2: Ajustes UI/Visual | ✅ COMPLETADO | Color destructivo (#AB2741) y labels simplificados |
| Fase 3: Reportes Dinámicos | ✅ COMPLETADO | Endpoint y frontend implementado |
| Fase 4: Validación y Pruebas | **PENDIENTE** | - |

---

## 8. Bitácora de Cambios

| Fecha | Cambio | Responsable |
|-------|--------|-------------|
| 2026-04-12 | Creación del ADR y plan de implementación | opencode |
| 2026-04-12 | Fase 1: Bug corregido - botón Derivar ahora aparece en estado REGISTRADO | opencode |
| 2026-04-12 | Fase 2: Labels simplificados a "Actualizar" y "Eliminar" | opencode |
| 2026-04-12 | Fase 2: Color botón destructivo (#EF4444 → #AB2741) | opencode |
| 2026-04-12 | Fase 3: Endpoint GET /documentos/estadisticas implementado | opencode |
| 2026-04-12 | Fase 3: Frontend consume endpoint para gráfico de barras dinámico | opencode |
