<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title}</title>
    <link rel="stylesheet" href="/static/css/dashboard.css">
</head>
<body>

<header>
    <h1>Welcome, ${username}!</h1>
    <p>This is your staff dashboard.</p>
</header>

<div class="container">
    <!-- Navigation Links -->
    <div class="links">
        <a href="/staff/profile">View Profile</a>
        <a href="/staff/add-student">Add Student</a>
        <a href="/logout">Logout</a>
    </div>

    <!-- Personal Information Section -->
    <section>
        <h2>Personal Information</h2>
        <p><strong>Name:</strong> ${personalInfo.firstName} ${personalInfo.middleName} ${personalInfo.lastName} ${(personalInfo.extensionName)!""}</p>
        <p><strong>Gender:</strong> ${personalInfo.gender}</p>
        <p><strong>Citizenship:</strong> ${personalInfo.citizenship}</p>
        <p><strong>Religion:</strong> ${personalInfo.religion}</p>
        <p><strong>Civil Status:</strong> ${personalInfo.civilStatus}</p>
        <p><strong>Email:</strong> ${personalInfo.email}</p>
        <p><strong>Contact Number:</strong> ${personalInfo.number}</p>
        <p><strong>Birth Date:</strong> ${personalInfo.birthDate}</p>
        <p><strong>Father's Name:</strong> ${personalInfo.fatherName}</p>
        <p><strong>Mother's Name:</strong> ${personalInfo.motherName}</p>
        <p><strong>Spouse's Name:</strong> ${(personalInfo.spouseName)!""}</p>
        <p><strong>Contact Person's Number:</strong> ${personalInfo.contactPersonNumber}</p>
    </section>

    <!-- Current Students Section -->
    <section class="student-list-container">
        <h3>Current Students</h3>
        <ul id="studentList">
            <!-- Student list will be dynamically populated -->
        </ul>
    </section>

</div>

<script src="/static/js/dashboard.js"></script>

</body>
</html>
