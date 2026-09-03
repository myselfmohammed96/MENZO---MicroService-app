import { showToast } from "/js/global/toast.js";

let form;


/* ******* Fetch APIs ******* */

/*
*
*   Check email existence
*
*/
async function checkEmailExistence(email) {
    try {
        const response = await fetch(checkEmailExistenceUrl, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email })
        });

        const data = await response.json().catch(() => ({}));

        if (!response.ok) {
            showToast("Unable to check email. Try again.", false);
        }

        return data.exists === true;
    } catch (error) {
        showToast("Unable to check email. Please try again.", false);
    }
}


/*
*
*   Post sign-in form data
*
*/
async function postFormData(formFields) {
    try {
        const response = await fetch(form.getAttribute("action"), {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                firstName: formFields.firstName,
                lastName: formFields.lastName,
                phoneNumber: formFields.phone,
                dateOfBirth: formFields.dob,
                email: formFields.email,
                gender: formFields.gender,
                password: formFields.password,
                confirmPassword: formFields.confirmPassword
            })
        });

        // const data = await response.json().catch(() => ({}));

        if (response.status === 201) {
            window.location.href = otpVerificationPage;
            //  ## need to store the redirect url (came from page) somewhere and should redirect after otp verification
        } else if (response.status >= 400 && response.status < 500) {
            showToast("Unable to register user. Try again.", false);
        } else if (response.status >= 500) {
            showToast("Server error. Try again later.", false);
        } else {
            showToast("Something went wrong. Try again.", false);
        }
    } catch (error) {
        showToast("Something went wrong. Please check your connection and try again.", false);
    }
}


/* ******* Data processing ******* */

/*
*
*   Sign-in form data processing
*
*/
async function processFormData() {
    let isValid = true;

    let formFields = {
        firstName: document.getElementById('first-name').value.trim(),
        lastName: document.getElementById('last-name').value.trim(),
        phone: document.getElementById('phone-number').value.trim(),
        dob: document.getElementById('dateOfBirth').value.trim(),
        email: document.getElementById('email').value.trim(),
        gender: document.getElementById('reg-form-gender').value.trim(),
        password: document.getElementById('password').value.trim(),
        confirmPassword: document.getElementById('confirm-password').value.trim()
    };

    let fieldErrorMsg = {
        firstName: document.getElementById('first-name-error-message'),
        lastName: document.getElementById('last-name-error-message'),
        phone: document.getElementById('phone-number-error-message'),
        dob: document.getElementById('dob-error-message'),
        email: document.getElementById('email-error-message'),
        gender: document.getElementById('gender-error-message'),
        password: document.getElementById('password-error-message'),
        confirmPassword: document.getElementById('confirm-password-error-message')
    };

    Object.values(fieldErrorMsg).forEach(el => el.textContent = "");

    // validation

    //  name
    if (!formFields.firstName || !formFields.lastName) {
        fieldErrorMsg.firstName.textContent = !formFields.firstName ? "*First name is required." : "";
        fieldErrorMsg.lastName.textContent = !formFields.lastName ? "*Last name is required." : "";
        isValid = false;
    }
    if (formFields.firstName.length > 50 || formFields.lastName.length > 50) {
        fieldErrorMsg.firstName.textContent = formFields.firstName.length > 50 ? "*First name must be at most 50 characters." : "";
        fieldErrorMsg.lastName.textContent = formFields.lastName.length > 50 ? "*Last name must be at most 50 characters." : "";
        isValid = false;
    }

    //  phone number
    if (!formFields.phone) {
        fieldErrorMsg.phone.textContent = "*Phone number is required.";
        isValid = false;
    }
    if (!/^(?:[6-9]\d{9}|(?:\+91|91|0)[6-9]\d{9})$/.test(formFields.phone)) {
        fieldErrorMsg.phone.textContent = "*Invalid phone number.";
    }

    //  date of birth
    if (!formFields.dob) {
        fieldErrorMsg.dob.textContent = "*Date of Birth is required.";
        isValid = false;
    }
    if (formFields.dob) {
        const dobDate = new Date(formFields.dob);
        const today = new Date();
        const minDate = new Date(today.getFullYear() - 5, today.getMonth(), today.getDate());
        if (dobDate > minDate) {
            fieldErrorMsg.dob.textContent = "*Please enter correct date of birth.";
            isValid = false;
        }
    }

    //  gender
    if (!formFields.gender) {
        fieldErrorMsg.gender.textContent = "*Gender required.";
        isValid = false;
    }

    //  email
    if (!formFields.email) {
        fieldErrorMsg.email.textContent = "*Email is required.";
        isValid = false;
    }
    if (formFields.email.length > 100) {
        fieldErrorMsg.email.textContent = "*Email must not exceed 100 characters.";
    }
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formFields.email)) {
        fieldErrorMsg.email.textContent = "*Invalid email address.";
    }
    if (formFields.email) {
        const emailExists = await checkEmailExistence(formFields.email);
        if (emailExists) {
            fieldErrorMsg.email.textContent = "*Email already exists.";
            isValid = false;
        }
    }

    //  password
    if (!formFields.password || !formFields.confirmPassword) {
        fieldErrorMsg.password.textContent = !formFields.password ? '*Password is required.' : "";
        fieldErrorMsg.confirmPassword.textContent = !formFields.confirmPassword ? "*Confirm password is required." : "";
        isValid = false;
    }
    if (formFields.password.length < 8 || formFields.password.length > 100) {
        fieldErrorMsg.password.textContent = "*Password must be 8-100 characters.";
        isValid = false;
    }
    if (formFields.password !== formFields.confirmPassword) {
        fieldErrorMsg.confirmPassword.textContent = "*Passwords do not match.";
        isValid = false;
    }

    if (!isValid) return;

    await postFormData(formFields);
}


/*
*
*   Dom loading event
*
*/
document.addEventListener("DOMContentLoaded", () => {

    form = document.querySelector('#reg-form');
    form.addEventListener("submit", async (e) => {
        e.preventDefault();
        await processFormData();
    });
});

