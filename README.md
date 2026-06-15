# ms-customer-core-adapter — Documentación Técnica

> **Repositorio:** aws-bdr-ms-integracion-customer-core-adapter-java
> **Owner / Squad:** Brandon Arbelaez — brandon.arbelaez@pragma.com.co
> **Dominio BIAN:** Party Reference Data — Integration Adapter
> **Producto/Programa:** BDR Integración
> **Versión:** No evidenciado en el proyecto
> **Estado:** Active
> **Contacto Operativo (On-call):** Brandon Arbelaez — brandon.arbelaez@pragma.com.co

---

## 1. Resumen

**Propósito:**
Capa de integración intermedia entre el microservicio expuesto al cliente (`ms-party-personal-data`) y el sistema core bancario COBIS. Traduce las peticiones internas al protocolo y modelo de datos de COBIS, gestiona la autenticación JWT contra COBIS con caché L1/L2, aplica resiliencia (retry + circuit breaker) y normaliza los errores upstream para que el consumidor pueda diferenciar sub-casos.

**Tipo de API:**
- [x] Interna

**Responsabilidades principales (endpoints documentados):**
- `GET adapter/party/{partyId}/party-profile` — Obtiene el perfil de party desde COBIS. Determina el subtipo del cliente (natural `P` / jurídica `C`) y llama al endpoint COBIS correspondiente.
- `GET adapter/party/{partyId}/party-location` — Obtiene las direcciones registradas de un party desde COBIS.

**Fuera de alcance (NO hace):**
- No desencripta campos PII (esa responsabilidad es del consumidor `ms-party-personal-data`).
- No construye códigos de error OMNIC; solo transporta el status y mensaje real de COBIS.
- No persiste datos; es un adapter de paso.
- No gestiona sesiones de usuario ni autorización de canal.

**Stakeholders:**
- Negocio: No evidenciado en el proyecto
- Arquitectura: No evidenciado en el proyecto
- Seguridad: No evidenciado en el proyecto
- Operaciones: No evidenciado en el proyecto

---

## 2. Estándares

### 2.1 BIAN
- **Service Domain / BIAN Functional Pattern:** Party Reference Data — Integration Adapter
- **BIAN APIs / Operations:** No aplica directamente; el adapter traduce al modelo interno de COBIS.
- **Eventos de dominio:** No evidenciado en el proyecto.

### 2.2 ISO-20022
No aplica según el código revisado.

---

## 3. Arquitectura

### 3.1 Contexto (C4 - Nivel 1)
Ver: `docs/diagrams/01-context.puml`

### 3.2 Componentes (C4 - Nivel 2)
Ver: `docs/diagrams/02-components.puml`

### 3.3 Flujo end-to-end

**Entrada:**
- HTTP REST sobre red interna. No evidenciado API Gateway ni WAF en el código del adapter.
- Puerto: `8080` (evidenciado en `application.yml`).

**Integraciones evidenciadas:**
- COBIS (sistema core bancario) — vía Feign HTTP con JWT Bearer.
- Redis — caché L2 del token JWT de COBIS (SSL habilitado, puerto 6379).
- AWS Parameter Store — configuración externalizada al arranque.
- AWS CloudWatch — exportación de métricas Micrometer.

**Flujo principal — party-profile (happy path):**
1. `AdapterMappingController` recibe `GET adapter/party/{partyId}/party-profile`.
2. `RequestContextFilter` captura headers en `RequestContextHolder` (ThreadLocal).
3. `RequestHeadersResolver` y `EncryptionHeadersResolver` resuelven y validan headers.
4. `PartyProfileUseCasePort.getPartyProfile(partyId)` es invocado.
5. `CobisAuthenticatedExecutor.execute(call)` obtiene JWT válido (L1 → L2 → refresh).
6. `CobisTypeCustomerApiAdapter` llama COBIS para determinar subtipo (`P` o `C`).
7. Según subtipo: `CobisNaturalProfileApiAdapter` o `CobisJuridicaProfileApiAdapter` llama COBIS.
8. `CobisMetricsAspect` registra latencia y errores vía `MicrometerMetricsAdapter`.
9. `PartyProfileMapper` convierte el modelo de dominio a `PartyProfileResponse`.
10. Retorna `HTTP 200` con el body.

