<?php
session_start();

include_once '../../includes/models/Token.php';
include_once '../../includes/config.php';

use models\Token;


$ktorStaffTestApiUrl = KTOR_HOST."/test-staff-call";

$token = Token::fromJson($_SESSION['SESSION_TOKEN']);

$options = getHttpPostOptionsWithToken($token->bearerToken,"");

$context = stream_context_create($options);

$response = file_get_contents($ktorStaffTestApiUrl, false, $context);
if($response){
    echo $response;
}
