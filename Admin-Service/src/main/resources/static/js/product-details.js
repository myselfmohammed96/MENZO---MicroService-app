const productDetailsAndItems = "http://localhost:8080/products/items";
const getItem = "http://localhost:8080/products/item";

let productId;
let itemsListWrapper;

const itemsMap = new Map();
window.itemDetailsMap = new Map();



//  ********* Image preview modal *********
const imagePreviewModal = (imageUrls) => {
    const wrappers = document.querySelectorAll('.image-preview-wrapper');
    const previewModal = document.getElementById('open-image-preview-modal');
    const modalImage = document.getElementById('modal-image');
    const modalCloseIcon = document.getElementById('modal-close');
    const leftImageNavBtn = document.querySelector('.left-nav-btn');
    const rightImageNavBtn = document.querySelector('.right-nav-btn');

    let currentImage;

    wrappers.forEach(wrapper => {
        const overlay = wrapper.querySelector('.image-overlay');

        wrapper.addEventListener('mousedown', () => {
            overlay.classList.add('active');
        });

        wrapper.addEventListener('mouseup', () => {
            overlay.classList.remove('active');
        });

        wrapper.addEventListener('mouseleave', () => {
            overlay.classList.remove('active');
        });

        wrapper.addEventListener('touchstart', () => {
            overlay.classList.add('active');
        });

        wrapper.addEventListener('touchend', () => {
            overlay.classList.remove('active');
        });

        //  ------- image click event -------
        wrapper.addEventListener('click', () => {
            const imageSrc = wrapper.querySelector('.image-preview').getAttribute('src');
            modalImage.setAttribute('src', imageSrc);

            currentImage = imageUrls.indexOf(imageSrc);
            previewModal.style.display = "flex";
        });
    });

    //  ------- left image button -------
    leftImageNavBtn.addEventListener('click', () => {
        if (currentImage === null || currentImage === undefined) return;

        if (currentImage === 0) {
            currentImage = imageUrls.length - 1;
            modalImage.setAttribute('src', "/" + imageUrls[currentImage]);
        } else {
            currentImage--;
            modalImage.setAttribute('src', "/" + imageUrls[currentImage]);
        }
    });

    //  ------- right image button -------
    rightImageNavBtn.addEventListener('click', () => {
        if (currentImage === null || currentImage === undefined) return;

        if (currentImage === imageUrls.length - 1) {
            currentImage = 0;
            modalImage.setAttribute('src', "/" + imageUrls[currentImage]);
            console.log("/" + imageUrls[currentImage]);
        } else {
            currentImage++;
            modalImage.setAttribute('src', "/" + imageUrls[currentImage]);
        }
    });

    //  ------- close image button -------
    modalCloseIcon.addEventListener('click', () => {
        currentImage = null;
        previewModal.style.display = 'none';
    });
}



//  ********* fetch methods *********

//  FETCH - basic product details & items list
const fetchProductDetailsAndItems = async () => {
    try {
        const response = await fetch(`${productDetailsAndItems}?id=${productId}`, {
            method: "GET",
            credentials: "include"
        });
        if (!response.ok) throw new Error("Failed to fetch Items");
        return response.json();
    } catch (error) {
        console.error("Error fetching items: ", error);
        return null;
    }
};

//  FETCH - product item details
const fetchItem = async (superSku) => {
    try {
        const response = await fetch(`${getItem}?ssku=${superSku}`, {
            method: "GET",
            credentials: "include"
        });
        if (!response.ok) throw new Error("Failed to fetch Item");
        return response.json();
    } catch (error) {
        console.error("Error fetching item: ", error);
        return null;
    }
}



//  ********* populating methods *********

//  POPULATE - basic product details
const populateProductDetails = (productDetails) => {
    document.getElementById('product-name').textContent = productDetails.productName ? productDetails.productName : "-";
    document.getElementById('category').textContent = productDetails.categoryName ? productDetails.categoryName : "-";
    document.getElementById('sub-category').textContent = productDetails.subCategoryName ? productDetails.subCategoryName : "-";
    document.getElementById('product-description').textContent = productDetails.description ? productDetails.description : "-";

    const podIndicator = document.getElementById('pod');
    podIndicator.textContent = productDetails.pod ? "Available" : "Not Available";
    podIndicator.classList = "";
    podIndicator.classList.add("pod-indicator", productDetails.pod ? "status-green" : "status-red");

    const productCreated = document.getElementById('product-created');
    if (productDetails.productCreated) {
        const createdDate = new Date(productDetails.productCreated);
        const createdDateFormat = createdDate.toLocaleDateString("en-GB", {
            day: "2-digit",
            month: "short",
            year: "numeric"
        });
        productCreated.textContent = createdDateFormat;
    } else {
        productCreated.textContent = "-";
    }
    const productUpdated = document.getElementById('product-updated');

    document.getElementById('item-weight').textContent = productDetails.itemWeight ? productDetails.itemWeight + " g" : "-";
    document.getElementById('generic-name').textContent = productDetails.genericName ? productDetails.genericName : "-";
    document.getElementById('country-of-origin').textContent = productDetails.countryOfOrigin ? productDetails.countryOfOrigin : "-";
    document.getElementById('manufacturer').textContent;
    document.getElementById('packer').textContent;
}

