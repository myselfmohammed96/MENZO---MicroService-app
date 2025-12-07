const getCategories = "http://localhost:8080/categories/get-all-parents";
const getSubCategories = "http://localhost:8080/categories/get-all-sub";
const getVariations = "http://localhost:8080/variations/get-variations";
const getColors = "http://localhost:8080/variations/colors";
const getSizes = "http://localhost:8080/variations/size";

let subCategoryChoices;
let colorChoices;

let variationsSpace;



//  ********* FETCH methods *********

//  FETCH - categories
async function fetchCategories() {
    try {
        const response = await fetch(getCategories, {
            method: "GET",
            credentials: "include"
        });
        if (!response.ok) {
            throw new Error("Error fetching categories: ", response.status);
        }
        return await response.json();
    } catch (error) {
        console.log("Fetching categories failed: ", error);
    }
}

//  FETCH - sub-categories
async function fetchSubCategories(categoryId) {
    try {
        const response = await fetch(`${getSubCategories}?id=${categoryId}`, {
            method: "GET",
            credentials: "include"
        });
        if (!response.ok) {
            throw new Error("Error fetching sub-categories: ", response.status);
        }
        return await response.json();
    } catch (error) {
        console.log("Fetching sub-categories failed: ", error);
    }
}

//  FETCH - variations
async function fetchVariations(subCategoryId) {
    try {
        const response = await fetch(`${getVariations}?id=${subCategoryId}`, {
            method: "GET",
            credentials: "include"
        });
        if (!response.ok) {
            throw new Error("Error fetching variations: ", response.status);
        }
        return await response.json();
    } catch (error) {
        console.log("Fetching variations failed: ", error);
    }
}

//  FETCH - colors
async function fetchColors() {
    try {
        const response = await fetch(getColors, {
            method: "GET",
            credentials: "include"
        });
        if (!response.ok) {
            throw new Error("Error fetching colors");
        }
        return await response.json();
    } catch (error) {
        console.error("Fetching colors failed: ", error);
    }
}

//  FETCH - sizes
async function fetchSizes() {
    try {
        const response = await fetch(getSizes, {
            method: "GET",
            credentials: "include"
        });
        if (!response.ok) {
            throw new Error("Error fetching sizes");
        }
        return await response.json();
    } catch (error) {
        console.error("Fetching sizes failed: ", error);
    }
}



//  ********* POPULATE - methods *********

//  POPULATE - categories
function populateCategories(categories = []) {
    try {
        if (categories.length === 0) {
            throw new Error("Invalid data: categories should not be empty");
        }
        const categorySelect = document.getElementById('category-select');
        categories.forEach(cat => {
            if (cat && cat.id !== undefined && cat.categoryName) {
                const categoryOption = document.createElement('option');
                categoryOption.value = cat.id;
                categoryOption.textContent = cat.categoryName;

                categorySelect.appendChild(categoryOption);
            } else {
                console.warn("Skipping invalid categories: ", cat);
            }
        });
        initializeCategoriesChoices();

        //  category select - event listener (to trigger sub-categories loading)
        categorySelect.addEventListener('change', async () => {
            const selectedCategoryId = categorySelect.value;
            if (!selectedCategoryId) return;
            await loadSubCategories(selectedCategoryId);        // ## is await needed here?

            variationsSpace.innerHTML = '';
        });
    } catch (error) {
        console.error("Category populate error: ", error);
    }
}

//  POPULATE - sub-categories
function populateSubCategories(subCategories = []) {
    try {
//        if (subCategories.length === 0) {
//            throw new Error("Invalid data: sub-categories should not be empty");
//        }
        const subCategorySelect = document.getElementById('sub-category-select');
        subCategorySelect.innerHTML = '';
        if(subCategoryChoices) {
            subCategoryChoices.removeActiveItems();
            subCategoryChoices.clearChoices();
            subCategoryChoices.disable();
        }

        subCategories.forEach(sub => {
            if (sub && sub.id !== undefined && sub.categoryName) {
                const subOption = document.createElement('option');
                subOption.value = sub.id;
                subOption.textContent = sub.categoryName;

                subCategorySelect.appendChild(subOption);
            } else {
                console.warn("Skipping invalid sub-categories: ", sub);
            }
        });
        subCategoryChoices.setChoices(
            [...subCategorySelect.options],
            'value',
            'text',
            true
        );
        subCategoryChoices.enable();

        //  sub-category select - event listener (to trigger variations loading)
        subCategorySelect.addEventListener('change', async () => {
            const selectedSubCategoryId = subCategorySelect.value;
            if (!selectedSubCategoryId) return;

            loadVariations(selectedSubCategoryId);
        });
    } catch (error) {
        console.error("Sub-category populate error: ", error);
    }
}

