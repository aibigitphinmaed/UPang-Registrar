<?php
namespace models;
class LocationInfo {
    public ?string $houseNumber;
    public ?string $street;
    public ?string $zone;
    public ?string $barangay;
    public ?string $cityMunicipality;
    public ?string $province;
    public ?string $country;
    public ?string $postalCode;

    public function __construct($data) {
        $this->houseNumber = $data['houseNumber'] ?? null;
        $this->street = $data['street'] ?? null;
        $this->zone = $data['zone'] ?? null;
        $this->barangay = $data['barangay'] ?? null;
        $this->cityMunicipality = $data['cityMunicipality'] ?? null;
        $this->province = $data['province'] ?? null;
        $this->country = $data['country'] ?? null;
        $this->postalCode = $data['postalCode'] ?? null;
    }

    public function toArray() {
        return get_object_vars($this);
    }
}
?>
