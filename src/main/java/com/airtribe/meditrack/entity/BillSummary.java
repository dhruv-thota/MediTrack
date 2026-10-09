package com.airtribe.meditrack.entity;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Immutable class representing a Bill Summary.
 * Included to demonstrate Java class immutability using a {@code final} class,
 * {@code final} private fields, no setter accessors, and value-based immutable types.
 */
public final class BillSummary {

    private final String billId;
    private final String appointmentId;
    private final String patientName;
    private final double amount;
    private final LocalDate issuedDate;

    /**
     * Primary constructor initializing all immutable fields.
     * Demonstrates widening primitive conversion (e.g., int fee to double amount).
     *
     * @param billId        Unique bill summary ID.
     * @param appointmentId Associated appointment ID.
     * @param patientName   Patient name.
     * @param amount        Billed amount.
     * @param issuedDate    Issue date (LocalDate is an immutable java.time class).
     */
    public BillSummary(String billId, String appointmentId, String patientName, double amount, LocalDate issuedDate) {
        this.billId = billId;
        this.appointmentId = appointmentId;
        this.patientName = patientName;
        this.amount = amount;
        this.issuedDate = issuedDate;
    }

    /**
     * Overloaded constructor demonstrating widening primitive type conversion (int baseFee -> double amount).
     *
     * @param billId        Unique bill summary ID.
     * @param appointmentId Associated appointment ID.
     * @param patientName   Patient name.
     * @param baseFee       Base fee as integer (widened to double).
     * @param issuedDate    Issue date.
     */
    public BillSummary(String billId, String appointmentId, String patientName, int baseFee, LocalDate issuedDate) {
        this(billId, appointmentId, patientName, (double) baseFee, issuedDate);
    }

    public String getBillId() {
        return billId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public String getPatientName() {
        return patientName;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillSummary that = (BillSummary) o;
        return Double.compare(that.amount, amount) == 0 &&
                Objects.equals(billId, that.billId) &&
                Objects.equals(appointmentId, that.appointmentId) &&
                Objects.equals(patientName, that.patientName) &&
                Objects.equals(issuedDate, that.issuedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(billId, appointmentId, patientName, amount, issuedDate);
    }

    @Override
    public String toString() {
        return "BillSummary{" +
                "billId='" + billId + '\'' +
                ", appointmentId='" + appointmentId + '\'' +
                ", patientName='" + patientName + '\'' +
                ", amount=" + amount +
                ", issuedDate=" + issuedDate +
                '}';
    }
}
