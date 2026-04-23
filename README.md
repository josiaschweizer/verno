# Verno - Course Management System

Verno is a comprehensive course management application built with Spring Boot and Vaadin. It provides a modern web interface for managing courses, participants, instructors, and course schedules with multi-tenant support.

## Overview

Verno is designed to help organizations manage their course offerings, track participants, assign instructors, and organize course schedules. The application features a clean, intuitive user interface with support for multiple languages, multi-tenant architecture, and REST API access.

## Features

### Core Functionality

- **Participants Management**: Manage participant information including personal details, contact information, course assignments, and parent/guardian relationships
- **Instructors Management**: Track instructor details, contact information, and course assignments
- **Courses Management**: Create and manage courses with details such as:
  - Title, capacity, and location
  - Course level and schedule
  - Weekday assignments
  - Duration and instructor assignments
- **Course Schedules**: Organize and manage course scheduling information
- **User Management**: Comprehensive user administration with role-based access control (ADMIN, MANDANT_ADMIN roles)
- **Mail System**: Track and manage email communications with mail log functionality
- **Report Generation**: Generate PDF reports for participants and courses
- **Dashboard**: Overview page providing quick access to key information
- **Settings**: 
  - User settings (personal preferences)
  - Tenant settings (quantities, shared settings, reports, course levels, mail configuration)

### Technical Features

- **Multi-tenant Architecture**: Support for multiple tenants with header-based routing and domain-based identification
- **REST API**: External API endpoints for tenant and application management, plus internal API for reports
- **Multi-language Support**: Internationalization (i18n) support for German and English
- **Responsive UI**: Modern Vaadin-based user interface with Lumo theme
- **Database**: PostgreSQL with Flyway migrations for schema versioning
- **File Storage**: Support for file uploads up to 200MB
- **Docker Support**: Ready for containerized deployment
- **Security**: Spring Security integration with role-based access control

## Technology Stack

- **Java**: 21
- **Spring Boot**: 4.0.0
- **Vaadin**: 25.0.0
- **Database**: PostgreSQL with Flyway migrations
- **Build Tool**: Maven
- **Frontend**: TypeScript/pnpm workspace for additional applications (onboarding)
- **Additional Libraries**:
  - libphonenumber: 8.13.41 (for phone number validation)
  - Spring Security (authentication and authorization)
  - Hibernate/JPA (ORM)
  - Flyway (database migrations)

## Project Structure

This is a multi-module Maven project with the following modules:

```
verno/
├── common/          # Shared utilities and common components
├── db/              # Database entities and JPA repositories
├── publ/            # Public constants, routes, and shared definitions
├── server/          # Business logic services and repositories
├── api/             # REST API endpoints (external and internal)
├── report/          # PDF report generation for participants and courses
├── ui/              # Vaadin UI components and views
├── typescript/      # TypeScript workspace with additional apps
│   └── apps/
│       └── onboarding/  # Onboarding application
├── scripts/         # Deployment and provisioning scripts
└── pom.xml          # Parent POM with module configuration
```

### Module Details

- **common**: Contains base components, database DTOs, exceptions, and utility classes
- **db**: JPA entities for the domain model (Course, Participant, Instructor, Address, etc.)
- **publ**: Public constants, API URLs, routes, and utilities shared across modules
- **server**: Service layer with business logic and data access repositories
- **api**: REST API controllers for external tenant/application management and internal report generation
- **report**: Report generation service for creating PDF documents
- **ui**: Vaadin-based user interface with views, layouts, and components
- **typescript**: TypeScript/pnpm workspace for standalone applications like onboarding

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6+ (or use the included Maven wrapper `./mvnw`)
- PostgreSQL database (version 12 or higher recommended)
- IDE (IntelliJ IDEA, Eclipse, or VS Code recommended)

### Database Setup

1. **Install PostgreSQL** if not already installed
2. **Create the database**:
```sql
CREATE DATABASE verno;
CREATE USER verno WITH PASSWORD 'verno';
GRANT ALL PRIVILEGES ON DATABASE verno TO verno;
```

3. The application uses Flyway for database migrations, which will automatically create the schema on first run.

### Running in Development Mode

1. **Using Maven Wrapper** (recommended):
```bash
./mvnw
```

2. **Using IDE**:
   - Import the project into your IDE
   - Run the `Application` class located in `ui/src/main/java/ch/verno/ui/Application.java`

3. **Access the Application**:
   - Open your browser and navigate to `http://localhost:8080`
   - Default credentials will be set up during initial provisioning

### Building for Production

To build the application for production:

```bash
./mvnw clean package
```

Or use the provided build script:

```bash
./scripts/build-app.sh
```

The JAR file will be created in the `ui/target/` directory and also copied to `app.jar` in the root.

### Docker Deployment

To build a Docker image:

```bash
docker build -t verno:latest .
```

If you use Vaadin commercial components, pass the license key as a build secret:

```bash
docker build --secret id=proKey,src=$HOME/.vaadin/proKey -t verno:latest .
```

For Vaadin offline key:

```bash
docker build --secret id=offlineKey,src=$HOME/.vaadin/offlineKey -t verno:latest .
```

Run the container:

```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/verno \
  -e SPRING_DATASOURCE_USERNAME=verno \
  -e SPRING_DATASOURCE_PASSWORD=verno \
  verno:latest
```

For deployment scripts, see the `scripts/` directory:
- `deploy.sh` - Deployment automation
- `provision-tenant.sh` - Tenant provisioning
- `repair-flyway.sh` - Flyway migration repair

## Configuration

Application configuration is located in `ui/src/main/resources/application.properties`. 

### Key Configuration Options

**Database (PostgreSQL)**:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/verno
spring.datasource.username=verno
spring.datasource.password=verno
spring.datasource.driver-class-name=org.postgresql.Driver
```

**Flyway Migrations**:
```properties
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration/common,classpath:db/migration/dev
```

**Multi-tenant Support**:
```properties
verno.mandant.enabled=true
verno.mandant.header-name=default
verno.mandant.allow-header-fallback=true
verno.mandant.base-domains=localhost:8080,https://www.verno-app.ch,verno.swiss,localhost
```

**File Storage**:
```properties
files.storage.root=./data/files
spring.servlet.multipart.max-file-size=200MB
spring.servlet.multipart.max-request-size=200MB
```

**JPA Settings**:
```properties
spring.jpa.hibernate.ddl-auto=none
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

## Application Navigation

The application includes the following main sections accessible via the side navigation:

1. **Participants** (order 1)
   - Participants Overview - Manage all participants
   - Participant Detail - Individual participant information

2. **Instructors** (order 2)
   - Instructors Overview - Manage all instructors
   - Instructor Detail - Individual instructor information

3. **Courses** (order 3)
   - Course Overview - Main course management
   - Courses Grid - List all courses
   - Course Detail - Individual course configuration
   - Course Schedules - Schedule management
   - Course Schedule Detail - Individual schedule details

4. **Mail Log** (order 96) - View email communication history

5. **User Settings** (order 97) - Personal user preferences

6. **Tenant Settings** (order 98) - Tenant configuration:
   - Quantity settings
   - Shared settings
   - Report settings
   - Course level settings
   - Mail settings

7. **User Management** (order 99) - Admin: Manage application users

All views are protected with role-based access control (ADMIN, MANDANT_ADMIN).

## REST API

The application provides REST API endpoints in the `api` module:

### External API
- **TenantController**: Manage tenant operations
- **ApplicationController**: Application-level operations

### Internal API
- **ReportController**: Generate and retrieve PDF reports

API endpoints are available under the base path configured in the application.

## Internationalization

The application supports multiple languages:
- German (de)
- English (en)

Language files are located in `ui/src/main/resources/i18n/`:
- `messages.properties` (default)
- `messages_de.properties` (German)
- `messages_en.properties` (English)

## Development

### Code Structure

The project follows a feature-based package structure, organizing code by functional units:

- **Base components**: Reusable UI components and utilities in `ui/src/main/java/ch/verno/ui/base/`
- **Feature packages**: Domain-specific functionality in `ui/src/main/java/ch/verno/ui/verno/`
- **Entities**: Domain model in `db/src/main/java/ch/verno/db/entity/`
- **Services**: Business logic in `server/src/main/java/ch/verno/server/service/`
- **API Controllers**: REST endpoints in `api/src/main/java/ch/verno/api/endpoints/`
- **Reports**: Report generation in `report/src/main/java/ch/verno/report/`

### TypeScript Development

The `typescript` directory contains a pnpm workspace for standalone applications:

```bash
cd typescript

# Install dependencies
pnpm install

# Run onboarding app in development
pnpm dev:onboarding

# Build onboarding app
pnpm build:onboarding

# Lint all TypeScript projects
pnpm lint
```

### Database Schema

The application uses JPA entities with Flyway migrations for the following domain objects:

**Core Entities**:
- `CourseEntity` - Course information
- `ParticipantEntity` - Participant details
- `InstructorEntity` - Instructor information
- `CourseScheduleEntity` - Course scheduling
- `CourseLevelEntity` - Course difficulty levels

**Supporting Entities**:
- `AddressEntity` - Address information
- `ParentEntity` - Parent/guardian information
- `GenderEntity` - Gender reference data
- `UserEntity` - Application users
- `TenantEntity` - Multi-tenant support
- `MailLogEntity` - Email tracking

**Flyway Migrations**: Located in `ui/src/main/resources/db/migration/`
- `common/` - Shared migrations across environments
- `dev/` - Development-specific migrations

## License

See [LICENSE.md](LICENSE.md) for license information.

## Next Steps

- Review the [Vaadin Building Apps](https://vaadin.com/docs/v25/building-apps) guides for adding features
- Configure production database and environment variables
- Set up tenant domains and multi-tenant routing
- Configure SMTP settings for email functionality
- Customize report templates in the `report` module
- Review security configuration and user roles
- Set up continuous integration/deployment pipelines
- Configure file storage location for production

## Support

For issues, questions, or contributions, please refer to the project repository.

github test