//  POPULATE - items list
let itemCount = 1;
const populateItemsList = (item, itemSelected) => {
    const itemList = document.createElement('div');
    itemList.classList.add('item-list');
    if(itemCount === 1) {
        itemList.classList.add('selected-item');
    }

    const itemListContent = document.createElement('div');
    itemListContent.classList.add('item-list-content');

    //  ------- item count number -------
    const itemCounterContainer = document.createElement('div');
    itemCounterContainer.classList.add('item-counter-container');

    const itemCountText = document.createElement('p');
    itemCountText.textContent = itemCount;
    itemCount++;

    itemCounterContainer.appendChild(itemCountText);

    //  ------- item icon image -------
    const itemIconContainer = document.createElement('div');
    itemIconContainer.classList.add('item-icon-container');

    const itemIcon = document.createElement('img');
    itemIcon.src = "/" + item.iconImage;

    itemIconContainer.appendChild(itemIcon);

    //  ------- item text content -------
    const itemTextContainer = document.createElement('div');
    itemTextContainer.classList.add('item-text-container');

    //  ------- item text content - row 1 -------
    const itemText1 = document.createElement('div');
    itemText1.classList.add('item-text-1');

    //  ------- item text content - row 1 - super SKU -------
    const itemSkuContainer = document.createElement('div');
    itemSkuContainer.classList.add('item-sku-container');

    const itemSku = document.createElement('p');
    itemSku.textContent = item.superSku;

    itemSkuContainer.appendChild(itemSku);

    //  ------- item text content - row 1 - item status -------
    const itemStatusContainer = document.createElement('div');
    itemStatusContainer.classList.add('item-status-container');

    const itemStatus = document.createElement('span');
    let colorClass = {
        ACTIVE: 'status-green',
        INACTIVE: 'status-red',
        PARTIAL: 'status-yellow'
    };
    let statusColor = colorClass[item.activeStatus];
    itemStatus.classList.add('item-status');
//        if(item.activeStatus === "ACTIVE" || item.activeStatus === "INACTIVE" || item.activeStatus === "PARTIAL") {
    itemStatus.classList.add(statusColor);
//        }

    itemStatus.textContent = item.activeStatus.charAt(0).toUpperCase() + item.activeStatus.slice(1).toLowerCase();

    itemStatusContainer.appendChild(itemStatus);
    itemText1.append(itemSkuContainer, itemStatusContainer);

    //  ------- item text content - row 2 -------
    const itemText2 = document.createElement('div');
    itemText2.classList.add('item-text-2');

    const itemText2Row = document.createElement('div');
    itemText2Row.classList.add('item-text-2-row');

    //  ------- item text content - row 2 - color details -------
    const itemColor = document.createElement('div');
    itemColor.classList.add('item-color');

    const itemColorKey = document.createElement('p');
    itemColorKey.classList.add('item-color-key');
    itemColorKey.textContent = 'Color:';

    const itemColorValue = document.createElement('p');
    itemColorValue.classList.add('item-color-value');
    itemColorValue.textContent = item.color;

    const itemColorIcon = document.createElement('div');
    itemColorIcon.classList.add('item-color-icon');
    itemColorIcon.style.backgroundColor = item.hexCode;

    itemColor.append(
        itemColorKey,
        itemColorValue,
        itemColorIcon
    );

    //  ------- item text content - row 2 - stock details -------
    const itemStock = document.createElement('div');
    itemStock.classList.add('item-stock');

    const itemStockText = document.createElement('p');

    let stockStatusWords = item.stockStatus.split("_");
    let finalText = "";
    stockStatusWords.forEach(word => {
        if(word === "OF") {
            finalText += word.toLowerCase() + " ";
        } else {
            finalText += word.charAt(0).toUpperCase() + word.slice(1).toLowerCase() + " ";
        }
    });
    itemStockText.textContent = finalText;

    itemStock.appendChild(itemStockText);
    itemText2Row.append(
        itemColor,
        itemStock
    );
    itemText2.appendChild(itemText2Row);
    itemTextContainer.append(
        itemText1,
        itemText2
    );

    //  ------- item options button -------
    const itemOptionButtonContainer = document.createElement('div');
    itemOptionButtonContainer.classList.add('item-option-button-container');

    const itemOptionButton = document.createElement('img');
    itemOptionButton.src = "/media/menu-icon.png";

    itemOptionButtonContainer.appendChild(itemOptionButton);
    itemListContent.append(
        itemCounterContainer,
        itemIconContainer,
        itemTextContainer,
        itemOptionButtonContainer
    );
    itemList.appendChild(itemListContent);
    itemList.addEventListener('click', () => {
        document.querySelectorAll('.item-list').forEach(item => {
            item.classList.remove('selected-item');
        });
        itemList.classList.add('selected-item');
        changeItemDetails(item.superSku);
    });
    if (itemSelected) {
        document.querySelectorAll('.item-list').forEach(item => {
            item.classList.remove('selected-item');
        });
        itemList.classList.add('selected-item');
    }
    itemsListWrapper.appendChild(itemList);
}

