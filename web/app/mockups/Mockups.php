<?php

include_once __DIR__. '/../models/Appointment.php';

// Function to return mock appointments
use App\Models\Appointment;

function getMockAppointments(): array {
    $mockAppointments = [];

    for ($i = 1; $i <= 25; $i++) {
        $mockAppointments[] = new Appointment(
            $i,
            rand(100, 200), // Random Student ID
            rand(201, 210), // Random Staff ID
            ($i % 2 == 0) ? "Consultation" : "Enrollment",
            ($i % 3 == 0) ? "Diploma" : "Transcript",
            ($i % 4 == 0) ? "Urgent processing needed" : "Regular appointment",
            "2025-03-" . str_pad(rand(1, 30), 2, "0", STR_PAD_LEFT), // Random requested date
            ($i % 5 == 0) ? "2025-03-" . str_pad(rand(1, 30), 2, "0", STR_PAD_LEFT) : null, // Scheduled date sometimes null
            ($i % 6 == 0) ? "Cancelled" : "Pending",
            ($i % 7 == 0) ? "2025-03-02 10:30:00" : null, // Notified sometimes null
            ($i % 3 == 0), // Random urgent flag
            ($i % 8 == 0) ? "Bring student ID" : null, // Remarks sometimes null
            ($i % 9 == 0) ? "No available slots" : null, // Cancellation reason sometimes null
            "2025-03-01 09:00:00",
            "2025-03-01 09:30:00"
        );
    }

    return $mockAppointments;
}
