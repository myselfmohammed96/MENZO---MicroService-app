const getAllItems = "http://localhost:8080/products/items";
const getItem = "";

let productId;
let itemsListWrapper;

const fetchAllItems = async () => {
    try {
        const response = await fetch(`${getAllItems}?id=${productId}`, {
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
        console.log(item.iconImage);
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
        itemStatus.classList.add('item-status', 'status-red');
        itemStatus.textContent = item.activeStatus;

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

const populateItemDetails = () => {}

const loadItems = async () => {
    try {
        itemsListWrapper.innerHTML = '';
        const items = await fetchAllItems();
        if (!items) return;
        populateItemsList(items);
    } catch (error) {
        console.error("Error loading product items");
    }
}

document.addEventListener("DOMContentLoaded", async () => {
    productId = document.getElementById('product-id').value;
    itemsListWrapper = document.getElementById('list-wrapper-body');

    loadItems();
});