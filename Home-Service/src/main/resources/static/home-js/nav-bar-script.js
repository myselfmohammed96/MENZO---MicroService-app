//window.addEventListener("DOMContentLoaded", () => {
    const userProfileIcon = document.getElementById("user-profile-icon");
    const userIconDropDown = document.getElementById("user-dropdown");

    //  Toggle dropdown
    userProfileIcon.addEventListener("click", () => {
        userIconDropDown.classList.toggle("hidden");
    });

    // Hide dropdown on outside click
    document.addEventListener("click", (e) => {
        if (!userProfileIcon.contains(e.target)) {
            userIconDropDown.classList.add("hidden");
        }
    });

    function updateUserProfileUI(user) {
        const greeting = document.getElementById("profile-greeting");
        const label = document.getElementById("profile-label");

        userIconDropDown.innerHTML = ""; // Clear previous options

        if (!user) {
            greeting.textContent = "Sign in";
            label.textContent = "Account";
            userIconDropDown.innerHTML = `
                <li><a href="/sign-in">Create an account</a></li>
                <li><a href="/login">Login</a></li>
            `;
        } else {
            greeting.textContent = `Hi, ${user.firstName}`;
            label.textContent = "My Profile";

            if (user.roles === "ADMIN") {
                userIconDropDown.innerHTML += `<li><a href="http://localhost:8080/admin/dashboard">Admin Panel</a></li>`;
            }

            userIconDropDown.innerHTML += `
                <li><a href="">My Profile</a></li>
                <li><a href="/logout">Logout</a></li>
            `;
        }
    }
//});



