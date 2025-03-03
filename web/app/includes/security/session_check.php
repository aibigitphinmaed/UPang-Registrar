<?php

use app\models\Token;

if (session_status() === PHP_SESSION_NONE) {
    session_start();
}
function checkToken(): bool
{
    return isset($_SESSION['SESSION_TOKEN']);
}

function checkExpiration(): bool
{
    $token = Token::fromJson($_SESSION['SESSION_TOKEN']);
    return $token->isExpired();
}

function requireLogin(): void
{
    if(!checkToken() and checkExpiration()){
        header("Location: /login");
        exit;
    }
}

function redirectIfLoggedIn(){
    if(checkToken() and !checkExpiration()){
        $role = $_SESSION['role'];
        switch (strtolower($role)) {
            case 'admin':
                header("Location: /admin");
                break;
            case 'staff':
                header("Location: /staff");
                break;
            default:
                header("Location: /login");
                break;
        }
    }
}





