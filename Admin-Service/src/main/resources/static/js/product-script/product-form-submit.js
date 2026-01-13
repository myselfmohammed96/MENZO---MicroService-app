const productSummaryRedirect = "";



/*
*   --------------------------------------
*   ********* VALIDATION methods *********
*   --------------------------------------
*/

let productNameValid = false;
let descriptionValid = false;
let categoryIdValid = false;
let subCategoryIdValid = false;
let colorIdValid = false;


let itemWeightValid = false;
let countryOfOriginValid = false;
let manufacturerValid = false;
let packerValid = false;
//let genericNameValid = false;
//let discountValid = false;
//let discountTypeValid = false;


//  ------- General info validation -------

//  product name
function validateProductName() {
    const input = document.getElementById('product-name');
    const error = document.getElementById('product-name-error-message');

    error.textContent = '';

    if (!input.value.trim()) {
        error.textContent = '*Name cannot be empty.';
        productNameValid = false;
        return;
    }
    if (input.value.length < 5 || input.value.length > 100) {
        error.textContent = '*Name must be 5–100 characters long.';
        productNameValid = false;
        return;
    }
    productNameValid = true;
}

//  description
function validateDescription() {
    const input = document.getElementById('product-description');
    const error = document.getElementById('product-description-error-message');

    error.textContent = '';

    if (!input.value.trim()) {
        error.textContent = '*Description cannot be empty.';
        descriptionValid = false;
        return;
    }
    descriptionValid = true;
}

//  category id
function validateCategoryId() {

    const input = document.getElementById('category-select');
    const error = document.getElementById('category-error-message');

    error.textContent = '';

    if (!input.value) {
        error.textContent = '*Category cannot be empty.';
        categoryIdValid = false;
        return;
    }
    categoryIdValid = true;
}

//  sub-category id
function validateSubCategoryId() {
    const input = document.getElementById('sub-category-select');
    const error = document.getElementById('sub-category-error-message');
//console.log("Calling validate sub category Id");
    error.textContent = '';
//console.log("cat value: " + input.value)
    if (!input.value) {
        error.textContent = '*Sub-category cannot be empty.';
        subCategoryIdValid = false;
        return;
    }
    subCategoryIdValid = true;
}

//  color id
function validateColorId() {
    const input = document.getElementById('color-select');
    const error = document.getElementById('color-error-message');

    error.textContent = '';

    if (!input.value) {
        error.textContent = '*Color cannot be empty.';
        colorIdValid = false;
        return;
    }
    colorIdValid = true;
}


//  ------- Other field validation -------

//  item weight
function validateItemWeight() {
    const input = document.getElementById('item-weight');
    const itemWeight = input.value.trim();
    const error = document.getElementById('item-weight-error-message');

    error.textContent = '';

    if (!itemWeight) {
        error.textContent = '*Item weight cannot be empty.';
        itemWeightValid = false;
        return;
    }
    if (!/^\d+(\.\d+)?$/.test(itemWeight)) {
        error.textContent = "*Item weight must be valid number.";
        itemWeightValid = false;
        return;
    }
    if (Number(itemWeight) <= 0) {
        error.textContent = "Item weight must be greater than 0.";
        itemWeightValid = false;
        return;
    }
    itemWeightValid = true;
}

//  country of origin
function validateCountryOfOrigin() {
    const input = document.getElementById('country-of-origin');
    const error = document.getElementById('country-of-origin-error-message');

    error.textContent = '';

    if (!input.value.trim()) {
        error.textContent = '*Country of Origin cannot be empty.';
        countryOfOriginValid = false;
        return;
    }
    countryOfOriginValid = true;
}

//  manufacturer
function validateManufacturer() {
    const input = document.getElementById('manufacturer');
    const error = document.getElementById('manufacturer-error-message');

    error.textContent = '';

    if (!input.value.trim()) {
        error.textContent = '*Manufacturer details cannot be empty.';
        manufacturerValid = false;
        return;
    }
    manufacturerValid = true;
}

//  packer
function validatePacker() {
    const input = document.getElementById('packer');
    const error = document.getElementById('packer-error-message');

    error.textContent = '';

    if (!input.value.trim()) {
        error.textContent = '*Packer details cannot be empty.';
        packerValid = false;
        return;
    }
    packerValid = true;
}

