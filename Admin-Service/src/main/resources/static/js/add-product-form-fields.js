const getAllCategories = "http://localhost:8080/categories/get-all-parents";
const getAllSub = "http://localhost:8080/categories/get-all-sub";

document.addEventListener('DOMContentLoaded', async () => {
    const categorySelect = document.getElementById('category-select');
    const subCategorySelect = document.getElementById('sub-category-select');
//    const variationsFieldSet = document.querySelector('#variations-fieldset');
    let subCategoryChoices;

    //  Parent Categories in select
    async function loadCategoriesInOptions() {
        try{
            const response = await fetch(getAllCategories, {
                method: "GET",
                credentials: "include"
            });
            if(!response.ok) {
                throw new Error(`HTTP error ${response.status}`);
            }
            const categories = await response.json();
            if (!Array.isArray(categories)) {
                throw new Error("Invalid data format: categories should be array");
            }

            categories.forEach(c => {
                if (c && c.id !== undefined && c.categoryName) {
                    addCategorySelectOptions(c.id, c.categoryName);
                } else {
                    console.warn("Skipping invalid categories: ", c);
                }
            });

            initializeCategoriesChoices();
        } catch (error) {
            console.error("Load Error: ",error);
            alert("Unable to load categories. Please try again.");
        }
    }

    function addCategorySelectOptions(id, categoryName) {
        try {
            const categoryOption = document.createElement('option');
            categoryOption.value = id;
            categoryOption.textContent = categoryName;
            categorySelect.appendChild(categoryOption);
        } catch (error) {
            console.error("Category Option Add Error: ", error);
        }
    }

    function initializeCategoriesChoices() {
        try {
            new Choices('#category-select', {
                placeholder: true,
                placeholderValue: 'Category',
                searchEnabled: true,
                searchPlaceholderValue: 'Search category...',
                shouldSort: true
            });
        } catch (error) {
            console.error("Choices Initialization Error: ", error);
            alert("Unable to initialize categories selector.");
        }
    }

    async function loadSubCategoriesInOptions(categoryId) {
        try {
            const response = await fetch(`${getAllSub}?id=${categoryId}`, {
                method: "GET",
                credentials: "include"
            });
            if(!response.ok) {
                throw new Error(`HTTP error ${response.status}`);
            }
            const subCategories = await response.json();

            if (!Array.isArray(subCategories)) {
                throw new Error("Invalid subcategory format");
            }
            subCategories.forEach(sub => {
                if(sub && sub.id !== undefined && sub.categoryName) {
                    addSubCategorySelectOptions(sub.id, sub.categoryName);
                } else {
                    console.warn("Skipping invalid sub categories: ", sub)
                }
            });
            subCategoryChoices.setChoices([...subCategorySelect.options], 'value', 'text', true);
//            subCategoryChoices.enable();
        } catch (error) {
            console.error("Sub-category Load Error: ", error);
            alert("Unable to load sub-categories. Please try again.");
        }
    }

    function addSubCategorySelectOptions(id, subCategoryName) {
        try {
            const subOption = document.createElement('option');
            subOption.value = id;
            subOption.textContent = subCategoryName;
            subCategorySelect.appendChild(subOption)
        } catch (error) {
            console.error("Sub Category Option Add Error: ", error);
        }
    }

    function initializeSubCategoriesChoices() {
        try {
            subCategorySelect.removeAttribute('disabled');
            document.querySelector('#sub-category-select').parentElement.classList.remove('select-disabled');

            subCategoryChoices = new Choices('#sub-category-select', {
                placeholder: true,
                placeholderValue: 'Sub-category',
                searchEnabled: true,
                searchPlaceholderValue: 'Search sub-category...',
                shouldSort: true
            });

        } catch (error) {
            console.error("Sub Category Choices Initialization Error: ", error);
            alert("Unable to initialize sub categories selector.");
        }
    }

    //  Category & Sub-category - Event listener
    categorySelect.addEventListener('change',async () => {
        const categoryId = categorySelect.value;

        subCategorySelect.innerHTML = '';
//        variationsFieldSet.innerHTML = '';
        if(subCategoryChoices) {
            subCategoryChoices.removeActiveItems();
            subCategoryChoices.clearChoices();
            subCategoryChoices.disable();
        }
        if (!categoryId) return;

        await loadSubCategoriesInOptions(categoryId);

        subCategoryChoices.enable();    //
    });
    initializeSubCategoriesChoices();
    subCategoryChoices.disable();

    await loadCategoriesInOptions();

    //  Image adding - FilePond
    FilePond.registerPlugin(
        FilePondPluginImagePreview,
        FilePondPluginImageValidateSize,
        FilePondPluginFileValidateType,
        FilePondPluginImageCrop
    );

    const pond = FilePond.create(document.querySelector('#product-images'), {
        allowMultiple: true,
        minFiles: 1,
        maxFiles: 3,
        acceptedFileTypes: ['image/*'],
        imageValidateSizeMinWidth: 300,
        imageValidateSizeMinHeight: 300,
        imagePreviewHeight: 150,
//        imageCropAspectRatio: '1:1',
//        stylePanelAspectRatio: 1,
        stylePanelLayout: 'compact',
        instantUpload: false,
        storeAsFile: true,
        labelIdle: 'Drag & Drop or <span class="filepond--label-action">Browse</span> to upload',
        credits: false
    });

    pond.on('addfile', updateImageStats);
    pond.on('removefile', updateImageStats);

    function updateImageStats() {
        const files = pond.getFiles();
        const count = files.length;
        const totalSizeMB = (files.reduce((sum, f) => sum + f.file.size, 0) / (1024 * 1024)).toFixed(2);
        document.getElementById('image-stats').innerText = `Images: ${count} | Total Size: ${totalSizeMB} MB`;
    }

});




