const getCategories = "";
const getSubCategories = "";
const getFilterOptions = "";
const getSortOptions = "";

let button;
let menu;
let container;

window.defaultSort;



/*
*   ---------------------------------
*   ********* FETCH methods *********
*   ---------------------------------
*/

//  FETCH - categories
async function fetchCategories() {
    try {
        const response = await fetch(getCategories, {
            method: "GET",
            credentials: "include"
        });
        if(!response.ok)  {
            throw new Error("Failed to fetch categories");
        }
        return response.json();
    } catch(error) {
        console.error("Error: ", error);
        return null;
    }
}

//  FETCH - sub-categories
async function fetchSubCategories() {
    try {
        const response = await fetch(getSubCategories, {
            method: "GET",
            credentials: "include"
        });
        if(!response.ok)  {
            throw new Error("Failed to fetch sub-categories");
        }
        return response.json();
    } catch(error) {
        console.error("Error: ", error);
        return null;
    }
}

//  FETCH - filter options
const fetchFilterOptions = async function() {
    try {
        const response = await fetch(getFilterOptions, {
            method: "GET",
            credentials: "include"
        });
        if(!response.ok)  {
            throw new Error("Failed to fetch filter options");
        }
        return response.json();
    } catch(error) {
        console.error("Error: ", error);
        return null;
    }
};

//  FETCH - sort options
async function fetchSortOptions() {
    try {
        const response = await fetch(getSortOptions, {
            method: "GET",
            credentials: "include"
        });
        if(!response.ok)  {
            throw new Error("Failed to fetch sort options" + response.status);
        }
        return response.json();
    } catch(error) {
        console.error("Error: ", error);
        return null;
    }
}



/*
*   ------------------------------------
*   ********* POPULATE methods *********
*   ------------------------------------
*/

//  POPULATE - categories
function populateCategories(categories = []) {
    try {
        container.category.innerHTML = '';

        const categorySection = document.createElement('div');
        categorySection.classList.add('category-section');

        const categoryTitle = document.createElement('p');
        categoryTitle.classList.add('category-title');
        categoryTitle.textContent = "Category";

        categorySection.appendChild(categoryTitle);

        categories.forEach(c => {
            const label = document.createElement("label");

            const input = document.createElement("input");
            input.type = "radio";
            input.name = "category";
            input.value = c.id;

            const span = document.createElement("span");
            span.classList.add("checkmark");

            label.appendChild(input);
            label.appendChild(span);
            label.append(` ${c.categoryName}`);

            categorySection.appendChild(label);
        });

        container.category.appendChild(categorySection);
    } catch (error) {
        console.error('Error populating category options: ', error);
    }
}

//  POPULATE - sub-categories
function populateSubCategories(subCategories = [], categoryName) {
    try {
        container.subCategory.innerHTML = '';

        container.subCategory.appendChild(document.createElement('hr')).classList.add("category-hr");

        const categorySection = document.createElement('div');
        categorySection.classList.add('category-section');

        const categoryTitle = document.createElement('p');
        categoryTitle.classList.add('category-title');
        categoryTitle.textContent = "Sub-category";

        categorySection.appendChild(categoryTitle);

        subCategories.forEach(sc => {
            const label = document.createElement("label");

            const input = document.createElement("input");
            input.type = "checkbox";
            input.name = "sub-cat";
            input.value = sc.id;

            const span = document.createElement("span");
            span.classList.add("checkmark");

            label.appendChild(input);
            label.appendChild(span);
            label.append(` ${sc.categoryName}`);

            categorySection.appendChild(label);
        });

        container.subCategory.appendChild(categorySection);
    } catch (error) {
        console.error('Error populating sub-category options: ', error);
    }
}

