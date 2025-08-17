const getAllGlobalFilterOptions = "http://localhost:8080/search-filter/all-products";

document.addEventListener("DOMContentLoaded", async () => {
    const filterButton = document.getElementById("filter-button");
    const filterPanel = document.getElementById("filter-panel");
    const filterContainer = document.getElementById("filter-container");

    filterButton.addEventListener("click", (e) => {
    console.log("hi..");
        e.stopPropagation();
        filterPanel.classList.toggle("hidden");
    });

    document.addEventListener("click", (e) => {
        if(!filterPanel.contains(e.target) && !filterButton.contains(e.target)) {
            filterPanel.classList.add("hidden");
        }
    });

    const fetchFilterOptions = async () => {
        try {
            const response = await fetch(getAllGlobalFilterOptions);
            if(!response.ok) throw new Error("Failed to fetch filter options");
            return response.json();
        } catch(err) {
            console.error("Error", err);
            return null;
        }
    };

    const renderFilterOptions = (options) => {
        filterContainer.innerHTML = '';
        filterContainer.innerHTML = `
            <h3 id="filter-heading">Filters</h3>
            <hr class="filter-hr">
        `;

        Object.entries(options).forEach(([filterName, values], index, arr) => {
            const section = document.createElement("div");
            section.classList.add("filter-section");

            const title = document.createElement("p");
            title.classList.add("filter-title");
            title.textContent = filterName.replace(/_/g, " ");
            section.appendChild(title);

            values.forEach(val => {
                const label = document.createElement("label");

                const input = document.createElement("input");
                input.type = "checkbox";
                input.name = filterName.toLowerCase();
                input.value = val;

                const span = document.createElement("span");
                span.classList.add("checkmark");

                label.appendChild(input);
                label.appendChild(span);
                label.append(` ${val}`);

                section.appendChild(label);
            });

            filterContainer.appendChild(section);

            if(index < arr.length - 1) {
                const hr = document.createElement("hr");
                hr.classList.add("filter-hr");
                filterContainer.appendChild(hr);
            }
//            filterContainer.appendChild(document.createElement("hr")).classList.add("filter-hr");
        });

        const applyBtn = document.createElement("button");
        applyBtn.type = "submit";
        applyBtn.classList.add("apply-filter-btn");
        applyBtn.textContent = "Apply Filters";
        filterContainer.appendChild(applyBtn);
    };

    const options = await fetchFilterOptions();
    if(options) {
        renderFilterOptions(options);
    }

    document.querySelector(".apply-filter-btn").addEventListener("click", function () {
        let filterRequestDtos = [];

        document.querySelectorAll(".filter-section").forEach(section => {
            let h3 = section.querySelector("p");
            let filterType = h3 ? h3.innerText.trim() : null;

            let selectedValues = [...section.querySelectorAll("input[type='checkbox']:checked")]
                .map(cb => cb.value.trim());

            if(selectedValues.length > 0) {
                filterRequestDtos.push({
                    filterType: filterType,
                    values: selectedValues.join(", ")
                });
            }
        });

        let requestDto = { filterRequestDtos };
        console.log(requestDto);

        window.setRequestDto(requestDto);

        if(window.loadProducts && filterRequestDtos) {
            window.loadProducts();
        }

//        fetch("http://localhost:8080/products/hi", {
//            method: "POST",
//            headers: { "Content-Type": "application/json" },
//            body: JSON.stringify(requestDto)
//        })
//        .then(res => res.json())
//        .then(data => console.log("Response: ", data))
//        .catch(err => console.error("Error: ", err));
    });
});