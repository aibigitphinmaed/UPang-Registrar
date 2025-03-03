<?php
namespace App\Controllers\Admin;

include_once __DIR__.'/../AuthController.php';

requireLogin();

class AdminController {
    public function dashboard() {
        header("Location: /Admin");
    }
}
