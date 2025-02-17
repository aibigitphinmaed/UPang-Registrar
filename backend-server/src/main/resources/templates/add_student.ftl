<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add New Student</title>
    <link rel="stylesheet" href="/static/css/add_student.css"> <!-- Link to the CSS file -->
</head>
<body>

<header>
    <h1>Add New Student</h1>
    <!-- Navigation Links -->
    <nav>
        <ul>
            <li><a href="/dashboard">Dashboard</a></li>
            <li><a href="/students">Student List</a></li>
            <li><a href="/logout">Logout</a></li>
        </ul>
    </nav>
</header>

<!-- Add New Student Form -->
<form id="addStudentForm">

    <!-- Personal Information Section -->
    <div class="form-section">
        <h2>Personal Information</h2>
        <label for="firstName">First Name:</label>
        <input type="text" id="firstName" name="firstName" required><br>

        <label for="middleName">Middle Name:</label>
        <input type="text" id="middleName" name="middleName" required><br>

        <label for="lastName">Last Name:</label>
        <input type="text" id="lastName" name="lastName" required><br>

        <label for="extensionName">Extension Name:</label>
        <input type="text" id="extensionName" name="extensionName"><br>

        <label for="gender">Gender:</label>
        <input type="text" id="gender" name="gender" required><br>

        <label for="citizenship">Citizenship:</label>
        <input type="text" id="citizenship" name="citizenship" required><br>

        <label for="religion">Religion:</label>
        <input type="text" id="religion" name="religion" required><br>

        <label for="civilStatus">Civil Status:</label>
        <input type="text" id="civilStatus" name="civilStatus" required><br>

        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required><br>

        <label for="number">Contact Number:</label>
        <input type="text" id="number" name="number" required><br>

        <label for="birthDate">Birth Date:</label>
        <input type="date" id="birthDate" name="birthDate" required><br>

        <label for="fatherName">Father's Name:</label>
        <input type="text" id="fatherName" name="fatherName" required><br>

        <label for="motherName">Mother's Name:</label>
        <input type="text" id="motherName" name="motherName" required><br>

        <label for="spouseName">Spouse's Name:</label>
        <input type="text" id="spouseName" name="spouseName"><br>

        <label for="contactPersonNumber">Contact Person's Number:</label>
        <input type="text" id="contactPersonNumber" name="contactPersonNumber" required><br>
    </div>

    <!-- Address Information Section -->
    <div class="form-section">
        <h2>Address Information</h2>

        <label for="houseNumber">House Number:</label>
        <input type="text" id="houseNumber" name="houseNumber" required><br>

        <label for="street">Street:</label>
        <input type="text" id="street" name="street" required><br>

        <label for="zone">Zone:</label>
        <input type="text" id="zone" name="zone" required><br>

        <label for="barangay">Barangay:</label>
        <input type="text" id="barangay" name="barangay" required><br>

        <label for="cityMunicipality">City/Municipality:</label>
        <input type="text" id="cityMunicipality" name="cityMunicipality" required><br>

        <label for="province">Province:</label>
        <input type="text" id="province" name="province" required><br>

        <label for="country">Country:</label>
        <input type="text" id="country" name="country" required><br>

        <label for="postalCode">Postal Code:</label>
        <input type="text" id="postalCode" name="postalCode" required><br>
    </div>

    <!-- Submit Button -->
    <div class="form-section">
        <button type="submit">Add Student</button>
    </div>

</form>

<script src="/static/js/add_student.js"></script> <!-- Link to the JS file -->

</body>
</html>
