const getProductList = "http://localhost:8080/products/all-products";
let tBody;
let currentSortParam;
let currentRequestDto;

/*
*   ********* product page loading *********
*
*   fetch & populate products - with server-side pagination
*   update page with change in 'sort' & 'filters'
*
*/

//  fetch products
const fetchProducts = async function(sortParam = null, requestDto) {
    console.log("sortParam: ", sortParam);
    console.log("requestDto: ", requestDto);
    try {
        const url = sortParam
                        ? `${getProductList}?sort=${sortParam}`
                        : getProductList;
        const response = await fetch(url, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: requestDto
                        ? JSON.stringify(requestDto)
                        : null
        });
        if(!response.ok) throw new Error("Failed to fetch products");
        return response.json();
    } catch(error) {
        console.error("Error fetching products", error);
        return null;
    }
};

//  populate products
const renderProducts = (products) => {
    tBody.innerHTML = '';
    products.forEach(product => {
        const statusColor = {
            'ACTIVE': 'status-green',
            'INACTIVE': 'status-red',
            'PARTIALLY_ACTIVE': 'status-yellow'
        }[product.activeStatus] || '';

        const row = `
            <tr>
                <td class="products-table-data">
                    <div class="product-content-wrapper">
                        <img class="product-icon-img" src="http://localhost:8080/${product.iconImage}" alt="">
                        <span class="product-text">${product.productName}</span>
                    </div>
                </td>
                <td class="center-text">${product.subCategoryName}</td>
                <td class="center-text">${product.startingPrice}</td>
                <td class="center-text">${product.totalItems}</td>
                <td class="center-text">
                    <span class="status-indicator ${statusColor}">${product.activeStatus.replace('_', ' ')}</span>
                </td>
                <td class="center-text action-buttons-column">
                    <a class="action-btn action-view-btn" href="#">View</a>
                    <a class="action-btn action-delete-btn" href="#">Delete</a>
                </td>
            </tr>
        `;
        tBody.insertAdjacentHTML('beforeend', row);
    });
};

//  load products
const loadProducts = async () => {
    console.log("currentSortParam: ", currentSortParam);
    console.log("currentRequestDto: ", currentRequestDto);
    try {
        const result = await fetchProducts(currentSortParam, currentRequestDto);
        if (!result) return;
        renderProducts(result.content);
    } catch(error) {
        console.error("Failed to load products: ", error);
    }
}



/*
*   ******* update new 'sort' & 'filter' *******
*   get updated 'sort' & 'filter' options
*   And initiate page content update
*/

//  update new sort
window.updateNewSort = (newSortParam = null) => {
    if(newSortParam && newSortParam !== currentSortParam) {
        currentSortParam = newSortParam;
        loadProducts();
    }
}

//  update new filter
window.updateNewFilter = (newRequestDto) => {
    if(newRequestDto && newRequestDto !== currentRequestDto) {
        currentRequestDto = newRequestDto;
        loadProducts();
    }
}



//  ******* DOM loading event *******

document.addEventListener("DOMContentLoaded", () => {
    tBody = document.getElementById('product-table-body');
    loadProducts();
});