//  POPULATE - variations
function populateVariations(variations = []) {
    try {
        variationsSpace.innerHTML = '';

        if (variations.length === 0) {
            throw new Error("Invalid data: variations should not be empty");
        }
        const variationsFieldSet = document.createElement('fieldset');
        variationsFieldSet.id = "variations-fieldset";

        const legend = document.createElement('legend');
        legend.textContent = "Variations";

        variationsFieldSet.appendChild(legend);
        variationsSpace.appendChild(variationsFieldSet);

        variations.forEach(variation => {
            if (variation && variation.variationName && Array.isArray(variation.options)) {
                const label = document.createElement('label');
                label.classList.add("general-info-label");
                label.textContent = variation.variationName + ':';

                //  create select for variation
                const select = document.createElement('select');
                select.classList.add('custom-select', 'variation-select');
                select.name = `variation.${variation.variationName}`
                select.required = true;

                //  populating options
                variation.options.forEach(option => {
                    const optionElement = document.createElement('option');
                    optionElement.value = option.optionId;
                    optionElement.textContent = option.optionValue;
                    select.appendChild(optionElement);
                });
                label.appendChild(select);
                variationsFieldSet.appendChild(label);

                //  initialize Choices.js for variation select
                new Choices(select, {
                    placeholder: true,
                    placeholderValue: variation.variationName,
                    searchEnabled: true,
                    searchPlaceholderValue: 'Search...',
                    itemSelectText: '',
                    shouldSort: true
                });
            } else {
                console.warn("Skipping invalid variation: ", variation);
            }
        });
    } catch (error) {
        console.error("Variations populate error: ", error);
    }
}

//  POPULATE - colors
function populateColors(colorOptions) {
    const colorSelect = document.getElementById('color-select');
    colorSelect.innerHTML = '';

    if(!colorOptions || !Array.isArray(colorOptions)) return;

    colorOptions.forEach(opt => {
        const option = document.createElement('option');
        option.value = opt.optionId;
        option.textContent = opt.optionValue;
        option.setAttribute(
            "data-custom-properties",
            JSON.stringify({ hex: opt.colorCode })
        );
        colorSelect.appendChild(option);
    });

    document.getElementById('color-select')._choicesInstance = initializeColorsChoices();
}

//  POPULATE - sizes
function populateSizes(sizeOptions) {
    const sizeContainer = document.getElementById('size-container');
    const stockContainer = document.getElementById('stock-container');
    const priceContainer = document.getElementById('price-container');

    sizeContainer.innerHTML = '';
    stockContainer.innerHTML = '';
    priceContainer.innerHTML = '';

    sizeOptions.forEach(size => {

        //  ------- SIZE CHECKBOX -------
        const sizeElement = document.createElement('div');
        sizeElement.classList.add('size-element');

        const sizeOption = document.createElement('label');
        sizeOption.classList.add('size-option');

        const sizeInput = document.createElement('input');
        sizeInput.type = 'checkbox';
        sizeInput.name = 'sizes';
        sizeInput.setAttribute("data-id", size.optionId);

        const sizeSpan = document.createElement('span');
        sizeSpan.textContent = size.optionValue;

        sizeOption.append(sizeInput, sizeSpan);
        sizeElement.appendChild(sizeOption);
        sizeContainer.appendChild(sizeElement);

        //  ------- STOCK INPUT -------
        const stockElement = document.createElement('div');
        stockElement.classList.add('size-stock-element');

        const stockInput = document.createElement('input');
        stockInput.classList.add('text-input-3', 'size-option-text-input');
        stockInput.type = 'text';
        stockInput.placeholder = "Enter stock";
        stockInput.setAttribute('data-id', size.optionId);
        stockInput.disabled = true;

        stockElement.appendChild(stockInput);
        stockContainer.appendChild(stockElement);

        //  ------- PRICE INPUT -------
        const priceElement = document.createElement('div');
        priceElement.classList.add('size-price-element');

        const priceInput = document.createElement('input');
        priceInput.classList.add('text-input-3', 'size-option-text-input');
        priceInput.type = 'text';
        priceInput.placeholder = "Enter price";
        priceInput.setAttribute('data-id', size.optionId);
        priceInput.disabled = true;

        priceElement.appendChild(priceInput);
        priceContainer.appendChild(priceElement);

        //  ------- ENABLE / DISABLE ON CHECK -------
        sizeInput.addEventListener('change', () => {
            const isChecked = sizeInput.checked;
            stockInput.disabled = !isChecked;
            priceInput.disabled = !isChecked;
        });
    });
}



//  ********* CHOICES.JS INITIALIZATION *********

//  INITIALIZE - category select
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
        return;
    }
}

