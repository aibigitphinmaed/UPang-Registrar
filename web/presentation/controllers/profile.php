<?php
session_start();
include '../security/session_check.php';
include_once '../../includes/models/PersonalInfo.php';
include_once '../../includes/models/LocationInfo.php';
include_once '../../includes/config.php';
include '../../includes/functions/custom_functions.php';

if (!isset($_SESSION['SESSION_TOKEN'])) {
    echo "<p class='error'>Please log in first.</p>";
    exit;
}

$tokenData = json_decode($_SESSION['SESSION_TOKEN'], true);
$bearerToken = $tokenData['bearerToken'] ?? null;

if (!$bearerToken) {
    echo "<p class='error'>Invalid session token.</p>";
    exit;
}

$personalInfo = fetchDataFromApi(KTOR_HOST . "/staff-personal", $bearerToken);
$locationInfo = fetchDataFromApi(KTOR_HOST . "/staff-address", $bearerToken);

if (!$personalInfo || !$locationInfo) {
    echo "<p class='error'>Failed to fetch profile data.</p>";
    exit;
}

include '../views/profile/profile.html';