////  generic name
//function validateGenericName() {}
//
////  discount
//function validateDiscount() {}
//
////  discount type
//function validateDiscountType() {}


/*
*   -------------------------------------------
*   ********* Get - form data methods *********
*   -------------------------------------------
*/

//  Get - new product details - returns object
const getProductDetails = () => {
    validateProductName();
    validateDescription();
    validateCategoryId();
    validateSubCategoryId();
    validateColorId();

    validateItemWeight();
    validateCountryOfOrigin();
    validateManufacturer();
    validatePacker();

    if (!productNameValid || !descriptionValid || !categoryIdValid
            || !subCategoryIdValid || !colorIdValid || !itemWeightValid
            || !countryOfOriginValid || !manufacturerValid || !packerValid) {
        return null;
    }

    const productDetails = {
        productName: document.getElementById('product-name').value.trim(),
        description:document.getElementById('product-description').value.trim(),
        categoryId: document.getElementById('category-select').value,
        subCategoryId: document.getElementById('sub-category-select').value,
        colorId: document.getElementById('color-select').value,
        status: document.querySelector('input[name="status"]:checked').value,
        pod: document.querySelector('input[name="pod"]:checked').value,
        freeDel: document.querySelector('input[name="freeDel"]:checked').value,
        itemWeight: document.getElementById('item-weight').value.trim(),
        genericName: document.getElementById('generic-name').value.trim(),
        countryOfOrigin: document.getElementById('country-of-origin').value.trim(),
        manufacturer: document.getElementById('manufacturer').value.trim(),
        packer: document.getElementById('packer').value.trim()
    };

    return productDetails;

    //  ## country of origin - should be dynamic (admin should select from a list of countries.. if new country needed then he should be able to type and add new one)
    //  ## also country of origin should have its own CRUD panel

//    const discount = document.getElementById('discount').value.trim();
//    const discountType = document.getElementById('discount-type').value.trim();
};

//  Get - size, stock & price details - returns array
const getSizeDetails = () => {
    try {
        const sizeErrorMessage = document.getElementById('size-details-error-message');
        sizeErrorMessage.textContent = '';

        const checkedSizes = document.querySelectorAll('input[name="sizes"]:checked');

        //  size validation
        if (checkedSizes.length === 0) {
            sizeErrorMessage.textContent = '*Sizes not selected.';
            return null;
        }

        const sizeDetails = [];

        for (const checkedSize of checkedSizes) {
            const id = checkedSize.dataset.id;
            const sizeValue = checkedSize.nextSibling.textContent.trim();

            const stock = document.querySelector(`input[data-id="${id}"][placeholder="Enter stock"]`).value.trim();
            const mrp = document.querySelector(`input[data-id="${id}"][placeholder="Enter MRP"]`).value.trim();
            const sellingPrice = document.querySelector(`input[data-id="${id}"][placeholder="Enter price"]`).value.trim();

            //  stock validation
            if (!stock) {
                sizeErrorMessage.textContent = `*Stock cannot be empty for size - ${sizeValue}`;
                return null;
            }
            if (!/^\d+$/.test(stock)) {
                sizeErrorMessage.textContent = `*Stock must be valid integer for size - ${sizeValue}`;
                return null;
            }
            if (Number(stock) <= 0) {
                sizeErrorMessage.textContent = `*Enter valid stock quantity for size - ${sizeValue}. Must be greater than 0.`;
                return null;
            }

            //  price validation
            if (!mrp) {
                sizeErrorMessage.textContent = `*MRP cannot be empty for size - ${sizeValue}`;
                return null;
            }
            if (!/^\d+(\.\d+)?$/.test(mrp)) {
                sizeErrorMessage.textContent = `*MRP must be valid number for size - ${sizeValue}`;
                return null;
            }
            if (Number(mrp) <= 0) {
                sizeErrorMessage.textContent = `*Enter valid MRP value for size - ${sizeValue}. Must be greater than 0.`;
                return null;
            }

            if (!sellingPrice) {
                sizeErrorMessage.textContent = `*Selling price cannot be empty for size - ${sizeValue}`;
                return null;
            }
            if (!/^\d+(\.\d+)?$/.test(sellingPrice)) {
                sizeErrorMessage.textContent = `*Selling price must be valid number for size - ${sizeValue}`;
                return null;
            }
            if (Number(sellingPrice) <= 0) {
                sizeErrorMessage.textContent = `*Enter valid Selling price for size - ${sizeValue}. Must be greater than 0.`;
                return null;
            }

            sizeDetails.push({
                sizeId: id,
                sizeValue,
                sizeStock: Number(stock),
                sizeMrp: Number(mrp),
                sizeSellingPrice: Number(sellingPrice)
            });
        }

        return sizeDetails;
    } catch (error) {
        console.error("Error getting size details: ", error);
    }
};

