<?php
namespace App\Controllers\Staff;

include_once __DIR__ . '/../../models/Appointment.php';

use App\Models\Appointment;

include_once __DIR__.'/../AuthController.php';
include_once __DIR__.'/../../includes/security/session_check.php';
require_once __DIR__ . "/../../mockups/Mockups.php";

include_once __DIR__. '/../../config/api.php';

class StaffController {

    private array $mockAppointments;
    public array $listOfAppointments;
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
        $serverResponseOnList = getAllAppointmentsApi();
        if($serverResponseOnList){
            $this->listOfAppointments = json_decode($serverResponseOnList, true);
        }else{
            $this->listOfAppointments = [];
        }

    }
    public function dashboard() {

    }

    public function appointments(): void
    {

        $search = $_GET['search'] ?? '';
        $statusFilter = $_GET['status'] ?? '';
        $typeFilter = $_GET['type'] ?? '';

        $listOfAppointments = $this->listOfAppointments;
        // Filter appointments based on user input
        $filteredAppointments = array_filter($listOfAppointments, function ($appointment) use ($search, $statusFilter, $typeFilter) {
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
        foreach ($this->listOfAppointments as $data) {
            if ($data->id === $id) {
                return $data;
            }
        }
        return null;
    }

    public function updateAppointment():void{
        $appointmentID = $_GET['id'] ?? null;

        if (!$appointmentID) {
            echo "Error: No appointment ID provided.";
            return;
        }

        // Retrieve existing appointment
        $appointment = $this->getAppointmentById((int) $appointmentID);

        if (!$appointment) {
            echo "Error: Appointment not found.";
            return;
        }

        // Update appointment fields from URL parameters
        $appointment->staffId = $_GET['staffId'] ?? $appointment->staffId;
        $appointment->appointmentType = $_GET['appointmentType'] ?? $appointment->appointmentType;
        $appointment->documentType = $_GET['documentType'] ?? $appointment->documentType;
        $appointment->reason = $_GET['reason'] ?? $appointment->reason;
        $appointment->requestedDate = $_GET['requestedDate'] ?? $appointment->requestedDate;
        $appointment->scheduledDate = $_GET['scheduledDate'] ?? $appointment->scheduledDate;
        $appointment->status = $_GET['status'] ?? $appointment->status;
        $appointment->isUrgent = isset($_GET['isUrgent']) && $_GET['isUrgent'] === '1';
        $appointment->remarks = $_GET['remarks'] ?? $appointment->remarks;
        $appointment->cancellationReason = $_GET['cancellationReason'] ?? $appointment->cancellationReason;
        $appointment->updatedAt = date('Y-m-d H:i:s'); // Set updated timestamp

        $response = updateAppointmentApi($appointment);
        if($response){
            error_log(json_decode($response, true));
        }
    }


}

