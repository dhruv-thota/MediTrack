package com.airtribe.meditrack.entity;

import java.time.LocalDate;

/**
 * Entity representing a Medical Appointment between a Patient and a Doctor.
 */
public class Appointment {

    private String appointmentId;
    private String patientId;
    private String doctorId;
    private LocalDate appointmentDate;
    private AppointmentStatus status;

    /**
     * Primary constructor for an Appointment.
     *
     * @param appointmentId   Unique appointment ID.
     * @param patientId       Referenced Patient ID.
     * @param doctorId        Referenced Doctor ID.
     * @param appointmentDate Scheduled appointment date.
     * @param status          Current appointment status.
     */
    public Appointment(String appointmentId, String patientId, String doctorId,
                       LocalDate appointmentDate, AppointmentStatus status) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.status = status;
    }

    /**
     * Convenience constructor setting default status to {@link AppointmentStatus#PENDING}.
     *
     * @param appointmentId   Unique appointment ID.
     * @param patientId       Referenced Patient ID.
     * @param doctorId        Referenced Doctor ID.
     * @param appointmentDate Scheduled appointment date.
     */
    public Appointment(String appointmentId, String patientId, String doctorId, LocalDate appointmentDate) {
        this(appointmentId, patientId, doctorId, appointmentDate, AppointmentStatus.PENDING);
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId='" + appointmentId + '\'' +
                ", patientId='" + patientId + '\'' +
                ", doctorId='" + doctorId + '\'' +
                ", appointmentDate=" + appointmentDate +
                ", status=" + status +
                '}';
    }
}
