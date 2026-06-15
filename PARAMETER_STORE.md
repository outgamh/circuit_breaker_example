# Parameter Store — ms-customer-core-adapter

Prefix base: `/bdr/integracion/${ENVIRONMENT}/customer/adapter/`

> Todos los parámetros deben crearse con ese prefix para que Spring Cloud AWS los inyecte automáticamente al arrancar la aplicación, sin necesidad de declararlos en el `application.yml`.

---

## Feign Clients — Base URL y Paths

Todos los clientes Feign (`CobisPartyLocationClient`, `CobisNaturalProfileClient`, `CobisTypeCustomerClient`, `CobisJuridicaProfileClient`, `CobisRiskProfileClient`) usan la misma base URL y sus respectivos paths.

| Nombre en YML | Nombre en Parameter Store |
|---|---|
| `omnic.cobis.auth.base-url` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/omnic.cobis.auth.base-url` |
| `cobis.api.cwc.party-location-full-path` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/cobis.api.cwc.party-location-full-path` |
| `cobis.api.cwc.party-risk-full-path` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/cobis.api.cwc.party-risk-full-path` |
| `cobis.api.cwc.natural-person-full-path` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/cobis.api.cwc.natural-person-full-path` |
| `cobis.api.cwc.type-customer-full-path` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/cobis.api.cwc.type-customer-full-path` |
| `cobis.api.cwc.juridica-profile-full-path` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/cobis.api.cwc.juridica-profile-full-path` |

---

## Feign Config Base — `CobisClientConfigBase`

| Nombre en YML | Nombre en Parameter Store |
|---|---|
| `cobis.api.timeout-ms` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/cobis.api.timeout-ms` |

> `cobis.api.timeout-ms` se usa tanto para connect timeout como para read timeout en todos los clientes Feign y en `RestTemplateConfig`.

---

## Auth Adapter — `CobisAuthApiAdapter`

| Nombre en YML | Nombre en Parameter Store |
|---|---|
| `cobis.credentials.username` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/cobis.credentials.username` |
| `cobis.credentials.password` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/cobis.credentials.password` |
| `cobis.jwt.lock-ttl-ms` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/cobis.jwt.lock-ttl-ms` |
| `cobis.jwt.max-wait-attempts` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/cobis.jwt.max-wait-attempts` |
| `cobis.jwt.backoff-ms` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/cobis.jwt.backoff-ms` |
| `cobis.login.culture` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/cobis.login.culture` |
| `cobis.login.role` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/cobis.login.role` |
| `cobis.login.office` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/cobis.login.office` |
| `cobis.login.filial` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/cobis.login.filial` |

---

## Cache L1 — `CobisTokenCacheLocal` (Caffeine)

| Nombre en YML | Nombre en Parameter Store |
|---|---|
| `cobis.jwt.refresh-skew-seconds` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/cobis.jwt.refresh-skew-seconds` |
| `cobis.jwt.min-ttl-accept-seconds` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/cobis.jwt.min-ttl-accept-seconds` |

---

## Cache L2 — `CobisTokenCacheRedis`

| Nombre en YML | Nombre en Parameter Store |
|---|---|
| `cobis.env` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/cobis.env` |
| `cobis.jwt.safety-seconds` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/cobis.jwt.safety-seconds` |

---

## Redis — `spring.data.redis`

| Nombre en YML | Nombre en Parameter Store |
|---|---|
| `spring.data.redis.host` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/spring.data.redis.host` |
| `spring.data.redis.port` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/spring.data.redis.port` |
| `spring.data.redis.password` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/spring.data.redis.password` |

---

## Resilience4j — Retry (`cobisApi`)

| Nombre en YML | Nombre en Parameter Store |
|---|---|
| `resilience4j.retry.instances.cobisApi.maxAttempts` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/resilience4j.retry.instances.cobisApi.maxAttempts` |
| `resilience4j.retry.instances.cobisApi.waitDuration` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/resilience4j.retry.instances.cobisApi.waitDuration` |
| `resilience4j.retry.instances.cobisApi.enableExponentialBackoff` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/resilience4j.retry.instances.cobisApi.enableExponentialBackoff` |
| `resilience4j.retry.instances.cobisApi.exponentialBackoffMultiplier` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/resilience4j.retry.instances.cobisApi.exponentialBackoffMultiplier` |

---

## Resilience4j — Circuit Breaker (`cobisApi`)

| Nombre en YML | Nombre en Parameter Store |
|---|---|
| `resilience4j.circuitbreaker.instances.cobisApi.slidingWindowSize` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/resilience4j.circuitbreaker.instances.cobisApi.slidingWindowSize` |
| `resilience4j.circuitbreaker.instances.cobisApi.failureRateThreshold` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/resilience4j.circuitbreaker.instances.cobisApi.failureRateThreshold` |
| `resilience4j.circuitbreaker.instances.cobisApi.waitDurationInOpenState` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/resilience4j.circuitbreaker.instances.cobisApi.waitDurationInOpenState` |
| `resilience4j.circuitbreaker.instances.cobisApi.permittedNumberOfCallsInHalfOpenState` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/resilience4j.circuitbreaker.instances.cobisApi.permittedNumberOfCallsInHalfOpenState` |

---

## Métricas — CloudWatch

| Nombre en YML | Nombre en Parameter Store |
|---|---|
| `CLOUDWATCH_METRICS_ENABLED` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/CLOUDWATCH_METRICS_ENABLED` |
| `CLOUDWATCH_NAMESPACE` | `/bdr/integracion/${ENVIRONMENT}/customer/adapter/CLOUDWATCH_NAMESPACE` |

---

## Notas

- Los parámetros de tipo **SecureString** recomendados (datos sensibles):
  - `cobis.credentials.username`
  - `cobis.credentials.password`
  - `spring.data.redis.password`

- El resto pueden ser de tipo **String**.

- El `application.yml` ya tiene configurado el import desde Parameter Store:
  ```yaml
  spring:
    config:
      import: optional:aws-parameterstore:/bdr/integracion/${ENVIRONMENT:desa}/customer/adapter/
  ```
  Por lo tanto, cualquier parámetro creado bajo ese prefix será inyectado automáticamente sin necesidad de declararlo en el YML.

- Los parámetros de Resilience4j son **opcionales** en Parameter Store; si no se crean, se usan los valores del `application.yml`. Solo créalos si necesitas ajustar los valores por ambiente sin redesplegar.
