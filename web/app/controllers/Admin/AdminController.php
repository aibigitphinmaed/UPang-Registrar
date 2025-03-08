<?php
namespace App\Controllers\Admin;

include_once __DIR__ . '/../../includes/security/session_check.php';

class AdminController {
    public function dashboard() {
        checkRole('admin');
    }
}
