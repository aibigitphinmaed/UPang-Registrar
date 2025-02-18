async function handleAddStudent(event) {
    event.preventDefault(); // Prevent the default form submission

    const formData = new FormData(event.target);



    // Validate that email and password do not have spaces
    const email = formData.get('email');
    if (email.includes(' ')) {
        alert('Email should not contain spaces');
        return;
    }

    const studentData = {
        userPersonalInfo: {
            firstName: formData.get('firstName'),
            middleName: formData.get('middleName'),
            lastName: formData.get('lastName'),
            extensionName: formData.get('extensionName'),
            gender: formData.get('gender'),
            citizenship: formData.get('citizenship'),
            religion: formData.get('religion'),
            civilStatus: formData.get('civilStatus'),
            email: formData.get('email'),
            number: formData.get('number'),
            birthDate: formData.get('birthDate'),
            fatherName: formData.get('fatherName'),
            motherName: formData.get('motherName'),
            spouseName: formData.get('spouseName'),
            contactPersonNumber: formData.get('contactPersonNumber')
        },
        userAddressInfo: {
            houseNumber: formData.get('houseNumber'),
            street: formData.get('street'),
            zone: formData.get('zone'),
            barangay: formData.get('barangay'),
            cityMunicipality: formData.get('cityMunicipality'),
            province: formData.get('province'),
            country: formData.get('country'),
            postalCode: formData.get('postalCode')
        }
    };

    const response = await fetch('/staff/add-student', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(studentData),
    });

    if (response.ok) {
        alert('Student added successfully');
        // Optionally redirect or reset the form
        window.location.href = "/dashboard"; // Redirect to dashboard or student list
    } else {
        alert('Failed to add student');
    }
}

document.getElementById('addStudentForm').addEventListener('submit', handleAddStudent);
