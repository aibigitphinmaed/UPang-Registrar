<?php
namespace App\Controllers;

require_once __DIR__ . "/../../app/includes/config.php";


use App\Models\Token;

use const App\Includes\KTOR_HOST;
use const App\Includes\MODELS_HOST;

require_once MODELS_HOST . "Token". ".php";



class AuthController
{
    public $jwtToken = Token::class;


    //region Dependency Injection construct
    private static $instance = null;
    public function __construct(){
        if (session_status() === PHP_SESSION_NONE) {
            session_start();
        }
    }

    public static function getInstance() {
        if (self::$instance === null) {
            self::$instance = new AuthController();
        }
        return self::$instance;
    }
    //endregion


    public function login(): void
    {

        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $username = trim($_POST['username']);
            $password = trim($_POST['password']);

            // API URL
            $api_url = KTOR_HOST . "/login";
            $data = json_encode(['username' => $username, 'password' => $password]);

            // cURL request
            $ch = curl_init($api_url);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
            curl_setopt($ch, CURLOPT_POST, true);
            curl_setopt($ch, CURLOPT_HTTPHEADER, ['Content-Type: application/json']);
            curl_setopt($ch, CURLOPT_POSTFIELDS, $data);

            $response = curl_exec($ch);
            $http_code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
            curl_close($ch);
            if ($http_code == 200) {
                $result = json_decode($response, true);

                // Debugging: Check what you're actually receiving
                error_log("API Response: " . json_encode($result));

                if (isset($result['bearerToken'])) {
                    // Create Token object
                    $_SESSION['SESSION_TOKEN'] = json_encode([
                        'bearerToken' => $result['bearerToken'],
                        'refreshToken' => $result['refreshToken'],
                        'expirationTimeDate' => $result['expirationTimeDate']
                    ]);

                    $this->jwtToken = Token::fromJson(json_encode($result));
                    $role = $this->retrieveRole();
                    $_SESSION['role'] = $role;

                    // Redirect based on role
                    switch (strtolower($role)) {
                        case 'admin':
                            header("Location: /admin");
                            break;
                        case 'staff':
                            header("Location: /staff");
                            break;
                        default:
                            header("Location: /login");
                            break;
                    }
                    exit();
                }
            }


            $this->showLoginView();

        }else{
            $this->showLoginView();
        }
    }

    public function logout(): void
    {
        $_SESSION = [];
        header("Location: /");
    }


    private function showLoginView(): void
    {
        include __DIR__ . "/../../app/views/login/login.php";
        exit();
    }

    private function retrieveRole():string
    {
        $ktorApiUrl = KTOR_HOST . "/get-role";
        $data = $this->jwtToken->bearerToken;
        $ch = curl_init($ktorApiUrl);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
            curl_setopt($ch, CURLOPT_POST, true);
            curl_setopt($ch, CURLOPT_HTTPHEADER, [
                'Content-Type: application/json',
                "Authorization: Bearer $data"
            ]);


        $response = curl_exec($ch);
        $http_code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        curl_close($ch);
        $decodedResponse = json_decode($response, true);

        if ($http_code !== 200 || !$decodedResponse) {
            return "Error: /get-role api failed";
        }

        return $decodedResponse['role'];
    }

    public function unauthorized(): void{
        include __DIR__. '/../views/unauthorized.php';
    }

}
