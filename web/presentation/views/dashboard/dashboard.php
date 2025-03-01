<?php
session_start();
include '../../security/session_check.php';

if (!isset($_SESSION['SESSION_TOKEN'])) {
    header("Location: ../login/login.html");
    exit();
}
?>

<!DOCTYPE html>
<html lang="EN">
<head>
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="../../../css/dashboard.css">
    <script>
        function loadPage(page) {
            fetch(page)
                .then(response => response.text())
                .then(data => {
                    document.getElementById("content").innerHTML = data;
                })
                .catch(error => console.error("Error loading page:", error));
        }
        function togglePassword(fieldId) {
            const input = document.getElementById(fieldId);
            if (input.type === "password") {
                input.type = "text";
            } else {
                input.type = "password";
            }
        }
    </script>
</head>
<body>
<div class="navbar">
    <div class="logo-container">
        <img src="../../../static/images/University_of_Pangasinan_logo%20(1).png" alt="Logo" class="logo">
        <h2>UNIVERSITY OF PANGASINAN</h2>
    </div>
    <nav>
        <a href="#">History</a>
        <a href="javascript:void(0);" onclick="loadPage('../../controllers/profile.php')">Profile</a>
        <a href="javascript:void(0);" onclick="loadPage('../appointment/appointment-management-view.php')">Appointment</a>
        <a href="../queue/queue.html">Queue</a>
        <a href="#">FAQ's</a>
        <a href="#">Feedback</a>
        <a href="../../controllers/logout.php">Logout</a>
    </nav>
</div>

<div id="content" class="content">
    <h3>Announcement</h3>
    <div class="announcement-box">
        <?php if (empty($announcements)): ?>
        <p>No announcements at the moment.</p>
        <?php else: ?>
        <?php foreach ($announcements as $announcement): ?>
        <p><strong><?= htmlspecialchars($announcement['title']) ?></strong>: <?= htmlspecialchars($announcement['message']) ?></p>
        <?php endforeach; ?>
        <?php endif; ?>
    </div>

    <h3>Upcoming Events</h3>
    <div class="events-container">
        <?php if (empty($events)): ?>
        <p>No upcoming events at the moment.</p>
        <?php else: ?>
        <?php foreach ($events as $event): ?>
        <div class="event-box">
            <p><strong><?= htmlspecialchars($event['event_name']) ?></strong></p>
            <p>Date: <?= htmlspecialchars($event['event_date']) ?></p>
        </div>
        <?php endforeach; ?>
        <?php endif; ?>
    </div>
</div>

<footer class="footer">
    <p>&copy;2025 Upang-Registrar.  All rights reserved</p>
</footer>

</body>
</html>
