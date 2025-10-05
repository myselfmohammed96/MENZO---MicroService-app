const toast = document.getElementById('toast');
const toastIcon = document.getElementById('toast-icon');
const toastTitle = document.getElementById('toast-title');
const toastMessage = document.getElementById('toast-message');

window.showToast = function(message, isSuccess = true) {
    toast.classList.remove('show', 'hide');
    toastIcon.classList.remove('toast-success-icon', 'toast-fail-icon');

    toastTitle.textContent = isSuccess ? "Success!" : "Failed!";
    toastMessage.textContent = message;
    toastIcon.classList.add(isSuccess ? 'toast-success-icon' : 'toast-fail-icon');

    toast.classList.add('show');
    setTimeout(() => {
        toast.classList.add('hide');
        toast.classList.remove('show');
    }, 5000);
}

document.addEventListener("DOMContentLoaded", () => {
    document.getElementById('toast-close-btn').addEventListener("click", () => {
        toast.classList.add('hide');
        toast.classList.remove('show');
    });
});