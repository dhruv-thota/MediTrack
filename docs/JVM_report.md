# JVM Concepts Demonstrated in MediTrack

## 1. Introduction

MediTrack is a menu-driven console application developed using Core Java 17. The project demonstrates several Java concepts, including object-oriented programming, collections, generics, exception handling, static members, and immutability.

The Java Virtual Machine (JVM) is responsible for executing Java bytecode. Understanding how Java code is compiled and executed helps explain how MediTrack creates objects, manages memory, invokes methods, and handles application data.

## 2. Java Compilation and Bytecode

Java source code is written in `.java` files. The Java compiler converts these source files into bytecode stored in `.class` files.

The project uses Maven and the Maven Wrapper to compile and execute the application.

The compilation process can be represented as:

```text
Java Source Files (.java)
          |
          | Java Compiler
          v
Bytecode Files (.class)
          |
          | Java Virtual Machine (JVM)
          v
Application Execution
```

To compile MediTrack, execute the following command from the project root:

```bash
./mvnw clean compile
```

The project targets Java 17 through its Maven compiler configuration. Maven manages the build process, while the JVM executes the compiled application.

Java bytecode is platform-independent in the sense that it can run on any compatible JVM, subject to the required Java version and environment.

## 3. JVM Runtime Memory Areas

The JVM uses different runtime memory areas to execute Java programs. These areas have different responsibilities.

### 3.1 Heap Memory

The heap is the runtime memory area where Java objects and arrays are allocated.

In MediTrack, objects such as `Patient`, `Doctor`, and `Appointment` are created while the application runs. Collections such as `ArrayList` and `HashMap` also hold application data and references to objects.

For example, the generic `DataStore<T>` uses an `ArrayList` to maintain records and a `HashMap` to retrieve records by their IDs.

Conceptually:

```text
DataStore<Patient>
       |
       +---- ArrayList<Patient>
       |          |
       |          +---- Patient objects
       |
       +---- HashMap<String, Patient>
                  |
                  +---- References to Patient objects
```

The list and map can hold references to the same patient objects. This allows the application to maintain ordered records while also supporting efficient ID-based lookups.

MediTrack stores its data in memory. Therefore, patient, doctor, and appointment records are lost when the application terminates.

### 3.2 Java Stack Memory

Each Java thread has its own JVM stack. The stack contains frames associated with active method calls.

A method frame contains information such as local variables, intermediate computations, and data needed to return to the calling method.

For example, when `Main` calls a method in `PatientService` to register a patient, the JVM creates method-call frames as execution proceeds through the application.

A local variable may hold a reference to a `Patient` object, while the object itself is allocated on the heap.

When a method completes, its stack frame is removed. This does not necessarily mean that objects referenced by that method are immediately removed from the heap.

### 3.3 Class Metadata and Static Members

The JVM maintains metadata for loaded classes and runtime representations of their static members.

Static fields belong to a class rather than to each individual instance.

MediTrack's ID generator uses static counters to generate identifiers for patients, doctors, and appointments. These counters maintain their values during the current application run.

The exact implementation of JVM memory areas is JVM-dependent; this report describes their conceptual roles rather than a specific JVM's internal memory layout.

### 3.4 Program Counter and Native Method Stacks

The JVM specification also defines a program counter for each thread and native method stacks for native method execution.

The program counter identifies the JVM instruction currently being executed by a thread. Native method stacks support execution of native methods where applicable.

MediTrack does not directly manage these areas, but they form part of the JVM execution model.

## 4. Object Creation and Garbage Collection

Java objects are created at runtime and remain available while they are reachable through references from active parts of the application.

For example, when a patient is registered, a `Patient` object is created and stored in the application's data store. The data store maintains references to that object.

Java uses garbage collection to reclaim heap memory occupied by objects that are no longer reachable.

In MediTrack, deleting a record removes the corresponding references maintained by the data store. If no other live references keep that object reachable, it may eventually become eligible for garbage collection.

Garbage collection is managed by the JVM. Application code should not assume that an object will be collected immediately or at a particular time.

Because MediTrack uses in-memory storage, garbage collection and application-level record deletion are separate concepts. Deleting a record from the data store removes it from the application's records; garbage collection determines when its memory can be reclaimed.

## 5. Static Initialization and ID Generation

MediTrack uses an ID generator to create identifiers for different entity types, including patients, doctors, and appointments.

The generator uses static counters and initialization to maintain the initial state for these identifiers.

Static members are associated with the class, allowing ID-generation methods to access shared counters without requiring a separate counter for every generator object.

For example, generated IDs may follow patterns such as:

- `PAT-1001` for a patient
- `DOC-2001` for a doctor
- `APT-3001` for an appointment

The exact values depend on the implementation and current counter state.

These counters exist only in application memory. When the program is restarted, the counters are initialized again. The generated IDs are not persistent database sequences.

## 6. Object-Oriented Programming and Runtime Behavior

MediTrack demonstrates several object-oriented programming concepts.

### 6.1 Inheritance

The application uses a common `Person` abstraction for related domain entities.

