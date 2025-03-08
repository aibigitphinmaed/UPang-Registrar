document.addEventListener("DOMContentLoaded", function () {
    const searchInput = document.getElementById("searchInput");
    const statusFilter = document.getElementById("statusFilter");
    const typeFilter = document.getElementById("typeFilter");
    const tableBody = document.getElementById("appointmentsBody");
    const rows = tableBody.getElementsByTagName("tr");

    console.log("Total rows found:", rows.length); // Debugging step

    function filterTable() {
        const searchValue = searchInput.value.trim().toLowerCase();
        const statusValue = statusFilter.value.trim().toLowerCase();
        const typeValue = typeFilter.value.trim().toLowerCase();

        for (let row of rows) {
            const cells = row.getElementsByTagName("td");

            // Skip rows without <td> elements (like empty or malformed rows)
            if (cells.length < 10) {
                console.warn("Skipping invalid row:", row);
                continue;
            }

            // Extract values safely
            const appointmentType = cells[1]?.innerText?.trim().toLowerCase() || "";
            const documentType = cells[2]?.innerText?.trim().toLowerCase() || "";
            const reason = cells[3]?.innerText?.trim().toLowerCase() || "";
            const requestedDate = cells[4]?.innerText?.trim().toLowerCase() || "";
            const scheduledDate = cells[5]?.innerText?.trim().toLowerCase() || "";
            const status = cells[6]?.innerText?.trim().toLowerCase() || "";
            const cancellationReason = cells[9]?.innerText?.trim().toLowerCase() || "";

            console.log("Processing row:", {
                appointmentType, documentType, reason, status
            });

            // Apply search filter (matches multiple columns)
            const matchesSearch =
                appointmentType.includes(searchValue) ||
                documentType.includes(searchValue) ||
                reason.includes(searchValue) ||
                requestedDate.includes(searchValue) ||
                scheduledDate.includes(searchValue) ||
                status.includes(searchValue) ||
                cancellationReason.includes(searchValue);

            // Apply status filter
            const matchesStatus = !statusValue || status === statusValue;

            // Apply appointment type filter
            const matchesType = !typeValue || appointmentType === typeValue;

            // Show or hide the row based on filters
            row.style.display = matchesSearch && matchesStatus && matchesType ? "" : "none";
        }
    }

    console.log("Attaching event listeners...");
    searchInput.addEventListener("input", filterTable);
    statusFilter.addEventListener("change", filterTable);
    typeFilter.addEventListener("change", filterTable);

    setTimeout(() => {
        console.log("Running filter setup after delay...");
        filterTable(); // Initial filtering
    }, 500);
});