//  POPULATE - item details - with images & size details
const populateItemDetails = (itemDetails) => {
    document.getElementById('starting-price').textContent = itemDetails.startingPrice ? itemDetails.startingPrice + "/-" : "-";
    document.getElementById('color').textContent = itemDetails.color ? itemDetails.color : "-";
    document.getElementById('color-icon').style.backgroundColor = itemDetails.hexCode ? itemDetails.hexCode : "none";
    document.getElementById('super-sku').textContent = itemDetails.superSku ? itemDetails.superSku : "-";

    if (itemDetails.imageUrls) {
        populateItemImages(itemDetails.imageUrls);
    }
    if (itemDetails.sizeDetails) {
        populateSizeDetails(itemDetails.sizeDetails);
    }
}

//  POPULATE - images
const populateItemImages = (imageUrls) => {
    const imagesContainer = document.querySelector('.product-images-container');
    imagesContainer.innerHTML = '';

    imageUrls.forEach(url => {
        const imagePreviewWrapper = document.createElement('div');
        imagePreviewWrapper.classList.add('image-preview-wrapper');

        const imagePreview = document.createElement('img');
        imagePreview.classList.add('image-preview');
        imagePreview.src = "/" + url;

        const imageOverlay = document.createElement('div');
        imageOverlay.classList.add('image-overlay');

        imagePreviewWrapper.append(
            imagePreview,
            imageOverlay
        );
        imagesContainer.appendChild(imagePreviewWrapper);
    });
    imagePreviewModal(imageUrls);
}

//  POPULATE - size details
const td = (textContent) => {
    const tdElement = document.createElement('td');
    tdElement.classList.add('center-text');
    tdElement.textContent = textContent;
    return tdElement;
}
const populateSizeDetails = (sizeDetails) => {
    const tBody = document.getElementById('size-table-body');
    tBody.innerHTML = '';

    sizeDetails.forEach(sDetails => {
        const tRow = document.createElement('tr');

        const status = document.createElement('td');
        status.classList.add('center-text');

        const statusSpan = document.createElement('span');
        statusSpan.classList.add("item-status", sDetails.active ? "status-green" : "status-red");
        statusSpan.textContent = sDetails.active ? 'Active' : 'Inactive';

        status.appendChild(statusSpan);

        const createdDate = new Date(sDetails.createdAt);
        const createdDateFormat = createdDate.toLocaleDateString("en-GB", {
            day: "2-digit",
            month: "short",
            year: "numeric"
        });

        const itemUpdated = document.createElement('td');
        itemUpdated.classList.add('center-text');
        itemUpdated.textContent = "22 Jul 24"

        const buttons = document.createElement('td');
        buttons.classList.add('center-text');

        //  ------- adding edit button -------
        const editBtn = document.createElement('button');
        editBtn.classList.add("edit-button");

        const editBtnIcon = document.createElement('img');
        editBtnIcon.src = "/media/edit.png";

        editBtn.appendChild(editBtnIcon);
        editBtn.addEventListener('click', () => {
            editItem(sDetails.itemId);
        });

        //  ------- adding delete button -------
        const deleteBtn = document.createElement('button');
        deleteBtn.classList.add('delete-button');

        const deleteBtnIcon = document.createElement('img');
        deleteBtnIcon.src = "/media/delete.png";

        deleteBtn.appendChild(deleteBtnIcon);
        deleteBtn.addEventListener('click', () => {
            deleteItem(sDetails.itemId);
        });

        buttons.append(
            editBtn,
            deleteBtn
        );

        tRow.append(
            td(sDetails.size),
            td(sDetails.sku),
            status,
            td(sDetails.qtyInStock + " units"),
            td(sDetails.price + "/-"),
            td(createdDateFormat),
            itemUpdated,
            buttons
        );
        tBody.appendChild(tRow);
    });
}



