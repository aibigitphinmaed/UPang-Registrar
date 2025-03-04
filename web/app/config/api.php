<?php

include_once __DIR__ . '/../models/Token.php';
include_once __DIR__ . '/../models/Appointment.php';
include_once __DIR__ . '/../includes/config.php';

use App\Models\Token;
use const App\Includes\KTOR_HOST;
use App\Models\Appointment;


function updateAppointmentApi(?Appointment $appointment) : String | bool{
    $api_url = KTOR_HOST. '/modify-student-appointment';
    $data = json_encode($appointment);
    $ch = curl_init($api_url);
    $token = Token::fromJson($_SESSION['SESSION_TOKEN']);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, [
        'Content-Type: application/json',
        'Authorization: Bearer ' . $token->bearerToken,
        ]);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $data);

    $response = curl_exec($ch);

    //Bad Request or Failed call on the backend
    return $response;
}

function getAllAppointmentsApi(): String | bool{
    $api_url = KTOR_HOST. '/get-all-appointments';
    $ch = curl_init($api_url);
    $token = Token::fromJson($_SESSION['SESSION_TOKEN']);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, [
        'Content-Type: application/json',
        'Authorization: Bearer ' . $token->bearerToken,
    ]);
    curl_setopt($ch, CURLOPT_POST, true);
    $response = curl_exec($ch);

    return $response;
}
