<?php
session_start();

include_once '../../includes/models/LoginRequest.php';
include_once '../../includes/models/Token.php';
include_once '../../includes/config.php';

use models\LoginRequest;
use models\Token;

$ktorApiUrl = KTOR_HOST . "/login";

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $username = $_POST['username'];
    $password = $_POST['password'];

    $loginRequest = json_encode(["username" => $username, "password" => $password]);

    // Initialize cURL
    $ch = curl_init($ktorApiUrl);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, ["Content-Type: application/json"]);
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $loginRequest);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);

    // Execute cURL request
    $response = curl_exec($ch);
    $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    curl_close($ch);

    if ($httpCode == 200 && $response) {
        $token = Token::fromJson($response);
        if ($token) {
            $_SESSION['SESSION_TOKEN'] = $token->toJson();
            header("Location: /presentation/views/dashboard/dashboard.php"); // Redirect only on success
            exit;
        }
    } else {
        // Extract error message from JSON response
        $decodedResponse = json_decode($response, true);
        $errorDetail = isset($decodedResponse['error']) ? $decodedResponse['error'] : "Invalid credentials.";

        // Redirect back to login.html with an error message
        header("Location: /presentation/views/login/login.html?error=" . urlencode($errorDetail));
        exit;
    }
}
