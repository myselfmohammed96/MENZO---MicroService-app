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

        // Password and confirm password - validation

        document.querySelector(".reg-form").addEventListener("submit", function (e) {
            const password = document.getElementById("password").value;
            const confirmPassword = document.getElementById("confirm-password").value;

            if(password !== confirmPassword){
                e.preventDefault();
                document.getElementById("password-match-error").style.display = "block";
                return;
            }else{
                document.getElementById("password-match-error").style.display = "none";
            }

            if (cropper) {
                e.preventDefault(); // Pause normal form submission
                cropper.getCroppedCanvas().toBlob(function (blob) {
                    const reader = new FileReader();            // Convert the blob to base64 string
                    reader.onloadend = function () {
                        document.getElementById("croppedImageInput").value = reader.result;         // Set base64 string as the value of the hidden input
                        e.target.submit();          // Submit the form after injecting the image
                    };
                    reader.readAsDataURL(blob);         // This converts Blob to base64
                });
            }
        });

