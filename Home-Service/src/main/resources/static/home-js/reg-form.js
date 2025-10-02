

// ********* Sign-in form - validation *********

const emailExistenceCheckUrl = "http://localhost:8080/user/is-exists";

async function checkEmailExistence(email) {
    try {
        const response = await fetch(emailExistenceCheckUrl, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email: email })
        });
        if(!response.ok) {
            const err = await response.json().catch(() => ({}));
            throw new Error(err.message || "Failed to check email existence");
        }
        return await response.json();
    } catch(error) {
        console.error("Existence check error: ", error);
        throw error;
    }
}

document.addEventListener("DOMContentLoaded", () => {

    const form = document.querySelector('.reg-form');

    form.addEventListener("submit", async(e) => {

        e.preventDefault();
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

        if(!formFields.firstName) { fieldErrorMsg.firstName.textContent = "*First name required."; isValid = false; }
        if(!formFields.lastName) { fieldErrorMsg.lastName.textContent = "*Last name required."; isValid = false; }
        if(!formFields.phone) { fieldErrorMsg.phone.textContent = "*Phone number required."; isValid = false; }
        if(!formFields.dob) { fieldErrorMsg.dob.textContent = "*Date of Birth required."; isValid = false; }
        if(!formFields.email) { fieldErrorMsg.email.textContent = "*Email required."; isValid = false; }
        if(!formFields.gender) { fieldErrorMsg.gender.textContent = "*Gender required."; isValid = false; }
        if(!formFields.password) { fieldErrorMsg.password.textContent = "*Password required."; isValid = false; }
        if(!formFields.confirmPassword) { fieldErrorMsg.confirmPassword.textContent = "*Confirm your password."; isValid = false; }

        if(formFields.dob) {
            const dobDate = new Date(formFields.dob);
            const today = new Date();
            const minDate = new Date(today.getFullYear()-5, today.getMonth(), today.getDate());
            if(dobDate > minDate) {
                fieldErrorMsg.dob.textContent = "*Enter correct date of birth.";
                isValid = false;
            }
        }

        if(formFields.password && formFields.password.length < 8) {
            fieldErrorMsg.password.textContent = "*Password must be at least 8 characters long.";
            isValid = false;
        }

        if(formFields.password !== formFields.confirmPassword) {
            fieldErrorMsg.confirmPassword.textContent = "*Passwords do not match. Please re-enter."
            isValid = false;
        }

        // if (formFields.email) {
        //     const exists = await fetch(`/check-email?email=${encodeURIComponent(formFields.email)}`)
        //                         .then(res => res.json());
        //     if(exists.taken) {
        //         fieldErrorMsg.email.textContent = "Email already exists";
        //         isValid = false;
        //     }
        // }

        if(formFields.email) {
            // const emailExists = await checkEmailExistence(formFields.email);
            // if(emailExists.exists) {
            //     fieldErrorMsg.email.textContent = "Email already exists.";
            //     isValid = false;
            // }
        }

        if(!isValid) return;
//        if(isValid) {
//            form.submit();
//        }

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
                }),
                credentials: "include"
            });

            const data = await response.json().catch(() => ({}));

            if(response.status === 201) {
                window.location.href = "/index";
//            } else if(response.status === 401) {
//                console.log(response);
//                console.log("Unauthorised... got it???");
//                passwordErrorMessage.textContent = "Incorrect email or password.";
            } else {
                console.error(response);
                alert(data.error || "Signin failed");
            }
        } catch (err) {
            console.error(err);
            alert("Something went wrong. Try again later.");
        }
    });
});

// Profile picture - image cropping, preview and upload