//const getAllVariations = "http://localhost:8080/variations/get-variations";
//const productPartialSearch = "http://localhost:8080/products/partial-search";
//const getProductById = "http://localhost:8080/products/get-by-id";

    //  === Product Name Autocomplete ===
//
//    const productNameInput = document.querySelector('input[name="productName"]');
//    let suggestionBox;
//
//    //  Partial suggestionBox for productName field
//    productNameInput.addEventListener("input", async () => {
//        console.log("Typing: ", productNameInput.value);
//        const query = productNameInput.value.trim();
//        if (query.length < 2) return removeSuggestionBox();
//
//        const response = await fetch(`${productPartialSearch}?name=${encodeURIComponent(query)}`);
//        const suggestions = await response.json();
//        showSuggestions(suggestions);
//    });
//
////    -------
//
//    productNameInput.addEventListener("blur", () => {
//        setTimeout(() => removeSuggestionBox(), 200);
//    });
//
////    -------
//
//    function showSuggestions(products) {
//        removeSuggestionBox();
//        suggestionBox = document.createElement("ul");
//        suggestionBox.className = "suggestion-box";
////        const suggestionBox = document.getElementById("suggestions");
////        suggestionBox.innerHTML = "";
//
//        products.forEach(product => {
//            const li = document.createElement("li");
//            li.textContent = product.productName;
//            li.dataset.id = product.id;
//
//            li.addEventListener("click", async () => {
//                const response = await fetch(`${getProductById}?id=${product.id}`);
//                const productDetails = await response.json();
//
//                fillProductForm(productDetails);
////                suggestionBox.innerHTML = "";
//                removeSuggestionBox(); //
//            });
//
//            suggestionBox.appendChild(li);
//        });
//
//        productNameInput.parentElement.style.position = "relative";
//        productNameInput.parentElement.appendChild(suggestionBox);
//    }
//
//    function removeSuggestionBox() {
//        if (suggestionBox) {
//            suggestionBox.remove();
//            suggestionBox = null;
//        }
//    }
//
//    function fillProductForm(productDetails) {
//        document.querySelector('select[name="categoryId"]').value = productDetails.categoryId;
//        document.querySelector('select[name="subCategoryId"]').value = productDetails.subCategoryId;
//        document.querySelector('textarea[name="description"]').value = productDetails.productDescription;
////        document.querySelector('input[name="status"][value="' + (productDetails.status || 'active') + '"]').checked = true;
////        document.querySelector('input[name="cod"][value="' + (productDetails.podAvailable ? 'available' : 'not_available') + '"]').checked = true;
//
//        document.querySelector(`input[name="status"][value="${productDetails.status}"]`).checked = true;
//        document.querySelector(`input[name="cod"][value="${productDetails.podAvailable ? 'available' : 'not_available'}"]`).checked = true;
//    }

//---------

//------------

//    async function loadVariations(subCategoryId) {
//        try {
//            const response = await fetch(`${getAllVariations}?id=${subCategoryId}`, {
//                method: "GET",
//                credentials: "include"
//            });
//            if (!response.ok) {
//                throw new Error(`HTTP error ${response.status}`);
//            }
//            const variations = await response.json();
//            if (!Array.isArray(variations)) {
//                throw new Error("Invalid variation format");
//            }
//
//            populateVariations(variations);
//        } catch (error) {
//            console.error("Variation Load Error: ", error);
//            alert("Unable to load variations. Please try again.");
//        }
//    }
//
//    function populateVariations(variations) {
//        variationsFieldSet.innerHTML = '<legend>Product Variations</legend>';
//
//        variations.forEach(variation => {
//            if(variation && variation.variationName && Array.isArray(variation.options)) {
//                const label = document.createElement('label');
//                label.classList.add("general-info-label");
//                label.textContent = variation.variationName + ':';
//
//                const select = document.createElement('select');
//                select.name = variation.variationName;
//                select.required = true;
//
//                const placeholderOption = document.createElement('option');
//                placeholderOption.value = '';
//                placeholderOption.textContent = `Select ${variation.variationName}`;
//                placeholderOption.disabled = true;
//                placeholderOption.selected = true;
//                select.appendChild(placeholderOption);
//
//                variation.options.forEach(option => {
//                    const optionElement = document.createElement('option');
//                    optionElement.value = option.id;
//                    optionElement.textContent = option.optionValue;
//                    select.appendChild(optionElement);
//                });
//                label.appendChild(select);
//                variationsFieldSet.appendChild(label);
//
//                new Choices(select, {
//                    placeholder: true,
//                    searchEnabled: true,
//                    searchPlaceholderValue: 'Search...',
//                    itemSelectText: '',
//                    shouldSort: true
//                });
//            } else {
//                console.warn("Skipping invalid variation: ", variation);
//            }
//        });
//    }

//-------------


//    subCategorySelect.addEventListener('change', async () => {
//        const subCategoryId = subCategorySelect.value;
//        variationsFieldSet.innerHTML = '';
//        if (!subCategoryId) return;
//        await loadVariations(subCategoryId);
//    });



//FilePond.setOptions({
//    server: {
//        process: {
//            url: '/products/upload-temp',
//            method: 'POST',
//            withCredentials: false,
//            headers: {},
//            timeout: 7000,
//            onload: response => response.key,
//            onerror: err => console.error(err)
//        },
//        revert: '/products/revert-temp'
//    }
//});