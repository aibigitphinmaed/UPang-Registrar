<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

// Prevent browser caching of protected pages
header("Cache-Control: no-store, no-cache, must-revalidate, max-age=0");
header("Cache-Control: post-check=0, pre-check=0", false);
header("Pragma: no-cache");
header("Expires: Sat, 01 Jan 2000 00:00:00 GMT");

// If user is not logged in, destroy session and redirect to login page
if (empty($_SESSION['SESSION_TOKEN'])) {
    session_unset();
    session_destroy();
    header("Location: ../login/login.html?error=Session Expired");
    exit();
}