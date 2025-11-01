document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector('#add-product-form');

    form.addEventListener("submit", async(e) => {
        e.preventDefault();
        let isValid = true;

        if(!form.checkValidity()) {
            isValid = false;
            alert("Please fill all required fields.");
            return;
        }
        if(form.price.value <= 0) {
            alert("Price must be positive");
            return;
        }
        if(!isValid) return;

        const formData = new FormData(form);
        try {
            const response = await fetch(form.action, {
                method: "POST",
                credentials: "include",
                body: formData
            });

            if(response.ok) {
                window.location.href = "/index";
            } else {
                console.error("Error submitting form");
            }
        } catch(error) {
            console.error(error);
            alert("Form submission failed. Try again");
        }
    });
});