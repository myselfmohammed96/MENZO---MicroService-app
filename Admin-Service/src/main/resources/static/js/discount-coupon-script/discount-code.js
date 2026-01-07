const checkDiscountCodeExist = "http://localhost:8080/discount/check-code-exist";

let now;



/*
*   ---------------------------------
*   ********* FETCH methods *********
*   ---------------------------------
*/

//  FETCH API - discount code - existence check
async function codeExistenceChecker(code) {
    try {
        const response = await fetch(checkDiscountCodeExist, {
            method: "POST",
            credentials: "include",
            headers: { "Content-Type": "application/json" },
            body:JSON.stringify({ discountCode: code })
        });
        if (!response.ok) {
            throw new Error("Code existence check failed");
        }
        const result = await response.json();
        return result.exists;
    } catch (error) {
        console.error("Error checking discount code existence: ", error);
        return true;
    }
}



/*
*   --------------------------------------
*   ********* VALIDATION methods *********
*   --------------------------------------
*/

let discountNameValid = false;
let discountLevelValid = false;
let discountTypeValid = false;
let discountValueValid = false;
let startDateValid = false;
let endDateValid = false;

let discountDescriptionValid = false;
let capTypeValid = false;
let capValueValid = false;
let discountPriorityValid = false;
let discountCodeValid = false;
let statusValid = false;

//  discount name
function validateDiscountName() {
    const input = document.getElementById('discount-name');
    const error = document.getElementById('discount-name-error-message');

    error.textContent = '';

    if (!input.value.trim()) {
        error.textContent = '*Name cannot be empty.';
        discountNameValid = false;
        return;
    }
    if (input.value.length < 5 || input.value.length > 100) {
        error.textContent = '*Name must be 5–100 characters long.';
        discountNameValid = false;
        return;
    }
    discountNameValid = true;
}

//  discount level
function validateDiscountLevel() {
    const input = document.getElementById('discount-level');
    const error = document.getElementById('discount-level-error-message');

    error.textContent = '';

    if (!input.value) {
        error.textContent = '*Level cannot be empty.';
        discountLevelValid = false;
        return;
    }
    discountLevelValid = true;
}

//  discount type
function validateDiscountType() {
    const input = document.getElementById('discount-type');
    const error = document.getElementById('discount-type-error-message');

    error.textContent = '';

    if (!input.value) {
        error.textContent = '*Type cannot be empty.';
        discountTypeValid = false;
        return;
    }
    discountTypeValid = true;
}

//  discount value
function validateDiscountValue() {
    const input = document.getElementById('discount-value');
    const value = input.value.trim();
    const error = document.getElementById('discount-value-error-message');
    const discountType = document.getElementById('discount-type').value;

    error.textContent = '';

    if (!discountType && value !== "") {
        error.textContent = '*Choose discount type first.';
        discountValueValid = false;
        return;
    }
    if (!value) {
        error.textContent = '*Value cannot be empty.';
        discountValueValid = false;
        return;
    }
    if (isNaN(value)) {
        error.textContent = '*Value should be a valid number.';
        discountValueValid = false;
        return;
    }
    if (Number(value) <= 0) {
        error.textContent = '*Value must be greater than 0.';
        discountValueValid = false;
        return;
    }
    if (discountType === "PERCENT" && Number(value) > 100) {
        error.textContent = '*Value should be less than or equal to 100%.';
        discountValueValid = false;
        return;
    }
    discountValueValid = true;
}

//  start date
function validateStartDate() {
    const input = document.getElementById('discount-start-date');
    const value = new Date(input.value);
    const error = document.getElementById('discount-start-date-error-message');

    error.textContent = '';

    if (!input.value) {
        error.textContent = '*Start date cannot be empty.';
        startDateValid = false;
        return;
    }
    if (isNaN(value.getTime())) {
        error.textContent = '*Invalid date & time.';
        startDateValid = false;
        return;
    }
    if (value < now) {
        error.textContent = '*Start date cannot be in past.';
        startDateValid = false;
        return;
    }
    startDateValid = true;
}

