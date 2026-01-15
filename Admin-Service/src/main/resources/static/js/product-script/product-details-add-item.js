let formModal;



/*
*   --------------------------------
*   ********* Modal methods *********
*   --------------------------------
*/

//  Toggle modal
const toggleFormModal = (modalStatus) => {
    const modalDisplay = getComputedStyle(formModal).display;

    if(modalStatus === "open" && modalDisplay === "none") {
        const loaded = window.loadColorAndSize();

        if (loaded) {
            formModal.style.display = 'flex';
        }
    } else if(modalStatus === "close" && modalDisplay === "flex" ){
        formModal.style.display = "none";
    }
}



/*
*   ------------------------------------------------------
*   ********* Data handling & validation methods *********
*   ------------------------------------------------------
*/

let colorIdValid = false;

//  validate color id
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

//  Get - item details - returns object
function getItemDetails() {
    //  product id
    const productIdValue = document.getElementById('product-id').value.trim();
    if (!productIdValue) {
        throw new Error("Product Id not found.");
    }
    const productId = Number(productIdValue);

    //  color id
    validateColorId();
    if (!colorIdValid) {
        return null;
    }

    //  building object
    const itemDetails= {
        productId: productId,
        colorId: document.getElementById("color-select").value,
        status: document.querySelector('input[name="status"]:checked').value
    };

    return itemDetails;
}

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
}



/*
*   --------------------------------
*   ********* Load methods *********
*   --------------------------------
*/

//  DOM Loading event loader
document.addEventListener("DOMContentLoaded", () => {
    formModal = document.getElementById('add-item-form-modal');

    //  Modal open
    document.getElementById('add-item-button').addEventListener('click', () => {
        toggleFormModal('open');
    });

    //  Modal close
    document.getElementById('modal-form-close').addEventListener('click', () => {
        toggleFormModal('close');
    });


    //  ------- Form submit - event -------
    const form = document.getElementById('add-item-form');
    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        try {
            const formData = new FormData(form);
            console.log("submit initiated");

            //  get details
            const itemDetails = getItemDetails();
            if (!itemDetails) {
                throw new Error("Item details not found.");
            }

            formData.append(
                "newItem",
                new Blob(
                    [JSON.stringify(itemDetails)],
                    { type: "application/json" }
                )
            );

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

            //  Form submit - POST
            console.log("submitting");
            const response = await fetch(form.action, {
                method: "POST",
                credentials: "include",
                body: formData
            });

            const data = await response.json().catch(() => ({}));

            if (response.ok) {
                window.handleSavedItemData(savedItemData.itemDetails);  //  ## have to check this post submit population
                toggleFormModal('close');
                window.showToast("Item saved successfully.", true);
            } else if (response.status >= 500) {
                console.error("Server error: ", data.message);
                toggleFormModal('close');
                window.showToast("Server error. Try again.", false);
            } else if (response.status >= 400) {
                console.error("Client error: ", data.message);
                window.showToast(data.message || "Invalid request.", false);
            }
        } catch (error) {
            console.error("Failed to save item: ", error);
            window.showToast("Error saving item. Try again.", false);
        }
    });


    //  ------- Image upload - FilePond -------
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
        imageValidateSizeMinHeigth: 300,
        imagePreviewHeight: 150,
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









































//  Get - size, stock & price details
//const getSizeDetails = () => {
//    return [...document.querySelectorAll('input[name="sizes"]:checked')]
//        .map(sizeCheckbox => {
//            const id = sizeCheckbox.dataset.id;
//
//            const stockInput = document.querySelector(`input[data-id="${id}"][placeholder="Enter stock"]`);
//            const priceInput = document.querySelector(`input[data-id="${id}"][placeholder="Enter price"]`);
//
//            const sizeValue = sizeCheckbox.nextSibling.textContent.trim();
//            const stock = stockInput.value.trim();
//            const price = priceInput.value.trim();
//
//            //  validation
//            if(!stock || !/^[0-9]+$/.test(stock)) {
//                throw new Error("Invalid stock for size " + sizeValue);
//            }
//
//            if (!price || !/^[0-9]+(\.[0-9]+)?$/.test(price)) {
//                throw new Error("Invalid price for size " + sizeValue);
//            }
//
//            //  return object
//            return {
//                sizeId: Number(id),
//                sizeValue,
//                sizeStock: Number(stock),
//                sizePrice: Number(price)
//            };
//        });
//};












//try {
//        const sizeDetails = await window.getSizeDetails();
//        return sizeDetails;
//    } catch (error) {
//        console.error("Error getting size details: ", error);
//        return null;
//    }
