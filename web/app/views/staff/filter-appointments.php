<?php

use App\Controllers\Staff\StaffController;

require_once __DIR__ . '/../../controllers/Staff/StaffController.php';

$controller = StaffController::getInstance();
$controller->appointments();

?>