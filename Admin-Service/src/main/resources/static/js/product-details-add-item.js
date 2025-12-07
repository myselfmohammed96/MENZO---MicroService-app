const getColors = "http://localhost:8080/variations/colors";
const getSizes = "http://localhost:8080/variations/size";



//  ********* fetch methods *********

//  FETCH - colors
const fetchColors = async () => {
    try {
        const response = await fetch(getColors, {
            method: "GET",
            credentials: "include"
        });
        if(!response.ok) throw new Error('error fetching colors');
        return response.json();
    } catch (error) {
        console.error("colors fetching failed");
        return null;
    }
};

//  FETCH - sizes
const fetchSizes = async () => {
    try {
        const response = await fetch(getSizes, {
            method: "GET",
            credentials: "include"
        });
        if(!response.ok) throw new Error("Error fetching colors");
        return response.json();
    } catch (error) {
        console.error("Failed fetching sizes");
        return null;
    }
};



//  ********* populating methods *********

//  POPULATE - colors
const populateColors = (colorOptions) => {
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

    //  ------- choice.js initialization -------
    const choices = new Choices('#color-select', {
        callbackOnCreateTemplates: function(template) {
            return {
//                item: (classNames, data) => {
//                    return template(`
//                        <div class="${classNames.item}">
//                            <span style="display:inline-block;width:12px;height:12px;background:${data.customProperties.hex};border-radius:3px;margin-right:6px;"></span>
//                            ${data.label}
//                        </div>
//                    `);
//                },
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

    document.getElementById('color-select')._choicesInstance = choices;
};

//  POPULATE - sizes
const populateSizes = (sizeOptions) => {
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
};



//  ********* modal methods *********

//  load modal
const loadModal = async (formModal) => {
    if (formModal) {
        const colors = await fetchColors();
        populateColors(colors.options);

        const sizes = await fetchSizes();
        populateSizes(sizes.options);

        formModal.style.display = "flex";
    }
}

//  Modal toggle
const toggleFormModal = (modalStatus) => {
    const formModal = document.getElementById('add-item-form-modal');
    const modalDisplay = getComputedStyle(formModal).display;

    if(modalStatus === "open" && modalDisplay === "none") {
        loadModal(formModal);
    } else if(modalStatus === "close" && modalDisplay === "flex" ){
        formModal.style.display = "none";
    }
}

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



//  ********* data loader - on page load *********

//  DOM Loading event loader
document.addEventListener("DOMContentLoaded", () => {

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
        const formData = new FormData(form);
        console.log("submit initiated");

        //  product ID
        const productIdValue = document.getElementById('product-id').value.trim();
        const productId = productIdValue === "" ? null : Number(productIdValue);

        //  newItem details object
        const newItemObject = {
            productId: productId,
            colorId: document.getElementById("color-select").value,
            status: document.querySelector('input[name="status"]:checked').value
        };
        formData.append(
            "newItem",
            new Blob([JSON.stringify(newItemObject)], { type: "application/json" })
        );

        //  size details
        try {
            const selectedSizes = getSizeDetails();
            formData.append(
                "sizeDetails",
                new Blob([JSON.stringify(selectedSizes)], { type: "application/json" })
            );
        } catch (error) {
            console.error("Error getting size details", error);
            return;
        }

        //  form submit - POST
        try {
            const response = await fetch(form.action, {
                method: "POST",
                credentials: "include",
                body: formData
            });

//            console.log("Statu : " + response.status);

            if(response.status !== 201) {
                console.error("Error submitting form");
                alert("Error saving item");
                return;
            }
            const savedItemData = await response.json();

            //  post form submit operations
            try {
                window.handleSavedItemData(savedItemData.itemDetails);
                toggleFormModal('close');
                alert("Item saved successfully");
            } catch (error) {
                console.error("Error populating saved item data.", error);
            }

        } catch (error) {
            console.error("Failed to save item. ", error);
            alert("Form submission failed. Try again");
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