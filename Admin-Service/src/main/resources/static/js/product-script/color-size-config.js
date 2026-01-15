const getColors = "http://localhost:8080/variations/colors";
const getSizes = "http://localhost:8080/variations/size";

let colorChoices;



//   ********* FETCH methods *********

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



//  ********* POPULATE methods *********

//  POPULATE - colors
function populateColors(colorOptions) {
    const colorSelect = document.getElementById('color-select');
    colorSelect.innerHTML = '';

    if(!colorOptions || !Array.isArray(colorOptions)) return;

    const placeholderOption = document.createElement('option');
    placeholderOption.value = '';
    placeholderOption.text = '';
    placeholderOption.setAttribute(
        "data-custom-properties",
        JSON.stringify({ hex: '' })
    );
    placeholderOption.selected = true;
    placeholderOption.disabled = true;

    colorSelect.appendChild(placeholderOption);

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
    try {
        const sizeContainer = document.getElementById('size-container');
        const stockContainer = document.getElementById('stock-container');
        const mrpContainer = document.getElementById('mrp-container');
        const sellingPriceContainer = document.getElementById('selling-price-container');

        sizeContainer.innerHTML = '';
        stockContainer.innerHTML = '';
        mrpContainer.innerHTML = '';
        sellingPriceContainer.innerHTML = '';

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
            stockInput.classList.add('input', 'input-100', 'product-form-input');
            stockInput.type = 'text';
            stockInput.placeholder = "Enter stock";
            stockInput.setAttribute('data-id', size.optionId);
            stockInput.disabled = true;

            stockElement.appendChild(stockInput);
            stockContainer.appendChild(stockElement);

            //  ------- MRP INPUT -------
            const mrpElement = document.createElement('div');
            mrpElement.classList.add('size-price-element');

            const mrpInput = document.createElement('input');
            mrpInput.classList.add('input', 'input-100', 'product-form-input');
            mrpInput.type = 'text';
            mrpInput.placeholder = "Enter MRP";
            mrpInput.setAttribute('data-id', size.optionId);
            mrpInput.disabled = true;

            mrpElement.appendChild(mrpInput);
            mrpContainer.appendChild(mrpElement);

            //  ------- SELLING PRICE INPUT -------
            const sellingPriceElement = document.createElement('div');
            sellingPriceElement.classList.add('size-price-element');

            const sellingPriceInput = document.createElement('input');
            sellingPriceInput.classList.add('input', 'input-100', 'product-form-input');
            sellingPriceInput.type = 'text';
            sellingPriceInput.placeholder = "Enter price";
            sellingPriceInput.setAttribute('data-id', size.optionId);
            sellingPriceInput.disabled = true;

            sellingPriceElement.appendChild(sellingPriceInput);
            sellingPriceContainer.appendChild(sellingPriceElement);

            //  ------- ENABLE / DISABLE ON CHECK -------
            sizeInput.addEventListener('change', () => {
                const isChecked = sizeInput.checked;
                stockInput.disabled = !isChecked;
                mrpInput.disabled = !isChecked;
                sellingPriceInput.disabled = !isChecked;
            });
        });
    } catch (error) {
        console.error("Error populating size detail fields: ", error);
    }
}



//  ********* CHOICES.JS INITIALIZATION *********

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



//  ********* LOAD methods *********

//  LOADER - colors & sizes
window.loadColorAndSize = async function() {

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
    return true;
}