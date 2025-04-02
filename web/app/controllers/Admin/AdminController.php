<?php
namespace App\Controllers\Admin;

include_once __DIR__ . '/../../includes/security/session_check.php';
include_once __DIR__. '/../../config/admin-api.php';

class AdminController {
    public function dashboard() {
        checkRole('admin');
    }
}
