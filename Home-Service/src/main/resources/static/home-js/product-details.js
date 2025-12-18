const getProductDetails = "http://localhost:8080/products/get-user-product-details";
const getImageUrls = "http://localhost:8080/products/image-urls";

let initialLoadSuperSku;
let selectedColor;
let selectedSize;

let previewImageSrc;
let imageZoom;

const colorMap = new Map();
const imageCache = new Map();



//  ********* fetch methods *********

//  FETCH - basic product details & items list --- DONE
async function fetchProductDetails(superSku) {
    try {
        const response = await fetch(`${getProductDetails}?ssku=${superSku}`, { method: "GET" });
        if (!response.ok) throw new Error("Failed to fetch product details");
        return await response.json();
    } catch (error) {
        console.error("Error fetching product details:", error);
        return null;
    }
}

//  FETCH - image urls --- DONE
async function fetchImageUrls(superSku) {
    try {
        const response = await fetch(`${getImageUrls}?ssku=${superSku}`, { method: "GET" });
        if (!response.ok) throw new Error ("Failed to fetch product image urls");
        return await response.json();
    } catch (error) {
        console.error("Error fetching product image urls:", error)
        return null;
    }
}



//  ********* populating methods *********

//  POPULATE - basic product details --- DONE
function populateProductDetails(productDetails = null) {
    try {
        if (!productDetails) {
            throw new Error("product details not found for population");
        }
        document.querySelector('.product-name').textContent = productDetails.productName ? productDetails.productName : "...";
        document.getElementById('product-description').textContent = productDetails.description ? productDetails.description : "...";

        document.getElementById('manufacturer').textContent = productDetails.manufacturer ? productDetails.manufacturer : "...";
        document.getElementById('packer').textContent = productDetails.packer ? productDetails.packer : "...";
        document.getElementById('country-of-origin').textContent = productDetails.countryOfOrigin ? productDetails.countryOfOrigin : "...";

        document.getElementById('item-weight').textContent = productDetails.itemWeight ? Math.floor(productDetails.itemWeight) + " g" : "...";
        document.getElementById('generic-name').textContent = productDetails.itemWeight ? productDetails.genericName : "...";

        //  product variation details
        const productVariationDetails = document.getElementById('product-variation-details');
        productVariationDetails.innerHTML = '';

        if (!productDetails.variations) {
            throw new Error("Product variations not available");
        }
        Object.entries(productDetails.variations).forEach(([key, value]) => {
            const detailsText = document.createElement('div');
            detailsText.classList.add('details-text');

            const detailsKey = document.createElement('strong');
            detailsKey.classList.add('details-key');
            detailsKey.textContent = key + ":";

            const detailsValue = document.createElement('p');
            detailsValue.classList.add('details-value');
            detailsValue.textContent = value;

            detailsText.append(
                detailsKey,
                detailsValue
            );
            productVariationDetails.appendChild(detailsText);
        });
    } catch (error) {
        console.error("Error populating product details.", error);
    }
}

//  POPULATE - color variation items --- DONE
async function populateColorOptions(items) {
    try {
        const colorBody = document.getElementById('color-body');
        colorBody.innerHTML = '';

        let colorName = document.getElementById('color-value');

        items.forEach(item => {
            const colorOption = document.createElement('div');
            colorOption.classList.add('color-option');
            colorOption.setAttribute('data-super-sku', item.superSku);
            colorOption.setAttribute('data-color-name', item.colorName);

            //  color name - on hover
            colorOption.addEventListener('mouseenter', (e) => {
                colorName.textContent = e.currentTarget.dataset.colorName;
            });
            colorOption.addEventListener('mouseleave', (e) => {
                colorName.textContent = selectedColor;
            });

            //  select option event
            colorOption.addEventListener('click', async (e) => {
                //  change color option
                try {
                    const superSku = e.currentTarget.dataset.superSku;
                    if (!superSku) {
                        throw new Error("Color option doesn't contain super SKU");
                    }
                    await loadColorOption(superSku);
                } catch (error) {
                    console.error("Error changing color option.", error);
                }
            });

            //  option image
            const imageContainer = document.createElement('div');
            imageContainer.classList.add('color-option-image-container');

            const image = document.createElement('img');
            image.classList.add('color-option-image');
            image.src = "/" + item.iconImage;

            imageContainer.appendChild(image);

            const hr = document.createElement('hr');
            hr.classList.add('custom-line-1');

            //  price details
            const priceContainer = document.createElement('div');
            priceContainer.classList.add('color-option-price-container');

            const finalPrice = document.createElement('span');
            finalPrice.classList.add('option-final-price');
            finalPrice.textContent = "₹489.00";

            const basePrice = document.createElement('span');
            basePrice.classList.add('option-base-price');
            basePrice.textContent = "₹ " + item.price.toLocaleString('en-IN', {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2
            });

            priceContainer.append(
                finalPrice,
                basePrice
            );

            const overlay = document.createElement('div');
            overlay.classList.add('color-option-overlay');

            colorOption.append(
                imageContainer,
                hr,
                priceContainer,
                overlay
            );

            colorBody.append(colorOption);
        });
    } catch (error) {
        console.error("Error populating color options.", error);
    }
}

