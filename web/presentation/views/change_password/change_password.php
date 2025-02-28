<?php
session_start();
include '../../security/session_check.php';

if (!isset($_SESSION['SESSION_TOKEN'])) {
    echo "Session expired. Please log in again.";
exit();
}
?>

<div class="change-password-container">
    <h3>Change Password</h3>
    <form action="../../controllers/process_change_password.php" method="POST">
        <label for="currentPassword">Current Password:</label>
        <div class="password-wrapper">
            <input type="password" id="currentPassword" name="currentPassword" required>
            <span class="toggle-password" onclick="togglePassword('currentPassword')">&#128065;</span>
        </div>

        <label for="newPassword">New Password:</label>
        <div class="password-wrapper">
            <input type="password" id="newPassword" name="newPassword" required>
            <span class="toggle-password" onclick="togglePassword('newPassword')">&#128065;</span>
        </div>

        <label for="confirmPassword">Confirm New Password:</label>
        <div class="password-wrapper">
            <input type="password" id="confirmPassword" name="confirmPassword" required>
            <span class="toggle-password" onclick="togglePassword('confirmPassword')">&#128065;</span>
        </div>

        <button type="submit" class="btn">Update Password</button>
    </form>
</div>
