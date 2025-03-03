<?php
// Define routes with full namespaces

$routes = [
    'login'  => 'App\Controllers\AuthController@login',
    'logout' => 'App\Controllers\AuthController@logout',
    'admin'  => 'App\Controllers\Admin\AdminController@dashboard',
    'staff'  => 'App\Controllers\Staff\StaffController@dashboard',
    'student-appointments'  => 'App\Controllers\Staff\StaffController@appointments',
];

$route = $_GET['route'] ?? 'login';

// Check if the requested route exists
if (array_key_exists($route, $routes)) {
    list($controllerNamespace, $method) = explode('@', $routes[$route]);

  //  $controllerFile = realpath(__DIR__.'/../app/controllers/' . str_replace('App\Controllers\\', '', $controllerNamespace) . '.php');



    // Convert namespace to file path (handles subdirectories)
    $controllerFile = realpath(__DIR__ . '/../app/controllers/' . str_replace(['App\\Controllers\\', '\\'], ['', '/'], $controllerNamespace) . '.php');

    // Ensure the controller file exists before requiring it
    if (file_exists($controllerFile)) {
        require_once $controllerFile;
        // Ensure the class exists before instantiating it

        if (class_exists($controllerNamespace)) {
            $instance = new $controllerNamespace();

            // Ensure the method exists before calling it
            if (method_exists($instance, $method)) {
                ob_start();
                $instance->$method();
                $content = ob_get_clean();

                require __DIR__ . '/../app/views/layout.php';
            } else {
                die("Error: Method '$method' not found in $controllerNamespace.");
            }

        } else {
            die("Error: Controller class '$controllerNamespace' not found.");
        }
    } else {

        die("Error: Controller file '$controllerFile' not found." . __DIR__);
    }
} else {
    die("404 - Page Not Found");
}


