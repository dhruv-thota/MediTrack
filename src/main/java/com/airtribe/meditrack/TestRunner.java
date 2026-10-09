package com.airtribe.meditrack;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.AppointmentStatus;
import com.airtribe.meditrack.entity.BillSummary;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.entity.Specialization;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.IdGenerator;
import com.airtribe.meditrack.util.Validator;

import java.time.LocalDate;
import java.util.List;

/**
 * Standalone, repeatable manual test runner using Core Java.
 * Verifies application services, domain entities, validation, custom exceptions,
 * and generic storage without external test frameworks.
 */
public class TestRunner {

    private static int passedCount = 0;
    private static int failedCount = 0;

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("       MediTrack Automated Test Runner           ");
        System.out.println("=================================================");

        runPatientServiceTests();
        runDoctorServiceTests();
        runAppointmentServiceTests();
        runSupportingComponentTests();

        System.out.println("=================================================");
        System.out.println("FINAL TEST SUMMARY:");
        System.out.println("  Tests Passed: " + passedCount);
        System.out.println("  Tests Failed: " + failedCount);
        System.out.println("=================================================");

        if (failedCount > 0) {
            System.err.println("Test execution FAILED with " + failedCount + " failure(s).");
            System.exit(1);
        } else {
            System.out.println("All tests passed successfully!");
            System.exit(0);
        }
    }

    private static void test(String name, RunnableTest body) {
        try {
            body.run();
            passedCount++;
            System.out.println("[PASS] " + name);
        } catch (Throwable t) {
            failedCount++;
            System.out.println("[FAIL] " + name + " -> Error: " + t.getMessage());
        }
    }

    @FunctionalInterface
    interface RunnableTest {
        void run() throws Exception;
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(message + " (Expected: " + expected + ", Actual: " + actual + ")");
        }
    }

    // =========================================================================
    // 1. PatientService Tests
    // =========================================================================

    private static void runPatientServiceTests() {
        System.out.println("\n--- PatientService Tests ---");

        test("PatientService: Create and retrieve patient", () -> {
            IdGenerator.resetCounters();
            PatientService service = new PatientService();
            Patient p = service.addPatient("John Doe", 35, "555-1111");
            
            assertEquals("PAT-1001", p.getId(), "Patient ID should match expected prefix");
            Patient retrieved = service.getPatientById("PAT-1001");
            assertEquals("John Doe", retrieved.getName(), "Retrieved patient name should match");
            assertEquals(35, retrieved.getAge(), "Retrieved patient age should match");
        });

        test("PatientService: Update patient details", () -> {
            IdGenerator.resetCounters();
            PatientService service = new PatientService();
            Patient p = service.addPatient("Jane Smith", 28, "555-2222");
            p.setName("Jane Smith-Updated");
            p.setAge(29);
            
            boolean updated = service.updatePatient(p);
            assertTrue(updated, "Update operation should return true");
            assertEquals("Jane Smith-Updated", service.getPatientById("PAT-1001").getName(), "Updated name should persist");
            assertEquals(29, service.getPatientById("PAT-1001").getAge(), "Updated age should persist");
        });

        test("PatientService: Delete patient when permitted", () -> {
            IdGenerator.resetCounters();
            PatientService service = new PatientService();
            Patient p = service.addPatient("Mark Wilson", 40, "555-3333");
            
            boolean deleted = service.deletePatient("PAT-1001");
            assertTrue(deleted, "Deletion should succeed when patient has no active appointments");
            assertEquals(null, service.getPatientById("PAT-1001"), "Deleted patient should no longer be retrievable");
        });

        test("PatientService: Search patients by ID, name, and age", () -> {
            IdGenerator.resetCounters();
            PatientService service = new PatientService();
            service.addPatient("Alice Cooper", 30, "555-4444");
            service.addPatient("Bob Cooper", 30, "555-5555");
            service.addPatient("Charlie Brown", 50, "555-6666");

            // Search by ID
            Patient searchByIdResult = service.searchById("PAT-1001");
            assertEquals("Alice Cooper", searchByIdResult.getName(), "Search by ID should return correct patient");

            // Search by Name (case-insensitive substring)
            List<Patient> coopers = service.searchByName("cooper");
            assertEquals(2, coopers.size(), "Search by name should return matching records");

            // Search by Age (overloaded search)
            List<Patient> age30List = service.searchByAge(30);
            assertEquals(2, age30List.size(), "Search by age should return matching records");
        });

        test("PatientService: Reject invalid patient data", () -> {
            PatientService service = new PatientService();
            
            boolean invalidAgeThrown = false;
            try {
                service.addPatient("Invalid Age", 0, "555-0000");
            } catch (InvalidDataException e) {
                invalidAgeThrown = true;
            }
            assertTrue(invalidAgeThrown, "Adding patient with age <= 0 should throw InvalidDataException");

            boolean blankNameThrown = false;
            try {
                service.addPatient("   ", 25, "555-0000");
            } catch (InvalidDataException e) {
                blankNameThrown = true;
            }
            assertTrue(blankNameThrown, "Adding patient with blank name should throw InvalidDataException");
        });
    }

    // =========================================================================
    // 2. DoctorService Tests
    // =========================================================================

    private static void runDoctorServiceTests() {
        System.out.println("\n--- DoctorService Tests ---");

        test("DoctorService: Register and retrieve doctor", () -> {
            IdGenerator.resetCounters();
            DoctorService service = new DoctorService();
            Doctor d = service.registerDoctor("Dr. Adams", 45, Specialization.CARDIOLOGY);
            
            assertEquals("DOC-2001", d.getId(), "Doctor ID should match expected prefix");
            Doctor retrieved = service.getDoctorById("DOC-2001");
            assertEquals("Dr. Adams", retrieved.getName(), "Doctor name should match");
            assertEquals(Specialization.CARDIOLOGY, retrieved.getSpecialization(), "Specialization should match");
        });

        test("DoctorService: Update and delete doctor", () -> {
            IdGenerator.resetCounters();
            DoctorService service = new DoctorService();
            Doctor d = service.registerDoctor("Dr. Baker", 50, Specialization.DERMATOLOGY);
            
            d.setSpecialization(Specialization.NEUROLOGY);
            boolean updated = service.updateDoctor(d);
            assertTrue(updated, "Doctor update should return true");
            assertEquals(Specialization.NEUROLOGY, service.getDoctorById("DOC-2001").getSpecialization(), "Updated specialization should persist");

            boolean deleted = service.deleteDoctor("DOC-2001");
            assertTrue(deleted, "Doctor deletion should return true");
            assertEquals(null, service.getDoctorById("DOC-2001"), "Deleted doctor should be null");
        });

        test("DoctorService: Search doctors by ID, name, and specialization", () -> {
            IdGenerator.resetCounters();
            DoctorService service = new DoctorService();
            service.registerDoctor("Dr. Clark", 40, Specialization.PEDIATRICS);
            service.registerDoctor("Dr. Clara", 42, Specialization.PEDIATRICS);
            service.registerDoctor("Dr. Dan", 55, Specialization.ORTHOPEDICS);

            // Search by ID
            Doctor searchId = service.searchById("DOC-2001");
            assertEquals("Dr. Clark", searchId.getName(), "Search by ID should match");

            // Search by Name
            List<Doctor> clarks = service.searchByName("cl");
            assertEquals(2, clarks.size(), "Search by name substring should return 2 doctors");

            // Search by Specialization
            List<Doctor> pediatrics = service.searchBySpecialization(Specialization.PEDIATRICS);
            assertEquals(2, pediatrics.size(), "Search by specialization should return matching doctors");
        });

        test("DoctorService: Reject invalid doctor data", () -> {
            DoctorService service = new DoctorService();
            
            boolean nullSpecThrown = false;
            try {
                service.registerDoctor("Dr. Fail", 40, null);
            } catch (InvalidDataException e) {
                nullSpecThrown = true;
            }
            assertTrue(nullSpecThrown, "Registering doctor with null specialization should throw InvalidDataException");

            boolean blankNameThrown = false;
            try {
                service.registerDoctor("", 40, Specialization.CARDIOLOGY);
            } catch (InvalidDataException e) {
                blankNameThrown = true;
            }
            assertTrue(blankNameThrown, "Registering doctor with blank name should throw InvalidDataException");
        });
    }

    // =========================================================================
    // 3. AppointmentService Tests
    // =========================================================================

    private static void runAppointmentServiceTests() {
        System.out.println("\n--- AppointmentService Tests ---");

        test("AppointmentService: Create appointment with valid IDs and verify status PENDING", () -> {
            IdGenerator.resetCounters();
            PatientService patientService = new PatientService();
            DoctorService doctorService = new DoctorService();
            AppointmentService appointmentService = new AppointmentService(patientService, doctorService);

            Patient p = patientService.addPatient("Patient One", 30, "555-0001");
            Doctor d = doctorService.registerDoctor("Doctor One", 45, Specialization.GENERAL_MEDICINE);

            Appointment appt = appointmentService.createAppointment(p.getId(), d.getId(), LocalDate.now().plusDays(1));
            assertEquals("APT-3001", appt.getAppointmentId(), "Appointment ID should match prefix");
            assertEquals(AppointmentStatus.PENDING, appt.getStatus(), "Initial status should be PENDING");
        });

        test("AppointmentService: Retrieve appointments by ID and retrieve all", () -> {
            IdGenerator.resetCounters();
            PatientService patientService = new PatientService();
            DoctorService doctorService = new DoctorService();
            AppointmentService appointmentService = new AppointmentService(patientService, doctorService);

            Patient p = patientService.addPatient("Patient Two", 25, "555-0002");
            Doctor d = doctorService.registerDoctor("Doctor Two", 50, Specialization.GYNECOLOGY);

            Appointment appt1 = appointmentService.createAppointment(p.getId(), d.getId(), LocalDate.now().plusDays(1));
            Appointment appt2 = appointmentService.createAppointment(p.getId(), d.getId(), LocalDate.now().plusDays(2));

            assertEquals(2, appointmentService.getAllAppointments().size(), "getAllAppointments should return 2 appointments");
            Appointment retrieved = appointmentService.getAppointmentById(appt1.getAppointmentId());
            assertEquals(appt1.getAppointmentId(), retrieved.getAppointmentId(), "Retrieved appointment ID should match");
        });

        test("AppointmentService: Cancel appointment and verify status becomes CANCELLED", () -> {
            IdGenerator.resetCounters();
            PatientService patientService = new PatientService();
            DoctorService doctorService = new DoctorService();
            AppointmentService appointmentService = new AppointmentService(patientService, doctorService);

            Patient p = patientService.addPatient("Patient Three", 40, "555-0003");
            Doctor d = doctorService.registerDoctor("Doctor Three", 48, Specialization.CARDIOLOGY);

            Appointment appt = appointmentService.createAppointment(p.getId(), d.getId(), LocalDate.now().plusDays(3));
            boolean cancelled = appointmentService.cancelAppointment(appt.getAppointmentId());
            
            assertTrue(cancelled, "Cancellation operation should return true");
            assertEquals(AppointmentStatus.CANCELLED, appointmentService.getAppointmentById(appt.getAppointmentId()).getStatus(), "Status should be CANCELLED");
        });

        test("AppointmentService: Reject creation with nonexistent patient or doctor IDs", () -> {
            IdGenerator.resetCounters();
            PatientService patientService = new PatientService();
            DoctorService doctorService = new DoctorService();
            AppointmentService appointmentService = new AppointmentService(patientService, doctorService);

            Patient p = patientService.addPatient("Valid Patient", 30, "555-0004");
            Doctor d = doctorService.registerDoctor("Valid Doctor", 40, Specialization.DERMATOLOGY);

            // Invalid Patient ID
            boolean invalidPatientThrown = false;
            try {
                appointmentService.createAppointment("PAT-9999", d.getId(), LocalDate.now());
            } catch (InvalidDataException e) {
                invalidPatientThrown = true;
            }
            assertTrue(invalidPatientThrown, "Nonexistent patient ID should throw InvalidDataException");

            // Invalid Doctor ID
            boolean invalidDoctorThrown = false;
            try {
                appointmentService.createAppointment(p.getId(), "DOC-9999", LocalDate.now());
            } catch (InvalidDataException e) {
                invalidDoctorThrown = true;
            }
            assertTrue(invalidDoctorThrown, "Nonexistent doctor ID should throw InvalidDataException");
        });

        test("AppointmentService: Throw AppointmentNotFoundException when cancelling nonexistent appointment", () -> {
            PatientService patientService = new PatientService();
            DoctorService doctorService = new DoctorService();
            AppointmentService appointmentService = new AppointmentService(patientService, doctorService);

            boolean exceptionThrown = false;
            try {
                appointmentService.cancelAppointment("APT-9999");
            } catch (AppointmentNotFoundException e) {
                exceptionThrown = true;
            }
            assertTrue(exceptionThrown, "Cancelling nonexistent appointment should throw AppointmentNotFoundException");
        });

        test("AppointmentService: Prevent patient deletion when active appointment exists", () -> {
            IdGenerator.resetCounters();
            PatientService patientService = new PatientService();
            DoctorService doctorService = new DoctorService();
            AppointmentService appointmentService = new AppointmentService(patientService, doctorService);

            Patient p = patientService.addPatient("Patient Four", 33, "555-0005");
            Doctor d = doctorService.registerDoctor("Doctor Four", 44, Specialization.ORTHOPEDICS);

            Appointment appt = appointmentService.createAppointment(p.getId(), d.getId(), LocalDate.now().plusDays(4));

            boolean deletionBlocked = false;
            try {
                patientService.deletePatient(p.getId());
            } catch (InvalidDataException e) {
                deletionBlocked = true;
            }
            assertTrue(deletionBlocked, "Deleting patient with active appointment should throw InvalidDataException");

            // Cancel appointment then verify deletion succeeds
            appointmentService.cancelAppointment(appt.getAppointmentId());
            boolean deleteSuccess = patientService.deletePatient(p.getId());
            assertTrue(deleteSuccess, "Patient deletion should succeed after active appointment is cancelled");
        });
    }

    // =========================================================================
    // 4. Supporting Component Tests
    // =========================================================================

    private static void runSupportingComponentTests() {
        System.out.println("\n--- Supporting Component Tests ---");

        test("Supporting: Validator throws InvalidDataException on invalid inputs", () -> {
            boolean thrown1 = false;
            try {
                Validator.validateString(null, "TestField");
            } catch (InvalidDataException e) {
                thrown1 = true;
            }
            assertTrue(thrown1, "Validator.validateString(null) should throw InvalidDataException");

            boolean thrown2 = false;
            try {
                Validator.validatePatientData("Alice", 160);
            } catch (InvalidDataException e) {
                thrown2 = true;
            }
            assertTrue(thrown2, "Validator.validatePatientData with age 160 should throw InvalidDataException");
        });

        test("Supporting: IdGenerator produces unique sequential IDs with correct category prefixes", () -> {
            IdGenerator.resetCounters();
            String p1 = IdGenerator.generatePatientId();
            String p2 = IdGenerator.generatePatientId();
            String d1 = IdGenerator.generateDoctorId();
            String a1 = IdGenerator.generateAppointmentId();

            assertEquals("PAT-1001", p1, "First patient ID should be PAT-1001");
            assertEquals("PAT-1002", p2, "Second patient ID should be PAT-1002");
            assertEquals("DOC-2001", d1, "First doctor ID should be DOC-2001");
            assertEquals("APT-3001", a1, "First appointment ID should be APT-3001");
        });

        test("Supporting: DataStore rejects duplicate and blank IDs without corrupting data", () -> {
            DataStore<DummyEntity> store = new DataStore<>(DummyEntity::getId);
            DummyEntity e1 = new DummyEntity("ID-1", "Original");
            
            boolean save1 = store.save(e1);
            assertTrue(save1, "First save should return true");
            assertEquals(1, store.size(), "Store size should be 1");

            // Attempt duplicate save
            DummyEntity e1Dup = new DummyEntity("ID-1", "Duplicate Overwrite Attempt");
            boolean saveDup = store.save(e1Dup);
            assertTrue(!saveDup, "Duplicate ID save should return false");
            assertEquals("Original", store.getById("ID-1").get().getName(), "Original entity state should be preserved");

            // Attempt blank ID save
            DummyEntity eBlank = new DummyEntity("   ", "Blank ID");
            boolean saveBlank = store.save(eBlank);
            assertTrue(!saveBlank, "Blank ID save should return false");
            assertEquals(1, store.size(), "Store size should remain 1");
        });

        test("Supporting: BillSummary is strictly immutable and cannot be mutated", () -> {
            BillSummary bill = new BillSummary("BILL-101", "APT-3001", "John Doe", 150.0, LocalDate.of(2026, 10, 10));
            
            assertEquals("BILL-101", bill.getBillId(), "Bill ID should match");
            assertEquals("APT-3001", bill.getAppointmentId(), "Appointment ID should match");
            assertEquals("John Doe", bill.getPatientName(), "Patient name should match");
            assertEquals(150.0, bill.getAmount(), "Amount should match");
            assertEquals(LocalDate.of(2026, 10, 10), bill.getIssuedDate(), "Issued date should match");

            // Inspecting class via reflection to confirm no setters exist
            boolean hasSetters = false;
            for (java.lang.reflect.Method m : BillSummary.class.getDeclaredMethods()) {
                if (m.getName().startsWith("set")) {
                    hasSetters = true;
                    break;
                }
            }
            assertTrue(!hasSetters, "BillSummary class must contain zero setter methods");
            assertTrue(java.lang.reflect.Modifier.isFinal(BillSummary.class.getModifiers()), "BillSummary class must be declared final");
        });
    }

    // Helper dummy entity class for DataStore testing
    private static class DummyEntity {
        private final String id;
        private final String name;

        public DummyEntity(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
