<?php
session_start();


require_once '../../includes/models/LoginRequest.php';
require_once '../../includes/models/Token.php';

use models\LoginRequest;
use models\Token;


if (!isset($_SESSION['SESSION_TOKEN'])) {
    header("Location: ../views/login.html");
    exit();
}


$jwtToken = Token::fromJson($_SESSION['SESSION_TOKEN']);




