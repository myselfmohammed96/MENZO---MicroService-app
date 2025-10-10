const addressUrl = "http://localhost:8080/user/address";

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





//  ********* Fetch APIs *********

//  Save new address / update address
async function saveAddress(id = null, addressForm) {
    try {
        const response = await fetch(id ? `${addressUrl}?id=${id}` : `${addressUrl}`, {
            method: id ? "PUT" : "POST",
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

//  Delete address
async function deleteAddress(id) {
    try {
        const response = await fetch(`${addressUrl}?id=${id}`, {
            method: "DELETE",
            credentials: "include"
        });
        return response;
    } catch (err) {
        console.error("Address delete failed." + err);
        return;
    }
}





async function deleteUserAddress(id) {
    const response = await deleteAddress(id);
    if(response.ok) {
        const deletedAddress = document.querySelector(`.address-entity[data-id='${id}']`);
        if(deletedAddress) deletedAddress.remove();

        window.showToast("Address deleted successfully", true);
    } else {
        window.showToast("Address delete failed.", false);
    }
}

async function processAddressData(id = null, addressForm) {
    const response = await saveAddress(id, addressForm);

    if(id === null && response.ok) {
        const result = await response.json();
        const savedAddressId = result.data.id;

        const addressContainer = document.getElementById('address-container');
        addressContainer.innerHTML += `
            <div data-id="${savedAddressId}" class="address-entity">
                <div class="address-header">
                    <h2 class="user-name">${addressForm.firstName} ${addressForm.lastName}</h2>
                    <div class="action-buttons">
                        <button onclick="openModal(${savedAddressId})" class="edit-button">Edit</button>
                        <button onclick="deleteUserAddress(${savedAddressId})" class="delete-button">Delete</button>
                    </div>
                </div>
                <p class="address">${addressForm.unitAddress}, ${addressForm.street}, ${addressForm.landmark ? addressForm.landmark + ", " : ""}${addressForm.city}, ${addressForm.state}, ${addressForm.country}, ${addressForm.pincode}</p>
                <hr class="hr-line-12">
            </div>
        `;
        window.showToast("Address added successfully", true);
    } else if(id && response.ok) {
        const updatedAddress = document.querySelector(`.address-entity[data-id='${id}']`);

        const nameElement = updatedAddress.querySelector('.user-name');
        const address = updatedAddress.querySelector('.address');

        nameElement.textContent = `${addressForm.firstName} ${addressForm.lastName}`;
        address.textContent = `${addressForm.unitAddress}, ${addressForm.street}, ${addressForm.landmark ? addressForm.landmark + ", " : ''}${addressForm.city}, ${addressForm.state}, ${addressForm.country}, ${addressForm.pincode}`;

        window.showToast("Address updated successfully", true);

    } else {}
}



//  ********* Modal handling *********

//  Open modal for Add & Edit address
function openModal(id = null) {
    const modal = document.querySelector('.modal');
    document.querySelectorAll('#user-modal #form-container input').forEach(input => input.value = '');

    if(id === null) {
        document.getElementById('modal-title').textContent = "Add Address";
        document.getElementById('address-id').value = null;
        modal.style.display = "flex";
    }
    if (id) {
        document.getElementById('modal-title').textContent = "Edit Address";
        document.getElementById('address-id').value = id;
        modal.style.display = "flex";
    }
}

//  Processing modal data
function saveModalData() {
    const addressForm = {
        firstName: document.getElementById('modal-first-name').value,
        lastName: document.getElementById('modal-last-name').value,
        phoneNumber: document.getElementById('modal-phone-number').value,
        unitAddress: document.getElementById('modal-house-no').value,
        street: document.getElementById('modal-street').value,
        landmark: document.getElementById('modal-landmark').value,
        city: document.getElementById('modal-city').value,
        state: document.getElementById('modal-state').value,
        country: document.getElementById('modal-country').value,
        pincode: document.getElementById('modal-pincode').value
    }

    const addressId = document.getElementById('address-id').value;
    const id = addressId === "null" || addressId === "" ? null : addressId;

    if (!addressForm.firstName || !addressForm.lastName || !addressForm.phoneNumber ||
        !addressForm.unitAddress || !addressForm.street || !addressForm.city ||
        !addressForm.state || !addressForm.country || !addressForm.pincode) {

        addressErrorMsg.firstName.textContent = !addressForm.firstName ? errorMsg : "";
        addressErrorMsg.lastName.textContent = !addressForm.lastName ? errorMsg : "";
        addressErrorMsg.phone.textContent = !addressForm.phoneNumber ? errorMsg : "";
        addressErrorMsg.houseNo.textContent = !addressForm.unitAddress ? errorMsg : "";
        addressErrorMsg.street.textContent = !addressForm.street ? errorMsg : "";
        addressErrorMsg.city.textContent = !addressForm.city ? errorMsg : "";
        addressErrorMsg.state.textContent = !addressForm.state ? errorMsg : "";
        addressErrorMsg.country.textContent = !addressForm.country ? errorMsg : "";
        addressErrorMsg.pincode.textContent = !addressForm.pincode ? errorMsg : "";
        return;
    }
    console.log(id, addressForm);
    processAddressData(id, addressForm)
    closeModal();
}

//  Close modal
function closeModal() {
    document.querySelector('.modal').style.display = "none";
}

