# Prices Service

Servicio REST desarrollado con Spring Boot para consultar el precio aplicable a un producto de una marca en una fecha determinada, resolviendo posibles coincidencias según la prioridad de las tarifas.

## Requisitos previos

- Java 21.
- No es necesario instalar Maven, ya que el proyecto incluye Maven Wrapper.

## Menú

1. [Ejecución de los tests](#ejecución-de-los-tests)
2. [Arranque de la aplicación](#arranque-de-la-aplicación)
3. [Pruebas con Swagger UI](#pruebas-con-swagger-ui)
4. [Verificación de los escenarios con cURL](#verificación-de-los-escenarios-con-curl)
5. [Arquitectura](#arquitectura)
6. [Estructura del proyecto](#estructura-del-proyecto)
7. [Funcionamiento de la API](#funcionamiento-de-la-api)
8. [Resumen de las pruebas](#resumen-de-las-pruebas)
9. [Stack tecnológico](#stack-tecnológico)
10. [Salud del microservicio](#salud-del-microservicio)
11. [Consola H2](#consola-h2)
12. [Repositorio](#repositorio)

## Ejecución de los tests

Para ejecutar todos los tests del proyecto:

```bash
./mvnw test
```

## Arranque de la aplicación

Para iniciar la aplicación en local:

```bash
./mvnw spring-boot:run
```

Al arrancar, la base de datos H2 en memoria se crea y carga automáticamente con los datos del enunciado.

La aplicación queda disponible en:

<http://localhost:8080>

## Pruebas con Swagger UI

Con la aplicación arrancada, accede a:

<http://localhost:8080/swagger-ui.html>

Desde Swagger UI puedes consultar el endpoint de precios e introducir la fecha de aplicación, el identificador del producto y el identificador de la marca.

## Verificación de los escenarios con cURL

Con la aplicación arrancada, ejecuta las siguientes peticiones:

```bash
# Escenario 1: 14/06/2020 a las 10:00 - tarifa 1
curl "http://localhost:8080/api/v1/prices?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1"

# Escenario 2: 14/06/2020 a las 16:00 - tarifa 2
curl "http://localhost:8080/api/v1/prices?applicationDate=2020-06-14T16:00:00&productId=35455&brandId=1"

# Escenario 3: 14/06/2020 a las 21:00 - tarifa 1
curl "http://localhost:8080/api/v1/prices?applicationDate=2020-06-14T21:00:00&productId=35455&brandId=1"

# Escenario 4: 15/06/2020 a las 10:00 - tarifa 3
curl "http://localhost:8080/api/v1/prices?applicationDate=2020-06-15T10:00:00&productId=35455&brandId=1"

# Escenario 5: 16/06/2020 a las 21:00 - tarifa 4
curl "http://localhost:8080/api/v1/prices?applicationDate=2020-06-16T21:00:00&productId=35455&brandId=1"
```

También está disponible una [colección de Postman](postman/Prices-Service.postman_collection.json) con estos cinco escenarios y validaciones automáticas de la respuesta.

## Arquitectura

El proyecto utiliza una arquitectura hexagonal, separando la lógica de negocio de los detalles externos como HTTP y la persistencia.

```text
             Adaptador REST
                   │
                   ▼
          Puerto de entrada
                   │
                   ▼
          Servicio de aplicación
                   │
                   ▼
          Puerto de salida
                   │
                   ▼
        Adaptador de persistencia
                   │
                   ▼
              Base de datos H2
```

### Capas principales

- `domain`: contiene los modelos de negocio, las excepciones y los puertos de entrada y salida.
- `application`: implementa los casos de uso mediante `PriceService`.
- `infrastructure.adapter.in`: expone el endpoint REST, transforma las respuestas y gestiona errores y trazabilidad.
- `infrastructure.adapter.out`: implementa la persistencia con Spring Data JPA y transforma las entidades en modelos de dominio.
- `infrastructure.config`: configura la inyección de dependencias, OpenAPI y los filtros web.

El dominio no depende de Spring ni de Lombok. Las dependencias hacia frameworks y bases de datos quedan aisladas en la capa de infraestructura.

## Estructura del proyecto

```text
src
├── main
│   ├── java/com/gft/price
│   │   ├── PricesApplication.java
│   │   ├── application
│   │   │   └── service
│   │   ├── domain
│   │   │   ├── exception
│   │   │   ├── model
│   │   │   └── port
│   │   │       ├── in
│   │   │       └── out
│   │   └── infrastructure
│   │       ├── adapter
│   │       │   ├── in/rest
│   │       │   └── out/persistence
│   │       └── config
│   └── resources
│       ├── application.yaml
│       ├── data.sql
│       └── schema.sql
└── test
    └── java/com/gft/price
        ├── application
        ├── domain
        └── infrastructure
```

Los tests mantienen una estructura paralela a la del código principal para facilitar su localización y mantenimiento.

## Funcionamiento de la API

### Consultar precio aplicable

```http
GET /api/v1/prices
```

Se envían tres parámetros mediante query string:

| Parámetro | Tipo | Obligatorio | Validación | Ejemplo |
|---|---|---|---|---|
| `applicationDate` | ISO date-time | Sí | Formato válido | `2020-06-14T10:00:00` |
| `productId` | Número | Sí | Mayor que 0 | `35455` |
| `brandId` | Número | Sí | Mayor que 0 | `1` |

Cuando existen varias tarifas aplicables para la misma marca, producto y fecha, se selecciona la tarifa con mayor prioridad.

**Ejemplo de respuesta exitosa (`200 OK`):**

```json
{
  "brandId": 1,
  "productId": 35455,
  "tariffId": 1,
  "startDate": "2020-06-14T00:00:00",
  "endDate": "2020-12-31T23:59:59",
  "amount": 35.50,
  "currencyCode": "EUR"
}
```

En la respuesta, `tariffId` identifica la tarifa aplicada, `amount` representa el precio final y `currencyCode` indica la moneda.

**Posibles errores:**

| Código | Cuándo ocurre |
|---|---|
| `400` | Falta un parámetro, el formato es inválido o el valor es menor o igual que 0. |
| `404` | No existe un precio aplicable para la combinación solicitada. |
| `500` | Se produce un error inesperado en el servidor. |

**Ejemplo de error (`404 Not Found`):**

```json
{
  "statusCode": 404,
  "message": "No applicable price found for brandId=1, productId=99999 at 2020-06-14T10:00",
  "timestamp": "2026-04-11T12:00:00"
}
```

## Resumen de las pruebas

| Clase | Tipo | Tests | Qué valida |
|---|---|---:|---|
| `PriceTest` | Unitario | 5 | Creación de precios válidos, fechas invertidas, importes y prioridades negativas, e identificadores no positivos. |
| `PriceQueryTest` | Unitario | 2 | Creación de consultas válidas y rechazo de identificadores no positivos. |
| `PriceServiceTest` | Unitario | 2 | El caso de uso con un mock del repositorio: precio encontrado y precio inexistente. |
| `PriceNotFoundExceptionTest` | Unitario | 1 | Que el mensaje incluya `brandId`, `productId` y la fecha de aplicación. |
| `PriceControllerTest` | Unitario | 6 | Construcción de la consulta REST, mapeo de la respuesta y propagación de errores del caso de uso para los cinco escenarios. |
| `PriceResponseMapperTest` | Unitario | 1 | Conversión del modelo de dominio a la respuesta REST. |
| `PriceEntityMapperTest` | Unitario | 1 | Conversión de la entidad JPA al modelo de dominio. |
| `PricePersistenceAdapterTest` | Unitario | 2 | Mapeo del precio encontrado y respuesta vacía cuando no existe. |
| `PriceEntityTest` | Unitario | 2 | Getters de la entidad y constructor sin argumentos requerido por JPA. |
| `MdcRequestFilterTest` | Unitario | 2 | Reutilización del request ID recibido y generación automática cuando falta. |
| `PriceServiceIntegrationTest` | Integración | 2 | Resolución del precio mediante el servicio real y propagación de `PriceNotFoundException` con H2. |
| `PriceControllerIntegrationTest` | Integración | 10 | Los 5 escenarios del enunciado y validaciones HTTP de errores `400` y `404` con la aplicación levantada. |
| `PriceJpaRepositoryTest` | Integración | 3 | Prioridad, límites inclusivos de las fechas, ausencia de precio fuera del rango y filtrado por producto. |
| `PricesApplicationTests` | Integración | 1 | Arranque correcto del contexto de Spring. |

**Total: 40 tests definidos.**

### Escenarios de error cubiertos en el controller

| Caso | Respuesta esperada |
|---|---:|
| Producto sin precio aplicable | `404` |
| `applicationDate` no enviado | `400` |
| `applicationDate` con formato inválido | `400` |
| `productId` = 0 | `400` |
| `brandId` = -1 | `400` |

### Casos cubiertos en la query del repositorio

| Escenario | Resultado esperado |
|---|---|
| Dos precios solapados | Gana el de mayor prioridad. |
| Fecha exactamente al inicio del rango | El precio se considera aplicable. |
| Fecha exactamente al final del rango | El precio se considera aplicable. |
| Fecha fuera de todos los rangos | Devuelve vacío. |
| `productId` diferente | Devuelve vacío. |

## Stack tecnológico

| Tecnología | Uso |
|---|---|
| Java 21 | Lenguaje y runtime de la aplicación. |
| Spring Boot 4.1.2 | Framework principal. |
| Spring Web MVC | Desarrollo del endpoint REST. |
| Spring Data JPA | Acceso y consultas sobre la base de datos. |
| H2 Database | Base de datos relacional en memoria. |
| Jakarta Bean Validation | Validación de los parámetros de entrada. |
| Springdoc OpenAPI 3.1.0 | Generación de la documentación OpenAPI y Swagger UI. |
| Spring Boot Actuator | Exposición de funcionalidades de monitorización. |
| Lombok | Reducción de código repetitivo en infraestructura y DTOs. |
| JUnit 5 | Tests unitarios y de integración. |
| Mockito | Simulación de dependencias en los tests unitarios. |
| Maven Wrapper | Compilación, ejecución y gestión del proyecto sin requerir Maven instalado globalmente. |

## Salud del microservicio

La aplicación utiliza Spring Boot Actuator para exponer un endpoint de salud:

```http
GET /actuator/health
```

Con la aplicación arrancada, puedes validarlo mediante:

```bash
curl http://localhost:8080/actuator/health
```

Si el microservicio está operativo, la respuesta será similar a:

```json
{
  "status": "UP"
}
```

## Consola H2

La consola web de H2 está disponible en:

<http://localhost:8080/h2-console>

Datos de conexión:

```text
JDBC URL: jdbc:h2:mem:prices
Usuario: SA
Contraseña: vacío
```

## Repositorio

El código fuente está disponible en el siguiente repositorio público:

[https://github.com/ojtoscano/prices](https://github.com/ojtoscano/prices)
