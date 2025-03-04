document.addEventListener("DOMContentLoaded", function () {
    const searchInput = document.getElementById("searchInput");
    const statusFilter = document.getElementById("statusFilter");
    const typeFilter = document.getElementById("typeFilter");
    const tableBody = document.getElementById("appointmentsBody");
    const rows = tableBody.getElementsByTagName("tr");

    function filterTable() {
        const searchValue = searchInput.value.trim().toLowerCase();
        const statusValue = statusFilter.value.trim().toLowerCase();
        const typeValue = typeFilter.value.trim().toLowerCase();


        for (let row of rows) {
            const cells = row.getElementsByTagName("td");
            console.log(cells)
            // Ensure the row has at least 12 columns before proceeding
            if (cells.length < 12) {
                console.error("Skipping row due to missing columns:", row);
                continue;
            }


            // Safely extract values
            const appointmentType = cells[1]?.innerText.trim().toLowerCase() || "";
            const documentType = cells[2]?.innerText.trim().toLowerCase() || "";
            const reason = cells[3]?.innerText.trim().toLowerCase() || "";
            const status = cells[6]?.innerText.trim().toLowerCase() || "";

            // Apply search filter (matches Document Type, Reason, Appointment Type, or Status)
            const matchesSearch =
                appointmentType.includes(searchValue) ||
                documentType.includes(searchValue) ||
                reason.includes(searchValue) ||
                status.includes(searchValue);

            // Apply status filter
            const matchesStatus = !statusValue || status === statusValue;

            // Apply appointment type filter
            const matchesType = !typeValue || appointmentType === typeValue;

            // Show or hide the row based on filters
            row.style.display = matchesSearch && matchesStatus && matchesType ? "" : "none";
        }
    }

    // Attach event listeners to filters
    searchInput.addEventListener("input", filterTable);
    statusFilter.addEventListener("change", filterTable);
    typeFilter.addEventListener("change", filterTable);
});
