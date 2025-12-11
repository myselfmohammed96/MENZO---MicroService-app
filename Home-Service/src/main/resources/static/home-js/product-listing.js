const getProductList = "http://localhost:8080/products/user-listing";

let productGrid;

let currentSortParam;
let currentRequestDto;
let currentPage = 1;
let pageSize = 6;



//  ********* FETCH methods *********

//  FETCH - products
async function fetchProducts(sortParam = null, requestDto = null, page) {
    try {
        const url = sortParam
                        ? `${getProductList}?page=${page - 1}&size=${pageSize}&sort=${sortParam}`
                        : `${getProductList}?page=${page - 1}&size=${pageSize}`;
//                        console.log(pageSize + " pageSize");
//                        console.log("page - " + page);
//                        console.log("url - " + url);
        const response = await fetch(url, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: requestDto
                        ? JSON.stringify(requestDto)
                        : null
        });
        if (!response.ok) throw new Error("Failed to fetch products");
        return response.json();
    } catch (error) {
        console.error("Error fetching products.", error);
        return null;
    }
}



//  ********* POPULATE methods *********

//  POPULATE - products
function populateProducts(products = []) {
    try {
        if (products.length === 0) {
            throw new Error("Invalid data: products should not be empty");
        }
        productGrid.innerHTML = '';

        products.forEach(product => {
            if (product && product.productId !== undefined) {
                //  product card
                const card = document.createElement('div');
                card.classList.add('product-card');

                const cardLink = document.createElement('a');
                cardLink.href = '';

                //  product image
                const cardImage = document.createElement('img');
                cardImage.classList.add('product-image-preview');
                cardImage.src = "/" + product.iconImage;

                //  product name
                const productName = document.createElement('p');
                productName.classList.add('product-name');
                productName.textContent = product.productName;

                //  discount tag
                const discountTag = document.createElement('div');
                discountTag.classList.add('discount-card');

                const discountText = document.createElement('p');
                discountText.classList.add('discount-text');
                discountText.textContent = '45% off';

                discountTag.appendChild(discountText);

                //  prices & wishlist button container
                const priceWishlistContainer = document.createElement('div');
                priceWishlistContainer.classList.add('discount-price-wishlist-container');

                const priceContainer = document.createElement('div');
                priceContainer.classList.add('na-discount-price');

                //  discount price
                const currentPrice = document.createElement('strong');
                currentPrice.classList.add('current-price');
                currentPrice.textContent = '₹ 1,500';

                //  base price (without adding discounts)
                const deprecatedPrice = document.createElement('p');
                deprecatedPrice.classList.add('deprecated-price');
                deprecatedPrice.textContent = '₹ 1,500';

                priceContainer.append(
                    currentPrice,
                    deprecatedPrice
                );

                //  wishlist button
                const wishlistLabel = document.createElement('label');
                wishlistLabel.classList.add('wishlist-label');

                const wishlistCheckbox = document.createElement('input');
                wishlistCheckbox.type = 'checkbox';
                wishlistCheckbox.classList.add('wishlist-checkbox');

                const wishlistHeart = document.createElement('span');
                wishlistHeart.classList.add('wishlist-heart');
                wishlistHeart.textContent = '❤';

                //  final appends
                wishlistLabel.append(
                    wishlistCheckbox,
                    wishlistHeart
                );
                priceWishlistContainer.append(
                    priceContainer,
                    wishlistLabel
                );
                cardLink.append(
                    cardImage,
                    productName,
                    discountTag,
                    priceWishlistContainer
                );
                card.appendChild(cardLink);
                productGrid.appendChild(card);
            } else {
                console.warn("Skipping invalid product: ", product);
            }
        });
    } catch (error) {
        console.error("Error populating products");
    }
}

//  POPULATE - pagination
function populatePagination(totalPages) {
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
}



//  ********* Data Loader methods *********

//  Loader - initial products
async function loadProducts() {
    try {
        const products = await fetchProducts(currentSortParam, currentRequestDto, currentPage);
        if (!products) {
            console.error("Products not found");
            return;
        }
        if (!Array.isArray(products.content)) {
            throw new Error("Invalid data format: products should be array");
        }
        populateProducts(products.content);
        populatePagination(products.totalPages);
//        console.log("totalpages: " + products.totalPages);
    } catch (error) {
        console.error("Error loading products. ", error);
    }
}


//  DOM Loading event
document.addEventListener('DOMContentLoaded', async () => {

    productGrid = document.querySelector('.product-grid');
    paginationContainer = document.querySelector('.pagination');
    //  ------- Load - initial data -------

    await loadProducts();
});