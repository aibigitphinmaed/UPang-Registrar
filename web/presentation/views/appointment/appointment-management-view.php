<?php


include '../../security/session_check.php';
include '../../../includes/models/Token.php';
include '../../../includes/models/Appointment.php';
include '../../../includes/models/AppointmentSearchRequest.php';
include '../../../includes/functions/custom_functions.php';
include '../../../includes/config.php';

use models\Token;
use models\AppointmentSearchRequest;
$tokenData = json_decode($_SESSION['SESSION_TOKEN'], true);

$token = Token::fromJson($_SESSION['SESSION_TOKEN']);
$ktorUrl = KTOR_HOST. "/get-all-appointments-by-status";



$searchQuery = $_POST['searchQuery'] ?? '';
$status = $_POST['status'] ?? '';
$appointmentDate = $_POST['appointmentDate'] ?? '';
$requestedDate = $_POST['requestedDate'] ?? '';
// Debugging: Print received values
echo "<pre>";

if($_SESSION['searchQuery'] !== ''){
    $searchQuery = $_SESSION['searchQuery'];
}
if($_SESSION['status'] !== ''){
    $status = $_SESSION['status'];
}
if($_SESSION['appointmentDate'] !== ''){
    $appointmentDate = $_SESSION['appointmentDate'];
}
if($_SESSION['requestedDate'] !== ''){
    $requestedDate = $_SESSION['requestedDate'];
}


echo "Search Query: " . htmlspecialchars($searchQuery) . "\n";
echo "Status: " . htmlspecialchars($status) . "\n";
echo "Appointment Date: " . htmlspecialchars($appointmentDate) . "\n";
echo "Requested Date: " . htmlspecialchars($requestedDate) . "\n";
echo "</pre>";

$contentRequest = new AppointmentSearchRequest(
    $searchQuery,
    $status,
    $appointmentDate,
    $requestedDate,
);
$jsonBody = $contentRequest->toJson();

echo $jsonBody;
//this will be call last
$serverResponse = fetchDataFromApiWithBody($ktorUrl, $jsonBody,$token->bearerToken);

echo $serverResponse;



include 'appointment-management.html';