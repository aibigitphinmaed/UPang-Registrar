<?php

namespace models;
use JsonSerializable;

class AppointmentSearchRequest implements JsonSerializable {

    private String $searchQuery;
    private String $status;
    private String $requestedDate;
    private String $appoinmentDate;
    public function jsonSerialize(): array
    {
        return [
            'searchQuery' => $this->searchQuery,
            'status' => $this->status,
            'requestedDate' => $this->requestedDate,
            'appoitnmentDate' => $this->appoinmentDate,
        ];
    }

    public function __construct($searchQuery, $status, $requestedDate, $appoinmentDate)
    {
        $this->searchQuery = $searchQuery;
        $this->status = $status;
        $this->requestedDate = $requestedDate;
        $this->appoinmentDate = $appoinmentDate;
    }

    public function toJson(): false|string
    {
        return json_encode(get_object_vars($this));
    }
}
