<?php
include_once __DIR__.'/../../includes/security/session_check.php';

redirectIfLoggedIn();

?>
<!DOCTYPE html>
<html>
<head>
    <title>Employee Login</title>
    <link rel="stylesheet" href="/css/login.css">
</head>
<body>
<div class="login-container">
    <img src="/static/images/University_of_Pangasinan_logo%20(1).png" alt="Logo" class="logo">
    <h2>UPang Registrar</h2>
    <form method="POST" action="?/route=login">
        <label>Username</label>
        <input type="text" name="username" placeholder="staff-id" required>
        <label>Password</label>
        <input type="password" name="password" placeholder="Password" required>
        <p id="error-message"></p> <!-- Error Message Area -->
        <button type="submit">Log in</button>
    </form>
    <a href="#" class="forgot-password">Forgot Password?</a>
</div>

<script type="module">
    import { showErrorFromURL } from __DIR__ . "/js/errorHandler.js";
    showErrorFromURL();
</script>
</body>
</html>