//  INITIALIZE - sub-category select
function initializeSubCategoriesChoices() {
    try {
//        subCategorySelect.removeAttribute('disabled');
//        document.querySelector('#sub-category-select').parentElement.classList.remove('select-disabled');

        const choices = new Choices('#sub-category-select', {
            placeholder: true,
            placeholderValue: 'Sub-category',
            searchEnabled: true,
            searchPlaceholderValue: 'Search sub-category...',
            shouldSort: true
        });
        return choices;
    } catch (error) {
        console.error("Sub-Category Choices Initialization Error: ", error);
        return;
    }
}

//  INITIALIZE - 'color' variation select
function initializeColorsChoices() {
    if (!colorChoices) {
        const choices = new Choices('#color-select', {
            callbackOnCreateTemplates: function(template) {
                return {
//                    item: (classNames, data) => {
//                        return template(`
//                            <div class="${classNames.item}">
//                                <span style="display:inline-block;width:12px;height:12px;background:${data.customProperties.hex};border-radius:3px;margin-right:6px;"></span>
//                                ${data.label}
//                            </div>
//                        `);
//                    },
                    choice: (classNames, data) => {
                        const hex = (data.customProperties && data.customProperties.hex) || 'transparent';
                        const trimmedLabel = data.label.length > 15 ? data.label.slice(0, 15) + "..." : data.label;
                        return template(`
                            <div
                                class="${classNames.item} ${classNames.choice} color-option"
                                data-select-text="${this.config.itemSelectText || ''}"
                                data-choice
                                data-id="${data.id}"
                                data-value="${data.value}"
                                title="${data.label}"
                            >
                                <span class="color-icon" style="background:${hex};"></span>
                                ${trimmedLabel}
                            </div>
                        `);
                    }
                };
            },
            removeItemButton: false,
            placeholder: true,
            placeholderValue: 'Color',
            searchEnabled: true,
            searchPlaceholderValue: 'Search color...',
            shouldSort: true,
            renderSelectedChoices: 'always'
        });
        colorChoices = choices;
        return choices;
    }
}



//  ********* Data Loader methods *********

//  LOADER - categories select
async function loadCategories() {
    try {
        const categories = await fetchCategories();
        if (!categories) {
            console.log("Categories not found");
            return;
        }
        if (!Array.isArray(categories)) {
            throw new Error("Invalid data format: categories should be array");
        }
        populateCategories(categories);
    } catch (error) {
        console.error("Error loading categories.", error);
    }
}

//  LOADER - sub-categories select
async function loadSubCategories(categoryId) {
    try {
        const subCategories = await fetchSubCategories(categoryId);
        if (!subCategories) {
            console.error("Sub-categories not found");
            return;
        }
        if (!Array.isArray(subCategories)) {
            throw new Error("Invalid data format: sub-categories should be array");
        }
        populateSubCategories(subCategories);
    } catch (error) {
        console.error("Error loading sub-categories.", error);
    }
}

//  LOADER - variations
async function loadVariations(subCategoryId) {
    try {
        const variations = await fetchVariations(subCategoryId);
        if (!variations) {
            console.error("Variations not found");
            return;
        }
        if (!Array.isArray(variations)) {
            throw new Error("Invalid data format: variations should be array");
        }
        populateVariations(variations);
    } catch (error) {
        console.error("Error loading variations.", error);
    }
}

//  LOADER - colors & sizes
async function loadColorAndSize() {

    //  load colors
    try {
        const colors = await fetchColors();
        if(!colors) {
            console.error("Colors not found");
            return;
        }
        if(!Array.isArray(colors.options)) {
            throw new Error("Invalid data format: color options should be array");
        }
        populateColors(colors.options);
    } catch (error) {
        console.error("Error loading colors.", error);
    }

    //  load sizes
    try {
        const sizes = await fetchSizes();
        if(!sizes) {
            console.error("Sizes not found");
            return;
        }
        if(!Array.isArray(sizes.options)) {
            throw new Error("Invalid data format: size options should be array");
        }
        populateSizes(sizes.options);
    } catch (error) {
        console.error("Error loading sizes.", error);
    }
}


//  DOM Loading event
document.addEventListener('DOMContentLoaded', async () => {

    //  ------- Load - initial data -------

    await loadCategories();
    await loadColorAndSize();

    variationsSpace = document.getElementById('variations-space');


    //  ------- Initialize - choices.js -------

    subCategoryChoices = initializeSubCategoriesChoices();
    subCategoryChoices.disable();

//    initializeColorsChoices();


    //  ------- Image adding - FilePond -------

    FilePond.registerPlugin(
        FilePondPluginImagePreview,
        FilePondPluginImageValidateSize,
        FilePondPluginFileValidateType,
        FilePondPluginImageCrop
    );

    const pond = FilePond.create(document.querySelector('#product-images'), {
        allowMultiple: true,
        minFiles: 3,
        maxFiles: 9,
        acceptedFileTypes: ['image/jpg', 'image/jpeg', 'image/png'],
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