//  end date
function validateEndDate() {
    const input = document.getElementById('discount-end-date');
    const value = new Date(input.value);
    const startAtValue = document.getElementById('discount-start-date').value;
    const startAt = startAtValue ? new Date(startAtValue) : null;
    const error = document.getElementById('discount-end-date-error-message');

    error.textContent = '';

    if (!input.value) {
        error.textContent = '*End date cannot be empty.';
        endDateValid = false;
        return;
    }
//    if (isNaN(value.getTime())) {
//        error.textContent = '*Invalid date & time.';
//        endDateValid = false;
//        return;
//    }
    if (value < now) {
        error.textContent = '*End date cannot be in past.';
        endDateValid = false;
        return;
    }
    if (startAt && value <= startAt) {
        error.textContent = '*End date must be after Start date.';
        endDateValid = false;
        return;
    }
    endDateValid = true;
}


//  discount description
function validateDiscountDescription() {
    const input = document.getElementById('discount-description');
    const error = document.getElementById('discount-description-error-message');

    error.textContent = '';

    if (input.value.trim() !== null && input.value.trim() !== "" && input.value.length > 255) {
        error.textContent = '*Description must not exceed 255 characters.';
        discountDescriptionValid = false;
        return;
    }
    discountDescriptionValid = true;
}

//  cap type
function validateCapType() {
    const input = document.getElementById('discount-cap-type');
    const error = document.getElementById('discount-cap-type-error-message');

    error.textContent = '';

    if (!input.value) {
        error.textContent = '*Cap type cannot be empty.';
        capTypeValid = false;
        return;
    }
    capTypeValid = true;
}

//  cap value
function validateCapValue() {
    const input = document.getElementById('discount-cap-value');
    const value = input.value.trim();
    const error = document.getElementById('discount-cap-value-error-message');
    const capType = document.getElementById('discount-cap-type').value;

    error.textContent = '';

    if (capType === 'NONE') {
        capValueValid = true;
        return;
    }

    if (!capType && value !== '') {
        error.textContent = '*Choose cap type first.';
        capValueValid = false;
        return;
    }
    if (!value) {
        error.textContent = `*Cap value cannot be empty for cap type ${capType}.`;
        capValueValid = false;
        return;
    }
    if (isNaN(value)) {
        error.textContent = '*Cap value should be a valid number.';
        capValueValid = false;
        return;
    }
    if (Number(value) <= 0) {
        error.textContent = '*Cap value must be greater than 0.';
        capValueValid = false;
        return;
    }
    if (capType === "PERCENT" && Number(value) > 100) {
        error.textContent = '*Cap value should be less than or equal to 100%.';
        capValueValid = false;
        return;
    }
    capValueValid = true;
}

//  discount priority
function validateDiscountPriority() {
    const input = document.getElementById('discount-priority');
    const error = document.getElementById('discount-priority-error-message');

    error.textContent = '';

    if (input.value.trim() !== null && input.value.trim() !== "" && !Number.isInteger(Number(input.value.trim()))) {
        error.textContent = '*Priority should be a valid integer.';
        discountPriorityValid = false;
        return;
    }
    discountPriorityValid = true;
}

//  discount code
function validateDiscountCode() {
    const input = document.getElementById('discount-code');
    const error = document.getElementById('discount-code-error-message');

    error.textContent = '';

    if (!input.value) {
        error.textContent = '*Discount code cannot be empty.';
        discountCodeValid = false;
        return;
    }
    discountCodeValid = true;
}

//  discount status
function validateStatus() {
    const input = document.getElementById('discount-status');
    const error = document.getElementById('discount-status-error-message');

    error.textContent = '';

    if (!input.value) {
        error.textContent = '*Status cannot be empty.';
        statusValid = false;
        return;
    }
    statusValid = true;
}


//  form submit validation
function formSubmitValidation() {
    validateDiscountName();
    validateDiscountLevel();
    validateDiscountType();
    validateDiscountValue();
    validateStartDate();
    validateEndDate();

    validateDiscountDescription();
    validateCapType();
    validateCapValue();
    validateDiscountPriority();
    validateDiscountCode();
    validateStatus();

    const discountField = document.getElementById('discount-description').value.trim();
    const capValueField = document.getElementById('discount-cap-value').value.trim();
    const priorityField = document.getElementById('discount-priority').value.trim();

    if (discountNameValid && discountLevelValid && discountTypeValid
            && discountValueValid && startDateValid && endDateValid
            && (!discountField || discountDescriptionValid) && capTypeValid
            && (!capValueField || capValueValid) && (!priorityField || discountPriorityValid)
            && discountCodeValid && statusValid) {
        return true;
    } else {
        return false;
    }
}



