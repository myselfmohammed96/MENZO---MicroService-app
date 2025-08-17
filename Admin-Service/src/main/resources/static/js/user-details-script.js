const getUserDetailsUrl = "http://localhost:8080/user/user-details";
const updateUserActiveStatusUrl = "http://localhost:8080/user/update-status";

document.addEventListener("DOMContentLoaded", () => {

    //  ******* User details fields *******
    const userDetailsFields = {
        fullName: document.getElementById('page-title'),
        email: document.getElementById('user-email'),
        phoneNumber: document.getElementById('user-phone-number'),
        dob: document.getElementById('user-dob'),
        gender: document.getElementById('user-gender'),
        createdAt: document.getElementById('account-created-at'),
        activeStatus: document.getElementById('status-text')
    };

    //  ******* User activeStatus switch & field *******
    const statusToggle = document.getElementById('status-toggle');
    let previousStatus = null;

    //  ******* Confirm Modal elements *******
    const confirmModal = document.getElementById('confirm-modal');
    const confirmYesBtn = document.getElementById('confirm-yes');
    const confirmNoBtn = document.getElementById('confirm-no');
    const confirmMessage = document.querySelector('.confirm-message');

    //  ******* Toast notification elements *******
    const toast = document.getElementById('toast');
    const toastIcon = document.getElementById('toast-icon');
    const toastTitle = document.getElementById('toast-title');
    const toastMessage = document.getElementById('toast-message');

    //  ******* Hidden Input element - userId *******
    const userIdElement = document.getElementById('user-id');
    if(!userIdElement) {
        console.error("User ID not found in the DOM.");
        return;
    }

    const currentUserId = userIdElement.value;
    if(!currentUserId) {
        console.error("User ID is empty.");
        return;
    }

    function showToast(message, isSuccess = true) {
        toast.classList.remove('toast-success', 'toast-fail', 'show', 'hide');
        toastIcon.classList.remove('toast-success-icon', 'toast-fail-icon');

        toastMessage.textContent = message;
        toastTitle.textContent = isSuccess ? "Success!" : "Failed!";
        toast.classList.add(isSuccess ? 'toast-success' : 'toast-fail');
        toastIcon.classList.add(isSuccess ? 'toast-success-icon' : 'toast-fail-icon');
        toast.classList.add('show');

        setTimeout(() => {
            toast.classList.remove('show');
            toast.classList.add('hide');
        }, 5000);
    }

    async function getUserDetails(userId) {
        try {
            const response = await fetch(`${getUserDetailsUrl}?id=${userId}`);
            if (!response.ok) {
                console.error("Failed to fetch user details. Status: " + response.status);
                return null;
            }
            return await response.json();
        } catch(error) {
            console.error("Error fetching user details: ", error);
            return null;
        }
    }

    async function updateUserActiveStatus(userId, block) {
        try {
            const response = await fetch(updateUserActiveStatusUrl, {
                method: "PUT",
                credentials: "include",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    userId: userId,
                    block: block
                })
            });
            if(!response.ok) throw new Error("Failed to update active status");

            userDetailsFields.activeStatus.textContent = block ? "Inactive" : "Active";
            userDetailsFields.activeStatus.classList.remove('status-green', 'status-red');
            userDetailsFields.activeStatus.classList.add(block ? 'status-red' : 'status-green');
            showToast(block ? "Blocked successfully" : "Unblocked successfully", true);
        } catch(error) {
            console.error("Error updating user active status.",error);
            showToast(block ? "Blocking user failed" : "Unblocking user failed");
            statusToggle.checked = previousStatus;
        }
    }

    function showConfirmModal(isBlocked, onConfirm) {
        confirmMessage.textContent = isBlocked
                ? "Do you really want to Unblock this user?"
                : "Do you really want to Block this user?";
        confirmModal.style.display = "flex";

        confirmYesBtn.onclick = () => {
//            confirmModal.classList.add('hidden');
            confirmModal.style.display = "none";
            onConfirm();
        };
        confirmNoBtn.onclick = () => {
//            confirmModal.classList.add('hidden');
            confirmModal.style.display = "none";
            statusToggle.checked = previousStatus;
        };
    }

    function formatDate(dateStr) {
        try {
            if(!dateStr) return "N/A";

            const months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
                            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
            const [datePart] = dateStr.split("T");
            const [year, month, day] = datePart.split("-");

            return `${day} ${months[+month - 1]} ${year}`;
        } catch (error) {
            console.warn("Invalid date: ", dateStr);
            return dateStr;
        }
    }

    async function loadUserDetails(userId) {
        const userDetails = await getUserDetails(userId);
        if(!userDetails) {
            console.warn("No user details returned.");
            return;
        }
        userDetailsFields.fullName.textContent = `${userDetails.firstName} ${userDetails.lastName}` || "N/A";
        userDetailsFields.email.textContent = userDetails.email || "N/A";
        userDetailsFields.phoneNumber.textContent = userDetails.phoneNumber || "N/A";
        userDetailsFields.dob.textContent = formatDate(userDetails.dateOfBirth);
        userDetailsFields.gender.textContent = userDetails.gender || "N/A";
        userDetailsFields.createdAt.textContent = formatDate(userDetails.createdAt);
        userDetailsFields.activeStatus.textContent = userDetails.active ? "Active" : "Inactive";
        userDetailsFields.activeStatus.classList.add(userDetails.active ? "status-green" : "status-red");
        statusToggle.checked = userDetails.active;
    }

    statusToggle.addEventListener("change", () => {
        console.log("Toggle changed");
        const isChecked = statusToggle.checked;
        console.log("Status is: " + isChecked);
        const isBlocked = isChecked;
        previousStatus = !isChecked;

        showConfirmModal(isBlocked, () => {
            updateUserActiveStatus(currentUserId, !isBlocked);
        });
    });

    loadUserDetails(currentUserId);
});



