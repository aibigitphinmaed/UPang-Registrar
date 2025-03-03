<?php

namespace App\Models;

use JsonSerializable;

class Appointment implements JsonSerializable
{
    public int $id;
    public int $studentId;
    public ?int $staffId;
    public string $appointmentType;
    public ?string $documentType;
    public ?string $reason;
    public ?string $requestedDate;
    public ?string $scheduledDate;
    public string $status;
    public ?string $notifiedAt;
    public bool $isUrgent;
    public ?string $remarks;
    public ?string $cancellationReason;
    public string $createdAt;
    public string $updatedAt;

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

    public function jsonSerialize(): array
    {
        return get_object_vars($this);
    }

    public static function fromJson(string $json): ?self
    {
        $data = json_decode($json, true);

        if (!$data) {
            return null;
        }

        return new self(
            $data['id'],
            $data['studentId'],
            $data['staffId'] ?? null,
            $data['appointmentType'],
            $data['documentType'] ?? null,
            $data['reason'] ?? null,
            $data['requestedDate'] ?? null,
            $data['scheduledDate'] ?? null,
            $data['status'],
            $data['notifiedAt'] ?? null,
            $data['isUrgent'],
            $data['remarks'] ?? null,
            $data['cancellationReason'] ?? null,
            $data['createdAt'],
            $data['updatedAt']
        );
    }
}
