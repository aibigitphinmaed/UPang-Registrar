<?php
session_start();


include_once '../../includes/models/LoginRequest.php';
include_once '../../includes/models/Token.php';

use models\LoginRequest;
use models\Token;

include_once '../../includes/config.php';



$ktorApiUrl = KTOR_HOST."/login";



if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $username = $_POST['username'];
    $password = $_POST['password'];

    $formDataRequest = json_encode(["username" => $username, "password" => $password]);
    $loginRequest = new LoginRequest($username, $password);

    $options = [
        "http" => [
            "header" => "Content-Type: application/json\r\n",
            "method" => "POST",
            "content" => json_encode($loginRequest),
        ]
    ];

    $context = stream_context_create($options);
    $response = file_get_contents($ktorApiUrl, false, $context);


    if($response){
        $token = Token::fromJson($response);
        if($token){
            $_SESSION['SESSION_TOKEN'] = $token->toJson();
            header("Location: /presentation/views/dashboard/dashboard.html");

        }else{
            echo "Failed to parse token JSON.";
        }
    }else{
        echo "Failed to connect to Ktor API.";
    }
}


