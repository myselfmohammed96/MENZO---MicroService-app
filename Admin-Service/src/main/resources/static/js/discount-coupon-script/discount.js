const getDiscounts = "http://localhost:8080/discount/listing";

let tBody;
//let modalForm;
let paginationContainer;

let currentSortParam;
let currentRequestDto;
let currentPage = 1;
let pageSize = 11;

//  FETCH discount listing
async function fetchDiscounts(sortParam = null, requestDto = null, page) {
    try {
        const url = sortParam
                        ? `${getDiscounts}?page=${page - 1}&size=${pageSize}&sort=${sortParam}`
                        : `${getDiscounts}?page=${page - 1}&size=${pageSize}`;
        const response = await fetch(url, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: requestDto
                        ? JSON.stringify(requestDto)
                        : null
        });
        if (!response.ok) throw new Error("Failed to fetch discounts");
        return response.json();
    } catch (error) {
        console.error("Error fetching discounts: ", error);
        return null;
    }
}

//  populate discounts
function populateDiscounts(discounts) {
    tBody.innerHTML = '';
    discounts.forEach(d => {
        const statusColor = {
            'ACTIVE': 'status-green',
            'INACTIVE': 'status-red',
            'SCHEDULED': 'status-blue',
            'PAUSED': 'status-amber',
            'EXPIRED': 'status-grey',
            'CANCELLED': 'status-maroon'
        }[d.status] || '';

        const level = d.level.charAt(0).toUpperCase()
                        + d.level.slice(1).toLowerCase().replace('_', '-');

        let value;
        if (d.type === "PERCENT") {
            value = d.value + " %";
        } else if (d.type === "FLAT") {
            value = "₹ " +  d.value;
        }

        const status = d.status.charAt(0).toUpperCase()
                        + d.level.slice(1).toLowerCase().replace('_', ' ');

        const tRow = `
            <tr>
                <td class="center-text user-table-data">${d.discountName}</td>
                <td class="center-text">${d.discountCode}</td>
                <td class="center-text">${level}</td>
                <td class="center-text">${value}</td>
                <td class="center-text">
                    <span class="status-indicator ${statusColor}">${status}</span>
                </td>
                <td class="center-text action-buttons-column">
                    <button class="action-view-btn">
                        <img src="../media/enter (1).png" alt="">
                    </button>
                    <button class="action-delete-btn">
                        <img src="../media/delete.png" alt="">
                    </button>
                </td>
            </tr>
        `;
        tBody.insertAdjacentHTML('beforeend', tRow);
    });
}

//  populate pagination
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










//function openModalForm() {
//    modalForm.style.display = "flex";
//}
//
//const closeModalForm = () => {
//    modalForm.style.display = "none";
//};

//  load products
async function loadDiscounts() {
    try {
        const result = await fetchDiscounts(currentSortParam, currentRequestDto, currentPage);
        if (!result) return;
        populateDiscounts(result.content);
        populatePagination(result.totalPages);
    } catch (error) {
        console.error("Failed to load discounts: ", error);
        return;
    }
}



document.addEventListener('DOMContentLoaded', () => {

    tBody = document.getElementById('discount-table');
    modalForm = document.getElementById('discount-form-modal');
    paginationContainer = document.querySelector('.pagination');

    await loadDiscounts();


//    //  open form modal
//    document.getElementById('add-button').addEventListener('click', () => {
//         openModalForm();
//    });
//
//    //  close form modal
//    document.getElementById('close-form-modal-btn').addEventListener('click', () => {
//        closeModalForm();
//    });
});