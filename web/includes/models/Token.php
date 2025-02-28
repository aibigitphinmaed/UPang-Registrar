<?php

namespace models;

use DateTime;
use Exception;

class Token
{
    public $bearerToken;
    public $refreshToken;
    public $expirationTimeDate; // Will be stored as "YYYY-MM-DD"

    public function __construct($bearerToken,$refreshToken,$expirationTimeDate) {
        $this->bearerToken = $bearerToken;
        $this->refreshToken = $refreshToken;
        $this->expirationTimeDate = $expirationTimeDate;
    }

    // Convert JSON string to Token object
    public static function fromJson($json) {
        try {
            $data = json_decode($json, true); // Decode JSON to array
            if (!$data) {
                throw new Exception("Invalid JSON");
            }

            return new Token(
                $data['bearerToken'],
                $data['refreshToken'],
                $data['expirationTimeDate']
            );
        } catch (Exception $e) {
            return null;
        }
    }

    // Convert Token object to JSON
    public function toJson() {
        return json_encode(get_object_vars($this));
    }
}


