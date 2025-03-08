<?php
include_once __DIR__.'/../includes/security/session_check.php';
$user_role = $_SESSION['role'];

?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>University System - <?= ucfirst($user_role) ?></title>
    <link rel="stylesheet" href="/css/sidebar.css">
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>

<!-- Sidebar -->

<?php if(isLoggedIn()): ?>
<div class="header">
    <img src="/static/images/University_of_Pangasinan_logo (1).png" alt="University Logo">
    <h1>UNIVERSITY OF PANGASINAN</h1>

    <ul class="nav-links">
        <li><a href="?route=dashboard">Dashboard</a></li>

        <?php if ($user_role === 'admin'): ?>
            <li><a href="?route=manage-users">Manage Users</a></li>
            <li><a href="?route=reports">Reports</a></li>

        <?php elseif ($user_role === 'staff'): ?>
            <li><a href="?route=student-appointments">Appointment</a></li>
            <li><a href="?route=queue">Queue</a></li>
            <li><a href="?route=feedback">Feedback</a></li>
        <?php endif; ?>

        <li><a href="?route=profile">Profile</a></li>
        <li><a href="/logout">Logout</a></li>
    </ul>
</div>
<?php endif; ?>
<!-- Content Area -->
<div class="content">
    <?= $content ?> <!-- Dynamic content -->
</div>

</body>
</html>
