<?php
session_start();
if (!isset($_SESSION['SESSION_TOKEN'])) {
    header("Location: /presentation/views/login/login.html");
} else {
    header("Location: /presentation/views/dashboard/dashboard.html");
}


