const productDetailsAndItems = "http://localhost:8080/products/items";
const getItem = "http://localhost:8080/products/item";

let productId;
let itemsListWrapper;

window.itemInfoMap = new Map();



//  ********* fetch & populate - basic product details & product items list *********

//  fetch basic product details and items list
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

//  populate basic product details
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

//  populate items list
let itemCount = 1;
const populateItemsList = (items) => {

    items.forEach(item => {
        const itemList = document.createElement('div');
        itemList.classList.add('item-list');

        const itemListContent = document.createElement('div');
        itemListContent.classList.add('item-list-content');

        //  item count number
        const itemCounterContainer = document.createElement('div');
        itemCounterContainer.classList.add('item-counter-container');

        const itemCountText = document.createElement('p');
        itemCountText.textContent = itemCount;
        itemCount++;

        itemCounterContainer.appendChild(itemCountText);

        //  item icon image
        const itemIconContainer = document.createElement('div');
        itemIconContainer.classList.add('item-icon-container');

        const itemIcon = document.createElement('img');
        itemIcon.src = "/" + item.iconImage;

        itemIconContainer.appendChild(itemIcon);

        //  item text content
        const itemTextContainer = document.createElement('div');
        itemTextContainer.classList.add('item-text-container');

        //  item text content - row 1
        const itemText1 = document.createElement('div');
        itemText1.classList.add('item-text-1');

        //  item text content - row 1 - super SKU
        const itemSkuContainer = document.createElement('div');
        itemSkuContainer.classList.add('item-sku-container');

        const itemSku = document.createElement('p');
        itemSku.textContent = item.superSku;

        itemSkuContainer.appendChild(itemSku);

        //  item text content - row 1 - item status
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
        if(item.activeStatus === "ACTIVE" || item.activeStatus === "INACTIVE" || item.activeStatus === "PARTIAL") {
            itemStatus.classList.add(statusColor);
        }

        itemStatus.textContent = item.activeStatus.charAt(0).toUpperCase() + item.activeStatus.slice(1).toLowerCase();

        itemStatusContainer.appendChild(itemStatus);
        itemText1.append(itemSkuContainer, itemStatusContainer);

        //  item text content - row 2
        const itemText2 = document.createElement('div');
        itemText2.classList.add('item-text-2');

        const itemText2Row = document.createElement('div');
        itemText2Row.classList.add('item-text-2-row');

        //  item text content - row 2 - color details
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

        itemColor.append(itemColorKey, itemColorValue, itemColorIcon);

        //  item text content - row 2 - stock details
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
        itemText2Row.append(itemColor, itemStock);
        itemText2.appendChild(itemText2Row);
        itemTextContainer.append(itemText1, itemText2);

        //  item options button
        const itemOptionButtonContainer = document.createElement('div');
        itemOptionButtonContainer.classList.add('item-option-button-container');

        const itemOptionButton = document.createElement('img');
        itemOptionButton.src = "/media/menu-icon.png";

        itemOptionButtonContainer.appendChild(itemOptionButton);
        itemListContent.append(itemCounterContainer, itemIconContainer, itemTextContainer, itemOptionButtonContainer);
        itemList.appendChild(itemListContent);
        itemsListWrapper.appendChild(itemList);
    });
}



//  ********* fetch & populate - items details *********

//  fetch product item details
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

//  edit product item details
const editItem = async (itemId) => {
    console.log("edit itemId: " + itemId);
}

//  delete product item
const deleteItem = async (itemId) => {
    console.log("delete itemId: ", itemId);
}

//  populate item details - with images & size details
const populateItemDetails = (itemDetails) => {
    document.getElementById('starting-price').textContent = itemDetails.startingPrice ? itemDetails.startingPrice + "/-" : "-";
    document.getElementById('color').textContent = itemDetails.color ? itemDetails.color : "-";
    document.getElementById('color-icon').style.backgroundColor = itemDetails.hexCode ? itemDetails.hexCode : "none";
    document.getElementById('super-sku').textContent = itemDetails.superSku ? itemDetails.superSku : "-";

//    const itemCreated = document.getElementById('item-created');
//    if (itemDetails.itemCreated) {
//        const createdDate = new Date(itemDetails.itemCreated);
//        const createdDateFormat = createdDate.toLocaleDateString("en-GB", {
//            day: "2-digit",
//            month: "short",
//            year: "numeric"
//        });
//        itemCreated.textContent = createdDateFormat;
//    } else {
//        itemCreated.textContent = "-";
//    }
//    itemUpdated: document.getElementById('item-updated');

    if (itemDetails.imageUrls) {
        populateItemImages(itemDetails.imageUrls);
    }
    if (itemDetails.sizeDetails) {
        populateSizeDetails(itemDetails.sizeDetails);
    }
}

