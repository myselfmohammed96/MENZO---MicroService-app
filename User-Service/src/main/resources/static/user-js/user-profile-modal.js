document.addEventListener("DOMContentLoaded", () => {

    document.addEventListener("keydown", function(e) {
        if(e.key === "Escape") {
            const userModal = document.getElementById("user-modal");
            if(userModal && userModal.style.display === "flex") {
                closeUserEditModal();
            }
        }
    });
});

async function updateUserDetails(latestUserDetails) {
    try {
        const response = await fetch("", {
            method: "PUT",
            credentials: "include",
            headers: { "Content-Type": "application/json"},
            body: JSON.stringify(latestUserDetails)
        });
        if(response.status === 400) {
            throw new Error("Invalid data");
        } else if(response.status === 404) {
            throw new Error("User not found");
        } else if(!response.ok) {
            const err = await response.json().catch(() => ({}));
            throw new Error(err.message || "Failed to update user details");
        }
        return await response;
    } catch(error) {
        console.error("Error updating user details: ", error);
        alert("Failed to update user details.");
        throw error;
    }
}

function openUserEditModal(userDetails) {
        const modal = document.getElementById("user-modal");
        const saveBtn = document.getElementById("save-btn");

        const firstName = document.getElementById("modal-first-name");
        const lastName = document.getElementById("modal-last-name");
        const phoneNumber = document.getElementById("modal-phone-number");
//        const Gender

        firstName.value = userDetails.firstName;
        lastName.value = userDetails.lastName;
        phoneNumber.value = userDetails.phoneNUmber;

        modal.style.display = "flex";

        saveBtn.onclick = async function(event) {
            event.stopPropagation();
            const latestUserDetails = {
                firstName: document.getElementById("modal-first-name").value.trim(),
                lastName: document.getElementById("modal-last-name").value.trim(),
                phoneNumber: document.getElementById("modal-phone-number").value.trim()
            }
            if(!latestUserDetails) return;
            await updateUserDetailsModal(latestUserDetails);
        }
    }

    async function updateUserDetailsModal(latestUserDetails = null) {
        try {
            if(latestUserDetails) {
                const response = await updateUserDetails(latestUserDetails);
                if(!response.ok) return;
            }
        } catch(e) {
            console.error("Modal update error", e);
            alert("Failed to update user details.");
        } finally {
            closeUserEditModal();
        }
    }

    function closeUserEditModal() {
        document.getElementById("user-modal").style.display = "none";
    }