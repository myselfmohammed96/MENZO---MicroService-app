
//  Save new address / update address - backend fetch api
async function saveAddress(newAddress, addressForm) {
    try {
        const response = await fetch(newAddress ? "/url1" : "/url2", {
            method: newAddress ? "POST" : "PUT",
            credentials: "include",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(addressForm)
        });
        return response;
    } catch (err) {
        console.error("Address saving failed." + err);
        return;
    }
}

async function processAddressData(newAddress, addressForm) {
    const response = saveAddress(newAddress, addressForm);
    if(newAddress && response.ok) {
        const addressContainer = document.getElementById('address-container');
        addressContainer.innerHTML += `
            <div class="address-entity">
                <div class="address-header">
                    <h2 class="user-name">${addressForm.firstName} ${addressForm.lastName}</h2>
                    <div class="action-buttons">
                        <button class="edit-button">Edit</button>
                        <button class="delete-button">Delete</button>
                    </div>
                </div>
                <p class="address">${addressForm.houseNo}, ${addressForm.street}, ${addressForm.landmark ? addressForm.landmark + ", " : ""}${addressForm.city}, ${addressForm.state}, ${addressForm.country}, ${addressForm.pincode}</p>
                <hr class="hr-line-12">
            </div>
        `;

        window.showToast("Address added successfully", true);
    }
}

//  Processing modal data
const addressErrorMsg = {
    firstName: document.getElementById('first-name-error-message'),
    lastName: document.getElementById('last-name-error-message'),
    phone: document.getElementById('phone-number-error-message'),
    houseNo: document.getElementById('house-no-error-message'),
    street: document.getElementById('street-error-message'),
    landmark: document.getElementById('landmark-error-message'),
    city: document.getElementById('city-error-message'),
    state: document.getElementById('state-error-message'),
    country: document.getElementById('country-error-message'),
    pincode: document.getElementById('pincode-error-message')
}

const errorMsg = "*This field cannot be empty.";

function saveModalData() {
    const addressForm = {
        firstName: document.getElementById('modal-first-name').value,
        lastName: document.getElementById('modal-last-name').value,
        phone: document.getElementById('modal-phone-number').value,
        houseNo: document.getElementById('modal-house-no').value,
        street: document.getElementById('modal-street').value,
        landmark: document.getElementById('modal-landmark').value,
        city: document.getElementById('modal-city').value,
        state: document.getElementById('modal-state').value,
        country: document.getElementById('modal-country').value,
        pincode: document.getElementById('modal-pincode').value
    }

    const newAddress = document.getElementById('new-address-indicator').value == 1 ? true : false;

    if (!addressForm.firstName || !addressForm.lastName || !addressForm.phone ||
        !addressForm.houseNo || !addressForm.street || !addressForm.city ||
        !addressForm.state || !addressForm.country || !addressForm.pincode) {

        addressErrorMsg.firstName.textContent = !addressForm.firstName ? errorMsg : "";
        addressErrorMsg.lastName.textContent = !addressForm.lastName ? errorMsg : "";
        addressErrorMsg.phone.textContent = !addressForm.phone ? errorMsg : "";
        addressErrorMsg.houseNo.textContent = !addressForm.houseNo ? errorMsg : "";
        addressErrorMsg.street.textContent = !addressForm.street ? errorMsg : "";
        addressErrorMsg.city.textContent = !addressForm.city ? errorMsg : "";
        addressErrorMsg.state.textContent = !addressForm.state ? errorMsg : "";
        addressErrorMsg.country.textContent = !addressForm.country ? errorMsg : "";
        addressErrorMsg.pincode.textContent = !addressForm.pincode ? errorMsg : "";
        return;
    }
    console.log(newAddress, addressForm);

    // processAddressData(addressForm)
    closeModal();
}


document.addEventListener("DOMContentLoaded", () => {
    const addButton = document.querySelector('.add-address-button');
    const modal = document.querySelector('.modal');

    //  Add new address button
    addButton.addEventListener("click", () => {
        document.getElementById('modal-title').textContent = "Add Address";
        document.getElementById('new-address-indicator').value = 1;
        modal.style.display = "flex";
    });

});

function closeModal() {
    document.querySelector('.modal').style.display = "none";
}


