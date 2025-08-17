document.addEventListener('DOMContentLoaded', async () => {
    const select = document.getElementById('my-multi-select');

    window.selectedVariationIds = [];

    async function loadVariationsInOptions() {
        try{
            const response = await fetch("http://localhost:8080/variations/get-all-variations", {
                method: "GET",
                credentials: "include"
            });
            if(!response.ok) {
                throw new Error(`HTTP error ${response.status}`);
            }

            const variations = await response.json();

            if (!Array.isArray(variations)) {
                throw new Error("Invalid data format: variations should be array");
            }

            variations.forEach(v => {
                if (v && v.id !== undefined && v.variationName) {
                    addSelectOptions(v.id, v.variationName);
                } else {
                    console.warn("Skipping invalid variation: ", v);
                }
            });

            initializeChoices();
        } catch (error) {
            console.error("Load Error: ", error);
            alert("Unable to load variations. Please try again.");
        }
    }

    function addSelectOptions(id, variationName) {
        try {
            const option = document.createElement('option');
            option.value = id;
            option.textContent = variationName;
            select.appendChild(option);
        } catch (error) {
            console.error("Option Add Error: ", error);
        }
    }

    function initializeChoices() {
        try {
            const multipleSelect = new Choices('#my-multi-select', {
                placeholder: true,
                placeholderValue: 'Select variations...',
                removeItemButton: true
            });

            const removeDataSelectText = () => {
                document.querySelectorAll('.choices__item[data-select-text]').forEach(el => {
                    el.removeAttribute('data-select-text');
                });
            };

            const container = document.querySelector('.choices__list--multiple');
            if (container) {
                const observer = new MutationObserver(removeDataSelectText);
                observer.observe(container, { childList: true, subtree: true });
            }

            // Also remove immediately once
            removeDataSelectText();

            multipleSelect.passedElement.element.addEventListener('change', () => {
                try {
                    const selected = multipleSelect.getValue(true);
                    if (!selected || !Array.isArray(selected)) {
                        throw new Error("Selected variations is not a valid array");
                    }
                    window.selectedVariationIds = selected;
                    console.log("Selected variations: ", selected);
                } catch (err) {
                    console.error("Selection Error: ", err);
                    window.selectedVariationIds = [];
                    alert("Failed to update selected variations.");
                }
            });
        } catch (error) {
            console.error("Choices Initialization Error: ", error);
            alert("Unable to initialize variation selector.");
        }
    }

    window.resetVariationSelect = async function () {
        try {
            if (select.choices) {
                select.choices.clearStore();
                select.choices.clearChoices();
                select.choices.destroy();
            }
            select.innerHTML = "";
            window.selectedVariationIds = [];
            await loadVariationsInOptions();
        } catch (err) {
            console.error("Reset Variation Select Error: ", err);
        }
    };

    await loadVariationsInOptions();
});

