let toast;
let toastIcon;
let toastTitle;
let toastMessage;


/*
*
*   Show toast notification
*   function call -> eg: showToast("Blocked successfully" , true);
*
*/
export function showToast(message, isSuccess = true) {

    toast.classList.remove('show', 'hide');
    toastIcon.classList.remove('toast-success-icon', 'toast-fail-icon');

    toastTitle.textContent = isSuccess ? "Success!" : "Failed!";
    toastMessage.textContent = message;
    toastIcon.classList.add(isSuccess ? 'toast-success-icon' : 'toast-fail-icon');

    toast.classList.add('show');

    //  auto close toast - after 10s (10000 milliseconds)
    setTimeout(() => {
        toast.classList.add('hide');
        toast.classList.remove('show');
    }, 10000);
}


/*
*
*   DOM Loading event
*
*/
document.addEventListener("DOMContentLoaded", () => {
    toast = document.getElementById('toast');
    toastIcon = document.getElementById('toast-icon');
    toastTitle = document.getElementById('toast-title');
    toastMessage = document.getElementById('toast-message');

    //  close toast button
    document.getElementById('toast-close-btn').addEventListener("click", () => {
        toast.classList.add('hide');
        toast.classList.remove('show');
    });
});
