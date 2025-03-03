<?php
namespace App\Controllers\Staff;

use App\Controllers\AuthController;

include_once __DIR__.'/../AuthController.php';
include_once __DIR__.'/../../includes/security/session_check.php';

class StaffController {
    public function __construct()
    {

    }
    public function dashboard() {
        requireLogin();
        echo "Hello STAFF";
    }
}

