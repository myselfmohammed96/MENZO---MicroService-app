//  Get - new product details
const getProductDetails = () => {
    const productName = document.getElementById('product-name').value.trim();
    const description = document.getElementById('product-description').value.trim();
    const categoryId = document.getElementById('category-select').value;
    const subCategoryId = document.getElementById('sub-category-select').value;
    if (!productName || !description || !categoryId || !subCategoryId) {
        //  validation
    }

    const colorId = document.getElementById('color-select').value;
    if (!colorId) {
        //  validation
    }
    const status = document.querySelector('input[name="status"]:checked').value;
    const pod = document.querySelector('input[name="pod"]:checked').value;

    const discount = document.getElementById('discount').value.trim();
    const discountType = document.getElementById('discount-type').value.trim();
    const itemWeight = document.getElementById('item-weight').value.trim();
    const genericName = document.getElementById('generic-name').value.trim();
    const countryOfOrigin = document.getElementById('country-of-origin').value.trim();
    const manufacturer = document.getElementById('manufacturer').value.trim();
    const packer = document.getElementById('packer').value.trim();
    if (!discount || !discountType || !itemWeight || !genericName || !countryOfOrigin || !manufacturer || !packer) {
        //  validation
    }

    const productDetails = {
        productName,
        description,
        categoryId,
        subCategoryId,

        colorId,
        status,
        pod,

        discount,
        discountType,
        itemWeight,
        genericName,
        countryOfOrigin,
        manufacturer,
        packer
    }
    return productDetails;
};

//  Get - size, stock & price details
const getSizeDetails = () => {
    return [...document.querySelectorAll('input[name="sizes"]:checked')]
        .map(sizeCheckbox => {
            const id = sizeCheckbox.dataset.id;

            const stockInput = document.querySelector(`input[data-id="${id}"][placeholder="Enter stock"]`);
            const priceInput = document.querySelector(`input[data-id="${id}"][placeholder="Enter price"]`);

            const sizeValue = sizeCheckbox.nextSibling.textContent.trim();
            const stock = stockInput.value.trim();
            const price = priceInput.value.trim();

            //  validation
            if(!stock || !/^[0-9]+$/.test(stock)) {
                throw new Error("Invalid stock for size " + sizeValue);
            }

            if (!price || !/^[0-9]+(\.[0-9]+)?$/.test(price)) {
                throw new Error("Invalid price for size " + sizeValue);
            }

            //  return object
            return {
                sizeId: Number(id),
                sizeValue,
                sizeStock: Number(stock),
                sizePrice: Number(price)
            };
        });
};

//  Get - variation details
const getVariationDetails = () => {
    const variationDetailsMap = new Map();
    const variationsFieldSet = document.getElementById('variations-fieldset');
    if (!variationsFieldSet) return null;

    variationsFieldSet.querySelectorAll('.variation-select').forEach(select => {
        const variationName = select.name.split(".")[1];
        variationDetailsMap.set(variationName, select.value);
    });

    return variationDetailsMap;
};

document.addEventListener("DOMContentLoaded", () => {

    //  ------- Form submit - event -------

    const form = document.querySelector('#add-product-form');
    form.addEventListener("submit", async(e) => {
        e.preventDefault();

        try {
            const formData = new FormData(form);
            console.log("product submit initiated");

            //  new product details
            const newProduct = getProductDetails();
            if (!newProduct) {
                throw new Error("Error getting new product details");
            }

            formData.append(
                "productDetails",
                new Blob([JSON.stringify(newProduct)], { type: "application/json" }),
                "productDetails.json"
            );
//            console.log("newProduct: " + newProduct);

            //  size details
            const selectedSizes = getSizeDetails();
            if (!selectedSizes) {
                throw new Error("Error getting size details");
            }

            formData.append(
                "sizeDetails",
                new Blob([JSON.stringify(selectedSizes)], { type: "application/json" })
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

//            if (response.status !== 201) {
            if (!response.ok) {
                console.error("Error submitting form");
                alert("Error saving product");
                return;
            }
            const data = await response.json();
            window.location.href = "/admin/product?id=" + data.productId;
        } catch(error) {
            console.error("Failed to save product. ", error);
            alert("Form submission failed. Try again");
        }
    });
});