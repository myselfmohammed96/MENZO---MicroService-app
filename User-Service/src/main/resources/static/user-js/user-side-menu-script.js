const getCategoriesWithSubCategoriesUrl = "http://localhost:8080/categories/get-all";

document.addEventListener("DOMContentLoaded", () => {

    //    ********* Categories & Sub-categories population *********

    const sideNavCategories = document.getElementById('categories-menu-content');

    async function getCategoriesWithSubCategories() {
        try {
            const response = await fetch(getCategoriesWithSubCategoriesUrl);
            if(!response.ok) throw new Error("Failed to load Categories in side nav");
            return await response.json();
        } catch (error) {
            console.error("Categories load error: ", error);
            return [];
        }
    }

    async function populateSideNavCategories() {
        const categories = await getCategoriesWithSubCategories();

        sideNavCategories.innerHTML = '';

        categories.forEach(category => {
            const li = document.createElement('li');
            li.classList.add('category-item');

            const categoryTitleContainer = document.createElement('div');
            categoryTitleContainer.classList.add('category-item-title-container');

            const categoryTitle = document.createElement('span');
            categoryTitle.textContent = category.categoryName;

            categoryTitleContainer.appendChild(categoryTitle);

            const subCategoryList = document.createElement('ul');
            subCategoryList.classList.add('sub-category-menu', 'hidden');

            (category.subCategories || []).forEach(sub => {
                const subLi = document.createElement('li');
                subLi.classList.add('sub-category-item');

                const a = document.createElement('a');
                a.setAttribute('href', `/url?id=${sub.id}`);
                a.textContent = sub.categoryName;

                subLi.appendChild(a);
                subCategoryList.appendChild(subLi);
            });

            li.append(categoryTitleContainer, subCategoryList);
            sideNavCategories.appendChild(li);
        });

        // ********* Sub-categories menu toggle *********

        const categoryItems = document.querySelectorAll('.category-item');

        categoryItems.forEach(item => {
            const titleContainer = item.querySelector('.category-item-title-container');
            const subMenu = item.querySelector('.sub-category-menu');

            if(titleContainer && subMenu) {
                titleContainer.addEventListener('click', () => {

                    categoryItems.forEach(other => {
                        if(other !== item) {
                            other.querySelector('.sub-category-menu')?.classList.add('hidden');
                        }
                    });
                    const isHidden = subMenu.classList.contains('hidden');
                    subMenu.classList.toggle('hidden', !isHidden);
                });
            }
        });
    }

    populateSideNavCategories();


    //  ********* Main side nav accordion *******
    const categories = {
        option: document.getElementById('categories-menu-option'),
        content: document.getElementById('categories-menu-content'),
        arrow: document.getElementById('categories-option-arrow')
    }

    const profile = {
        option: document.getElementById('profile-menu-option'),
        content: document.getElementById('profile-menu-content'),
        arrow: document.getElementById('profile-option-arrow')
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