//  POPULATE - images
function populateImages(imageUrls) {
    try {
        const imageIconsContainer = document.getElementById('image-icons-wrapper');
        const imagePreviewDiv = document.getElementById('image-preview');
        const imageZoomDiv = document.getElementById('image-zoom');
        imageIconsContainer.innerHTML = '';

        let imageCount = 1;
        imageUrls.forEach(url => {

            //  getting image using image url
            const imageObj = loadImage(url);
            const imageIconContainer = document.createElement('div');
            imageIconContainer.classList.add('image-icon-container');
            if (imageCount === 1) {
                imageIconContainer.classList.add('image-icon-select');
                if (imageObj.complete) {
                    previewImageSrc = imageObj.src;
                } else {
                    imageObj.onload = () => previewImageSrc = imageObj.src;
                }
            }

            const imageIcon = document.createElement('img');
            if (imageObj.complete) {
                imageIcon.src = imageObj.src;
            } else {
                imageObj.onload = () => imageIcon.src = imageObj.src;
            }

            imageIconContainer.appendChild(imageIcon);
            imageIconContainer.addEventListener('click', (e) => {
                //  change highlight
                document.querySelectorAll('.image-icon-container').forEach(icon => icon.classList.remove('image-icon-select'));
                e.currentTarget.classList.add('image-icon-select');

                //  change image preview
                previewImageSrc = e.currentTarget.querySelector('img').getAttribute('src');
                imagePreviewDiv.src = previewImageSrc;
                imageZoomDiv.style.setProperty('--url', `url('${previewImageSrc}')`);
            });
            imageIconsContainer.appendChild(imageIconContainer);
            imageCount++;

            imagePreviewDiv.src = previewImageSrc;
            imageZoomDiv.style.setProperty('--url', `url('${previewImageSrc}')`);
        });
    } catch (error) {
        console.error("Error populating images");
    }
}

//  POPULATE - sizes
function populateSizes(sizes) {
    const sizeOrder = ['M', 'L', 'S', 'XL', 'XS', '2XL', '2XS', '3XL', '4XL', '5XL'];

    const sizeBody = document.getElementById('size-body');
    sizeBody.innerHTML = '';

    try {
        let index = Number.MAX_SAFE_INTEGER;

        sizes.forEach(size => {

            //  getting preferred initial size index (initial size select)
            if (sizeOrder.includes(size)) {
                const sizeIndex = sizeOrder.findIndex(s => s === size);
                if (sizeIndex < index) index = sizeIndex;
            }
            const sizeOption = document.createElement('div');
            sizeOption.classList.add('size-option');

            const sizeText = document.createElement('span');
            sizeText.textContent = size;

            sizeOption.appendChild(sizeText);
            sizeOption.addEventListener("click", (e) => {
                selectedSize = e.currentTarget.textContent;
                document.querySelectorAll('.size-option').forEach(opt => opt.classList.remove('size-option-select'));
                e.currentTarget.classList.add('size-option-select');
            });
            sizeBody.appendChild(sizeOption);
        });
        const defaultSize = sizeOrder[index];
        const defaultSizeOption = [...document.querySelectorAll('.size-option')]
                .find(opt => opt.textContent.trim() === defaultSize);
        defaultSizeOption?.classList.add('size-option-select');
    } catch (error) {
        console.error("Error populating sizes:", error);
    }
}

