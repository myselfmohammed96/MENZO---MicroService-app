const getProductList = "http://localhost:8080/products/all-products";
let tBody;
let paginationContainer;
let currentSortParam;
let currentRequestDto;
let currentPage = 1;
//  ##  apply server side default pageSize control -
//  (with admin preference & user preference on client side)
let pageSize = 3;

/*
*   ********* product page loading *********
*
*   fetch & populate products - with server-side pagination
*   update page with change in 'sort' & 'filters'
*
*/

//  fetch products
const fetchProducts = async function(sortParam = null, requestDto = null, page) {
//    console.log("sortParam: ", sortParam);
//    console.log("requestDto: ", requestDto);
    try {
        const url = sortParam
                        ? `${getProductList}?page=${page - 1}&size=${pageSize}&sort=${sortParam}`
                        : `${getProductList}?page=${page - 1}&size=${pageSize}`;
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
        console.error("Error fetching products - ", error);
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
                <td class="center-text">${product.minPrice}</td>
                <td class="center-text">${product.colorCount}</td>
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

//  render pagination
const renderPagination = (totalPages) => {
//    console.log("pagination -> " + totalPages)
    paginationContainer.innerHTML = '';
    if (currentPage > 1) {
        paginationContainer.innerHTML += `<a href="#" data-page="${currentPage - 1}">&laquo;</a>`;
    }
    for (let i=1; i<=totalPages; i++) {
        paginationContainer.innerHTML += `<a href="#" data-page="${i}" class="${i === currentPage ? 'active-page' : ''}">${i}</a>`;
    }
    if (currentPage < totalPages) {
        paginationContainer.innerHTML += `<a href="#" data-page="${currentPage + 1}">&raquo;</a>`;
    }

    //  adding pagination event listeners
    document.querySelectorAll('.pagination a').forEach(a => {
        a.addEventListener('click', async (e) => {
            e.preventDefault();
            const page = parseInt(e.target.dataset.page);
            if (page !== currentPage) {
                currentPage = page;
                await loadProducts();
            }
        });
    });
};

//  load products
const loadProducts = async () => {
    console.log("currentSortParam: ", currentSortParam);
    console.log("currentRequestDto: ", currentRequestDto);
//    console.log("currentPage: ", currentPage);
//    console.log("pageSize: ", pageSize);
    try {
        const result = await fetchProducts(currentSortParam, currentRequestDto, currentPage);
        if (!result) return;
        renderProducts(result.content);
//        console.log("total => ", result.page.totalPages)
        renderPagination(result.page.totalPages);
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
window.updateNewSort = async (newSortParam = null) => {
    if(newSortParam && newSortParam !== currentSortParam) {
        currentSortParam = newSortParam;
        await loadProducts();
    }
}

//  update new filter
window.updateNewFilter = async (newRequestDto) => {
    if(newRequestDto && newRequestDto !== currentRequestDto) {
        currentRequestDto = newRequestDto;
        await loadProducts();
    }
}



//  ******* DOM loading event *******

document.addEventListener("DOMContentLoaded", async () => {
    tBody = document.getElementById('product-table-body');
    paginationContainer = document.querySelector('.pagination');
    await loadProducts();
});