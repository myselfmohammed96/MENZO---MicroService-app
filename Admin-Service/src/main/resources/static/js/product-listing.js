const getProductList = "http://localhost:8080/products/all-products";
let tBody;

//  ******* fetch products *******
const fetchProducts = async function(sortParam = null) {
    try {
        const url = sortParam ? `${getProductList}?sort=${sortParam}` : getProductList;
        const response = await fetch(url, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: null
        });
        if(!response.ok) throw new Error("Failed to fetch products");
        return response.json();
    } catch(error) {
        console.error("Error fetching products", error);
        return null;
    }
};



//  ******* render products *******
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



//  ******* load products *******
window.loadProducts = async (sortParam = null) => {
    try {
        console.log(sortParam + " ... <-");
        const result = await fetchProducts(sortParam);
        if (!result) return;
        renderProducts(result.content);
    } catch(error) {
        console.error("Failed to load products: ", error);
    }
}



//  ******* DOM loading event *******
document.addEventListener("DOMContentLoaded", () => {
    tBody = document.getElementById('product-table-body');
    loadProducts();
});