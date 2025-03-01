<?php

const ADMIN_USER = 'admin';
const ADMIN_PASS = '1234';
const KTOR_HOST = 'http://backend-server:8080';


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



