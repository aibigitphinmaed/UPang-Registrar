function viewAppointment(appointmentId) {
    window.location.href = `/staff/view-student-appointment?id=` + appointmentId;
}

function modifyAppointment(appointmentId){
    const form = document.getElementById("appointmentForm");
    const formData = new FormData(form);


    let params = new URLSearchParams();

    formData.forEach((value, key) => {
        params.append(key, value);
    });

    // Handle checkbox separately
    const isUrgent = form.querySelector('input[name="isUrgent"]').checked;
    params.set("isUrgent", isUrgent ? "1" : "0"); // Convert to 1 or 0

    // Redirect with updated query parameters
    window.location.href = `/staff/update-appointment?${params.toString()}`;

}
