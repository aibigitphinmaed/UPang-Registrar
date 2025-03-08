<?php
namespace app\models;

class PersonalInfo
{
    public ?string $firstName;
    public ?string $lastName;
    public ?string $middleName;
    public ?string $extensionName;
    public ?string $gender;
    public ?string $citizenship;
    public ?string $religion;
    public ?string $civilStatus;
    public ?string $email;
    public ?string $number;
    public ?string $birthDate;
    public ?string $fatherName;
    public ?string $motherName;
    public ?string $spouseName;
    public ?string $contactPersonNumber;

    public function __construct($data)
    {
        $this->firstName = $data['firstName'] ?? null;
        $this->lastName = $data['lastName'] ?? null;
        $this->middleName = $data['middleName'] ?? null;
        $this->extensionName = $data['extensionName'] ?? null;
        $this->gender = $data['gender'] ?? null;
        $this->citizenship = $data['citizenship'] ?? null;
        $this->religion = $data['religion'] ?? null;
        $this->civilStatus = $data['civilStatus'] ?? null;
        $this->email = $data['email'] ?? null;
        $this->number = $data['number'] ?? null;
        $this->birthDate = $data['birthDate'] ?? null;
        $this->fatherName = $data['fatherName'] ?? null;
        $this->motherName = $data['motherName'] ?? null;
        $this->spouseName = $data['spouseName'] ?? null;
        $this->contactPersonNumber = $data['contactPersonNumber'] ?? null;
    }

    public function toArray()
    {
        return get_object_vars($this);
    }
}

?>
