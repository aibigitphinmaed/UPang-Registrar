<?php

namespace app\models;

class LoginRequest
{

    public String $username;
    public String $password;

    public function __construct($username, $password) {
        $this->username = $username;
        $this->password = $password;
    }

    public function toJson(): false|string
    {
        return json_encode(get_object_vars($this));
    }
}