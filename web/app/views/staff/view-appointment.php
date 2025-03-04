
<h2>Appointment Details</h2>

<?php if (isset($this->appointmentSelected)): ?>
    <p><strong>ID:</strong> <?= htmlspecialchars($this->appointmentSelected->id) ?></p>
    <p><strong>Student ID:</strong> <?= htmlspecialchars($this->appointmentSelected->studentId) ?></p>
    <p><strong>Staff ID:</strong> <?= htmlspecialchars($this->appointmentSelected->staffId ?? 'N/A') ?></p>
    <p><strong>Appointment Type:</strong> <?= htmlspecialchars($this->appointmentSelected->appointmentType) ?></p>
    <p><strong>Document Type:</strong> <?= htmlspecialchars($this->appointmentSelected->documentType ?? 'N/A') ?></p>
    <p><strong>Reason:</strong> <?= htmlspecialchars($this->appointmentSelected->reason ?? 'N/A') ?></p>
    <p><strong>Requested Date:</strong> <?= htmlspecialchars($this->appointmentSelected->requestedDate ?? 'N/A') ?></p>
    <p><strong>Scheduled Date:</strong> <?= htmlspecialchars($this->appointmentSelected->scheduledDate ?? 'N/A') ?></p>
    <p><strong>Status:</strong> <?= htmlspecialchars($this->appointmentSelected->status) ?></p>
    <p><strong>Urgent:</strong> <?= $this->appointmentSelected->isUrgent ? 'Yes' : 'No' ?></p>
    <p><strong>Remarks:</strong> <?= htmlspecialchars($this->appointmentSelected->remarks ?? 'N/A') ?></p>
    <p><strong>Cancellation Reason:</strong> <?= htmlspecialchars($this->appointmentSelected->cancellationReason ?? 'N/A') ?></p>
    <p><strong>Created At:</strong> <?= htmlspecialchars($this->appointmentSelected->createdAt) ?></p>
    <p><strong>Updated At:</strong> <?= htmlspecialchars($this->appointmentSelected->updatedAt) ?></p>
<?php else: ?>
    <p>Appointment not found.</p>
<?php endif; ?>

<a href="/student-appointments">Back to Appointments</a>
