
<h2>Student Appointments</h2>

<!-- Search and Filter Section -->
<form id="filterForm">
    <input type="text" name="search" id="searchInput" placeholder="Search by Student ID, Staff ID, Status, Appointment Type">

    <select name="status" id="statusFilter">
        <option value="">All Statuses</option>
        <option value="Pending">Pending</option>
        <option value="Cancelled">Cancelled</option>
        <option value="Completed">Completed</option>
        <option value="Rejected">Rejected</option>
    </select>

    <select name="type" id="typeFilter">
        <option value="">All Types</option>
        <option value="Consultation">Consultation</option>
        <option value="Enrollment">Enrollment</option>
    </select>
</form>



<table border="1">
    <thead>
    <tr>
        <th>ID</th>
        <th>Student ID</th>
        <th>Staff ID</th>
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
    <tbody>
    <?php foreach ($filteredAppointments as $appointment): ?>
        <tr>
            <td><?= htmlspecialchars($appointment->id) ?></td>
            <td><?= htmlspecialchars($appointment->studentId) ?></td>
            <td><?= htmlspecialchars($appointment->staffId ?? 'N/A') ?></td>
            <td><?= htmlspecialchars($appointment->appointmentType) ?></td>
            <td><?= htmlspecialchars($appointment->documentType ?? 'N/A') ?></td>
            <td><?= htmlspecialchars($appointment->reason ?? 'N/A') ?></td>
            <td><?= htmlspecialchars($appointment->requestedDate ?? 'N/A') ?></td>
            <td><?= htmlspecialchars($appointment->scheduledDate ?? 'N/A') ?></td>
            <td><?= htmlspecialchars($appointment->status) ?></td>
            <td><?= $appointment->isUrgent ? 'Yes' : 'No' ?></td>
            <td><?= htmlspecialchars($appointment->remarks ?? 'N/A') ?></td>
            <td><?= htmlspecialchars($appointment->cancellationReason ?? 'N/A') ?></td>
            <td><?= htmlspecialchars($appointment->createdAt) ?></td>
            <td><?= htmlspecialchars($appointment->updatedAt) ?></td>
        </tr>
    <?php endforeach; ?>
    </tbody>
</table>