//  POPULATE - filter options
function populateFilterOptions(filters = []) {
    try {
        container.filter.innerHTML = '';

        let counter = 1;
        filters.forEach(f => {
            if (!Array.isArray(f.filterOptions)) {
                throw new Error("Invalid format: filter options should be array");
            }
            if (f.filterOptions.length === 0) {
                throw new Error(`Filter options not found for filter '${f.filterType}'.`);
            }

            const filterSection = document.createElement('div');
            filterSection.classList.add('filter-section');

            const filterTitle = document.createElement('p');
            filterTitle.classList.add('filter-title');
            filterTitle.textContent = f.filterType.replace(/_/g, " ");
            filterTitle.dataset.value = f.typeValue;

            filterSection.appendChild(filterTitle);

            f.filterOptions.forEach(opt => {
                const label = document.createElement("label");

                const input = document.createElement("input");
                input.type = "checkbox";
                input.name = f.typeValue.toLowerCase();
                input.value = opt;

                const span = document.createElement("span");
                span.classList.add("checkmark");

                label.appendChild(input);
                label.appendChild(span);
                label.append(` ${opt}`);

                filterSection.appendChild(label);
            });

            container.filter.appendChild(section);

            if (counter < filters.length) {
                container.filter.appendChild(document.createElement("hr")).classList.add("filter-hr");
                counter++;
            }
        });
    } catch (error) {
        console.error('Error populating filter options: ', error);
    }
}

//  POPULATE - sort options
function populateSortOptions(sortOptions = []) {
    try {
        container.sort.innerHTML = '';

        const sortMenu = document.createElement('ul');
        sortMenu.classList.add('sort-menu');

        let madeDefault = false;
        sortOptions.forEach(s => {
            const sortOption = document.createElement('li');
            sortOption.classList.add('sort-option');

            if (s.defaultSort) {
                if (!madeDefault) {
                    sortOption.classList.add('applied-sort');
                    defaultSort = {
                        text: s.text,
                        value: s.value
                    };
                    madeDefault = true;
                } else {
                    console.log('Default sort already made - ', defaultSort.text);
                }
            }

            sortOption.dataset.value = s.value;
            sortOption.textContent = s.text

            sortOption.addEventListener('click', () => {
                selectSort(s.value);    //  fn call

                document.querySelectorAll('.sort-option').forEach(o => {
                    if (o.classList.contains('applied-sort')) {
                        o.classList.remove('applied-sort');
                    }
                });
                sortOption.classList.add('applied-sort');
                menu.sort.classList.add('hidden');
            });

            sortMenu.appendChild(sortOption);
        });

        container.sort.appendChild(sortMenu);
    } catch (error) {
        console.error('Error populating sort options: ', error);
    }
}



/*
*   ------------------------------------
*   ********* ACTION - methods *********
*   ------------------------------------
*/

function activateMenu() {


    button.filter.addEventListener('click', () => {
        if (!menu.category.classList.contains('hidden')) {
            menu.category.classList.add('hidden');
        }
        if (!menu.sort.classList.contains('hidden')) {
            menu.sort.classList.add('hidden');
        }
        menu.filter.classList.toggle('hidden');
    });

    button.sort.addEventListener('click', () => {
        if (!menu.category.classList.contains('hidden')) {
            menu.category.classList.add('hidden');
        }
        if (!menu.filter.classList.contains('hidden')) {
            menu.filter.classList.add('hidden');
        }
        menu.sort.classList.toggle('hidden');
    });

    document.addEventListener('click', (e) => {
        if (!menu.category.contains(e.target) && !button.category.contains(e.target)) {
            menu.category.classList.add('hidden');
        }
        if (!menu.filter.contains(e.target) && !button.filter.contains(e.target)) {
            menu.filter.classList.add('hidden');
        }
        if (!menu.sort.contains(e.target) && !button.sort.contains(e.target)) {
            menu.sort.classList.add('hidden');
        }
    });
}



/*
*   ----------------------------------
*   ********* LOAD methods *********
*   ----------------------------------
*/

