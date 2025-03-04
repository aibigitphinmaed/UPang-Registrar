<?php
namespace App\Controllers\Staff;

include_once __DIR__ . '/../../models/Appointment.php';

use App\Models\Appointment;

include_once __DIR__.'/../AuthController.php';
include_once __DIR__.'/../../includes/security/session_check.php';
require_once __DIR__ . "/../../mockups/Mockups.php";

class StaffController {

    private array $mockAppointments;
    public $appointmentSelected;
    private static ?StaffController $instance = null;
    public static function getInstance(): ?StaffController
    {
        checkRole('staff');
        if (self::$instance === null) {
            self::$instance = new StaffController();
        }
        return self::$instance;
    }
    public function __construct()
    {
        checkRole('staff');
        $this->mockAppointments = getMockAppointments();
    }
    public function dashboard() {

    }

    public function appointments(): void
    {

        $search = $_GET['search'] ?? '';
        $statusFilter = $_GET['status'] ?? '';
        $typeFilter = $_GET['type'] ?? '';

        // Filter appointments based on user input
        $filteredAppointments = array_filter($this->mockAppointments, function ($appointment) use ($search, $statusFilter, $typeFilter) {
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

    public function viewStudentAppointment(): void
    {
        $appointmentID = $_GET['id'] ?? null;
        $this->appointmentSelected = $this->getAppointmentById($appointmentID);
        include_once __DIR__ . "/../../views/staff/view-appointment.php";
    }



    private function getAppointmentById(int $id): ?Appointment{
        foreach ($this->mockAppointments as $data) {
            if ($data->id === $id) {
                return $data;
            }
        }
        return null;
    }


}

