document.addEventListener("DOMContentLoaded", async () => {

    document.addEventListener("keydown", function(e) {
        if(e.key === "Escape") {
            const userModal = document.getElementById("user-modal");
            if(userModal && userModal.style.display === "flex") {
                closeUserEditModal();
            }
        }
    });

//    const gender = await genderEnum();
//    console.log(gender);
});

const genderEnum = async function() {
    try {
        const response = await fetch("/user/user-gender");
        if(response.ok) {
            return await response.json();
        } else {
            console.log("Error fetching gender enum.");
            return;
        }
    } catch (err) {
        console.error("Gender fetching failed", err);
        throw err;
    }
}

const userModal = document.getElementById('user-modal');
const modalBody = document.getElementById('modal-body');
const modalTitle = document.getElementById('modal-title');

window.openModal = async function(field, status = null) {
    const saveBtn = document.getElementById('save-btn');
    let value;
    let saveBtnNew;

    modalBody.innerHTML = '';

    switch (field) {
        case "name":
            modalTitle.textContent = "Edit Name";
            modalBody.innerHTML = `
                <div class="input-container">
                    <input type="text" id="modal-first-name" placeholder="First Name" />
                </div>
                <p id="first-name-error-message" class="error-message"></p>
                <div class="input-container">
                    <input type="text" id="modal-last-name" placeholder="Last Name" />
                </div>
                <p id="last-name-error-message" class="error-message"></p>
            `;
            saveBtn.replaceWith(saveBtn.cloneNode(true));
            saveBtnNew = document.getElementById('save-btn');
            saveBtnNew.addEventListener("click", () => {
                value = {
                    firstName: document.getElementById('modal-first-name').value,
                    lastName: document.getElementById('modal-last-name').value
                };

                const firstNameErrorMsg = document.getElementById('first-name-error-message');
                const lastNameErrorMsg = document.getElementById('last-name-error-message');
                firstNameErrorMsg.textContent = '';
                lastNameErrorMsg.textContent = '';

                if (!value.firstName || !value.lastName) {
                    firstNameErrorMsg.textContent = !value.firstName ? '*This field cannot be empty.' : "";
                    lastNameErrorMsg.textContent = !value.lastName ? '*This field cannot be empty.' : "";
                    return;
                }
                processData(field, status, value);

                closeUserEditModal();
            });
            break;

        case "phone":
            modalTitle.textContent = status === null ? "Add Phone number" : "Edit Phone number";
            modalBody.innerHTML = `
                <div class="input-container">
                    <input type="text" id="modal-phone-number" placeholder="Phone Number" />
                </div>
                <p id="phone-number-error-message" class="error-message"></p>
            `;
            saveBtn.replaceWith(saveBtn.cloneNode(true));
            saveBtnNew = document.getElementById('save-btn');
            saveBtnNew.addEventListener("click", () => {
                value = {
                    phoneNumber: document.getElementById('modal-phone-number').value
                };

                const phoneNumberErrorMsg = document.getElementById('phone-number-error-message');
                phoneNumberErrorMsg.textContent = '';

                if (!value.phoneNumber) {
                    phoneNumberErrorMsg.textContent = '*This field cannot be empty.';
                    return;
                }
                processData(field, status, value);

                closeUserEditModal();
            });
            break;

        case "dob":
            modalTitle.textContent = status === null ? "Add Date of Birth" : "Edit Date of Birth";
            modalBody.innerHTML = `
                <div class="input-container">
                    <input type="date" id="modal-dob" placeholder="Date of Birth" />
                </div>
                <p id="dob-error-message" class="error-message"></p>
            `;
            flatpickr('#modal-dob', {
                dateFormat: "d-m-Y",
                maxDate: "today"
            });

            saveBtn.replaceWith(saveBtn.cloneNode(true));
            saveBtnNew = document.getElementById('save-btn');
            saveBtnNew.addEventListener("click", () => {
                value = {
                    dateOfBirth: document.getElementById('modal-dob').value
                };

                const dobErrorMsg = document.getElementById('dob-error-message');
                dobErrorMsg.textContent = '';

                if (!value.dateOfBirth) {
                    dobErrorMsg.textContent = '*This field cannot be empty.';
                    return;
                }

                if (value.dateOfBirth) {
                    const dobDate = new Date(value.dateOfBirth);
                    const today = new Date();
                    const minDate = new Date(today.getFullYear()-5, today.getMonth(), today.getDate());
                    if(dobDate > minDate) {
                        dobErrorMsg.textContent = '*Please enter correct date of birth.';
                        return;
                    }
                }
                processData(field, status, value);

                closeUserEditModal();
            });
            break;

        case "gender":
            const gender = await genderEnum();
            modalTitle.textContent = status === null ? "Add Gender" : "Edit Gender";
            modalBody.innerHTML = `
                <div class="input-container">
                    <select id="modal-gender">
                        <option value="" disabled selected hidden>Gender</option>
                    </select>
                </div>
                <p id="gender-error-message" class="error-message"></p>
            `;
            const genderSelect = document.getElementById('modal-gender');
            gender.forEach(g => {
                const genderOption = document.createElement('option');
                genderOption.value = g;
                genderOption.textContent = g.charAt(0).toUpperCase() + g.substring(1).toLowerCase();
                genderSelect.appendChild(genderOption);
            });

            saveBtn.replaceWith(saveBtn.cloneNode(true));
            saveBtnNew = document.getElementById('save-btn');
            saveBtnNew.addEventListener("click", () => {
                value = {
                    gender: document.getElementById('modal-gender').value
                };

                const genderErrorMsg = document.getElementById('gender-error-message');
                genderErrorMsg.textContent = '';

                if (!value.gender) {
                    genderErrorMsg.textContent = '*This field cannot be empty.';
                    return;
                }
                processData(field, status, value);

                closeUserEditModal();
            });
            break;

        case "password":
            modalTitle.textContent = status === false ? "Add Password" : "Edit Password";
            if (status) {
                modalBody.innerHTML = `
                    <div class="input-container">
                        <input type="password" id="current-password" placeholder="Current Password" />
                    </div>
                    <p id="current-password-error-message" class="error-message"></p>
                    <div class="input-container">
                        <input type="password" id="new-password" placeholder="New Password" />
                    </div>
                    <p id="new-password-error-message" class="error-message"></p>
                    <div class="input-container">
                        <input type="password" id="confirm-password" placeholder="Confirm New Password" />
                    </div>
                    <p id="confirm-password-error-message" class="error-message"></p>
                `;
                saveBtn.replaceWith(saveBtn.cloneNode(true));
                saveBtnNew = document.getElementById('save-btn');
                saveBtnNew.addEventListener('click', () => {
                    value = {
                        currentPassword: document.getElementById("current-password").value,
                        newPassword: document.getElementById('new-password').value,
                        confirmPassword: document.getElementById('confirm-password').value
                    };

                    const currentPasswordErrorMsg = document.getElementById('current-password-error-message');
                    const newPasswordErrorMsg = document.getElementById('new-password-error-message');
                    const confirmPasswordErrorMsg = document.getElementById('confirm-password-error-message');

                    currentPasswordErrorMsg.textContent = '';
                    newPasswordErrorMsg.textContent = '';
                    confirmPasswordErrorMsg.textContent = '';

                    if (!value.currentPassword || !value.newPassword || !value.confirmPassword) {
                        currentPasswordErrorMsg.textContent = !value.currentPassword ? '*This field cannot be empty.' : '';
                        newPasswordErrorMsg.textContent = !value.newPassword ? '*This field cannot be empty.' : ''
                        confirmPasswordErrorMsg.textContent = !value.confirmPassword ? '*This field cannot be empty.' : '';
                        return;
                    }

                    if (value.currentPassword === value.newPassword) {
                        newPasswordErrorMsg.textContent = "*Entering the current password.";
                        return;
                    }

                    if (value.newPassword.length < 8) {
                        newPasswordErrorMsg.textContent = "*Password must be at least 8 characters long.";
                        return;
                    }

                    if (value.newPassword !== value.confirmPassword) {
                        confirmPasswordErrorMsg.textContent = "*Passwords do not match. Try again.";
                        return;
                    }
                    processData(field, status, value);

                    closeUserEditModal();
                });
            } else {
                modalBody.innerHTML = `
                    <div class="input-container">
                        <input type="password" id="new-password" placeholder="New Password" />
                    </div>
                    <p id="new-password-error-message" class="error-message"></p>
                    <div class="input-container">
                        <input type="password" id="confirm-password" placeholder="Confirm New Password" />
                    </div>
                    <p id="confirm-password-error-message" class="error-message"></p>
                `;
                saveBtn.replaceWith(saveBtn.cloneNode(true));
                saveBtnNew = document.getElementById('save-btn');
                saveBtnNew.addEventListener('click', () => {
                    value = {
                        newPassword: document.getElementById('new-password').value,
                        confirmPassword: document.getElementById('confirm-password').value
                    };

                    const newPasswordErrorMsg = document.getElementById('new-password-error-message');
                    const confirmPasswordErrorMsg = document.getElementById('confirm-password-error-message');
                    newPasswordErrorMsg.textContent = '';
                    confirmPasswordErrorMsg.textContent = '';

                    if (!value.newPassword || !value.confirmPassword) {
                        newPasswordErrorMsg.textContent = !value.newPassword ? '*This field cannot be empty.' : "";
                        confirmPasswordErrorMsg.textContent = !value.confirmPassword ? '*This field cannot be empty.' : "";
                        return;
                    }

                    if (value.newPassword.length < 8) {
                        newPasswordErrorMsg.textContent = "*Password must be at least 8 characters long.";
                        return;
                    }

                    if (value.newPassword !== value.confirmPassword) {
                        confirmPasswordErrorMsg.textContent = "*Passwords do not match. Try again.";
                        return;
                    }
                    processData(field, status, value);

                    closeUserEditModal();
                });
            }
            break;
    }
    userModal.style.display = "flex";
}

