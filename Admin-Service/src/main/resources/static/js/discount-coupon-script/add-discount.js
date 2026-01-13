const getDiscountLevel = "http://localhost:8080/discount/level";
const getDiscountType = "http://localhost:8080/discount/type";
const getDiscountStatus = "http://localhost:8080/discount/form-status";
const getCapType = "http://localhost:8080/discount/cap-type";
const saveDiscountUrl = "http://localhost:8080/discount";

const discountSummaryRedirect = "http://localhost:8080/admin/discount-summary"

let confirmModal;
let refreshButton;

let discountCodeChoices;



/*
*   ---------------------------------
*   ********* FETCH methods *********
*   ---------------------------------
*/

//  FETCH - discount level
async function fetchDiscountLevel() {
    try {
        const response = await fetch(getDiscountLevel, {
            method: "GET",
            credentials: "include"
        });
        if (!response.ok) {
            throw new Error("Error fetching discount levels: ", response.status);
        }
        return await response.json();
    } catch (error) {
        console.error("Fetching discount levels failed: ", error);
    }
}

//  FETCH - discount type
async function fetchDiscountType() {
    try {
        const response = await fetch(getDiscountType, {
            method: "GET",
            credentials: "include"
        });
        if (!response.ok) {
            throw new Error("Error fetching discount types: ", response.status);
        }
        return await response.json();
    } catch (error) {
        console.log("Fetching discount types failed: ", error);
    }
}

//  FETCH - discount cap type
async function fetchCapType() {
    try {
        const response = await fetch(getCapType, {
            method: "GET",
            credentials: "include"
        });
        if (!response.ok) {
            throw new Error("Error fetching cap types: ", response.status);
        }
        return await response.json();
    } catch (error) {
        console.log("Fetching cap types failed: ", error);
    }
}

//  FETCH - discount status
async function fetchDiscountStatus() {
    try {
        const response = await fetch(getDiscountStatus, {
            method: "GET",
            credentials: "include"
        });
        if (!response.ok) {
            throw new Error("Error fetching discount status: ", response.status);
        }
        return await response.json();
    } catch (error) {
        console.log("Fetching discount status failed: ", error);
    }
}



/*
*   ------------------------------------
*   ********* POPULATE methods *********
*   ------------------------------------
*/

//  POPULATE - discount level
function populateDiscountLevels(discountLevels = []) {
    try {
        if (discountLevels.length === 0) {
            throw new Error("Invalid data: discount levels should not be empty");
        }
        const discountLevelSelect = document.getElementById('discount-level');
        discountLevels.forEach(level => {
            const levelOption = document.createElement('option');
            levelOption.value = level;
            levelOption.textContent = level.charAt(0).toUpperCase()
                                        + level.slice(1).toLowerCase().replace('_', '-');
            discountLevelSelect.appendChild(levelOption);
        });
    } catch (error) {
        console.error("Discount levels populate error: ", error);
    }
}

//  POPULATE - discount type
function populateDiscountTypes(discountTypes = []) {
    try {
        if (discountTypes.length === 0) {
            throw new Error("Invalid data: discount types should not be empty");
        }
        const discountTypeSelect = document.getElementById('discount-type');
        discountTypes.forEach(type => {
            const typeOption = document.createElement('option');
            typeOption.value = type;
            typeOption.textContent = type.charAt(0).toUpperCase()
                                        + type.slice(1).toLowerCase();
            discountTypeSelect.appendChild(typeOption);
        });
    } catch (error) {
        console.error("Discount types populate error: ", error);
    }
}

//  POPULATE - discount cap type
function populateCapTypes(capTypes = []) {
    try {
        if (capTypes.length === 0) {
            throw new Error("Invalid data: cap types should not be empty");
        }
        const capTypeSelect = document.getElementById('discount-cap-type');
        capTypes.forEach(cType => {
            const cTypeOption = document.createElement('option');
            cTypeOption.value = cType;
            cTypeOption.textContent = cType.charAt(0).toUpperCase()
                                        + cType.slice(1).toLowerCase();
            capTypeSelect.appendChild(cTypeOption);
        });
    } catch (error) {
        console.error("Cap types populate error: ", error);
    }
}

//  POPULATE - discount status
function populateDiscountStatus(discountStatus = []) {
    try {
        if (discountStatus.length === 0) {
            throw new Error("Invalid data: discount status should not be empty");
        }
        const discountStatusSelect = document.getElementById('discount-status');
        discountStatus.forEach(status => {
            const statusOption = document.createElement('option');
            statusOption.value = status;
            statusOption.textContent = status.charAt(0).toUpperCase()
                                        + status.slice(1).toLowerCase();
            discountStatusSelect.appendChild(statusOption);
        });
    } catch (error) {
        console.error("Discount status populate error: ", error);
    }
}



/*
*   ---------------------------------
*   ********* OTHER methods *********
*   ---------------------------------
*/

//  REFRESH - form
function refreshForm() {
    const fields = document.querySelectorAll('.form-input');
    const hasData = [...fields].some(el => el.value && el.value.trim() !== "");

    if (!hasData) return;

    confirmModal.querySelector('.modal-message').textContent = "Do you really want to clear the form data?";

    confirmModal.querySelector('#confirm-yes').addEventListener('click', () => {
        fields.forEach(el => {
            if (el.tagName === "SELECT") {
                el.selectedIndex = 0;
            } else {
                el.value = "";
            }
        });
        confirmModal.style.display = 'none';
    });

    confirmModal.querySelector('#confirm-no').addEventListener('click', () => {
        confirmModal.style.display = 'none';
    });

    confirmModal.style.display = 'flex';
}



