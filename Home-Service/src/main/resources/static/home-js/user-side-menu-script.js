document.addEventListener("DOMContentLoaded", () => {

// ********* Menu population *********

    const categoryMenu = document.getElementById('categories-menu-content');

    async function getCategories() {
        try {
            const response = await fetch("http://localhost:8080/categories/get-all");
            if(!response.ok) throw new Error("Failed to load categories for side menu population.");
            return await response.json();
        } catch (error) {
            console.error("Side menu load error: ", error);
            return [];
        }
    }

    async function populateCategories() {
        const categories = await getCategories();

        categoryMenu.innerHTML = "";

        if(categories) {
            categories.forEach(category => {
                const categoryLi = document.createElement("li");
                categoryLi.classList.add("category-item");

                const categoryTitle = document.createElement("div");
                categoryTitle.classList.add("category-item-title-container");
                categoryTitle.innerHTML = `
                    <a href="/category/${category.id}">${category.categoryName}</a>
                    <span class="menu-active-indicator">+</span>
                `;

                const subCategoryList = document.createElement("ul");
                subCategoryList.classList.add("sub-category-menu", "hidden");

                category.subCategories.forEach(subCategory => {
                    const subCategoryLi = document.createElement("li");
                    subCategoryLi.classList.add("sub-category-item");
                    subCategoryLi.innerHTML = `
                        <a href="/url/${subCategory.id}">${subCategory.categoryName}</a>
                    `;
                    subCategoryList.appendChild(subCategoryLi);
                });

                categoryLi.append(categoryTitle, subCategoryList);
                categoryMenu.appendChild(categoryLi);
            });

            // ********* Sub-categories menu toggle *********
            const categoryItems = document.querySelectorAll('.category-item');

            categoryItems.forEach(item => {
                const titleContainer = item.querySelector('.category-item-title-container');
                const subMenu = item.querySelector('.sub-category-menu');
                const indicator = item.querySelector('.menu-active-indicator');

                if(titleContainer && subMenu && indicator) {
                    indicator.addEventListener('click', (e) => {
                        categoryItems.forEach(other => {
                            if(other !== item) {
                                other.querySelector('.sub-category-menu')?.classList.add('hidden');
                                const otherIndicator = other.querySelector('.menu-active-indicator');
                                if(otherIndicator) otherIndicator.textContent = '+';
                            }
                        });
                        const isHidden = subMenu.classList.contains('hidden');
                        subMenu.classList.toggle('hidden', !isHidden);
                        indicator.textContent = isHidden ? '-' : '+';
                    });
                }
            });
        }


    }

    populateCategories();

// ********* Menu elements toggle *********
    //  ****** Categories menu elements *******
    const categories = {
        option: document.getElementById('categories-menu-option'),
        content: document.getElementById('categories-menu-content'),
        arrow: document.getElementById('categories-option-arrow'),
        link: document.getElementById('categories-link')
    }

    //  ******* Profile menu elements *******
    const profile = {
        option: document.getElementById('profile-menu-option'),
        content: document.getElementById('profile-menu-content'),
        arrow: document.getElementById('profile-option-arrow'),
        link: document.getElementById('profile-link')
    }

    //  Heading link - click event management
    categories.link.addEventListener("click", (e) => {
        e.stopPropagation();
    });

    profile.link.onclick = (e) => {
        e.preventDefault();
    }

    //  accordion toggle
    function closeAll() {
        categories.content.classList.remove('display');
        categories.arrow.classList.remove('flip');
        profile.content.classList.remove('display');
        profile.arrow.classList.remove('flip');
    }

    categories.option.addEventListener('click', () => {
        const isOpen = categories.content.classList.contains('display');
        closeAll();
        if (!isOpen) {
            categories.content.classList.add('display');
            categories.arrow.classList.add('flip');
        }
    });

    profile.option.addEventListener('click', () => {
        const isOpen = profile.content.classList.contains('display');
        closeAll();
        if (!isOpen) {
            profile.content.classList.add('display');
            profile.arrow.classList.add('flip');
        }
    });

});


