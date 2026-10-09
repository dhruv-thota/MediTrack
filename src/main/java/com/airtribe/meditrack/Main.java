package com.airtribe.meditrack;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.entity.Specialization;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Main console application entry point for MediTrack.
 * Provides interactive, menu-driven CLI workflows for Patient, Doctor,
 * and Appointment Management with robust input validation and error handling.
 */
public class Main {

    private final PatientService patientService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final Scanner scanner;

    public Main() {
        this.patientService = new PatientService();
        this.doctorService = new DoctorService();
        this.appointmentService = new AppointmentService(patientService, doctorService);
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        Main app = new Main();
        app.run();
    }

    public void run() {
        System.out.println("==================================================");
        System.out.println("          Welcome to MediTrack System             ");
        System.out.println("==================================================");

        boolean running = true;
        while (running) {
            printMainMenu();
            String choiceStr = readLine("Enter option (1-4): ");
            int choice = parseChoice(choiceStr);

            switch (choice) {
                case 1 -> patientMenu();
                case 2 -> doctorMenu();
                case 3 -> appointmentMenu();
                case 4 -> {
                    System.out.println("\nThank you for using MediTrack. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option. Please enter a number between 1 and 4.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Patient Management");
        System.out.println("2. Doctor Management");
        System.out.println("3. Appointment Management");
        System.out.println("4. Exit");
    }

    // =========================================================================
    // Patient Management Menu & Handlers
    // =========================================================================

    private void patientMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- PATIENT MANAGEMENT ---");
            System.out.println("1. Register Patient");
            System.out.println("2. View Patient by ID");
            System.out.println("3. List All Patients");
            System.out.println("4. Search Patients by Name");
            System.out.println("5. Search Patients by Age");
            System.out.println("6. Update Patient");
            System.out.println("7. Delete Patient");
            System.out.println("8. Back to Main Menu");

            int choice = parseChoice(readLine("Enter choice (1-8): "));
            switch (choice) {
                case 1 -> registerPatient();
                case 2 -> viewPatientById();
                case 3 -> listAllPatients();
                case 4 -> searchPatientsByName();
                case 5 -> searchPatientsByAge();
                case 6 -> updatePatient();
                case 7 -> deletePatient();
                case 8 -> back = true;
                default -> System.out.println("Invalid option. Please enter a number between 1 and 8.");
            }
        }
    }

    private void registerPatient() {
        System.out.println("\n[Register Patient]");
        String name = readLine("Enter patient full name: ");
        int age = readInt("Enter patient age: ");
        String contact = readLine("Enter contact number: ");

        try {
            Patient p = patientService.addPatient(name, age, contact);
            System.out.println("SUCCESS: Patient registered with ID: " + p.getId());
        } catch (InvalidDataException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private void viewPatientById() {
        System.out.println("\n[View Patient by ID]");
        String id = readLine("Enter patient ID: ");
        Patient p = patientService.getPatientById(id);
        if (p == null) {
            System.out.println("Patient with ID '" + id + "' not found.");
        } else {
            displayPatient(p);
        }
    }

    private void listAllPatients() {
        System.out.println("\n[All Registered Patients]");
        List<Patient> patients = patientService.getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("No patients currently registered.");
        } else {
            patients.forEach(this::displayPatient);
        }
    }

    private void searchPatientsByName() {
        System.out.println("\n[Search Patients by Name]");
        String name = readLine("Enter name to search: ");
        List<Patient> results = patientService.searchByName(name);
        if (results.isEmpty()) {
            System.out.println("No patients found matching name '" + name + "'.");
        } else {
            results.forEach(this::displayPatient);
        }
    }

    private void searchPatientsByAge() {
        System.out.println("\n[Search Patients by Age]");
        int age = readInt("Enter age to search: ");
        List<Patient> results = patientService.searchByAge(age);
        if (results.isEmpty()) {
            System.out.println("No patients found with age " + age + ".");
        } else {
            results.forEach(this::displayPatient);
        }
    }

    private void updatePatient() {
        System.out.println("\n[Update Patient]");
        String id = readLine("Enter patient ID to update: ");
        Patient p = patientService.getPatientById(id);
        if (p == null) {
            System.out.println("Patient with ID '" + id + "' not found.");
            return;
        }

        System.out.println("Current details: " + p);
        String newName = readLine("Enter new name (leave blank to keep '" + p.getName() + "'): ");
        if (!newName.isBlank()) {
            p.setName(newName);
        }

        String ageStr = readLine("Enter new age (leave blank to keep " + p.getAge() + "): ");
        if (!ageStr.isBlank()) {
            try {
                int newAge = Integer.parseInt(ageStr.trim());
                p.setAge(newAge);
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Invalid age format. Age not updated.");
            }
        }

        String newContact = readLine("Enter new contact (leave blank to keep '" + p.getContactNumber() + "'): ");
        if (!newContact.isBlank()) {
            p.setContactNumber(newContact);
        }

        try {
            boolean updated = patientService.updatePatient(p);
            if (updated) {
                System.out.println("SUCCESS: Patient updated successfully.");
            } else {
                System.out.println("ERROR: Failed to update patient.");
            }
        } catch (InvalidDataException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private void deletePatient() {
        System.out.println("\n[Delete Patient]");
        String id = readLine("Enter patient ID to delete: ");
        try {
            boolean deleted = patientService.deletePatient(id);
            if (deleted) {
                System.out.println("SUCCESS: Patient '" + id + "' deleted successfully.");
            } else {
                System.out.println("ERROR: Patient '" + id + "' not found.");
            }
        } catch (InvalidDataException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private void displayPatient(Patient p) {
        System.out.println(" - ID: " + p.getId() + " | Name: " + p.getName() +
                " | Age: " + p.getAge() + " | Contact: " + p.getContactNumber());
    }

    // =========================================================================
    // Doctor Management Menu & Handlers
    // =========================================================================

    private void doctorMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- DOCTOR MANAGEMENT ---");
            System.out.println("1. Register Doctor");
            System.out.println("2. View Doctor by ID");
            System.out.println("3. List All Doctors");
            System.out.println("4. Search Doctors by Name");
            System.out.println("5. Search Doctors by Specialization");
            System.out.println("6. Update Doctor");
            System.out.println("7. Delete Doctor");
            System.out.println("8. Back to Main Menu");

            int choice = parseChoice(readLine("Enter choice (1-8): "));
            switch (choice) {
                case 1 -> registerDoctor();
                case 2 -> viewDoctorById();
                case 3 -> listAllDoctors();
                case 4 -> searchDoctorsByName();
                case 5 -> searchDoctorsBySpecialization();
                case 6 -> updateDoctor();
                case 7 -> deleteDoctor();
                case 8 -> back = true;
                default -> System.out.println("Invalid option. Please enter a number between 1 and 8.");
            }
        }
    }

    private void registerDoctor() {
        System.out.println("\n[Register Doctor]");
        String name = readLine("Enter doctor full name: ");
        int age = readInt("Enter doctor age: ");
        Specialization specialization = promptSpecialization();

        if (specialization == null) {
            System.out.println("ERROR: Invalid specialization selection.");
            return;
        }

        try {
            Doctor d = doctorService.registerDoctor(name, age, specialization);
            System.out.println("SUCCESS: Doctor registered with ID: " + d.getId());
        } catch (InvalidDataException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private void viewDoctorById() {
        System.out.println("\n[View Doctor by ID]");
        String id = readLine("Enter doctor ID: ");
        Doctor d = doctorService.getDoctorById(id);
        if (d == null) {
            System.out.println("Doctor with ID '" + id + "' not found.");
        } else {
            displayDoctor(d);
        }
    }

    private void listAllDoctors() {
        System.out.println("\n[All Registered Doctors]");
        List<Doctor> doctors = doctorService.getAllDoctors();
        if (doctors.isEmpty()) {
            System.out.println("No doctors currently registered.");
        } else {
            doctors.forEach(this::displayDoctor);
        }
    }

    private void searchDoctorsByName() {
        System.out.println("\n[Search Doctors by Name]");
        String name = readLine("Enter doctor name to search: ");
        List<Doctor> results = doctorService.searchByName(name);
        if (results.isEmpty()) {
            System.out.println("No doctors found matching name '" + name + "'.");
        } else {
            results.forEach(this::displayDoctor);
        }
    }

    private void searchDoctorsBySpecialization() {
        System.out.println("\n[Search Doctors by Specialization]");
        Specialization spec = promptSpecialization();
        if (spec == null) {
            System.out.println("ERROR: Invalid specialization selection.");
            return;
        }
        List<Doctor> results = doctorService.searchBySpecialization(spec);
        if (results.isEmpty()) {
            System.out.println("No doctors found with specialization '" + spec + "'.");
        } else {
            results.forEach(this::displayDoctor);
        }
    }

    private void updateDoctor() {
        System.out.println("\n[Update Doctor]");
        String id = readLine("Enter doctor ID to update: ");
        Doctor d = doctorService.getDoctorById(id);
        if (d == null) {
            System.out.println("Doctor with ID '" + id + "' not found.");
            return;
        }

        System.out.println("Current details: " + d);
        String newName = readLine("Enter new name (leave blank to keep '" + d.getName() + "'): ");
        if (!newName.isBlank()) {
            d.setName(newName);
        }

        String ageStr = readLine("Enter new age (leave blank to keep " + d.getAge() + "): ");
        if (!ageStr.isBlank()) {
            try {
                int newAge = Integer.parseInt(ageStr.trim());
                d.setAge(newAge);
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Invalid age format. Age not updated.");
            }
        }

        System.out.println("Do you want to update specialization? (y/N)");
        String changeSpec = readLine("> ");
        if (changeSpec.equalsIgnoreCase("y")) {
            Specialization newSpec = promptSpecialization();
            if (newSpec != null) {
                d.setSpecialization(newSpec);
            }
        }

        try {
            boolean updated = doctorService.updateDoctor(d);
            if (updated) {
                System.out.println("SUCCESS: Doctor updated successfully.");
            } else {
                System.out.println("ERROR: Failed to update doctor.");
            }
        } catch (InvalidDataException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private void deleteDoctor() {
        System.out.println("\n[Delete Doctor]");
        String id = readLine("Enter doctor ID to delete: ");
        boolean deleted = doctorService.deleteDoctor(id);
        if (deleted) {
            System.out.println("SUCCESS: Doctor '" + id + "' deleted successfully.");
        } else {
            System.out.println("ERROR: Doctor '" + id + "' not found.");
        }
    }

    private Specialization promptSpecialization() {
        Specialization[] specs = Specialization.values();
        System.out.println("Select Specialization:");
        for (int i = 0; i < specs.length; i++) {
            System.out.println("  " + (i + 1) + ". " + specs[i].getDisplayName());
        }
        int index = readInt("Choice (1-" + specs.length + "): ");
        if (index >= 1 && index <= specs.length) {
            return specs[index - 1];
        }
        return null;
    }

    private void displayDoctor(Doctor d) {
        System.out.println(" - ID: " + d.getId() + " | Name: " + d.getName() +
                " | Age: " + d.getAge() + " | Specialization: " + d.getSpecialization());
    }

    // =========================================================================
    // Appointment Management Menu & Handlers
    // =========================================================================

    private void appointmentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- APPOINTMENT MANAGEMENT ---");
            System.out.println("1. Create Appointment");
            System.out.println("2. View Appointment by ID");
            System.out.println("3. List All Appointments");
            System.out.println("4. View Appointments by Patient ID");
            System.out.println("5. Cancel Appointment");
            System.out.println("6. Back to Main Menu");

            int choice = parseChoice(readLine("Enter choice (1-6): "));
            switch (choice) {
                case 1 -> createAppointment();
                case 2 -> viewAppointmentById();
                case 3 -> listAllAppointments();
                case 4 -> viewAppointmentsByPatientId();
                case 5 -> cancelAppointment();
                case 6 -> back = true;
                default -> System.out.println("Invalid option. Please enter a number between 1 and 6.");
            }
        }
    }

    private void createAppointment() {
        System.out.println("\n[Create Appointment]");
        String patientId = readLine("Enter Patient ID: ");
        String doctorId = readLine("Enter Doctor ID: ");
        String dateStr = readLine("Enter Appointment Date (yyyy-MM-dd, or blank for tomorrow): ");

        LocalDate appointmentDate;
        if (dateStr.isBlank()) {
            appointmentDate = LocalDate.now().plusDays(1);
        } else {
            try {
                appointmentDate = LocalDate.parse(dateStr.trim());
            } catch (DateTimeParseException e) {
                System.out.println("ERROR: Invalid date format. Please use yyyy-MM-dd format.");
                return;
            }
        }

        try {
            Appointment appt = appointmentService.createAppointment(patientId, doctorId, appointmentDate);
            System.out.println("SUCCESS: Appointment created with ID: " + appt.getAppointmentId() +
                    " (Status: " + appt.getStatus() + ")");
        } catch (InvalidDataException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private void viewAppointmentById() {
        System.out.println("\n[View Appointment by ID]");
        String id = readLine("Enter Appointment ID: ");
        Appointment appt = appointmentService.getAppointmentById(id);
        if (appt == null) {
            System.out.println("Appointment with ID '" + id + "' not found.");
        } else {
            displayAppointment(appt);
        }
    }

    private void listAllAppointments() {
        System.out.println("\n[All Appointments]");
        List<Appointment> appointments = appointmentService.getAllAppointments();
        if (appointments.isEmpty()) {
            System.out.println("No appointments currently scheduled.");
        } else {
            appointments.forEach(this::displayAppointment);
        }
    }

    private void viewAppointmentsByPatientId() {
        System.out.println("\n[View Appointments by Patient ID]");
        String patientId = readLine("Enter Patient ID: ");
        List<Appointment> appts = appointmentService.getAppointmentsByPatientId(patientId);
        if (appts.isEmpty()) {
            System.out.println("No appointments found for Patient ID '" + patientId + "'.");
        } else {
            appts.forEach(this::displayAppointment);
        }
    }

    private void cancelAppointment() {
        System.out.println("\n[Cancel Appointment]");
        String id = readLine("Enter Appointment ID to cancel: ");
        try {
            boolean cancelled = appointmentService.cancelAppointment(id);
            if (cancelled) {
                System.out.println("SUCCESS: Appointment '" + id + "' status updated to CANCELLED.");
            } else {
                System.out.println("ERROR: Could not cancel appointment '" + id + "'.");
            }
        } catch (AppointmentNotFoundException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private void displayAppointment(Appointment appt) {
        System.out.println(" - ID: " + appt.getAppointmentId() +
                " | Patient: " + appt.getPatientId() +
                " | Doctor: " + appt.getDoctorId() +
                " | Date: " + appt.getAppointmentDate() +
                " | Status: " + appt.getStatus());
    }

    // =========================================================================
    // Console Input Helpers
    // =========================================================================

    private String readLine(String prompt) {
        System.out.print(prompt);
        if (scanner.hasNextLine()) {
            return scanner.nextLine().trim();
        }
        return "";
    }

    private int readInt(String prompt) {
        while (true) {
            String input = readLine(prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid numeric input. Please enter a valid integer.");
            }
        }
    }

    private int parseChoice(String choiceStr) {
        try {
            return Integer.parseInt(choiceStr);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