/*
*   --------------------------------
*   ********* INIT methods *********
*   --------------------------------
*/

//  Choices init
const initializeChoices = () => {
    try {
        new Choices('#discount-level', {
            placeholder: true,
            placeholderValue: 'Enter discount level',
            searchEnabled: true,
            searchPlaceholderValue: 'Search level...',
            shouldSort: true
        });
        new Choices('#discount-type', {
            placeholder: true,
            placeholderValue: 'Enter discount type',
            searchEnabled: true,
            searchPlaceholderValue: 'Search type...',
            shouldSort: true
        });
        new Choices('#discount-cap-type', {
            placeholder: true,
            placeholderValue: 'Enter cap type',
            searchEnabled: true,
            searchPlaceholderValue: 'Search cap type...',
            shouldSort: true
        });
        new Choices('#discount-status', {
            placeholder: true,
            placeholderValue: 'Enter status',
            searchEnabled: true,
            searchPlaceholderValue: 'Search status...',
            shouldSort: true
        });
    } catch (error) {
        console.error("Choices initialization error: ", error);
        return;
    }
};

//  Date pickr init
const initializeDateFlatPickr = () => {
    flatpickr("#discount-start-date", {
        enableTime: true,
        dateFormat: "Y-m-d\\TH:i",
        altInput: true,
        altFormat: "d-m-Y h:i K",
        minDate: "today",
        time_24hr: false
    });
    flatpickr("#discount-end-date", {
        enableTime: true,
        dateFormat: "Y-m-d\\TH:i",
        altInput: true,
        altFormat: "d-m-Y h:i K",
        minDate: "today",
        time_24hr: false
    });
};



//  form submit
window.submitForm = async function () {
    try {
        const formData = {
            discountCode: document.getElementById('discount-code').value,
            discountName: document.getElementById('discount-name').value.trim(),
            discountDescription: document.getElementById('discount-description').value.trim(),
            level: document.getElementById('discount-level').value,
            type: document.getElementById('discount-type').value,
            discountStatus: document.getElementById('discount-status').value,
            value: document.getElementById('discount-value').value.trim(),
            capType: document.getElementById('discount-cap-type').value,
            capValue: document.getElementById('discount-cap-value').value.trim(),
            priority: document.getElementById('discount-priority').value.trim(),
            startAt: document.getElementById('discount-start-date').value || null,
            endAt: document.getElementById('discount-end-date').value || null
        };

        const response = await fetch(saveDiscountUrl, {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            credentials: "include",
            body: JSON.stringify(formData)
        });

        const data = await response.json().catch(() => ({}));

        if (response.ok) {
            window.location.replace(`${discountSummaryRedirect}?id=${data.discountId}&success=true`);
//            window.location.replace(`/discount/summary?id=${data.discountId}&success=true`);
        } else if (response.status >= 500) {
            console.error("Server error: ", data.message);
            window.showToast("Server error. Try again.", false);
        } else if (response.status >= 400) {
            console.error("Client error: ", data.message);
            window.showToast(data.message || "Invalid request.", false);
        }
    } catch (error) {
        console.error("Form submit failed: ", error);
        window.showToast("Error saving discount. Try again.", false);
    }
}



/*
*   --------------------------------
*   ********* LOAD methods *********
*   --------------------------------
*/

//  LOADER - Select fields
const loadSelectFields = async () => {
    try {
        //  Fetching & validation
        const discountLevels = await fetchDiscountLevel();
        if (!discountLevels) {
            console.log("Discount levels not found");
            return;
        }
        if (!Array.isArray(discountLevels.enumValues)) {
            throw new Error("Invalid data format: discount levels should be array");
        }

        const discountTypes = await fetchDiscountType();
        if (!discountTypes) {
            console.log("Discount types not found");
            return;
        }
        if (!Array.isArray(discountTypes.enumValues)) {
            throw new Error("Invalid data format: discount types should be array");
        }

        const capTypes = await fetchCapType();
        if (!capTypes) {
            console.log("Cap types not found");
            return;
        }
        if (!Array.isArray(capTypes.enumValues)) {
            throw new Error("Invalid data format: cap types should be array");
        }

        const discountStatus = await fetchDiscountStatus();
        if (!discountStatus) {
            console.log("Discount status not found");
            return;
        }
        if (!Array.isArray(discountStatus.enumValues)) {
            throw new Error("Invalid data format: discount status should be array");
        }

        //  populating data
        populateDiscountLevels(discountLevels.enumValues);
        populateDiscountTypes(discountTypes.enumValues);
        populateCapTypes(capTypes.enumValues);
        populateDiscountStatus(discountStatus.enumValues);

        initializeChoices();

    } catch (error) {
        console.error("Error loading Select fields: ", error);
    }
};


//  DOM Loading event
document.addEventListener('DOMContentLoaded', async () => {

    confirmModal = document.getElementById('confirm-modal');
    refreshButton = document.getElementById('refresh-button');
    refreshButton.addEventListener('click', () => {
        refreshForm();
    });

    await loadSelectFields();
    initializeDateFlatPickr();  //  ## add date constraints to get appropriate date inputs. from user
});

