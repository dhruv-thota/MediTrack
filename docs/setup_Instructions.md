# MediTrack Setup and Usage Instructions

This guide explains how to prepare the environment, compile MediTrack,
run the console application, and execute its custom test runner.

## 1. Prerequisites

-   **JDK 17 or later** installed.
-   A terminal or command-line interface.
-   Git, if cloning the repository.
-   Internet access on the first Maven Wrapper/build run if required to
    download Maven or Maven dependencies.

The project uses the Maven Wrapper, so a separate Maven installation is
not normally required.

### Verify Java

Run:

``` bash
java -version
javac -version
```

Confirm that the reported Java version is 17 or later. If `java` or
`javac` is not found, install a JDK and configure `JAVA_HOME` and your
`PATH` according to your operating system.

## 2. Obtain the project

Clone the repository and enter its directory:

``` bash
git clone https://github.com/dhruv-thota/MediTrack.git
cd MediTrack
```

If you already have the project locally, open a terminal in the
repository root---the directory containing `pom.xml`, `mvnw`, and
`mvnw.cmd`.

## 3. Compile the project

### macOS or Linux

``` bash
./mvnw clean compile
```

If the wrapper script does not have execute permission, run:

``` bash
chmod +x mvnw
./mvnw clean compile
```

### Windows Command Prompt or PowerShell

``` bat
mvnw.cmd clean compile
```

A successful compile should end with Maven reporting `BUILD SUCCESS`.
Compilation verifies that the Java source files compile; it does not by
itself verify every runtime behavior.

## 4. Run MediTrack

### macOS or Linux

``` bash
./mvnw exec:java
```

### Windows

``` bat
mvnw.cmd exec:java
```

The application is a menu-driven console program. The main menu
provides:

1.  Patient Management
2.  Doctor Management
3.  Appointment Management
4.  Exit

Choose a displayed option by entering its number and pressing Enter.
Within a submenu, choose an operation or the option to return to the
previous menu.

### Patient management

The patient submenu supports registering a patient, viewing a patient by
ID, listing patients, searching by name or age, updating details, and
deleting a patient. A patient with an active appointment must not be
deleted.

### Doctor management

The doctor submenu supports registering a doctor, viewing a doctor by
ID, listing doctors, searching by name or specialization, updating
details, and deleting a doctor.

### Appointment management

The appointment submenu supports creating an appointment, viewing it by
ID, listing appointments, viewing appointments by patient ID, and
cancelling an appointment. Create patients and doctors first, then use
their generated IDs when booking an appointment.

The application generates IDs during the current run. For example, the
first patient, doctor, and appointment may receive IDs such as
`PAT-1001`, `DOC-2001`, and `APT-3001`. The exact values depend on the
ID generator's current implementation and state.

## 5. Run the custom test runner

The project includes a custom Java test runner rather than a third-party
testing framework.

### macOS or Linux

``` bash
./mvnw exec:java -Dexec.mainClass="com.airtribe.meditrack.TestRunner"
```

### Windows

``` bat
mvnw.cmd exec:java -Dexec.mainClass="com.airtribe.meditrack.TestRunner"
```

The expected result, based on the current project test suite, is **19
passed and 0 failed**. Confirm the actual output when you run the
command. A failed test should cause a non-zero process exit status.

## 6. Recommended end-to-end check

After launching the application:

1.  Register a patient and record the generated patient ID.
2.  Register a doctor and record the generated doctor ID.
3.  Create an appointment using those IDs and a valid date.
4.  View the appointment and confirm its initial status is `PENDING`.
5.  Try deleting the patient while the appointment is active; deletion
    should be rejected.
6.  Cancel the appointment and confirm its status becomes `CANCELLED`.
7.  Try deleting the patient again; deletion should now be allowed if no
    other active appointments exist.
8.  Exit using the main menu.

Also try an invalid menu choice and non-numeric input where a number is
expected. Confirm the application displays a useful message and
continues rather than crashing.

## 7. Data and limitations

MediTrack stores records in memory using Java collections. Data is not
saved to a database or file and is lost when the application exits.
Generated ID counters also restart with a new application run.

The application is a Core Java console project. It does not provide a
web interface, database persistence, authentication, external API
integrations, or a complete billing workflow.

## 8. Troubleshooting

  -----------------------------------------------------------------------
  Symptom                             Checks
  ----------------------------------- -----------------------------------
  `java` or `javac` is not found      Install JDK 17 or later and verify
                                      `PATH` and `JAVA_HOME`.

  `Permission denied` when running    On macOS/Linux, run
  `./mvnw`                            `chmod +x mvnw`.

  Compilation fails                   Read the first compiler error and
                                      verify that the project is using
                                      JDK 17 or later.

  Maven cannot download artifacts     Check network access and retry. The
                                      first build may need to download
                                      Maven and plugin artifacts.

  `exec:java` cannot find the main    Run the command from the repository
  class                               root and verify that compilation
                                      succeeds first.

  A test fails                        Read the failing test name and
                                      error output; do not treat a
                                      successful compilation alone as
                                      proof that tests passed.
  -----------------------------------------------------------------------

## 9. Before submission

From the repository root, run:

``` bash
./mvnw clean compile
./mvnw exec:java -Dexec.mainClass="com.airtribe.meditrack.TestRunner"
./mvnw exec:java
```

On Windows, replace `./mvnw` with `mvnw.cmd`. The last command starts an
interactive application; complete the manual checks described above
before considering verification finished.