//  populate images
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

        imagePreviewWrapper.append(imagePreview, imageOverlay);
        imagesContainer.appendChild(imagePreviewWrapper);
    });
}

//  populate size details
const populateSizeDetails = (sizeDetails) => {
    const tBody = document.getElementById('size-table-body');
    tBody.innerHTML = '';

    sizeDetails.forEach(sDetails => {
        const tRow = document.createElement('tr');

        const size = document.createElement('td');
        size.classList.add('center-text');
        size.textContent = sDetails.size;

        const sku = document.createElement('td');
        sku.classList.add('center-text');
        sku.textContent = sDetails.sku

        const status = document.createElement('td');
        status.classList.add('center-text');

        const statusSpan = document.createElement('span');
        statusSpan.classList.add("item-status", sDetails.active ? "status-green" : "status-red");
        statusSpan.textContent = sDetails.active ? 'Active' : 'Inactive';

        status.appendChild(statusSpan);

        const stock = document.createElement('td');
        stock.classList.add('center-text');
        stock.textContent = sDetails.qtyInStock + " units";

        const itemCreated = document.createElement('td');
        itemCreated.classList.add('center-text');
        const createdDate = new Date(sDetails.createdAt);
        const createdDateFormat = createdDate.toLocaleDateString("en-GB", {
            day: "2-digit",
            month: "short",
            year: "numeric"
        });
        itemCreated.textContent = createdDateFormat;

        const itemUpdated = document.createElement('td');
        itemUpdated.classList.add('center-text');
        itemUpdated.textContent = "22 Jul 24"

        const buttons = document.createElement('td');
        buttons.classList.add('center-text');

        //  adding edit button
        const editBtn = document.createElement('button');
        editBtn.classList.add("edit-button");

        const editBtnIcon = document.createElement('img');
        editBtnIcon.src = "/media/edit.png";

        editBtn.appendChild(editBtnIcon);
        editBtn.addEventListener('click', () => {
            editItem(sDetails.itemId);
        });
//        editBtn.textContent = '✎';

        //  adding delete button
        const deleteBtn = document.createElement('button');
        deleteBtn.classList.add('delete-button');

        const deleteBtnIcon = document.createElement('img');
        deleteBtnIcon.src = "/media/delete.png";

        deleteBtn.appendChild(deleteBtnIcon);
        deleteBtn.addEventListener('click', () => {
            deleteItem(sDetails.itemId);
        });
//        deleteBtn.textContent = '🗑︎';

        buttons.append(editBtn, deleteBtn);

        tRow.append(size, sku, status, stock, itemCreated, itemUpdated, buttons);
        tBody.appendChild(tRow);
    });
}



//  ********* initial data loader - on page load *********
const loadInitialData = async () => {
    let firstItem;
    try {
        itemsListWrapper.innerHTML = '';
        const details = await fetchProductDetailsAndItems();

        if (!details) return;
        populateProductDetails(details);
        populateItemsList(details.productItems);
        firstItem = details.productItems[0];
    } catch (error) {
        console.error("Error loading product items");
    }
    try {
        if (firstItem) {
            const itemDetails = await fetchItem(firstItem.superSku);
            if(!itemDetails) return;
            itemDetails.superSku = firstItem.superSku
            itemDetails.color = firstItem.color
            itemDetails.hexCode = firstItem.hexCode

            itemInfoMap.set(firstItem.superSku, itemDetails);
            populateItemDetails(itemDetails)
        } else {
            console.error("No product item found");
            return;
        }
    } catch (error) {
        console.error("Error loading initial item");
    }
}

//const closeFormModal = () => {
//    const formModal = document.getElementById('add-item-form-modal');
//    const modalDisplay = getComputedStyle(formModal).display;
//
//    if(modalDisplay ===)
//}



//  ********* DOM Loading event listener *********
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