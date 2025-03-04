<?php

$authRoutes = require __DIR__ . '/auth.php';
$adminRoutes = require __DIR__ . '/admin.php';
$staffRoutes = require __DIR__ . '/staff.php';

return array_merge($authRoutes, $adminRoutes, $staffRoutes);
