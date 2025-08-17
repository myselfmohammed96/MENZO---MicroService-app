const getAllProductItemsListing = "http://localhost:8080/products/product-items";

document.addEventListener('DOMContentLoaded', async () => {

    const wrappers = document.querySelectorAll('.image-preview-wrapper');
    const images = window.images;

    const previewModal = document.getElementById('open-image-preview-modal');
    const modalImage = document.getElementById('modal-image');
    const modalCloseIcon = document.getElementById('modal-close');
    const leftImageNavBtn = document.querySelector('.left-nav-btn');
    const rightImageNavBtn = document.querySelector('.right-nav-btn');

    let currentImage;


    //  ********* Product images - preview *********

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

        wrapper.addEventListener('click', () => {
            const imageSrc = wrapper.querySelector('.image-preview').getAttribute('src');
            const relativePath = imageSrc.replace("http://localhost:8080/", "");
            currentImage = images.indexOf(relativePath);

            modalImage.setAttribute('src', imageSrc);
            previewModal.style.display = "flex";
        });
    });

    leftImageNavBtn.addEventListener('click', () => {
        if(currentImage === null || currentImage === undefined) return;

        if(currentImage === 0) {
            currentImage = images.length-1;
            modalImage.setAttribute('src', "http://localhost:8080/" + images[currentImage]);
        } else {
            currentImage--
            modalImage.setAttribute('src', "http://localhost:8080/" + images[currentImage]);
        }
    });

    rightImageNavBtn.addEventListener('click', () => {
        if(currentImage === null || currentImage === undefined) return;

        if(currentImage === images.length-1) {
            currentImage = 0;
            modalImage.setAttribute('src', "http://localhost:8080/" + images[currentImage]);
        } else {
            currentImage++;
            modalImage.setAttribute('src', "http://localhost:8080/" + images[currentImage]);
        }
    })

    modalCloseIcon.addEventListener('click', () => {
        currentImage = null;
        previewModal.style.display = 'none';
    });


    //  ********* Loading the product items in table *********

    const tBody = document.getElementById('product-items-table-body');
    const paginationContainer = document.querySelector('.pagination');

    const productId = window.productId;
    let currentPage = 1;
    const pageSize = 10;

    const fetchProductItems = async (productId, page = 1) => {
        try {
            const response = await fetch(`${getAllProductItemsListing}?id=${productId}&page=${page - 1}&size=${pageSize}`);
            if (!response.ok) return;
            return response.json();
        } catch(err) {
            console.error("Error", err);
            return null;
        }
    };

    const renderProductItems = (productItems) => {
        tBody.innerHTML = '';
        productItems.forEach(item => {
            const statusColor = {
                'ACTIVE': 'status-green',
                'INACTIVE': 'status-red'
            }[item.activeStatus] || '';

            const row = `
                <tr>
                    <td class="products-table-data">
                        <div class="product-content-wrapper">
                            <img class="product-icon-img" src="http://localhost:8080/${item.iconImage}" alt="">
                            <span class="product-text">${item.sku}</span>
                        </div>
                    </td>
                    <td class="center-text">${item.price}</td>
                    <td class="center-text">${item.size}</td>
                    <td class="center-text">${item.stockQty}</td>
                    <td class="center-text">
                        <span class="status-indicator ${statusColor}">${item.activeStatus}</span>
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

    const renderPagination = (totalPages) => {
        paginationContainer.innerHTML = '';

        if (currentPage > 1) {
            paginationContainer.innerHTML += `<a href="#" data-page="${currentPage - 1}">&laquo; Previous</a>`;
        }
        for (let i = 1; i <= totalPages; i++) {
            paginationContainer.innerHTML += `<a href="#" data-page="${i}" class="${i === currentPage ? 'active-page' : ''}">${i}</a>`;
        }
        if (currentPage < totalPages) {
            paginationContainer.innerHTML += `<a href="#" data-page="${currentPage + 1}">Next &raquo;</a>`;
        }

        attachPaginationEvents();
    };

    const attachPaginationEvents = () => {
        document.querySelectorAll('.pagination a').forEach(a => {
            a.addEventListener('click', async (e) => {
                e.preventDefault();
                const page = parseInt(e.target.dataset.page);
                if(e !== currentPage) {
                    currentPage = page;
                    await loadProductItems(productId);
                }
            });
        });
    };

    const loadProductItems = async (productId) => {
        const result = await fetchProductItems(productId, currentPage);
        if(!result) return;

        const productItems = result.content;
        const totalPages = result.totalPages;

        renderProductItems(productItems);
        renderPagination(totalPages);
    };

    loadProductItems(productId);
});

//    async function loadProductItems(productId) {
//        const response = await fetch(`getAllProductItemsListing?id=${productId}&page&size`);
//        const data = await response.json();
//        const productItems = data.content;
//
//        const tBody = document.getElementById('product-items-table-body');
//        tBody.innerHTML = '';
//
//        productItems.forEach(item => {
//            const statusColor = {
//                'ACTIVE': 'status-green',
//                'INACTIVE': 'status-red'
//            }[item.activeStatus] || '';
//
//            const row = `
//                <tr>
//                    <td class="products-table-data">
//                        <div class="product-content-wrapper">
//                            <img class="product-icon-img" src="http://localhost:8080/${item.iconImage}" alt="">
//                            <span class="product-text">${item.sku}</span>
//                        </div>
//                    </td>
//                    <td class="center-text">${item.price}</td>
//                    <td class="center-text">${item.size}</td>
//                    <td class="center-text">${item.stockQty}</td>
//                    <td class="center-text">
//                        <span class="status-indicator ${statusColor}">${item.activeStatus}</span>
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
//
////    await loadProductItem();
//    console.log("Product items have been loaded successfully.");
