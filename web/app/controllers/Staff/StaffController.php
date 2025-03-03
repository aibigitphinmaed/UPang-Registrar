<?php
namespace App\Controllers\Staff;

use App\Controllers\AuthController;

include_once __DIR__.'/../AuthController.php';
include_once __DIR__.'/../../includes/security/session_check.php';

class StaffController {

    private static ?StaffController $instance = null;
    public static function getInstance(): ?StaffController
    {
        if (self::$instance === null) {
            self::$instance = new StaffController();
        }
        return self::$instance;
    }
    public function __construct()
    {

    }
    public function dashboard() {

    }
    public function appointments() {
        //mockups
        require_once __DIR__ . "/../../mockups/Mockups.php";
        $mockAppointments = getMockAppointments();

        $search = $_GET['search'] ?? '';
        $statusFilter = $_GET['status'] ?? '';
        $typeFilter = $_GET['type'] ?? '';

        // Filter appointments based on user input
        $filteredAppointments = array_filter($mockAppointments, function ($appointment) use ($search, $statusFilter, $typeFilter) {
            $matchesSearch = empty($search) ||
                stripos($appointment->id, $search) !== false ||
                stripos($appointment->studentId, $search) !== false ||
                stripos($appointment->staffId, $search) !== false ||
                stripos($appointment->appointmentType, $search) !== false;

            $matchesStatus = empty($statusFilter) || stripos($appointment->status, $statusFilter) !== false;
            $matchesType = empty($typeFilter) || stripos($appointment->appointmentType, $typeFilter) !== false;

            return $matchesSearch && $matchesStatus && $matchesType;
        });



        include_once __DIR__ . "/../../views/staff/staff-appointments.php";
    }

}

