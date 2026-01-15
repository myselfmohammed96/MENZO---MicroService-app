//window.getSizeDetails = function () {
//    try {
//
//    console.log("this method is called - size")
//        const sizeErrorMessage = document.getElementById('size-details-error-message');
//        sizeErrorMessage.textContent = '';
//
//        const checkedSizes = document.querySelectorAll('input[name="sizes"]:checked');
//
//        //  size validation
//        if (checkedSizes.length === 0) {
//            sizeErrorMessage.textContent = '*Sizes not selected.';
//            return null;
//        }
//
//        const sizeDetails = [];
//
//        for (const checkedSize of checkedSizes) {
//            const id = checkedSize.dataset.id;
//            const sizeValue = checkedSize.nextSibling.textContent.trim();
//
//            const stock = document.querySelector(`input[data-id="${id}"][placeholder="Enter stock"]`).value.trim();
//            const mrp = document.querySelector(`input[data-id="${id}"][placeholder="Enter MRP"]`).value.trim();
//            const sellingPrice = document.querySelector(`input[data-id="${id}"][placeholder="Enter price"]`).value.trim();
//
//            //  stock validation
//            if (!stock) {
//                sizeErrorMessage.textContent = `*Stock cannot be empty for size - ${sizeValue}`;
//                return null;
//            }
//            if (!/^\d+$/.test(stock)) {
//                sizeErrorMessage.textContent = `*Stock must be valid integer for size - ${sizeValue}`;
//                return null;
//            }
//            if (Number(stock) <= 0) {
//                sizeErrorMessage.textContent = `*Enter valid stock quantity for size - ${sizeValue}. Must be greater than 0.`;
//                return null;
//            }
//
//            //  price validation
//            if (!mrp) {
//                sizeErrorMessage.textContent = `*MRP cannot be empty for size - ${sizeValue}`;
//                return null;
//            }
//            if (!/^\d+(\.\d+)?$/.test(mrp)) {
//                sizeErrorMessage.textContent = `*MRP must be valid number for size - ${sizeValue}`;
//                return null;
//            }
//            if (Number(mrp) <= 0) {
//                sizeErrorMessage.textContent = `*Enter valid MRP value for size - ${sizeValue}. Must be greater than 0.`;
//                return null;
//            }
//
//            if (!sellingPrice) {
//                sizeErrorMessage.textContent = `*Selling price cannot be empty for size - ${sizeValue}`;
//                return null;
//            }
//            if (!/^\d+(\.\d+)?$/.test(sellingPrice)) {
//                sizeErrorMessage.textContent = `*Selling price must be valid number for size - ${sizeValue}`;
//                return null;
//            }
//            if (Number(sellingPrice) <= 0) {
//                sizeErrorMessage.textContent = `*Enter valid Selling price for size - ${sizeValue}. Must be greater than 0.`;
//                return null;
//            }
//
//            sizeDetails.push({
//                sizeId: id,
//                sizeValue,
//                sizeStock: Number(stock),
//                sizeMrp: Number(mrp),
//                sizeSellingPrice: Number(sellingPrice)
//            });
//        }
//
//console.log("type:", Object.prototype.toString.call(selectedSizes));
//console.log("isArray:", Array.isArray(selectedSizes));
//        console.log(sizeDetails)
//
//        return sizeDetails;
//    } catch (error) {
//        console.error("Error getting size details: ", error);
//    }
//}