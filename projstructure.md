# Verno Architecture

Verno is structured as a modular Maven project. The goal of this architecture is to clearly separate UI, business logic, persistence and communication concerns while keeping the internal RPC mechanism type-safe and easy to use.

## Module Overview

```text
verno
??? lib
??? common
??? contract
??? client
??? db
??? report
??? server
??? ui
??? gateway
??? arch
```

## Dependency Structure

```text
                ???????????????
                ?     lib     ?
                ???????????????
                       ?
                ???????????????
                ?   common    ?
                ???????????????
                       ?
                ???????????????
                ?  contract   ?
                ???????????????
                       ?
              ???????????????????
              ?                 ?
      ????????????????? ????????????????
      ?    client     ? ?    server    ?
      ????????????????? ????????????????
              ?                 ?
              ?          ???????????????
              ?          ?     db      ?
              ?          ???????????????
              ?
      ?????????????????
      ?      ui       ?
      ?????????????????


      ?????????????????
      ?   gateway     ?
      ?????????????????
              ?
              ?
           server


      ?????????????????
      ?    report     ?
      ?????????????????


      ?????????????????
      ?     arch      ?
      ?????????????????
```

## Module Responsibilities

### `lib`

General-purpose utility library.

Examples:

- `New`
- `Lazy`
- Custom annotations
- Utility classes
- Language library

Restrictions:

- Must not depend on Spring
- Must not depend on Vaadin
- Must not depend on JPA
- Must not depend on any other Verno module


---

### `common`

Shared domain concepts and reusable value objects.

Examples:

- `Language`
- `PhoneNumber`
- `Address`
- `Gender`
- `Money`
- `DateRange`

Restrictions:

- Should remain framework-light
- Must not depend on `ui`, `db` or `server`


---

### `contract`

Contains the internal RPC contracts.

Examples:

```java
@RpcEndpoint
public interface CourseEndpoint {

    List<CourseDto> findCourses(CourseFilterDto filter);

}
```

Contains:

- RPC endpoint interfaces
- DTOs
- Filters
- Requests
- Responses
- RPC exceptions


---

### `client`

Client-side implementation of the RPC system.

Responsibilities:

- Creating endpoint proxies
- Serializing requests
- Sending HTTP requests
- Deserializing responses
- Error handling

Example:

```java
CourseEndpoint endpoint =
        rpcClient.create(CourseEndpoint.class);
```


---

### `server`

Contains the business logic.

Examples:

- BOs
- Services
- Security
- Mail handling
- Billing
- RPC endpoint implementations

Example:

```java
@RpcResource
public class CourseEndpointImpl
        implements CourseEndpoint {

}
```


---

### `db`

Persistence layer.

Contains:

- Entities
- Repositories
- JPA configuration
- Flyway migrations
- PostgreSQL support
- H2 support


---

### `ui`

Vaadin frontend.

Contains:

- Views
- Components
- Binder logic
- Themes
- Navigation
- Layouts

The UI communicates with the backend exclusively through the RPC client.


---

### `gateway`

HTTP entry point for external communication.

Examples:

- REST controllers
- Stripe webhooks
- Health checks
- Download endpoints
- Public APIs


---

### `report`

Report generation module.

Contains:

- JasperReports
- OpenHtmlToPdf
- Thymeleaf
- PDF generation


---

### `arch`

Architecture validation module.

Contains:

- ArchUnit tests
- Dependency rules
- Layer validation


## RPC Mechanism

The internal communication between `ui` and `server` is based on a custom RPC mechanism.

Example contract:

```java
@RpcEndpoint
public interface CourseEndpoint {

    List<CourseDto> findCourses(CourseFilterDto filter);

}
```

Server implementation:

```java
@RpcResource
public class CourseEndpointImpl
        implements CourseEndpoint {

    @Override
    public List<CourseDto> findCourses(CourseFilterDto filter) {
        ...
    }

}
```

Client usage:

```java
CourseEndpoint endpoint =
        rpcClient.create(CourseEndpoint.class);

endpoint.findCourses(filter);
```

The RPC framework automatically resolves endpoint implementations through a registry that scans all classes annotated with `@RpcResource`.


## Architectural Rules

The following rules should be enforced using ArchUnit.

```text
ui
 ??? may depend on
     client
     contract
     common
     lib


server
 ??? may depend on
     contract
     common
     db
     report
     lib


db
 ??? must not depend on
     ui
     server
     gateway


common
 ??? must not depend on
     ui
     db
     server


lib
 ??? must not depend on
     any Verno module
```