//  POPULATE - price details
function populatePriceDetails(price, discount) {
    document.querySelector('#mrp-price').textContent = "₹ " + Math.floor(price).toLocaleString('en-IN');
}



//  ********* Data Loader methods *********

//  LOADER - color map details --- DONE
function loadColorMap(items) {
    items.forEach(item => {
        try {
            if (!Array.isArray(item.sizes)) {
                throw new Error("Invalid data format: sizes array not found for item with super sku: " + item.superSku);
            }
            const optionDetails = {
                colorName: item.colorName,
                price: item.price,
                discount: null,
                sizes: item.sizes
            }
            colorMap.set(item.superSku, optionDetails);
        } catch (error) {
            console.error("Error loading color map:", error);
        }
    });
}

//  LOADER - initial color option
async function loadColorOption(superSku) {
    try {
        //  change highlight
        document.querySelectorAll('.color-option')
            .forEach(opt => opt.classList.remove('color-option-select'));

        const selectedOption = document.querySelector(
            `.color-option[data-super-sku="${superSku}"]`
        );
        selectedColor = selectedOption.dataset.colorName;
        selectedOption.classList.add('color-option-select');

        //  color details
        const colorDetails = colorMap.get(superSku);
        if (!colorDetails) throw new Error("Color details not found with super SKU - " + superSku);

        //  Image loading
        let imageUrls = colorDetails.imageUrls;
        if (!Array.isArray(imageUrls)) {
            imageUrls = await fetchImageUrls(superSku);
            if (!Array.isArray(imageUrls)) throw new Error("Invalid data format: images should be an array");
            colorDetails.imageUrls = imageUrls;
        }
        if (imageUrls.length === 0) throw new Error("Invalid data: images not found");

        populateImages(imageUrls);

        //  populate Sizes
        populateSizes(colorDetails.sizes);

        //  change price details
        populatePriceDetails(colorDetails.price, colorDetails.discount);
    } catch (error) {
        console.error("Error loading color option:", error);
    }
}

function loadImage(url) {
    if (imageCache.has(url)) return imageCache.get(url);

    const img = new Image();
    img.src = '/' + url;
    imageCache.set(url, img);
    return img;
}













//  LOADER - product details and item... --- PARTIALLY DONE
async function loadProductDetails() {
    try {
        const productDetails = await fetchProductDetails(initialLoadSuperSku);
        if (!productDetails) {
            console.error("Product details not found");
            return;
        }
        if (!Array.isArray(productDetails.items)) {
            throw new Error("Invalid data format: items should be array");
        }
        loadColorMap(productDetails.items);
        populateProductDetails(productDetails);
        populateColorOptions(productDetails.items);

        await loadColorOption(initialLoadSuperSku);
    } catch (error) {
        console.error("Error loading product details");
    }
}


//  DOM Loading event
document.addEventListener("DOMContentLoaded", async () => {

    //  ------- Load - initial data -------

    initialLoadSuperSku = document.getElementById('init-load-super-sku').textContent.trim();
    if (!initialLoadSuperSku) {
        console.error("Super sku not found");
        return;
    }

    await loadProductDetails(initialLoadSuperSku);


    //  ------- Image zoom -------

    imageZoom = document.getElementById('image-zoom');
    imageZoom.addEventListener('mousemove', (e) => {
        imageZoom.style.setProperty('--display', 'block');
        let pointer = {
            x: (e.offsetX * 100) / imageZoom.offsetWidth,
            y: (e.offsetY * 100) / imageZoom.offsetHeight
        }

        imageZoom.style.setProperty('--zoom-x', pointer.x + '%');
        imageZoom.style.setProperty('--zoom-y', pointer.y + '%');
    });
    imageZoom.addEventListener('mouseout', () => {
        imageZoom.style.setProperty('--display', 'none');
    });
});