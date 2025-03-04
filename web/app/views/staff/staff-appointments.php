<?php
include_once __DIR__.'/../../includes/security/session_check.php';
?>


<h2>Student Appointments</h2>
<script src="/js/filtering.js"></script>
<script src="/js/appointments.js"></script>

<!-- Search and Filter Section -->
<input type="text" id="searchInput" placeholder="Search by Student ID, Staff ID, Status, Appointment Type">

<select id="statusFilter">
    <option value="">All Statuses</option>
    <option value="Pending">Pending</option>
    <option value="Cancelled">Cancelled</option>
    <option value="Completed">Completed</option>
    <option value="Rejected">Rejected</option>
</select>

<select id="typeFilter">
    <option value="">All Types</option>
    <option value="Consultation">Consultation</option>
    <option value="Enrollment">Enrollment</option>
</select>


<table border="1" id="appointmentsTable">
    <thead>
    <tr>
        <th>Actions</th>
        <th>Appointment Type</th>
        <th>Document Type</th>
        <th>Reason</th>
        <th>Requested Date</th>
        <th>Scheduled Date</th>
        <th>Status</th>
        <th>Urgent</th>
        <th>Remarks</th>
        <th>Cancellation Reason</th>
        <th>Created At</th>
        <th>Updated At</th>
    </tr>
    </thead>
    <tbody id="appointmentsBody">

    <?php if (!empty($filteredAppointments) && is_array($filteredAppointments)): ?>
        <?php foreach ($filteredAppointments as $appointment): ?>
            <?php if ($appointment !== null): ?>
                <tr data-student-id="<?= htmlspecialchars($appointment->studentId ?? 'N/A') ?>"
                    data-staff-id="<?= htmlspecialchars($appointment->staffId ?? 'N/A') ?>">
                    <td>
                        <button onclick="viewAppointment('<?= htmlspecialchars($appointment->id ?? '') ?>')">View</button>
                    </td>
                    <td><?= htmlspecialchars($appointment->appointmentType ?? 'N/A') ?></td>
                    <td><?= htmlspecialchars($appointment->documentType ?? 'N/A') ?></td>
                    <td><?= htmlspecialchars($appointment->reason ?? 'N/A') ?></td>
                    <td><?= htmlspecialchars($appointment->requestedDate ?? 'N/A') ?></td>
                    <td><?= htmlspecialchars($appointment->scheduledDate ?? 'N/A') ?></td>
                    <td><?= htmlspecialchars($appointment->status ?? 'N/A') ?></td>
                    <td><?= isset($appointment->isUrgent) && $appointment->isUrgent ? 'Yes' : 'No' ?></td>
                    <td><?= htmlspecialchars($appointment->remarks ?? 'N/A') ?></td>
                    <td><?= htmlspecialchars($appointment->cancellationReason ?? 'N/A') ?></td>
                    <td><?= htmlspecialchars($appointment->createdAt ?? 'N/A') ?></td>
                    <td><?= htmlspecialchars($appointment->updatedAt ?? 'N/A') ?></td>
                </tr>
            <?php endif; ?>
        <?php endforeach; ?>
    <?php else: ?>
        <tr>
            <td colspan="12">No appointments available.</td>
        </tr>
    <?php endif; ?>

    </tbody>

</table>
