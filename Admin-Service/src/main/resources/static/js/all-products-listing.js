const getAllProductsListing = "http://localhost:8080/products/all-products-listing";
const getProductItemsByProductId = "http://localhost:8080/admin/product-items"

document.addEventListener("DOMContentLoaded", async () => {
    const tBody = document.getElementById('product-table-body');
    const paginationContainer = document.querySelector('.pagination');
    let requestDto = null;

    let currentPage = 1;
    const pageSize = 10;

    const fetchProducts = async (page = 1, dto) => {
        try {
            const response = await fetch(`${getAllProductsListing}?page=${page - 1}&size=${pageSize}`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: dto ? JSON.stringify(dto) : null
            });
            if (!response.ok) throw new Error("Failed to fetch products");
            return response.json();
        } catch(err) {
            console.error("Error", err);
            return null;
        }
    };

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
                        <a class="action-btn action-view-btn" href="${getProductItemsByProductId}?id=${product.id}">View</a>
                        <a class="action-btn action-delete-btn" href="#">Delete</a>
                    </td>
                </tr>
            `;
            tBody.insertAdjacentHTML('beforeend', row);
        });
    };

    const renderPagination = (totalPages) => {
        paginationContainer.innerHTML = '';
        if (currentPage > 1) {
            paginationContainer.innerHTML += `<a href="#" data-page="${currentPage - 1}">&laquo;</a>`;
        }
        for (let i = 1; i <= totalPages; i++) {
            paginationContainer.innerHTML +=`<a href="#" data-page="${i}" class="${i === currentPage ? 'active-page' : ''}">${i}</a>`;
        }
        if (currentPage < totalPages) {
            paginationContainer.innerHTML += `<a href="#" data-page="${currentPage + 1}">&raquo;</a>`;
        }

        attachPaginationEvents();
    };

    const attachPaginationEvents = () => {
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

//    document.querySelector(".apply-filter-btn").addEventListener("click", function () {
//            let filterRequestDtos = [];
//
//            document.querySelectorAll(".filter-section").forEach(section => {
//                let h3 = section.querySelector("p");
//                let filterType = h3 ? h3.innerText.trim() : null;
//
//                let selectedValues = [...section.querySelectorAll("input[type='checkbox']:checked")]
//                    .map(cb => cb.value.trim());
//
//                if(selectedValues.length > 0) {
//                    filterRequestDtos.push({
//                        filterType: filterType,
//                        values: selectedValues.join(", ")
//                    });
//                }
//            });
//
//            let requestDto = { filterRequestDtos };
//            console.log(requestDto);
//    });

    const loadProducts = async () => {
        const result = await fetchProducts(currentPage, requestDto);
        if (!result) return;

        const products = result.content;
        const totalPages = result.totalPages;

        renderProducts(products);
        renderPagination(totalPages);
    };

    window.loadProducts = loadProducts;
    window.setRequestDto = (dto) => { requestDto = dto };
    loadProducts();
});



//    console.log("The script has loaded - beginning");
//    const loadProducts = async() => {
//        const response = await fetch(getAllProductsListing);
//        const data = await response.json();
//        const products = data.content;
//
//        tBody.innerHTML = '';
//
//        products.forEach(product => {
//            const statusColor = {
//                'ACTIVE': 'status-green',
//                'INACTIVE': 'status-red',
//                'PARTIALLY_ACTIVE': 'status-yellow'
//            }[product.activeStatus] || '';
//
//            const row = `
//                <tr>
//                    <td class="products-table-data">
//                        <div class="product-content-wrapper">
//                            <img class="product-icon-img" src="http://localhost:8080/${product.iconImage}" alt="">
//                            <span class="product-text">${product.productName}</span>
//                        </div>
//                    </td>
//                    <td class="center-text">${product.subCategoryName}</td>
//                    <td class="center-text">${product.startingPrice}</td>
//                    <td class="center-text">${product.totalItems}</td>
//                    <td class="center-text">
//                        <span class="status-indicator ${statusColor}">${product.activeStatus.replace('_', ' ')}</span>
//                    </td>
//                    <td class="center-text action-buttons-column">
//                        <a class="action-btn action-view-btn" href="#">View</a>
//                        <a class="action-btn action-delete-btn" href="#">Delete</a>
//                    </td>
//                </tr>
//            `;
//            tBody.insertAdjacentHTML('beforeend', row);
//        });
//    };

//    await loadProducts();
//    console.log("The script has loaded - end");
//});