//  Get - variation details - returns map
const getVariationDetails = () => {
    const variationDetailsMap = new Map();

    const variationsFieldSet = document.getElementById('variations-fieldset');
    if (!variationsFieldSet) {
        console.error("No variations found.");
        return null;
    }

    const variationLabels = variationsFieldSet.querySelectorAll('label');

    for (const label of variationLabels) {
        const select = label.querySelector('select');
        const errorMessage = label.querySelector('.error-message');
        errorMessage.textContent = '';

        const variationName = select.name.split(".")[1];

        //  validation
        if (!select.value) {
            errorMessage.textContent = `*${variationName} not selected.`;
            return null;
        }

        variationDetailsMap.set(variationName, select.value);
    }

    return variationDetailsMap;
};



/*
*   ----------------------------------
*   ********* SUBMIT methods *********
*   ----------------------------------
*/

//  submit form to backend
async function submitForm() {}



/*
*   --------------------------------
*   ********* LOAD methods *********
*   --------------------------------
*/

//  DOM Loading event
document.addEventListener("DOMContentLoaded", () => {

    //  ------- Form submit - event -------

    const form = document.querySelector('#add-product-form');
    form.addEventListener("submit", async(e) => {
        e.preventDefault();

        try {
            const formData = new FormData(form);
//            console.log("product submit initiated");

            //  new product details
            const newProduct = getProductDetails();
            if (!newProduct) {
                throw new Error("Product details not found.");
            }

            formData.append(
                "productDetails",
                new Blob(
                    [JSON.stringify(newProduct)],
                    { type: "application/json" }
                ),
                "productDetails.json"
            );
//            console.log("newProduct: " + newProduct);

            //  size details
            const selectedSizes = getSizeDetails();
            if (!selectedSizes) {
                throw new Error("Error getting size details");
            }
            if (!Array.isArray(selectedSizes)) {
                throw new Error("Size details should be array.");
            }
            if (selectedSizes.length === 0) {
                throw new Error("Size details empty. At least one needed.");
            }

            formData.append(
                "sizeDetails",
                new Blob(
                    [JSON.stringify(selectedSizes)],
                    { type: "application/json" }
                )
            );
//            console.log("selectedSizes: " + selectedSizes);

            //  variation details
            const variationDetails = getVariationDetails();
            if (!variationDetails) {
                throw new Error("Error getting variation details: ", variationDetails);
            }
            formData.append(
                "variationDetails",
                new Blob(
                    [JSON.stringify(Object.fromEntries(variationDetails))],
                    { type: "application/json" }
                )
            );
//            console.log("variationDetails: " + variationDetails);

            //  Form submit - POST
            console.log("submitting");
            const response = await fetch(form.action, {
                method: "POST",
                credentials: "include",
                body: formData
            });

            const data = await response.json().catch(() => ({}));

            if (response.ok) {
//                window.location.replace(`${productSummaryRedirect}?id=${data.productId}&success=true`);
window.location.replace("/index");
            } else if (response.status >= 500) {
                console.error("Server error: ", data.message);
                window.showToast("Server error. Try again.", false);
            } else if (response.status >= 400) {
                console.error("Client error: ", data.message);
                window.showToast(data.message || "Invalid request.", false);
            }
//            if (response.status !== 201) {
//            if (!response.ok) {
//                console.error("Error submitting form");
//                alert("Error saving product");
//                return;
//            }
//            const data = await response.json();
//            window.location.href = "/admin/product?id=" + data.productId;
        } catch(error) {
            console.error("Failed to save product. ", error);
            window.showToast("Error saving product. Try again.", false);
        }
    });
});