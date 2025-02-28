<?php
session_start();
include '../security/session_check.php';
include_once '../../includes/models/PersonalInfo.php';
include_once '../../includes/models/LocationInfo.php';
include_once '../../includes/config.php';

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

function fetchDataFromApi($url, $bearerToken) {
    $ch = curl_init($url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, [
        "Authorization: Bearer $bearerToken",
        "Content-Type: application/json"
    ]);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);

    $response = curl_exec($ch);
    $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    curl_close($ch);

    if ($httpCode !== 200) {
        return null;
    }

    return json_decode($response, true);
}

$personalInfo = fetchDataFromApi(KTOR_HOST . "/staff-personal", $bearerToken);
$locationInfo = fetchDataFromApi(KTOR_HOST . "/staff-address", $bearerToken);

if (!$personalInfo || !$locationInfo) {
    echo "<p class='error'>Failed to fetch profile data.</p>";
    exit;
}

include '../views/profile/profile.html';

