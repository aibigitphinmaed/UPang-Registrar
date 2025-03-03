<?php

require_once __DIR__ . "/../../models/Token.php";

use App\Models\Token;

if (session_status() === PHP_SESSION_NONE) {
    session_start();
}
function checkToken(): bool
{
    return isset($_SESSION['SESSION_TOKEN']);
}

function checkExpiration(): bool
{
    if(!checkToken()) {
        return true;
    }
    $token = Token::fromJson($_SESSION['SESSION_TOKEN']);

    return $token->isExpired();
}

function requireLogin(): void
{
    if(!checkToken() and checkExpiration()){
        header("Location: /login");
        exit;
    }else{
        redirectIfLoggedIn();
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





