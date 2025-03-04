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
    }
}

function redirectIfLoggedIn(): void
{
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

function checkRole($allowedRole): void
{
    if (!isset($_SESSION['role']) ||($_SESSION['role'] !== $allowedRole) ) {
        header_remove();
        header("Location: /unauthorized"); // Redirect to an unauthorized page
        exit;
    }
}

function isLoggedIn(): bool{
    if(isset($_SESSION['role']) and !checkExpiration()){
        return true;
    }else{
        return false;
    }
}