//-----------


//const updateUserStatusUrl = "http://localhost:8080/user/update-status";

//const modal = document.getElementById("confirm-modal");
//const confirmYesBtn = document.getElementById("confirm-yes");
//const confirmNoBtn = document.getElementById("confirm-no");
//const confirmMessage = document.querySelector(".confirm-message");
//
//let previousStatus = null;
//
//function showToast(message, success = true) {
//    const toast = document.createElement("div");
//    toast.textContent = message;
//    toast.className = `toast ${success ? 'toast-success' : 'toast-fail'}`;
//    document.body.appendChild(toast);
//
//    setTimeout(() => toast.classList.add('show'), 10);
//    setTimeout(() => {
//        toast.classList.remove('show');
//        setTimeout(() => document.body.removeChild(toast), 300);
//    }, 3000);
//}
//
//function showConfirmationModal(isBlocking, onConfirm) {
//    confirmMessage.textContent = isBlocking
//        ? "Do you really want to block this user?"
//        : "Do you want to unblock this user?";
//    modal.classList.remove("hidden");
//
//    confirmYesBtn.onclick = () => {
//        modal.classList.add("hidden");
//        onConfirm();
//    };
//
//    confirmNoBtn.onclick = () => {
//        modal.classList.add("hidden");
//        statusToggle.checked = previousStatus; // Revert toggle
//    };
//}
//
//async function updateUserStatus(userId, activeStatus) {
//    try {
//        const response = await fetch(updateUserStatusUrl, {
//            method: "PUT",
//            headers: {
//                "Content-Type": "application/json"
//            },
//            body: JSON.stringify({
//                userId: userId,
//                active: activeStatus
//            })
//        });
//
//        if (!response.ok) throw new Error("Failed status update");
//
//        const newStatusText = activeStatus ? "Active" : "Inactive";
//        userDetailsFields.activeStatus.textContent = newStatusText;
//        userDetailsFields.activeStatus.className = "pod-indicator " + (activeStatus ? "status-green" : "status-red");
//        showToast(activeStatus ? "Unblocked successfully" : "Blocked successfully", true);
//    } catch (error) {
//        console.error(error);
//        showToast(activeStatus ? "Unblocking failed" : "Blocking failed", false);
//        statusToggle.checked = previousStatus; // Revert toggle
//    }
//}
//
//statusToggle.addEventListener("change", () => {
//    const isChecked = statusToggle.checked;
//    const isBlocking = !isChecked;
//    previousStatus = !isBlocking;
//
//    showConfirmationModal(isBlocking, () => {
//        updateUserStatus(currentUserId, !isBlocking);
//    });
//});
