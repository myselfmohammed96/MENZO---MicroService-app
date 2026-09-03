window.addEventListener("DOMContentLoaded", () => {
    const userProfileIcon = document.getElementById("user-profile-icon");
    const userIconDropDown = document.getElementById("user-dropdown");
    const bottomNavBar = document.querySelector("#bottom-nav-bar ul");

    const user = window.user;

    //  Toggle dropdown
    userProfileIcon.addEventListener("click", (e) => {
//        ************
//        e.stopPropagation();
        userIconDropDown.classList.toggle("hidden");
    });

    document.addEventListener("click", (e) => {
        if(!userProfileIcon.contains(e.target) && !userIconDropDown.contains(e.target)) {
            setTimeout(() => userIconDropDown.classList.add("hidden"), 100);
        }
    });

    function updateUserProfileUI(user) {
        const greeting = document.getElementById("profile-greeting");
        const label = document.getElementById("profile-label");

        userIconDropDown.innerHTML = "";

        console.log(user);
        if (!user) {
            greeting.textContent = "Sign in";
            label.textContent = "Account";
            userIconDropDown.innerHTML = `
                <li><a href="http://localhost:8080/sign-in">Create an account</a></li>
                <li><a href="http://localhost:8080/login">Login</a></li>
            `;
        } else {
            greeting.textContent = `Hi, ${user.firstName.split(" ")[0]}`;
            label.textContent = "My Profile";

            if (user.roles === "ADMIN") {
                userIconDropDown.innerHTML += `<li><a href="http://localhost:8080/admin/dashboard">Admin Panel</a></li>`;
            }

            userIconDropDown.innerHTML += `
                <li><a href="http://localhost:8080/user/profile">My Profile</a></li>
                <li><a href="/logout">Logout</a></li>
            `;
        }
    }

    async function getCategories() {
        try {
            const response = await fetch("http://localhost:8080/categories/get-all-parents");
            if(!response.ok) throw new Error("Failed to load categories");
            return await response.json();
        } catch (error) {
            console.error("Load Error: ", error);
//            alert("Unable to load categories.");
            return [];
        }
    }

    async function populateCategories(maxVisible = 9) {
        const categories = await getCategories();
//        bottomNavBar.id = "bottom-nav-options-container";
        bottomNavBar.innerHTML = "";

        if(categories.length <= maxVisible) {
            categories.slice(0, maxVisible).forEach(category => {
                const li = document.createElement("li");
                li.classList.add("bottom-nav-options");
                li.innerHTML = `<a href="/category/${category.id}">${category.categoryName}</a>`;
                bottomNavBar.appendChild(li);
            });
        } else {
            categories.slice(0, maxVisible - 1).forEach(category => {
                const li = document.createElement("li");
                li.classList.add("bottom-nav-options");
                li.innerHTML = `<a href="/category/${category.id}">${category.categoryName}</a>`;
                bottomNavBar.appendChild(li);
            });

            const moreLi = document.createElement("li");
            moreLi.classList.add("bottom-nav-options");
            moreLi.id = "more-option"
            moreLi.innerHTML = `
                <span>More ▾</span>
                <ul class="more-dropdown hidden"></ul>
            `;
            bottomNavBar.appendChild(moreLi);

            const moreDropdown = moreLi.querySelector(".more-dropdown");
            categories.slice(maxVisible - 1).forEach(category => {
                const li = document.createElement("li");
                li.innerHTML = `<a href="/category/${category.id}">${category.categoryName}</a>`;
                moreDropdown.appendChild(li);
            });

            moreLi.addEventListener("click", (e) => {
//            ***************
                if(e.target.tagName.toLowerCase() === "a") return;
                e.stopPropagation();
                moreDropdown.classList.toggle("hidden");
            });

            document.addEventListener("click", (e) => {
                if(!moreLi.contains(e.target)) {
                    moreDropdown.classList.add("hidden");
                }
            });
        }
    }

    updateUserProfileUI(user);
    populateCategories();

});