/*
*   --------------------------------
*   ********* INIT methods *********
*   --------------------------------
*/

//  Discount code - choices init
function initializeDiscountCodeChoices() {
    try {
        return new Choices('#discount-code', {
            placeholder: true,
            placeholderValue: 'Enter code',
            searchEnabled: false,
            shouldSort: true
        });
    } catch (error) {
        console.error("Discount code choices init error: ", error);
        return;
    }
}

function codeGenInit() {
console.log("code gen init")
console.log("name - " + discountNameValid + ", level - " + discountLevelValid +
", type - " + discountTypeValid + ", value - " + discountValueValid + ", start - " +
 startDateValid + ", end - " + endDateValid)
    if (discountNameValid && discountLevelValid && discountTypeValid
            && discountValueValid && startDateValid && endDateValid) {
            console.log("code gen initiating");
        codeLoader();
    }
    return;
}



/*
*   --------------------------------
*   ********* INIT methods *********
*   --------------------------------
*/

//  Listen - fields required for code generation
function listenCodeGenFields() {
    const debounced = {
        discountName: debounce(validateDiscountName, 500),
        discountLevel: debounce(validateDiscountLevel, 500),
        discountType: debounce(validateDiscountType, 500),
        discountValue: debounce(validateDiscountValue, 500),
        startDate: debounce(validateStartDate, 500),
        endDate: debounce(validateEndDate, 500)
    };

    //    discount name
    document.getElementById('discount-name')
            .addEventListener('input', () => {
                debounced.discountName();
                codeGenInit();
            });

    //    discount level
    document.getElementById('discount-level')
            .addEventListener('change', () => {
                debounced.discountLevel();
                codeGenInit();
            });

    //    discount type
    document.getElementById('discount-type')
            .addEventListener('change', () => {
                debounced.discountType();
                debounced.discountValue();
                codeGenInit();
            });

    //    discount value
    document.getElementById('discount-value')
            .addEventListener('input', () => {
                debounced.discountValue();
                codeGenInit();
            });

    //    start date
    document.getElementById('discount-start-date')
            .addEventListener('change', () => {
                debounced.startDate();
                codeGenInit();
            });

    //    end date
    document.getElementById('discount-end-date')
            .addEventListener('change', () => {
                debounced.endDate();
                codeGenInit();
            });
}

//  Listen - other fields required for submit
function isOtherFieldsValid() {
    const debounced = {
        description: debounce(validateDiscountDescription, 500),
        capType: debounce(validateCapType, 500),
        capValue: debounce(validateCapValue, 500),
        priority: debounce(validateDiscountPriority, 500),
        code: debounce(validateDiscountCode, 500),
        status: debounce(validateStatus, 500)
    };

    //    discount description
    document.getElementById('discount-description')
            .addEventListener('input', debounced.description);

    //    cap type
    document.getElementById('discount-cap-type')
            .addEventListener('change', debounced.capType);

    document.getElementById('discount-cap-type')
            .addEventListener('change', debounced.capValue);

    //    cap value
    document.getElementById('discount-cap-value')
            .addEventListener('input', debounced.capValue);

    //    priority
    document.getElementById('discount-priority')
            .addEventListener('input', debounced.priority);

    //    discount code
    document.getElementById('discount-code')
            .addEventListener('change', debounced.code);

    //    discount status
    document.getElementById('discount-status')
            .addEventListener('change', debounced.status);
}

//  debounce/delay listener action
function debounce(fn, delay) {
    let timer;
//    console.log("debounces../.")
    return function (...args) {
        clearTimeout(timer);
        timer = setTimeout(() => fn.apply(this, args), delay);
    };
}



/*
*   ------------------------------------
*   ********* Code gen methods *********
*   ------------------------------------
*/

