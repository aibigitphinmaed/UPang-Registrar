<?php
namespace app\models;
use JsonSerializable;

class Appointment implements JsonSerializable {
    private int $id;
    private int $studentId;
    private ?int $staffId;
    private string $appointmentType;
    private ?string $documentType;
    private ?string $reason;
    private ?string $requestedDate;
    private ?string $scheduledDate;
    private string $status;
    private ?string $notifiedAt;
    private bool $isUrgent;
    private ?string $remarks;
    private ?string $cancellationReason;
    private string $createdAt;
    private string $updatedAt;

    public function __construct(
        int $id,
        int $studentId,
        ?int $staffId,
        string $appointmentType,
        ?string $documentType,
        ?string $reason,
        ?string $requestedDate,
        ?string $scheduledDate,
        string $status,
        ?string $notifiedAt,
        bool $isUrgent,
        ?string $remarks,
        ?string $cancellationReason,
        string $createdAt,
        string $updatedAt
    ) {
        $this->id = $id;
        $this->studentId = $studentId;
        $this->staffId = $staffId;
        $this->appointmentType = $appointmentType;
        $this->documentType = $documentType;
        $this->reason = $reason;
        $this->requestedDate = $requestedDate;
        $this->scheduledDate = $scheduledDate;
        $this->status = $status;
        $this->notifiedAt = $notifiedAt;
        $this->isUrgent = $isUrgent;
        $this->remarks = $remarks;
        $this->cancellationReason = $cancellationReason;
        $this->createdAt = $createdAt;
        $this->updatedAt = $updatedAt;
    }

    // Implement JsonSerializable to customize JSON serialization
    public function jsonSerialize(): array {
        return [
            'id' => $this->id,
            'studentId' => $this->studentId,
            'staffId' => $this->staffId,
            'appointmentType' => $this->appointmentType,
            'documentType' => $this->documentType,
            'reason' => $this->reason,
            'requestedDate' => $this->requestedDate,
            'scheduledDate' => $this->scheduledDate,
            'status' => $this->status,
            'notifiedAt' => $this->notifiedAt,
            'isUrgent' => $this->isUrgent,
            'remarks' => $this->remarks,
            'cancellationReason' => $this->cancellationReason,
            'createdAt' => $this->createdAt,
            'updatedAt' => $this->updatedAt
        ];
    }
}
?>