`Patient` and `Doctor` extend `Person`, allowing shared characteristics and behavior to be defined in a common parent class while each subclass adds its own details.

This reduces duplication and provides a consistent structure for related entities.

### 6.2 Encapsulation

Encapsulation groups data and the operations associated with that data within a class.

For example, patient-related information and behavior are represented by the `Patient` class, while patient-management operations are handled by `PatientService`.

Appropriate access modifiers help control how application state can be accessed and modified. Private fields and public methods can be used to expose only the operations that callers need.

### 6.3 Polymorphism

Polymorphism allows code to work with a common parent type or interface while the actual runtime object determines which overridden method implementation is invoked.

For example, if `Patient` and `Doctor` override a method inherited from `Person`, a call through a `Person` reference can execute the implementation belonging to the actual object.

This supports flexible and extensible object-oriented design.

### 6.4 Interfaces

Interfaces define contracts that implementing classes agree to provide.

MediTrack includes a search-related interface, allowing search operations to be described through a common contract rather than being tied exclusively to one concrete class.

Interfaces help separate expected behavior from implementation details.

### 6.5 Generics

MediTrack uses a generic `DataStore<T>` to support different entity types while preserving compile-time type checking.

The same general storage implementation can be used for patients, doctors, or appointments without duplicating the collection-management logic for every entity type.

Generics reduce unnecessary casting and make the intended data type explicit.

## 7. Collections and In-Memory Data Management

MediTrack uses Java collections to manage records while the application is running.

### ArrayList

An `ArrayList` maintains records in an ordered collection and supports iteration over all stored records.

This is useful when listing patients, doctors, or appointments in the console application.

### HashMap

A `HashMap` associates keys with values. In MediTrack's data store, record IDs can be used as keys to retrieve individual records.

A hash-based lookup typically provides expected constant-time lookup performance, assuming a suitable distribution of hash values.

### Using Both Collections

Maintaining both a list and a map supports different access patterns:

- The list supports ordered iteration.
- The map supports ID-based lookup.

The data store must keep these structures consistent during insertion, updating, and deletion. MediTrack's `DataStore<T>` centralizes these operations to avoid duplicating the storage logic across services.

## 8. Immutability

MediTrack includes a `BillSummary` model designed to be immutable.

An immutable object is an object whose observable state cannot be changed after it has been constructed.

A typical immutable class uses:

- A final class, where appropriate.
- Private final fields.
- Controlled construction.
- No setter methods that mutate its state.

The `BillSummary` model demonstrates how immutable objects can represent values that should remain stable after creation.

Immutability makes code easier to reason about because callers cannot change an object's state through its public API after construction.

However, immutability of one model does not make the entire application immutable. MediTrack's services and data stores still support changes to application records.

## 9. Primitive Type Conversions

Java supports primitive type conversions, including widening and narrowing conversions.

### 9.1 Widening Conversion

A widening conversion converts a value to a primitive type that can represent a wider range of values.

For example:

```java
int count = 25;
long largerCount = count;
```

Here, the `int` value is converted to `long`. This conversion is implicit.

### 9.2 Narrowing Conversion

A narrowing conversion converts a value to a primitive type with a smaller range or precision. It generally requires an explicit cast.

```java
long total = 130L;
int narrowedTotal = (int) total;
```

This example produces the value `130`, but narrowing can lose information if the original value cannot be represented by the destination type.

These examples illustrate Java's primitive conversion rules and are independent of a particular MediTrack business operation.

## 10. Exception Handling

MediTrack uses exception handling to deal with invalid data and unsuccessful operations.

Custom exceptions, such as `InvalidDataException` and `AppointmentNotFoundException`, communicate specific application error conditions.

For example, attempting to cancel an appointment that does not exist can result in an appointment-related exception. Invalid input can also be rejected during validation.

The console interface catches expected exceptions and displays understandable messages to users. This helps prevent ordinary input mistakes from unnecessarily exposing stack traces.

Unexpected programming errors should still be investigated. Exception handling should not be used to silently ignore errors that indicate a defect in the application.

## 11. Limitations and Runtime Considerations

MediTrack is an educational Core Java application, so some production-level concerns are intentionally outside its scope.

- Records are stored in memory and are not persisted between runs.
- ID counters restart when a new application process initializes them.
- The application does not implement its own memory manager.
- Garbage collection is handled by the JVM.
- The project is not designed as a JVM performance benchmark.
- Actual heap usage, garbage-collection behavior, JIT compilation, and runtime memory layout depend on the JVM and execution environment.

A successful compilation or test run does not, by itself, provide measurements of memory usage or JVM performance. Such claims would require appropriate runtime profiling or measurement.

## 12. Conclusion

MediTrack provides a practical example of how a Java application is compiled into bytecode and executed by the JVM.

Its domain entities, service classes, generic data store, static ID generator, exception handling, and immutable `BillSummary` model provide concrete examples for discussing Java execution and language concepts.

The project demonstrates these concepts through a small console application built with Core Java 17, without requiring an application framework or third-party runtime dependencies.