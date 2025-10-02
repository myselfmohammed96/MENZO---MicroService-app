document.addEventListener("DOMContentLoaded", () => {

    const form = document.querySelector('.auth-form');

    form.addEventListener("submit", async(e) => {

        e.preventDefault();
        let isValid = true;

        const email = document.getElementById('email').value.trim();
        const password = document.getElementById('password').value.trim();

        const emailErrorMsg = document.getElementById('email-error-message');
        const passwordErrorMsg = document.getElementById('password-error-message');

        emailErrorMsg.textContent = "";
        passwordErrorMsg.textContent = "";

        console.log(email + " *** " + password);
        if(!email) { emailErrorMsg.textContent = "*Email required."; isValid = false; }
        if(!password) { passwordErrorMsg.textContent = "*Password required."; isValid = false; }

        if(!isValid) return;

//        if(isValid) {
//            form.submit();
//        }

        try {
            const response = await fetch(form.getAttribute("action"), {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    email: email,
                    password: password
                }),
                credentials: "include"
            });

            const data = await response.json().catch(() => ({}));

            if(response.ok) {
                window.location.href = "/index";
            } else if(response.status === 401) {
                console.log(response);
                console.log("Unauthorised... got it???");
                passwordErrorMsg.textContent = "Incorrect email or password.";
            } else {
                alert(data.error || "Login failed");
            }

        } catch (err) {
            console.error(err);
            alert("Something went wrong. Try again.");
        }

    });
});