//  ********* 'edit' & 'delete' methods *********

//  EDIT - product item details
const editItem = async (itemId) => {
    console.log("edit itemId: " + itemId);
}

//  DELETE - product item
const deleteItem = async (itemId) => {
    console.log("delete itemId: ", itemId);
}



//  ********* other methods *********

//  CHANGE - item details (by selecting from the 'items list')
const changeItemDetails = async (superSku) => {
    //  ------- check if present in 'itemDetailsMap' -------
    if (itemDetailsMap.has(superSku)) {
        populateItemDetails(itemDetailsMap.get(superSku));
    } else {
        //  ------- fetch - if not present in map -------
        let itemDetails;
        try {
            itemDetails = await fetchItem(superSku);
            if (!itemDetails) {
                console.error("Error fetching itemDetails.");
                return;
            }
            const itemListingData = itemsMap.get(superSku);

            itemDetails.superSku = itemListingData.superSku;
            itemDetails.color = itemListingData.color;
            itemDetails.hexCode = itemListingData.hexCode;

            itemDetailsMap.set(itemListingData.superSku, itemDetails);
            populateItemDetails(itemDetails);
        } catch (error) {
            console.error("Error fetching item details with super SKU: " + superSku + " - " + error);
        }
    }
}

//  handle newly saved item data
window.handleSavedItemData = (savedItem) => {
    if (!savedItem || !savedItem.superSku) {
        console.error("Product save failed: invalid server response");
        return;
    }
    const itemListData = {
        superSku: savedItem.superSku,
        color: savedItem.color,
        hexCode: savedItem.hexCode,
        stockStatus: savedItem.stockStatus,
        activeStatus: savedItem.activeStatus,
        iconImage: savedItem.imageUrls[0]
    }
    itemsMap.set(savedItem.superSku, itemListData);
    populateItemsList(itemListData, true);

    itemDetailsMap.set(savedItem.superSku, savedItem);
    changeItemDetails(savedItem.superSku);
}



//  ********* data loader - on page load *********

// initial data loader
const loadInitialData = async () => {
    let firstItem;
    itemsListWrapper.innerHTML = '';

    let details;
    let itemDetails;

    //  ------- fetching initial data -------
    try {
        //  ------- fetching product details & available items list -------
        details = await fetchProductDetailsAndItems();
        if (!details) {
            console.error("Error fetching product details and items.");
            return;
        }
        details.productItems.forEach((item, index) => {
            if(index === 0) {
                firstItem = item;
            }
            itemsMap.set(item.superSku, item);
        });

        //  ------- fetching item details -------
        if (firstItem) {
            itemDetails = await fetchItem(firstItem.superSku);
            if (!itemDetails) {
                console.error("Error fetching itemDetails");
                return;
            }
        } else {
            console.error("No product item found.");
            return;
        }
        itemDetails.superSku = firstItem.superSku;      // ## better to do this in the backend
        itemDetails.color = firstItem.color;
        itemDetails.hexCode = firstItem.hexCode;

        itemDetailsMap.set(firstItem.superSku, itemDetails);
    } catch (error) {
        console.error("Error fetching initial data.", error);
    }

    //  ------- loading initial data to the template -------
    try {
        populateProductDetails(details);
        details.productItems.forEach(item => {
            populateItemsList(item);
        });
        populateItemDetails(itemDetails);
    } catch (error) {
        console.error("Error loading initial data.", error);
    }
}

//  DOM Loading event listener
document.addEventListener("DOMContentLoaded", async () => {
    productId = document.getElementById('product-id').value;
    itemsListWrapper = document.getElementById('list-wrapper-body');


//    //  product item form modal - toggle events
//    document.getElementById('add-item-button').addEventListener("click", () => {
//        toggleFormModal('open');
//    });
//
//    document.getElementById('modal-form-close').addEventListener("click", () => {
//        toggleFormModal('close');
//    });

    loadInitialData();
});