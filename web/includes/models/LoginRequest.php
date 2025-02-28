<?php

namespace models;

class LoginRequest
{

    public $username;
    public $password;

    public function __construct($username, $password) {
        $this->username = $username;
        $this->password = $password;
    }

    public function toJson(){
        return json_encode(get_object_vars($this));
    }
}