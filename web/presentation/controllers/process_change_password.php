<?php
session_start();
include '../../security/session_check.php';
require '../../includes/models/Token.php';

use models\Token;

if (!isset($_SESSION['SESSION_TOKEN'])) {
    echo "Session expired. Please log in again.";
    exit();
}

$currentPassword = $_POST['currentPassword'] ?? '';
$newPassword = $_POST['newPassword'] ?? '';

// Debug: Print received values
echo "<pre>Received Form Data:\n";
print_r($_POST);
echo "</pre>";

// Validate input
if (empty($currentPassword) || empty($newPassword)) {
    echo "<p style='color:red;'>All fields are required.</p>";
    exit();
}

// Prepare request data
$data = json_encode([
    "currentPassword" => $currentPassword,
    "newPassword" => $newPassword
]);

$tokenSession = $_SESSION['SESSION_TOKEN'];
$url = "http://localhost:8080/staff-change-password";
$token = Token::fromJson($tokenSession);
// Debug: Show what will be sent

// Send request using cURL (more reliable)
$ch = curl_init($url);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_POST, true);
curl_setopt($ch, CURLOPT_HTTPHEADER, [
    "Content-Type: application/json",
    "Authorization: Bearer $token->bearerToken"
]);
curl_setopt($ch, CURLOPT_POSTFIELDS, $data);

$response = curl_exec($ch);
$httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
curl_close($ch);


if ($httpCode === 400) {
    echo "<p style='color:red;'>Bad Request: Check input data.</p>";
    exit();
} elseif ($httpCode === 401) {
    echo "<p style='color:red;'>Unauthorized: Invalid token.</p>";
    exit();
} elseif ($httpCode === 500) {
    echo "<p style='color:red;'>Server Error: Something went wrong.</p>";
    exit();
}

echo "<p style='color:green;'>Password changed successfully!</p>";

session_destroy();
header("Location: ../../index.php");
exit();