let cropper;

        const uploadBox = document.getElementById("uploadBox");
        const uploadInput = document.getElementById("uploadImage");
        const uploadedPreview = document.getElementById("uploadedPreview");
        const plusSign = document.getElementById("upload-box-plus-sign");

        const cropperImageContainer = document.getElementById("modal-back-drop");

        uploadBox.addEventListener("click", () => uploadInput.click());     // Open file dialog when clicking the upload box
        uploadInput.addEventListener("change", handleFile);

        uploadBox.addEventListener("dragover", (e) => {         // Drag & Drop Feature
            e.preventDefault();
            uploadBox.style.borderColor = "#28a745";
        });

        uploadBox.addEventListener("dragleave", () => {
            uploadBox.style.borderColor = "#007bff";
        });

        uploadBox.addEventListener("drop", (e) => {
            e.preventDefault();
            uploadBox.style.borderColor = "#007bff";
            let file = e.dataTransfer.files[0];
            processFile(file);
        });

        function handleFile(event) {
            let file = event.target.files[0];
            processFile(file);
        }

        function processFile(file) {
            if (file) {
                let reader = new FileReader();
                reader.onload = function(e) {

                    // Show the cropping area
                    const image = document.getElementById("previewImage-img");
                    image.src = e.target.result;
                    cropperImageContainer.style.display = "block";
                    // cropButton.style.display = "inline-block";

                    if (cropper) cropper.destroy();
                    cropper = new Cropper(image, { aspectRatio: 9 / 9});
                };
                reader.readAsDataURL(file);
            }
        }

        // crop button clicking event

        cropButton.addEventListener("click", function() {

            let croppedCanvas = cropper.getCroppedCanvas();     // Get the cropped canvas

            uploadedPreview.src = croppedCanvas.toDataURL();
            uploadedPreview.style.display = "block";
            plusSign.style.display = "none";
            cropperImageContainer.style.display = "none";
        });

        // Close modal with the 'X' button

        document.getElementById("close-modal").addEventListener("click", function(){
            cropperImageContainer.style.display = "none";
        });




        // ********* Sign-in form - validation *********

//        const emailExistenceCheckUrl = "http://localhost:8080/user/is-exists";
//
//        async function checkEmailExistence(email) {
//            try {
//                const response = await fetch(emailExistenceCheckUrl, {
//                    method: "POST",
//                    headers: { "Content-Type": "application/json" },
//                    body: JSON.stringify({ email: email })
//                });
//                if(!response.ok) {
//                    const err = await response.json().catch(() => ({}));
//                    throw new Error(err.message || "Failed to check email existence");
//                }
//                return await response.json();
//            } catch(error) {
//                console.error("Existence check error: ", error);
//                throw error;
//            }
//        }

//        document.querySelector(".reg-form").addEventListener("submit", async function (e) {
//
//            const email = document.getElementById("email").value.trim();
//            const password = document.getElementById("password").value.trim();
//            const confirmPassword = document.getElementById("confirm-password").value.trim();
//
//
//            const emailExists = await checkEmailExistence(email);
//            if(emailExists.exists) {
//                console.log("Email already exists");
//            } else {
//                console.log("Email available");
//            }
//
//
//            if(password.length < 8) {
//                e.preventDefault();
//                let passwordErrorMessage = document.getElementById("password-match-error");
//                passwordErrorMessage.textContent = "*Password must be at least 8 characters long.";
//                passwordErrorMessage.style.display = "block";
//                return;
//            } else if(password !== confirmPassword){
//                e.preventDefault();
//                let passwordErrorMessage = document.getElementById("password-match-error");
//                passwordErrorMessage.textContent = "*Passwords do not match. Please re-enter.";
//                passwordErrorMessage.style.display = "block";
////                document.getElementById("password-match-error").style.display = "block";
//                return;
//            } else{
//                document.getElementById("password-match-error").style.display = "none";
//            }
//
//            if (cropper) {
//                e.preventDefault(); // Pause normal form submission
//                cropper.getCroppedCanvas().toBlob(function (blob) {
//                    const reader = new FileReader();            // Convert the blob to base64 string
//                    reader.onloadend = function () {
//                        document.getElementById("croppedImageInput").value = reader.result;         // Set base64 string as the value of the hidden input
//                        e.target.submit();          // Submit the form after injecting the image
//                    };
//                    reader.readAsDataURL(blob);         // This converts Blob to base64
//                });
//            }
//        });

