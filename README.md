# Product Pricing API

![Java](https://img.shields.io/badge/Java-21-007396?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-6DB33F?logo=springboot&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-9.7.1-02303A?logo=gradle&logoColor=white)
![H2](https://img.shields.io/badge/DB-H2%20in--memory-1F5FA8)
![Coverage](https://img.shields.io/badge/coverage-98%25-brightgreen?logo=codecov&logoColor=white)

Servicio REST que resuelve el precio aplicable a un producto de una cadena en una fecha determinada y permite gestionar las tarifas que lo determinan.

Cuando varias tarifas se solapan en el tiempo, el servicio decide cuál aplica según su prioridad. Además de la consulta, expone un CRUD completo sobre las tarifas (`/prices`), con validación de dominio y errores homogéneos en formato [RFC 9457 Problem Details](https://www.rfc-editor.org/rfc/rfc9457).

## Tabla de contenidos

- [Stack](#stack)
- [Arquitectura](#arquitectura)
- [Requisitos](#requisitos)
- [Puesta en marcha](#puesta-en-marcha)
- [API](#api)
- [Datos de ejemplo](#datos-de-ejemplo)
- [Tests](#tests)
- [Cobertura](#cobertura)
- [Calidad de código](#calidad-de-código)
- [Configuración](#configuración)
- [Decisiones de diseño](#decisiones-de-diseño)
- [Contribuir](#contribuir)
- [Notas del autor](#notas-del-autor)

## Stack

| Área | Tecnología |
|---|---|
| Lenguaje | Java 21 |
| Framework | Spring Boot 4.1.1 (Web MVC, Data JPA) |
| Base de datos | H2 en memoria |
| Build | Gradle 9.7.1 (wrapper incluido) + `Makefile` |
| Documentación | springdoc-openapi 3.1.1 (OpenAPI 3 + Swagger UI) |
| Tests | JUnit 5, Mockito, AssertJ, Spring MockMvc, JaCoCo |
| Calidad | Spotless (google-java-format), Checkstyle, SpotBugs + FindSecBugs |

## Arquitectura

Arquitectura hexagonal (Ports & Adapters) con CQRS ligero mediante buses de comandos y queries en memoria. El código se organiza en dos raíces de código fuente dentro de un único módulo Gradle:

```
apps/main/com/inditex/apps/pricing      # Adaptadores de entrada y arranque de Spring
├── config                              # Versionado de API, OpenAPI
└── controller                          # Controllers REST (uno por caso de uso) y manejo de errores
    └── prices/v1/{get,post,put,patch,delete}

pricing/main/com/inditex/pricing        # Núcleo de negocio, sin dependencias de framework en el dominio
├── prices                              # Bounded context de precios
│   ├── domain                          # Agregado Price, value objects, puertos, excepciones de dominio
│   ├── application                     # Casos de uso: create, update, patch, delete, find, findbyid, searchall
│   └── infrastructure/persistence/jpa  # Adaptador JPA del puerto PriceRepository
└── shared                              # Kernel compartido: Identifier, value objects base, buses
```

Flujo de una petición:

```
Controller ──► CommandBus / QueryBus ──► Handler ──► Caso de uso ──► PriceRepository (puerto)
                                                                          │
                                                                PriceJpaAdapter (adaptador) ──► H2
```

Cada controller declara su propio `errorMapping` (excepción de dominio → status HTTP); `ApiExceptionHandler` lo aplica y construye la respuesta `ProblemDetail` con un `errorCode` estable.

## Requisitos

- JDK 21
- `make` (opcional; todos los comandos tienen equivalente con `./gradlew`)

No es necesario instalar Gradle ni ninguna base de datos: el proyecto usa el wrapper y H2 en memoria.

## Puesta en marcha

```bash
git clone https://github.com/alesssruizz/product-pricing-api.git
cd product-pricing-api
make run          # ./gradlew bootRun
```

La aplicación arranca en `http://localhost:8080/pricing-service` y carga los datos de ejemplo (`pricing/main/resources/database/data.sql`) en cada arranque.

| Recurso | URL |
|---|---|
| API | `http://localhost:8080/pricing-service/api/v1` |
| Swagger UI | `http://localhost:8080/pricing-service/swagger-ui/index.html` |
| OpenAPI (JSON) | `http://localhost:8080/pricing-service/v3/api-docs` |
| Health check | `http://localhost:8080/pricing-service/health-check` |

Comandos disponibles:

| Comando | Descripción |
|---|---|
| `make all` | Build completo con tests (`./gradlew clean build`) |
| `make run` | Arranca la aplicación |
| `make test` | Ejecuta los tests y genera el informe de cobertura |
| `make lint` | Checkstyle y SpotBugs sobre código y tests |
| `make fix-lint` | Aplica el formato automático (Spotless) |

## API

Todas las rutas cuelgan de `/pricing-service/api/v1`. La versión forma parte de la ruta (`/api/{version}`).

| Método | Ruta | Descripción | Éxito |
|---|---|---|---|
| `GET` | `/prices/find` | Precio aplicable para una cadena, un producto y una fecha | `200` |
| `GET` | `/prices` | Listado de tarifas (sin orden garantizado) | `200` |
| `GET` | `/prices/{id}` | Tarifa por id | `200` |
| `POST` | `/prices` | Crea una tarifa con id aportado por el cliente | `201` + `Location` |
| `PUT` | `/prices/{id}` | Reemplaza una tarifa existente | `204` |
| `PATCH` | `/prices/{id}` | Modifica parcialmente una tarifa existente | `204` |
| `DELETE` | `/prices/{id}` | Elimina una tarifa | `204` |

### Consultar el precio aplicable

```bash
curl "http://localhost:8080/pricing-service/api/v1/prices/find?applicationDate=2020-06-14T16:00:00&productId=35455&brandId=1"
```

```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 2,
  "startDate": "2020-06-14T15:00:00",
  "endDate": "2020-06-14T18:30:00",
  "price": 25.45,
  "currency": "EUR"
}
```

Entre las tarifas cuya vigencia incluye la fecha, aplica la de mayor `priority`; a igual prioridad, la de inicio más reciente.

### Crear una tarifa

El `id` es un UUID que genera el cliente.

```bash
curl -i -X POST "http://localhost:8080/pricing-service/api/v1/prices" \
  -H "Content-Type: application/json" \
  -d '{
        "id": "3f2b8c1e-7d4a-4b9e-9c51-2a6f0e8d7b13",
        "brandId": 1,
        "productId": 35455,
        "priceList": 5,
        "priority": 2,
        "startDate": "2021-01-01T00:00:00",
        "endDate": "2021-01-31T23:59:59",
        "price": 12.30,
        "currency": "EUR"
      }'
```

```
HTTP/1.1 201 Created
Location: http://localhost:8080/pricing-service/api/v1/prices/3f2b8c1e-7d4a-4b9e-9c51-2a6f0e8d7b13
```

### Errores

Los errores siguen el formato Problem Details (`application/problem+json`) e incluyen un `errorCode` cuando el error procede del dominio:

```json
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "Price not found for brandId <1>, productId <35455> and application date <2019-01-01T00:00>",
  "errorCode": "price_not_found"
}
```

| Status | `errorCode` | Causa |
|---|---|---|
| `400` | `invalid_uuid` | El id (body o path) no es un UUID válido |
| `400` | `price_field_required` | Falta un campo obligatorio o viene vacío |
| `400` | `invalid_date_format` | Fecha que no cumple ISO-8601 (`yyyy-MM-ddTHH:mm:ss`) |
| `400` | `invalid_price_date_range` | `endDate` no es posterior a `startDate` |
| `400` | `invalid_price_quantity` | Precio menor o igual que cero |
| `400` | `invalid_price_currency` | Moneda que no es un código ISO 4217 |
| `400` | `invalid_reference` | La cadena o el producto no existen |
| `400` | — | Parámetro ausente o mal tipado, o JSON mal formado (validación de Spring) |
| `404` | `price_not_found` | No existe la tarifa o no hay ninguna aplicable |
| `409` | `price_id_already_exists` | `POST` con un id que ya existe |
| `409` | `price_already_exists` | Ya hay otra tarifa con la misma cadena, producto, prioridad y fecha de inicio |
| `500` | — | Error inesperado (se registra en el log) |

La especificación completa, con todos los esquemas, está disponible en Swagger UI.

## Datos de ejemplo

| ID | BRAND_ID | START_DATE | END_DATE | PRICE_LIST | PRODUCT_ID | PRIORITY | PRICE | CURR |
|---|---|---|---|---|---|---|---|---|
| `…0001` | 1 | 2020-06-14 00:00:00 | 2020-12-31 23:59:59 | 1 | 35455 | 0 | 35.50 | EUR |
| `…0002` | 1 | 2020-06-14 15:00:00 | 2020-06-14 18:30:00 | 2 | 35455 | 1 | 25.45 | EUR |
| `…0003` | 1 | 2020-06-15 00:00:00 | 2020-06-15 11:00:00 | 3 | 35455 | 1 | 30.50 | EUR |
| `…0004` | 1 | 2020-06-15 16:00:00 | 2020-12-31 23:59:59 | 4 | 35455 | 1 | 38.95 | EUR |

Los ids completos son `00000000-0000-0000-0000-00000000000N` (N = 1..4).

## Tests

```bash
make test         # ./gradlew test
```

- **Unitarios** (`pricing/test`): dominio y casos de uso, sin Spring, con Mockito.
- **Aceptación** (`apps/test`): peticiones HTTP reales con MockMvc contra H2. Cada contexto de Spring usa su propia base de datos en memoria.

Los escenarios del enunciado están cubiertos en `PricesGetApplicableControllerShould` (producto 35455, cadena 1):

| Fecha de aplicación | Tarifa | Precio |
|---|---|---|
| 2020-06-14 10:00 | 1 | 35.50 EUR |
| 2020-06-14 16:00 | 2 | 25.45 EUR |
| 2020-06-14 21:00 | 1 | 35.50 EUR |
| 2020-06-15 10:00 | 3 | 30.50 EUR |
| 2020-06-16 21:00 | 4 | 38.95 EUR |

## Cobertura

El proyecto tiene un **98 % de cobertura** de código según JaCoCo.

| Métrica | Cobertura | Cubierto / total |
|---|---|---|
| Instrucciones | 98 % | 2.287 / 2.327 |
| Líneas | 97 % | 562 / 575 |
| Ramas | 96 % | 81 / 84 |
| Métodos | 96 % | 183 / 189 |
| Clases | 100 % | 82 / 82 |

El informe se genera automáticamente al ejecutar `make test`, en `build/reports/jacoco/test/html/index.html`.

## Calidad de código

| Herramienta | Uso |
|---|---|
| Spotless + google-java-format | Formato del código (`make fix-lint`) |
| Checkstyle | Estilo Google (configuración en `config/checkstyle`), 0 avisos permitidos |
| SpotBugs + FindSecBugs | Análisis estático y de seguridad (exclusiones en `config/spotbugs`) |

`make fix-lint` deja el código listo para que `make lint` pase sin avisos.

## Configuración

Configuración principal en `apps/main/resources/application.properties`.

| Propiedad | Valor por defecto |
|---|---|
| `server.port` | `8080` |
| `server.servlet.context-path` | `/pricing-service` |
| `spring.datasource.url` | `jdbc:h2:mem:pricingdb` |
| `spring.jpa.hibernate.ddl-auto` | `validate` (el esquema lo crea `schema.sql`) |
| `spring.h2.console.enabled` | `false` |

**Perfil `dev`**: habilita la consola de H2.

```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

Consola en `http://localhost:8080/pricing-service/h2-console` (JDBC URL `jdbc:h2:mem:pricingdb`, usuario `sa`, sin contraseña).

**Logs**: consola y fichero `var/logs/product-pricing-api.log` (configuración en `apps/main/resources/logback.xml`).

## Decisiones de diseño

- **Ids generados por el cliente.** El id de una tarifa es un UUID que envía el cliente en el `POST`. La validación vive en `Identifier` (`shared/domain`), reutilizable por cualquier identificador, y lanza el error genérico `InvalidUUID`.
- **Alta y modificación explícitas en el puerto.** `PriceRepository` expone `create` y `update` en lugar de un `save` genérico. La entidad JPA implementa `Persistable`, de forma que un alta se ejecuta como `persist` (solo `INSERT`) y una modificación como `merge`.
- **Errores por caso de uso.** Cada controller decide qué status corresponde a cada error de dominio; el dominio no conoce HTTP.
- **Versionado por ruta.** `/api/v1/...`, resuelto con el soporte nativo de versionado de API de Spring.

## Contribuir

Las incidencias y dudas se gestionan mediante [issues](https://github.com/alesssruizz/product-pricing-api/issues). Las pull requests son bienvenidas; antes de abrir una:

1. Ejecuta `make fix-lint`, `make lint` y `make test`.
2. Usa [Conventional Commits](https://www.conventionalcommits.org/) en los mensajes (`feat:`, `fix:`, `refactor:`, `test:`, `docs:`…).

## Notas del autor

Estaré atento al repositorio estos días por si tenéis cualquier incidencia o duda: podéis abrir una issue y la contestaré lo antes posible. Y, por supuesto, encantado de recibir cualquier PR para mejorar el código y tener así feedback directo vuestro.

### Desarrollo asistido por IA

Parte de este proyecto se construyó con ayuda de Claude Code, siguiendo un flujo de Spec-Driven Development (propuesta → spec → tareas → implementación → verificación) apoyado en sub-agentes para algunas fases de implementación y testing. Todas las decisiones de arquitectura, los criterios de diseño y la revisión final fueron revisados por mí.

### Puntos a explicar

> Recordatorio personal: temas a desarrollar en la presentación o en la revisión del código.

1. **UUID generado por el cliente.** Por qué el cliente aporta el id (identidad conocida antes de persistir, `Location` inmediato) y por qué un `POST` repetido con el mismo id devuelve `409 price_id_already_exists` en vez de ser idempotente.
2. **`Identifier` e `InvalidUUID` en el kernel compartido.** La validación (UUID canónico de 36 caracteres) vive en `shared/domain` para reutilizarse; se eligió un error genérico (`invalid_uuid`) frente a uno por agregado, asumiendo que el `errorCode` no indica qué id falló.
3. **`Persistable` e `isNew`: `persist` frente a `merge`.** Con ids asignados y sin `@Version`, `save()` de Spring Data siempre haría `merge` (`SELECT` + `INSERT`). `PriceJpaEntity` implementa `Persistable<UUID>` con un flag `@Transient` que fijan `forCreate`/`forUpdate`; no se usan callbacks `@PostLoad` porque el adaptador construye siempre una entidad nueva desde el dominio.
4. **Por qué se mantiene `existsById` antes del alta.** La clave primaria es la red de seguridad, pero `DataIntegrityViolationException` no distingue qué restricción falló y acabaría en un `500`; la comprobación explícita expresa la regla de dominio y conserva el orden de validación.
5. **Igualdad de `Identifier` con Lombok.** `@EqualsAndHashCode` usa `instanceof` + `canEqual`: dos subclases distintas con el mismo UUID serían iguales. Hoy solo existe `PriceId`; si aparece otra, hay que añadir `@EqualsAndHashCode(callSuper = true)` en las subclases o volver a un `equals` basado en `getClass()`.