async function updateUser(value) {
    try {
        const response = await fetch("http://localhost:8080/user/update-user", {
            method: "PUT",
            credentials: "include",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(value)
        });
        if(response.ok) {
            console.log("yes");
            return await response.json();
        } else {
            console.error("no");
            return;
        }
    } catch (err) {
        console.error("User update failed", err);
        throw err;
    }
}

async function changePassword(value, status) {
    try {
        const response = await fetch(`http://localhost:8080/user/update-password?present=${status}`, {
            method: "PUT",
            credentials: "include",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(value)
        });
        if(response.ok) {
            console.log("password changed");
            return await response.json();
        } else if(response.status === 401) {
            console.error("password mismatch");
            return;
        } else {
            console.error("Error changing password.");
            return;
        }
    } catch (err) {
        console.error("Password update failed", err);
        throw err;
    }
}


async function processData(field, status, value) {
    console.log(field + " " + status);
    console.log(value);

    let response;
    switch(field) {
        case 'name':
            response = await updateUser(value);
            console.log(response);
            console.log("hello");
            break;
        case 'phone':
            response = await updateUser(value);
            console.log(response);
            console.log("phone is here");
            break;
        case 'dob':
            response = await updateUser(value);
            console.log(response);
            console.log("dob here");
            break;
        case 'gender':
            response = await updateUser(value);
            console.log(response);
            console.log("gender done")
            break;
        case 'password':
            response = await changePassword(value, status);
            console.log("password update.");
            break;
    }
}

































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

//function openUserEditModal(userDetails) {
//        const modal = document.getElementById("user-modal");
//        const saveBtn = document.getElementById("save-btn");
//
//        const firstName = document.getElementById("modal-first-name");
//        const lastName = document.getElementById("modal-last-name");
//        const phoneNumber = document.getElementById("modal-phone-number");
////        const Gender
//
//        firstName.value = userDetails.firstName;
//        lastName.value = userDetails.lastName;
//        phoneNumber.value = userDetails.phoneNUmber;
//
//        modal.style.display = "flex";
//
//        saveBtn.onclick = async function(event) {
//            event.stopPropagation();
//            const latestUserDetails = {
//                firstName: document.getElementById("modal-first-name").value.trim(),
//                lastName: document.getElementById("modal-last-name").value.trim(),
//                phoneNumber: document.getElementById("modal-phone-number").value.trim()
//            }
//            if(!latestUserDetails) return;
//            await updateUserDetailsModal(latestUserDetails);
//        }
//    }

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