//  Code build & Load in select
async function codeLoader(codeData) {
    try {
    console.log("code loader running")
        const codeData = {
            discountName: document.getElementById('discount-name').value.trim(),
            level: document.getElementById('discount-level').value,
            type: document.getElementById('discount-type').value,
            value: Number(document.getElementById('discount-value').value.trim()),
            startYear: new Date(document.getElementById('discount-start-date').value).getFullYear(),
            endYear: new Date(document.getElementById('discount-end-date').value).getFullYear()
        };

        const discountCodes = await codeBuilder(codeData);
        console.log("code loader called")

        if (!discountCodes) {
            throw new Error("Discount codes not available");
        }
        if (!Array.isArray(discountCodes)) {
            throw new Error("Invalid data: discountCodes should be array");
        }
        if (discountCodes.length !== 4) {
            throw new Error("discountCodes should have 4 codes");
        }

        const discountCodeSelect = document.getElementById('discount-code');
        discountCodeSelect.innerHTML = '';
console.log(discountCodes)
        discountCodes.forEach(code => {
            const codeOption = document.createElement('option');
            codeOption.value = code;
            codeOption.textContent = code;

            discountCodeSelect.appendChild(codeOption);
        });
        discountCodeChoices.setChoices(
            [...discountCodeSelect.options],
            'value',
            'text',
            true
        );
        discountCodeChoices.enable();
    } catch (error) {
        console.error("Error loading discount codes");
    }
}

//  Code builder - Building 4 unique discount codes
async function codeBuilder(codeData) {
    try {
        const codeElements = {};

        //  ------- Name abbreviation -------
        const STOP_WORDS = new Set([
            'for', 'the', 'to', 'of', 'and', 'sale', 'offer', 'discount', 'deal', 'promo'
        ]);

        const words = codeData.discountName
                .toLowerCase()
                .replace(/[^a-z\s]/g, '')
                .split(/\s+/)
                .filter(w => w && !STOP_WORDS.has(w));

        let abbrev = words.slice(0, 2).map(w => w[0]).join('');

        //  fallback
        if (!abbrev) abbrev = 'DS';

        //  ------- Year -------
        const currentYear = now.getFullYear();
        const year = currentYear.toString().slice(-2);

        codeElements.nameYear = (abbrev + year).toUpperCase();

        //  ------- level -------
        switch(codeData.level.toLowerCase()) {
            case 'global': codeElements.level = 'GL';
            break;
            case 'category': codeElements.level = 'CT';
            break;
            case 'sub-category': codeElements.level = 'SC';
            break;
            case 'product': codeElements.level = 'PR';
            break;
            case 'variant': codeElements.level = 'VR';
            break;
            default: codeElements.level = 'DL';
        }

        //  ------- value & type -------
        codeElements.valueType =
            codeData.type === 'PERCENT'
                ? `${codeData.value}P`
                : `${codeData.value}F`;

        //  ------- Building 4 unique codes -------
        const codes = [];

        for (let i = 1; i <= 4; i++) {
            let attempts = 0;
            let code;

            do {
                code = compileDiscountCode(codeElements, i);
                attempts++;
            } while (await codeExistenceChecker(code) && attempts < 15);

            if (attempts === 15) {
                throw new Error("Unable to generate unique discount code");
            }
            console.log("building code")
console.log(code)
            codes.push(code);
        }
        return codes;
    } catch (error) {
        console.error("Error building discount code: ", error);
    }
}

//  Compiling the discount code elements - with random number
function compileDiscountCode(codeElements, i) {
    const rand = Math.floor(10000 + Math.random() * 90000);

    switch (i) {
        case 1:
            return `${codeElements.nameYear}-${codeElements.level}-${rand}-${codeElements.valueType}`;
        case 2:
            return `${codeElements.level}-${codeElements.nameYear}-${rand}-${codeElements.valueType}`;
        case 3:
            return `${codeElements.level}-${rand}-${codeElements.nameYear}-${codeElements.valueType}`;
        case 4:
            return `${codeElements.nameYear}-${rand}-${codeElements.level}-${codeElements.valueType}`;
        default:
            return null;
    }
}



