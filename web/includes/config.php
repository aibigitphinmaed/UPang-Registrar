<?php

define('ADMIN_USER', 'admin');
define('ADMIN_PASS', '1234');
define('KTOR_HOST', 'http://localhost:8080');


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
