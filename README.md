# Verno - Course Management System

Verno is a comprehensive course management application built with Spring Boot and Vaadin. It provides a modern web interface for managing courses, participants, instructors, and course schedules.

## Overview

Verno is designed to help organizations manage their course offerings, track participants, assign instructors, and organize course schedules. The application features a clean, intuitive user interface with support for multiple languages.

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
- **Dashboard**: Overview page providing quick access to key information
- **Settings**: User and mandant (tenant) configuration options

### Technical Features

- **Multi-language Support**: Internationalization (i18n) support for German and English
- **Responsive UI**: Modern Vaadin-based user interface with Lumo theme
- **Database**: H2 in-memory database for development (configurable for production)
- **Docker Support**: Ready for containerized deployment

## Technology Stack

- **Java**: 21
- **Spring Boot**: 4.0.0
- **Vaadin**: 25.0.0
- **Database**: H2 (development), configurable for production
- **Build Tool**: Maven
- **Additional Libraries**:
  - libphonenumber: 8.13.41 (for phone number validation)

## Project Structure

This is a multi-module Maven project with the following modules:

```
verno/
├── common/          # Shared utilities and common components
├── db/              # Database entities and JPA repositories
├── server/          # Business logic services and repositories
├── ui/              # Vaadin UI components and views
└── pom.xml          # Parent POM with module configuration
```

### Module Details

- **common**: Contains base components, database DTOs, exceptions, and utility classes
- **db**: JPA entities for the domain model (Course, Participant, Instructor, Address, etc.)
- **server**: Service layer with business logic and data access repositories
- **ui**: Vaadin-based user interface with views, layouts, and components

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6+ (or use the included Maven wrapper `./mvnw`)
- IDE (IntelliJ IDEA, Eclipse, or VS Code recommended)

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
   - The H2 console is available at `http://localhost:8080/h2-console`
     - JDBC URL: `jdbc:h2:mem:verno`
     - Username: `verno`
     - Password: `verno`

### Building for Production

To build the application for production:

```bash
./mvnw clean package
```

The JAR file will be created in the `ui/target/` directory.

### Docker Deployment

To build a Docker image:

```bash
docker build -t verno:latest .
```

If you use Vaadin commercial components, pass the license key as a build secret:

```bash
docker build --secret id=proKey,src=$HOME/.vaadin/proKey .
```

For Vaadin offline key:

```bash
docker build --secret id=offlineKey,src=$HOME/.vaadin/offlineKey .
```

Run the container:

```bash
docker run -p 8080:8080 verno:latest
```

## Configuration

Application configuration is located in `ui/src/main/resources/application.properties`. The default configuration uses an H2 in-memory database. For production, update the datasource configuration to use your preferred database (PostgreSQL, MySQL, etc.).

### Current Configuration

- **Database**: H2 in-memory (`jdbc:h2:mem:verno`)
- **H2 Console**: Enabled at `/h2-console`
- **JPA**: Auto-update DDL enabled for development

## Application Navigation

The application includes the following main sections accessible via the side navigation:

1. **Dashboard** - Overview and welcome page
2. **Participants** - Manage course participants
3. **Instructors** - Manage course instructors
4. **Courses** - Manage courses and course schedules
5. **Settings** - User and mandant settings

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

### Database Schema

The application uses JPA entities for the following domain objects:
- `CourseEntity` - Course information
- `ParticipantEntity` - Participant details
- `InstructorEntity` - Instructor information
- `CourseScheduleEntity` - Course scheduling
- `CourseLevelEntity` - Course difficulty levels
- `AddressEntity` - Address information
- `ParentEntity` - Parent/guardian information
- `GenderEntity` - Gender reference data

## License

See [LICENSE.md](LICENSE.md) for license information.

## Next Steps

- Review the [Vaadin Building Apps](https://vaadin.com/docs/v25/building-apps) guides for adding features
- Configure production database settings
- Customize the theme and UI components
- Add authentication and authorization if needed

## Support

For issues, questions, or contributions, please refer to the project repository.