**Flujo principal — party-location (happy path):**
1. `AdapterMappingController` recibe `GET adapter/party/{partyId}/party-location`.
2. `RequestContextFilter` captura headers en `RequestContextHolder`.
3. `PartyLocationHelper.getPartyLocation(partyId, correlationId)` es invocado.
4. `PartyLocationUseCasePort.getPartyLocation(partyId, correlationId)` delega al use case.
5. `CobisAuthenticatedExecutor.execute(call)` obtiene JWT válido.
6. `CobisPartyLocationApiAdapter` llama COBIS con `@CircuitBreaker` y `@Retry`.
7. `CobisAddressMapper` convierte `CobisAddressesResponseDto` → `List<PartyLocationModel>`.
8. `PartyLocationMapper` convierte `List<PartyLocationModel>` → `List<PartyLocationDto>`.
9. Retorna `HTTP 200` con la lista (puede ser `[]` si COBIS no retorna direcciones).

---

## 4. Contrato de API

**Fuente de verdad:** `docs/openapi/openapi.yaml`

### 4.1 Endpoints documentados

#### GET `adapter/party/{partyId}/party-profile`

**Path variable:**
- `partyId` — `String`, `@NotBlank`. Obligatorio.

**Headers obligatorios (validados por `RequestHeaders` + `@Valid`):**
- `X-Correlation-ID` — `@NotBlank`
- `X-Request-ID` — `@NotBlank`
- `X-Channel-Id` — `@NotBlank`
- `X-End-User-Login` — `@NotBlank`
- `X-End-User-Terminal` — `@NotBlank`
- `X-End-User-Request-Date-Time` — `@NotBlank`

**Headers opcionales:**
- `Accept-Language`
- `X-End-User-Last-Logged-Date-Time`

**Headers de encriptación** (resueltos por `EncryptionHeadersResolver`, sin validación `@NotBlank` evidenciada en `EncryptionHeaders`):
- `X-OMNIC-ENC`, `X-OMNIC-KEK-KID`, `X-OMNIC-EDEK`, `X-OMNIC-ENC-ALG`, `X-OMNIC-ENC-CTX`, `X-OMNIC-ENC-FIELDS`

**Query param opcional:**
- `branchCode` — `String`, no requerido.

**Respuesta 200:**
```json
{
  "fullName": "String",
  "firstName": "String",
  "middleName": "String",
  "lastName": "String",
  "marriedLastName": "String",
  "dateOfBirth": "String",
  "gender": "String",
  "nationality": "String",
  "maritalStatus": "String",
  "customerSegment": "String",
  "lifecycleStatus": "String",
  "profession": { "code": "String", "name": "String" }
}
```

---

#### GET `adapter/party/{partyId}/party-location`

**Path variable:**
- `partyId` — `String`, `@NotBlank`. Obligatorio.

**Headers:** Mismos que `party-profile`.

**Query param opcional:**
- `branchCode` — `String`, no requerido.

**Respuesta 200:**
```json
[
  {
    "addressType": "String",
    "streetType": "String",
    "streetName": "String",
    "buildingNumber": "String",
    "unitNumber": "String",
    "districtName": "String",
    "villageName": "String",
    "otherIndications": "String",
    "municipality": { "code": "String", "name": "String" },
    "department": { "code": "String", "name": "String" },
    "country": { "code": "String", "name": "String" },
    "zone": { "code": "String", "name": "String" },
    "propertyStatus": { "code": "String", "name": "String" },
    "fullAddress": "String"
  }
]
```
> Puede retornar `[]` si COBIS no devuelve direcciones.

### 4.2 Respuesta de error (ambos endpoints)

```json
{
  "status": 404,
  "error": "NotFound",
  "message": "El objeto no fue encontrado.",
  "path": "/adapter/party/123/party-location",
  "correlationId": "abc-123",
  "details": null
}
```
> `details` solo está presente en errores `409`: `{ "code": "String", "message": "String" }`.

### 4.3 Catálogo de errores