//  LOAD - categories menu
async function loadCategories() {
    try {
        const categories = await fetchCategories();
        if (!categories) {
            throw new Error('Categories not available.');
        }
        if (!Array.isArray(categories)) {
            throw new Error("Invalid format: categories should be array.");
        }
        if (categories.length === 0) {
            throw new Error("Categories not found.");
        }

        populateCategories(categories);
    } catch (error) {
        console.error('Error loading categories menu: ', error);
        return;
    }
}

//  LOAD - sub categories menu
async function loadSubCategories() {
    try {
        const subCategories = await fetchSubCategories();
        if (!subCategories) {
            throw new Error('Sub-categories not available.');
        }
        if (!Array.isArray(subCategories)) {
            throw new Error(`Invalid format: subCategories should be array for category '${categoryName}'.`);
        }
        if (subCategories.length === 0) {
            throw new Error(`Sub-categories not found for category '${categoryName}'.`);
        }

        populateSubCategories(subCategories);
    } catch (error) {
        console.error('Error loading sub-categories menu: ', error);
    }
}

//  LOAD - filter options menu
async function loadFilterOptions() {
    try {
        const filterOptions = await fetchFilterOptions();
        if (!filterOptions) {
            throw new Error('Filter options not available.');
        }
        if (!Array.isArray(filterOptions)) {
            throw new Error("Invalid format: filter options should be array.");
        }
        if (filterOptions.length === 0) {
            throw new Error("Filter options not found.");
        }

        populateFilterOptions(filterOptions);
    } catch (error) {
        console.error('Error loading filter options menu: ', error);
    }
}

//  LOAD - sort options menu
async function loadSortOptions() {
    try {
        const sortOptions = await fetchSortOptions();
        if (!sortOptions) {
            throw new Error('Sort options not available.');
        }
        if (!Array.isArray(sortOptions)) {
            throw new Error("Invalid format: sort options should be array.");
        }
        if (sortOptions.length === 0) {
            throw new Error("Sort options not found.");
        }

        populateSortOptions(sortOptions);
    } catch (error) {
        console.error('Error loading sort options menu: ', error);
    }
}

//  init Load
function initLoad() {
    try {
        button.category.addEventListener('click', async () => {
            await loadCategories();

            if (!menu.filter.classList.contains('hidden')) {
                menu.filter.classList.add('hidden');
            }
            if (!menu.sort.classList.contains('hidden')) {
                menu.sort.classList.add('hidden');
            }
            menu.category.classList.toggle('hidden');
        });

        button.filter.addEventListener('click', async () => {
            await loadFilterOptions();
        });

        button.sort.addEventListener('click', async () => {
            await loadSortOptions();
        });
    } catch (error) {
        console.error('Error at initial load: ', error);
    }
}


//  DOM Loading event
document.addEventListener('DOMContentLoaded', () => {
    button = {
        category: document.getElementById("category-button"),
        filter: document.getElementById("filter-button"),
        sort: document.getElementById("sort-button")
    };
    menu = {
        category: document.getElementById("category-panel"),
        filter: document.getElementById("filter-panel"),
        sort: document.getElementById("sort-panel")
    };
    container = {
        category: document.getElementById("category-container"),
        subCategory: document.getElementById("sub-category-container"),
        filter: document.getElementById("filter-container"),
        sort: document.getElementById("sort-container")
    };

    initLoad();

    activateMenu();
});














//function sortMenuAction() {
//    const sortOptions = document.querySelectorAll('.sort-option');
//
//    sortOptions.forEach(opt => {
//        opt.addEventListener('click', () => {
//            console.log(opt.textContent);
//            console.log(opt.dataset.value);
//
//            sortOptions.forEach(o => {
//                if (o.classList.contains('applied-sort')) {
//                    o.classList.remove('applied-sort');
//                }
//            });
//            opt.classList.add('applied-sort');
//            menu.sort.classList.add('hidden');
//        });
//    });
//}