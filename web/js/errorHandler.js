export function showErrorFromURL() {
    const urlParams = new URLSearchParams(window.location.search);
    const errorMessage = urlParams.get("error");

    if (errorMessage) {
        const errorElement = document.getElementById("error-message");
        if (errorElement) {
            errorElement.innerText = errorMessage;
            errorElement.style.color = "red";
        }
    }
}