| HTTP | Origen | Descripción | ¿Retry R4j? | ¿Retry 401? |
|---:|---|---|---|---|
| 400 | Validación adapter | `partyId` en blanco / header faltante | No | No |
| 400 | COBIS | Mensaje de solicitud mal formateado | No | No |
| 401 | COBIS | Token inválido | No | **Sí (1 vez)** |
| 401 | COBIS | No autorizado para ejecutar la operación | No | **Sí (1 vez)** |
| 403 | COBIS | Canal no autorizado | No | No |
| 403 | COBIS | Solicitud denegada | No | No |
| 404 | COBIS | Objeto no encontrado | No | No |
| 409 | COBIS | Error de ejecución COBIS (con `details`) | No | No |
| 422 | COBIS | Datos inconsistentes / subtipo no soportado | No | No |
| 429 | COBIS | Límite de operaciones superado | **Sí (4 intentos)** | No |
| 500 | COBIS | Error inesperado | **Sí (4 intentos)** | No |
| 503 | COBIS | Servicio no disponible | **Sí (4 intentos)** | No |
| 503 | Adapter | Circuit breaker abierto | — | No |
| 503 | Adapter | Token JWT no disponible | — | No |
| 504 | COBIS | Timeout | **Sí (4 intentos)** | No |

### 4.4 Seguridad
- Autenticación hacia COBIS: JWT Bearer obtenido mediante `CobisAuthApiAdapter` (validate + login contra COBIS).
- Credenciales COBIS: inyectadas desde AWS Parameter Store (`cobis.credentials.username`, `cobis.credentials.password`).
- Redis con SSL habilitado (`spring.data.redis.ssl.enabled: true`).
- No evidenciado OAuth2/mTLS en el adapter para autenticar a sus consumidores.

---

## 5. Cumplimiento y Datos Sensibles

- Los headers `X-OMNIC-ENC*` son recibidos y propagados por el adapter pero no procesados (no desencripta).
- Credenciales COBIS almacenadas como `SecureString` en Parameter Store (evidenciado en `PARAMETER_STORE.md`).
- No evidenciado masking de PII en logs del adapter.

---

## 6. Observabilidad

**Logs:**
- Formato: texto con patrón `[correlationId=%X{correlationId}]` en MDC (evidenciado en `application.yml` y `RequestContextFilter`).
- `correlationId` propagado desde header `X-Correlation-ID` al MDC en cada request.
- Nivel: `INFO` para el paquete del adapter; `INFO` para Spring, Redis, Resilience4j.

**Métricas (Micrometer — evidenciadas en `MicrometerMetricsAdapter`):**
- `cobis.token.cache.l1.hit` — hits en caché L1 (Caffeine).
- `cobis.token.cache.l2.hit` — hits en caché L2 (Redis).
- `cobis.token.cache.l2.miss` — misses en caché L2.
- `cobis.token.refresh.count` — refrescos de token JWT.
- `cobis.token.refresh.lock.contention` — contención de lock distribuido.
- `cobis.call.latency` — latencia de llamadas COBIS por operación (tag: `operation`).
- `cobis.call.errors.total` — errores de llamadas COBIS por operación y status (tags: `operation`, `status_code`).

**Exportación:**
- Prometheus: `GET /prometheus` (evidenciado en `application.yml`).
- CloudWatch: namespace `BDR/CustomerCoreAdapter`, step `1m`, batch `20` (evidenciado en `application.yml`).

**Trazas distribuidas:** No evidenciado OpenTelemetry en el proyecto.

---

## 7. Operación (Runbook)

Ver: `docs/runbook/RUNBOOK.md`

---

## 8. Decisiones de Arquitectura (ADRs)

Ver: `docs/adr/`

---

## 9. Software Architecture Document (SAD)

Ver: `docs/sad/SAD.md`

---

## 10. Referencias

- OpenAPI: `docs/openapi/openapi.yaml`
- Diagramas: `docs/diagrams/`
- Runbook: `docs/runbook/RUNBOOK.md`
- SAD: `docs/sad/SAD.md`
- Parameter Store: `PARAMETER_STORE.md`
