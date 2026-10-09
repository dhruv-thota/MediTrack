# MediTrack

A menu-driven, console-based patient, doctor, and appointment management application built using **Core Java 17**. This project demonstrates object-oriented programming, collections, generics, exception handling, input validation, and basic application design.

## Features

### Patient Management
- Register and view patients.
- List all patients.
- Search patients by ID, name, or age.
- Update patient details and delete patients.
- Prevent deletion of patients who have active appointments.

### Doctor Management
- Register and view doctors.
- List all doctors.
- Search doctors by ID, name, or specialization.
- Update doctor details and delete doctors.

### Appointment Management
- Create appointments for registered patients and doctors.
- View appointments by ID or list all appointments.
- View appointments belonging to a patient.
- Cancel appointments and update their status.
- Validate patient and doctor references before creating appointments.

### Validation and Error Handling
- Validate required fields and domain-specific values.
- Handle invalid input and expected application exceptions.
- Generate unique, sequential IDs for patients, doctors, and appointments during a run.

## Technology Stack

- **Language:** Java 17
- **Build tool:** Apache Maven
- **Testing:** Custom Java-based test runner
- **Storage:** In-memory collections
- **External runtime dependencies:** None

## Prerequisites

- Java Development Kit (JDK) 17 or later
- A terminal or command-line interface
- Git, if cloning the repository

The project includes a Maven Wrapper, so a separate Maven installation is not required.

Verify your Java installation:

```bash
java -version
```

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/dhruv-thota/MediTrack.git
cd MediTrack
```

### 2. Compile the project

On macOS or Linux:

```bash
./mvnw clean compile
```

On Windows:

```bat
mvnw.cmd clean compile
```

### 3. Run the application

On macOS or Linux:

```bash
./mvnw exec:java
```

On Windows:

```bat
mvnw.cmd exec:java
```

The application displays a main menu for patient management, doctor management, appointment management, and exiting the program. Select an option and follow the prompts.

### 4. Run the test suite

On macOS or Linux:

```bash
./mvnw exec:java -Dexec.mainClass="com.airtribe.meditrack.TestRunner"
```

On Windows:

```bat
mvnw.cmd exec:java -Dexec.mainClass="com.airtribe.meditrack.TestRunner"
```

The custom test runner executes checks for patient services, doctor services, appointment services, and supporting components. A successful run should report **19 tests passed and 0 failed**.

## Project Structure

```text
MediTrack/
├── src/
│   └── main/
│       └── java/
│           └── com/airtribe/meditrack/
│               ├── constants/
│               ├── entity/
│               ├── exception/
│               ├── interfaces/
│               ├── service/
│               ├── util/
│               ├── Main.java
│               └── TestRunner.java
├── docs/
│   ├── setup_instructions.md
│   └── JVM_report.md
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

- `entity/` — Domain objects representing patients, doctors, appointments, and bill summaries.
- `service/` — Business operations for managing patients, doctors, and appointments.
- `util/` — Input validation and ID generation utilities.
- `interfaces/` — Application interfaces, including search-related contracts.
- `exception/` — Custom exceptions for application errors.
- `constants/` — Shared application constants.
- `Main.java` — Console application entry point and menu interface.
- `TestRunner.java` — Custom test runner for verifying application behavior.

## Core Java Concepts Demonstrated

- **Object-oriented programming:** Classes, objects, inheritance, encapsulation, abstraction, and polymorphism.
- **Collections:** `ArrayList` and `HashMap` for in-memory storage and lookups.
- **Generics:** A reusable generic data store.
- **Interfaces and enums:** Contracts for search operations and controlled domain values.
- **Exception handling:** Custom exceptions and validation of invalid operations.
- **Static members:** Shared ID-generation counters and initialization.
- **Immutability:** An immutable `BillSummary` model.
- **Date handling:** `LocalDate` for appointment dates.
- **Primitive conversions:** Examples of primitive types and widening/narrowing conversions.

## Data Storage and Limitations

MediTrack uses in-memory storage. **Data is not persisted between application runs.** Restarting the program creates a fresh application state and resets the in-memory ID counters.

This project does not include a database, web interface, authentication, external APIs, or a complete billing workflow. `BillSummary` is included as a Java model demonstrating immutability, not as a full billing module.

## Documentation

- [Setup Instructions](docs/Setup_Instructions.md) — Environment setup, execution steps, and application usage.
- [JVM Report](docs/JVM_Report.md) — JVM and memory-management concepts illustrated by the implementation.

## License

This project was developed as an educational project. No separate open-source license is specified.
