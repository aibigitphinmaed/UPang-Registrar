async function loadStudentList() {
    try {
        const response = await fetch('/staff/get-students', {
            method: 'GET',
        });

        if (!response.ok) {
            throw new Error('Failed to load students');
        }

        const students = await response.json();
        const studentList = document.getElementById('studentList');
        studentList.innerHTML = ''; // Clear current list

        students.forEach(student => {
            const li = document.createElement('li');
            li.textContent = `${student.id} : ${student.username}`;
            studentList.appendChild(li);
        });
    } catch (error) {
        console.error(error);
        alert('An error occurred while loading the student list');
    }
}

// Optionally load the student list when the page is loaded
window.onload = loadStudentList;
