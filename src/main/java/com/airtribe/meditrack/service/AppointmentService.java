package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.AppointmentStatus;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.IdGenerator;
import com.airtribe.meditrack.util.Validator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Service managing Appointment creation, retrieval, and cancellation, enforcing
 * cross-entity existence rules for Patients and Doctors.
 */
public class AppointmentService {

    private final DataStore<Appointment> appointmentStore;
    private final PatientService patientService;
    private final DoctorService doctorService;

    /**
     * Constructs AppointmentService with references to PatientService and DoctorService.
     * Automatically wires active-appointment checks back to PatientService.
     *
     * @param patientService PatientService reference.
     * @param doctorService  DoctorService reference.
     */
    public AppointmentService(PatientService patientService, DoctorService doctorService) {
        this.patientService = Objects.requireNonNull(patientService, "patientService cannot be null");
        this.doctorService = Objects.requireNonNull(doctorService, "doctorService cannot be null");
        this.appointmentStore = new DataStore<>(Appointment::getAppointmentId);

        // Wire dependency so PatientService can verify active appointments prior to deletion
        this.patientService.setAppointmentService(this);
    }

    /**
     * Creates a new appointment after verifying referenced patient and doctor exist.
     * Initialized with status {@link AppointmentStatus#PENDING}.
     *
     * @param patientId       Referenced patient ID.
     * @param doctorId        Referenced doctor ID.
     * @param appointmentDate Scheduled date.
     * @return Created Appointment instance.
     * @throws InvalidDataException if validation fails or patient/doctor ID does not exist.
     */
    public Appointment createAppointment(String patientId, String doctorId, LocalDate appointmentDate)
            throws InvalidDataException {
        Validator.validateAppointmentData(patientId, doctorId, appointmentDate, AppointmentStatus.PENDING);

        if (patientService.getPatientById(patientId) == null) {
            throw new InvalidDataException("Cannot create appointment: Patient with ID '" + patientId + "' does not exist.");
        }
        if (doctorService.getDoctorById(doctorId) == null) {
            throw new InvalidDataException("Cannot create appointment: Doctor with ID '" + doctorId + "' does not exist.");
        }

        String appointmentId = IdGenerator.generateAppointmentId();
        Appointment appointment = new Appointment(
                appointmentId,
                patientId,
                doctorId,
                appointmentDate,
                AppointmentStatus.PENDING
        );

        boolean saved = appointmentStore.save(appointment);
        if (!saved) {
            throw new InvalidDataException("Failed to save appointment with ID " + appointmentId);
        }
        return appointment;
    }

    /**
     * Retrieves an appointment by ID.
     *
     * @param appointmentId Appointment ID.
     * @return Appointment instance or null if not found.
     */
    public Appointment getAppointmentById(String appointmentId) {
        if (appointmentId == null || appointmentId.isBlank()) {
            return null;
        }
        return appointmentStore.getById(appointmentId).orElse(null);
    }

    /**
     * Retrieves all recorded appointments.
     *
     * @return Unmodifiable list of appointments.
     */
    public List<Appointment> getAllAppointments() {
        return appointmentStore.getAll();
    }

    /**
     * Retrieves all appointments for a specified patient ID.
     *
     * @param patientId Target patient ID.
     * @return List of matching appointments.
     */
    public List<Appointment> getAppointmentsByPatientId(String patientId) {
        List<Appointment> result = new ArrayList<>();
        if (patientId == null || patientId.isBlank()) {
            return result;
        }
        for (Appointment appt : appointmentStore.getAll()) {
            if (patientId.equals(appt.getPatientId())) {
                result.add(appt);
            }
        }
        return result;
    }

    /**
     * Checks if a patient has any active appointments (PENDING or CONFIRMED).
     *
     * @param patientId Patient ID to check.
     * @return true if active appointments exist, false otherwise.
     */
    public boolean hasActiveAppointmentsForPatient(String patientId) {
        if (patientId == null || patientId.isBlank()) {
            return false;
        }
        for (Appointment appt : appointmentStore.getAll()) {
            if (patientId.equals(appt.getPatientId()) &&
                    (appt.getStatus() == AppointmentStatus.PENDING || appt.getStatus() == AppointmentStatus.CONFIRMED)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Cancels an appointment by changing its status to {@link AppointmentStatus#CANCELLED}.
     *
     * @param appointmentId Target appointment ID.
     * @return true if cancelled successfully.
     * @throws AppointmentNotFoundException if appointment ID does not exist.
     */
    public boolean cancelAppointment(String appointmentId) throws AppointmentNotFoundException {
        if (appointmentId == null || appointmentId.isBlank()) {
            throw new AppointmentNotFoundException("Appointment ID cannot be null or blank.");
        }

        Appointment appt = appointmentStore.getById(appointmentId).orElse(null);
        if (appt == null) {
            throw new AppointmentNotFoundException("Appointment with ID '" + appointmentId + "' not found.");
        }

        appt.setStatus(AppointmentStatus.CANCELLED);
        return appointmentStore.update(appt);
    }
}