/*
*   --------------------------------
*   ********* LOAD methods *********
*   --------------------------------
*/


//  DOM Loading event
document.addEventListener('DOMContentLoaded', () => {

    now = new Date();

    discountCodeChoices = initializeDiscountCodeChoices();
    discountCodeChoices.disable();

    listenCodeGenFields();
    isOtherFieldsValid();

    document.querySelector('#discount-form').addEventListener('submit', (e) => {
        e.preventDefault();

        if (formSubmitValidation()) {
            window.submitForm();
        }
        return;
    });
});
































//  code generation listeners - init
//function initCodeGenerationListeners() {
//    const fields = [
//        'discount-name',
//        'discount-level',
//        'discount-type',
//        'discount-value',
//        'discount-start-date',
//        'discount-end-date'
//    ];
//
//    fields.forEach(id => {
//        document.getElementById(id).addEventListener('input', debounce(codeGenerationInit, 400));
//        document.getElementById(id).addEventListener('change', debounce(codeGenerationInit, 400));
//    });
//    console.log("init code generation listeners")
//}





//  code generation init (initializes the process after data validation)
//  ## should be called while filling the form
//async function codeGenerationInit() {
//    try {
//    console.log("code-generationInit here...")
//        const codeData = {
//            discountName: document.getElementById('discount-name').value.trim(),
//            level: document.getElementById('discount-level').value,
//            type: document.getElementById('discount-type').value,
////            value: Number(document.getElementById('discount-value').value),
//            value: document.getElementById('discount-value').value.trim(),
//            startYear: document.getElementById('discount-start-date').value
//                        ? new Date(document.getElementById('discount-start-date').value).getFullYear()
//                        : null,
//            endYear: document.getElementById('discount-end-date').value
//                        ? new Date(document.getElementById('discount-end-date').value).getFullYear()
//                        : null
//        };
//
//        let isValidCodeData = true;

        //  ------- Validation -------

//        //  discount name
//        if (!codeData.discountName) {
//            isValidCodeData = false;
////            console.error("discount name not found")
//        }
//        if (codeData.discountName.length < 5 || codeData.discountName.length > 100) {
//            isValidCodeData = false;
////            console.error("discount name not proper")
//        }
//
//        // discount level
//        if (!codeData.level) {
//            isValidCodeData = false;
////            console.error("discount level not found")
//        }
//
//        //  discount type
//        if (!codeData.type) {
//            isValidCodeData = false;
////            console.error("discount type not found")
//        }
//
//        //  discount value
//        if (!codeData.value) {
//            isValidCodeData = false;
////            console.error("discount value not found")
//        }
//        if (codeData.value <= 0) {
//            isValidCodeData = false;
////            console.error("discount value <= 0")
//        }
//        if (codeData.type === "PERCENT" && codeData.value > 100) {
//            isValidCodeData = false;
////            console.error("discount value invalid")
//        }
//
//        //  start date
//        const currentYear = new Date().getFullYear();
//
//        if (!codeData.startYear) {
//            isValidCodeData = false;
////            console.error("discount start year not found")
//        }
//        if (Number.isNaN(codeData.startYear)) {
//            isValidCodeData = false;
////            console.error("discount start year nan")
//        }
//        if (codeData.startYear < currentYear) {
//            isValidCodeData = false;
////            console.error("discount start year invalid")
//        }
//
//        //  end date
//        if (!codeData.endYear) {
//            isValidCodeData = false;
////            console.error("discount end year not found")
//        }
//        if (Number.isNaN(codeData.endYear)) {
//            isValidCodeData = false;
////            console.error("discount end year nan")
//        }
//        if (codeData.endYear < currentYear) {
//            isValidCodeData = false;
////            console.error("discount end year invalid")
//        }
//        if (codeData.endYear < codeData.startYear) {
//            isValidCodeData = false;
////            console.error("discount end year less than start year")
//        }
//
//        codeData.currentYear = currentYear;
//console.log("is valid code data = " + isValidCodeData)
//        if (isValidCodeData === false) {
//            return;
//        }
//
//        await codeLoader(codeData);
//    } catch (error) {
//        console.error("Discount code generation init failed: ", error);
//    }
//}
