const getAllGlobalFilterOptions = "http://localhost:8080/search-filter/all-products";



//  ******* fetch filter options *******

//const fetchFilterOptions = async function() {
//    try {
//        const response = await fetch(getAllGlobalFilterOptions);
//        if(!response.ok) throw new Error("Failed to fetch filter options");
//        return response.json();
//    } catch(err) {
//        console.error("Error", err);
//        return null;
//    }
//};


//  ******* render filter options *******

//const renderFilterOptions = function(options) {
//    filterContainer.innerHTML = '';
//    filterContainer.innerHTML = `
//        <h3 id="filter-heading">Filters</h3>
//        <hr class="filter-hr">
//    `;
//
//    Object.entries(options).forEach(([filterName, values], index, arr) => {
//        const section = document.createElement("div");
//        section.classList.add("filter-section");
//        const title = document.createElement("p");
//        title.classList.add("filter-title");
//        title.textContent = filterName.replace(/_/g, " ");
//        section.appendChild(title);
//        values.forEach(val => {
//            const label = document.createElement("label");
//
//            const input = document.createElement("input");
//            input.type = "checkbox";
//            input.name = filterName.toLowerCase();
//            input.value = val;
//
//            const span = document.createElement("span");
//            span.classList.add("checkmark");
//
//            label.appendChild(input);
//            label.appendChild(span);
//            label.append(` ${val}`);
//
//            section.appendChild(label);
//        });
//
//        filterContainer.appendChild(section);
//
//        if(index < arr.length - 1) {
//            const hr = document.createElement("hr");
//            hr.classList.add("filter-hr");
//            filterContainer.appendChild(hr);
//        }
//        //            filterContainer.appendChild(document.createElement("hr")).classList.add("filter-hr");
//    });
//
//}

document.addEventListener("DOMContentLoaded", async () => {
    const filterButton = document.getElementById("filter-button");
    const filterPanel = document.getElementById("filter-panel");
//    const filterContainer = document.getElementById("filter-container");
    const sortButton = document.getElementById("sort-button");
    const sortPanel = document.getElementById("sort-panel");

    //  ******* eventListener on 'filter' & 'sort buttons *******
    filterButton.addEventListener("click", (e) => {
        e.stopPropagation();
        if(!sortPanel.classList.contains("hidden")) {
            sortPanel.classList.add("hidden");
        }
        filterPanel.classList.toggle("hidden");
    });

    sortButton.addEventListener("click", (e) => {
        e.stopPropagation();
        if(!filterPanel.classList.contains("hidden")) {
            filterPanel.classList.add("hidden");
        }
        sortPanel.classList.toggle("hidden");
    });

    document.addEventListener("click", (e) => {
        if(!filterPanel.contains(e.target) && !filterButton.contains(e.target)) {
            filterPanel.classList.add("hidden");
        }
        if(!sortPanel.contains(e.target) && !sortButton.contains(e.target)) {
            sortPanel.classList.add("hidden");
        }
    });



    const blackList = ["Featured", "Avg. Customer Review", "Best Sellers"];
    const sortOptions = document.querySelectorAll('.sort-option');
    sortOptions.forEach(opt => {
        opt.addEventListener('click', () => {
            console.log(opt.textContent + " - selected");
            if(blackList.includes(opt.textContent)) {
                console.log("*Returning...");
                return;
            }
            window.loadProducts(opt.dataset.value);
            sortOptions.forEach(o => {
                if(o.classList.contains('applied-sort')) {
                    o.classList.remove('applied-sort');
                }
            });
            opt.classList.add("applied-sort");
            sortPanel.classList.add('hidden');
        });
    });




//    const applyBtn = document.createElement("button");
//    applyBtn.type = "submit";
//    applyBtn.classList.add("apply-filter-btn");
//    applyBtn.textContent = "Apply Filters";
//    filterContainer.appendChild(applyBtn);
//
//
//    const options = await fetchFilterOptions();
//    if(options) {
//        renderFilterOptions(options);
//    }
});



//document.querySelector(".apply-filter-btn").addEventListener("click", function () {
//    let filterRequestDtos = [];
//
//    document.querySelectorAll(".filter-section").forEach(section => {
//        let h3 = section.querySelector("p");
//        let filterType = h3 ? h3.innerText.trim() : null;
//        let selectedValues = [...section.querySelectorAll("input[type='checkbox']:checked")]
//            .map(cb => cb.value.trim());
//
//        if(selectedValues.length > 0) {
//            filterRequestDtos.push({
//                filterType: filterType,
//                values: selectedValues.join(", ")
//            });
//        }
//    });
//
//    let requestDto = { filterRequestDtos };
//    console.log(requestDto);
//
//    window.setRequestDto(requestDto);
//
//    if(window.loadProducts && filterRequestDtos) {
//        window.loadProducts();
//    }
//
//});
//    });










//        fetch("http://localhost:8080/products/hi", {
//            method: "POST",
//            headers: { "Content-Type": "application/json" },
//            body: JSON.stringify(requestDto)
//        })
//        .then(res => res.json())
//        .then(data => console.log("Response: ", data))
//        .catch(err => console.error("Error: ", err));