# Product Pricing API

Servicio REST que resuelve la tarifa y el precio final aplicable a un producto de una cadena en una fecha determinada, cuando existen varias tarifas con rangos de vigencia solapados.

## Stack

- Java 21
- Spring Boot 4.1.1 (Web MVC, Data JPA)
- H2 (base de datos en memoria)
- Gradle
- JUnit 5 + Mockito + AssertJ

## Arquitectura

Arquitectura Hexagonal (Ports & Adapters) + CQRS ligero, organizada en dos módulos Gradle:

- **`apps`**: adaptadores de entrada HTTP (controllers, manejo de errores, configuración de Spring).
- **`pricing`**: el core de negocio, en `com.inditex.pricing`, dividido por bounded context (`prices`) y kernel compartido (`shared`):
  - `domain`: entidades, value objects y puertos (interfaces de repositorio). Sin dependencias de framework.
  - `application`: casos de uso (queries + handlers), orquestan el dominio.
  - `infrastructure`: adaptadores concretos (JPA, Spring MVC).

La regla de negocio central —qué tarifa aplica cuando dos se solapan— vive en `Price.mostApplicable(List<Price>)`, en el dominio, testeada sin Spring ni base de datos.

## Cómo levantar el proyecto

```bash
./gradlew bootRun
```

La aplicación arranca en `http://localhost:8080` e inicializa H2 con los datos de ejemplo del enunciado (`pricing/main/resources/database/data.sql`) en cada arranque.

## Cómo correr los tests

```bash
./gradlew test
```

Incluye tests de aceptación (HTTP + H2 real) y unitarios (dominio y aplicación, sin Spring).

## Endpoint principal

### `GET /api/v1/price`

Devuelve el **único** precio aplicable para un producto, una cadena y una fecha dados.

**Parámetros de entrada** (query params):

| Parámetro | Tipo | Descripción |
|---|---|---|
| `applicationDate` | `string` (ISO-8601, `yyyy-MM-ddTHH:mm:ss`) | Fecha y hora de aplicación |
| `productId` | `long` | Identificador del producto |
| `brandId` | `long` | Identificador de la cadena (1 = ZARA) |

**Ejemplo de petición:**

```
GET /api/v1/price?applicationDate=2020-06-14T16:00:00&productId=35455&brandId=1
```

**Respuesta `200 OK`:**

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

**Errores:**

| Status | `errorCode` | Cuándo |
|---|---|---|
| `400 Bad Request` | `invalid_date_format` | `applicationDate` no es una fecha ISO-8601 válida |
| `404 Not Found` | `price_not_found` | No hay ninguna tarifa vigente para esos parámetros |

### `GET /api/v1/prices`

Devuelve el listado completo de tarifas cargadas (utilidad de consulta, sin lógica de negocio).

## Datos de ejemplo (H2)

| BRAND_ID | START_DATE | END_DATE | PRICE_LIST | PRODUCT_ID | PRIORITY | PRICE | CURR |
|---|---|---|---|---|---|---|---|
| 1 | 2020-06-14 00:00:00 | 2020-12-31 23:59:59 | 1 | 35455 | 0 | 35.50 | EUR |
| 1 | 2020-06-14 15:00:00 | 2020-06-14 18:30:00 | 2 | 35455 | 1 | 25.45 | EUR |
| 1 | 2020-06-15 00:00:00 | 2020-06-15 11:00:00 | 3 | 35455 | 1 | 30.50 | EUR |
| 1 | 2020-06-15 16:00:00 | 2020-12-31 23:59:59 | 4 | 35455 | 1 | 38.95 | EUR |

`PRIORITY` desambigua qué tarifa aplica cuando dos rangos se solapan: gana la de mayor prioridad.

## Documentación interactiva (OpenAPI)

Con la aplicación levantada:

- Spec: `http://localhost:8080/v3/api-docs`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`

Generada automáticamente por `springdoc-openapi` a partir del código — no hay un YAML mantenido a mano.

## Consola H2

`http://localhost:8080/h2-console` — JDBC URL: `jdbc:h2:mem:pricingdb`, usuario `sa`, sin contraseña.
