
<button onclick="window.location.href='/student-appointments'">
    Back to Appointments
</button>

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

  <!--Form below-->

<form id="appointmentForm">


        <input type="hidden" name="id" value="<?= htmlspecialchars($this->appointmentSelected->id) ?>">
        <input type="hidden" name="createdAt" value="<?= htmlspecialchars($this->appointmentSelected->createdAt) ?>" readonly>

        <input type="hidden" name="updatedAt" value="<?= htmlspecialchars($this->appointmentSelected->updatedAt) ?>" readonly>

        <label><strong>Student ID:</strong></label>
        <input type="text" name="studentId" value="<?= htmlspecialchars($this->appointmentSelected->studentId) ?>" readonly><br>

        <label><strong>Staff ID:</strong></label>
        <input type="text" name="staffId" value="<?= htmlspecialchars($this->appointmentSelected->staffId ?? 'N/A') ?>"><br>

        <label><strong>Appointment Type:</strong></label>
        <input type="text" name="appointmentType" value="<?= htmlspecialchars($this->appointmentSelected->appointmentType) ?>"><br>

        <label><strong>Document Type:</strong></label>
        <input type="text" name="documentType" value="<?= htmlspecialchars($this->appointmentSelected->documentType ?? '') ?>"><br>

        <label><strong>Reason:</strong></label>
        <input type="text" name="reason" value="<?= htmlspecialchars($this->appointmentSelected->reason ?? '') ?>"><br>

        <label><strong>Requested Date:</strong></label>
        <input type="date" name="requestedDate" value="<?= htmlspecialchars($this->appointmentSelected->requestedDate ?? '') ?>"><br>

        <label><strong>Scheduled Date:</strong></label>
        <input type="date" name="scheduledDate" value="<?= htmlspecialchars($this->appointmentSelected->scheduledDate ?? '') ?>"><br>

        <label><strong>Status:</strong></label>
        <select name="status">
            <option value="pending" <?= $this->appointmentSelected->status === 'pending' ? 'selected' : '' ?>>Pending</option>
            <option value="approved" <?= $this->appointmentSelected->status === 'approved' ? 'selected' : '' ?>>Approved</option>
            <option value="rejected" <?= $this->appointmentSelected->status === 'rejected' ? 'selected' : '' ?>>Rejected</option>
        </select><br>

        <label><strong>Urgent:</strong></label>
        <input type="checkbox" name="isUrgent" <?= $this->appointmentSelected->isUrgent ? 'checked' : '' ?>><br>

        <label><strong>Remarks:</strong></label>
        <textarea name="remarks"><?= htmlspecialchars($this->appointmentSelected->remarks ?? '') ?></textarea><br>

        <label><strong>Cancellation Reason:</strong></label>
        <textarea name="cancellationReason"><?= htmlspecialchars($this->appointmentSelected->cancellationReason ?? '') ?></textarea><br>



        <button onclick="modifyAppointment()">Save Changes</button>

</form>
<?php else: ?>
    <p>Appointment not found.</p>
<?php endif; ?>





