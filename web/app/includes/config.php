<?php
namespace App\Includes;
const ADMIN_USER = 'Admin';
const ADMIN_PASS = '1234';
const KTOR_HOST = 'http://backend-server:8080';

const MODELS_HOST = __DIR__ . "/../models/";

function getHttpPostOptionsWithToken($token,$data){
    return [
        "http" => [
            "header" => "Content-Type: application/json\r\n" .
                "Authorization: Bearer " . $token . "\r\n",
            "method" => "POST",
            "content" => !empty($data) ? json_encode($data) : ""
        ]
    ];
}



