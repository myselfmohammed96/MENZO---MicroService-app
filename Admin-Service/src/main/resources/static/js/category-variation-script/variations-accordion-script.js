// *************** API URLs ***************

const apiBase = "http://localhost:8080/variations";

const urls = {
    getAll: `${apiBase}/get-all`,
    addNewVariation: `${apiBase}/add-variation`,
    updateVariation: `${apiBase}/update-variation`,
    deleteVariation: `${apiBase}/delete-variation`,
    addNewOption: `${apiBase}/add-option`,
    updateOption: `${apiBase}/update-option`,
    deleteOption: `${apiBase}/delete-option`
};

// *************** CRUD functions on variations and options ***************

//window.addEventListener("DOMContentLoaded", () => {

    // Load variations from backend on page load
    async function loadVariations() {
        try{
            const response = await fetch(urls.getAll, { credentials: "include"});
            if (!response.ok) throw new Error("Failed to load variations");
            const variations = await response.json();
            variations.forEach(v => addVariation(v.id, v.variationName, v.options));
        } catch (error) {
            console.error("Load Error: ", error);
            alert("Unable to load variations.");
        }
    }

    // Save new variation
    async function saveVariation(name) {
        try{
            const response = await fetch(urls.addNewVariation, {
                method: "POST",
                credentials: "include",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ variationName: name })
            });
            if(!response.ok) {
                const err = await response.json().catch(() => ({}));
                throw new Error(err.message || "Failed to save variation");
            }
            return await response;
        } catch (error){
            console.error("Save error: ", error);
            alert("Failed to save variation.");
            throw error;
        }
    }

    // Update variation by variationId
    async function updateVariationHandler(name, variationId) {
        try{
            const response = await fetch(`${urls.updateVariation}?id=${variationId}`, {
                method: "PUT",
                credentials: "include",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ variationName: name })
            });
            if(response.status === 400) {
                throw new Error("Invalid data");
            } else if (response.status === 404) {
                throw new Error("Variation not found");
            } else if (!response.ok) {
                const err = await response.json().catch(() => ({}));
                throw new Error(err.message || "Failed to update variation");
            }
            return await response;
        } catch (error) {
            console.error("Error updating variation: ", error);
            alert("Failed to update variation.");
            throw error;
        }
    }

    // Delete variation by variationId
    async function deleteVariationHandler(variationId, element) {
        try{
            const response = await fetch(`${urls.deleteVariation}?id=${variationId}`, {
                    method: "DELETE",
                    credentials: "include"
            });
            if (!response.ok) {
                const err = await response.json().catch(() => ({}));
                throw new Error(err.message || "Failed to delete variation");
            }
            element.remove();
        } catch (error) {
            console.error("Deletion failed: ", error);
            alert("Failed to delete variation.");
        }
    }

    // Save new variation options
    async function saveOption(name, variationId) {
        try {
            const response = await fetch(urls.addNewOption, {
                method: "POST",
                credentials: "include",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ variationId: variationId, optionValue: name})
            });
            if (!response.ok) {
                const err = await response.json().catch(() => ({}));
                throw new Error(err.message || "Failed to save variation option");
            }
            return await response;
        } catch (error) {
            console.error("Save variation option error: ", error);
            alert("Failed to save variation option.");
            throw error;
        }
    }

    // Update option by optionId
    async function updateOptionHandler(name, optionId) {
        try{
            const response = await fetch(`${urls.updateOption}?id=${optionId}`, {
                method: "PUT",
                credentials: "include",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ optionValue: name })
            });
            if (response.status === 400) {
                throw new Error("Invalid data");
            } else if (response.status === 404) {
                throw new Error("Variation option not found");
            } else if (!response.ok) {
                const err = await response.json().catch(() => ({}));
                throw new Error(err.message || "Failed to update variation option");
            }
            return await response;
        } catch (error) {
            console.error("Error updating variation option: ", error);
            alert("Failed to update variation option.");
            throw error;
        }
    }

    // Delete option by optionId
    async function deleteOptionHandler(optionId, optionDiv) {
        try{
            const response = await fetch(`${urls.deleteOption}?id=${optionId}`, {
                method: "DELETE",
                credentials: "include"
            });
            if (!response.ok) {
                const err = await response.json().catch(() => ({}));
                throw new Error(err.message || "Failed to delete variation option");
            }
            optionDiv.remove();
        } catch (error) {
            console.error("Deletion failed: ", error);
            alert("Failed to delete variation option.");
        }
    }

    // Add new variation - UI functions
    function addVariation(variationId = null, variationName = "", options = []) {
        let container = document.getElementById("accordion-container");

        let accordion = document.createElement("div");
        accordion.classList.add("accordion");
        accordion.setAttribute("data-id", variationId);

        // Accordion header
        let header = document.createElement("div");
        header.classList.add("accordion-header");

        let variationNamePara = document.createElement("p");
        variationNamePara.textContent = variationName;
        variationNamePara.classList.add("variation-name-para");

        let buttonGroup = document.createElement("div");
        buttonGroup.classList.add("button-group");

        let updateBtn = document.createElement("button");
        updateBtn.classList.add("update-variation-btn");
        updateBtn.textContent = "Edit";
        updateBtn.onclick = async function (event) {
            event.stopPropagation();
            if(variationId) await openVariationModal(variationNamePara, variationId);
        }

        let deleteBtn = document.createElement("button");
        deleteBtn.classList.add("delete-variation-btn");
        deleteBtn.textContent = "Delete";
        deleteBtn.onclick = async function (event) {
            event.stopPropagation();
            if (variationId) await deleteVariationHandler(variationId, accordion);
        };

        buttonGroup.append(updateBtn, deleteBtn);
        header.append(variationNamePara, buttonGroup);

        // Accordion content - options list of variation
        let content = document.createElement("div");
    //    content.id = "accordion-content";
        content.classList.add("accordion-content");

        //Add Option button container
        let addOptionContainer = document.createElement("div");
        addOptionContainer.classList.add("add-option-btn-container");

        let addOptionBtn = document.createElement("button");
        addOptionBtn.textContent = "Add Option";
        addOptionBtn.classList.add("add-option-btn");
        addOptionBtn.onclick = async function (event) {
            event.stopPropagation();
            if(variationId) await openAddOptionModal(variationId);
        };

        addOptionContainer.appendChild(addOptionBtn);
        content.appendChild(addOptionContainer);

        // Add existing options from backend
        options.forEach(option => {
            const optionDiv = addOption(option.id, option.optionValue);
            content.appendChild(optionDiv);
        });

        accordion.append(header, content);
        container.appendChild(accordion);

        // Toggle visibility on header click
        header.onclick = () => {
            const allContents = document.querySelectorAll(".accordion-content");
            allContents.forEach(v => {
                if (v !== content) {
                    v.style.display = "none";
                }
            });
            content.style.display = content.style.display === "block" ? "none" : "block";
        };
    }

    // Add new option - UI functions
    function addOption(optionId = null, optionValue = "") {
        let optionDiv = document.createElement("div");
        optionDiv.classList.add("option");
        optionDiv.dataset.id = optionId;

        let optionPara = document.createElement("p");
        optionPara.classList.add("option-name-para");
        optionPara.textContent = optionValue;

        let optionBtnGroup = document.createElement("div");
        optionBtnGroup.classList.add("button-group");

        let optionEditBtn = document.createElement("button");
        optionEditBtn.classList.add("update-variation-btn");
        optionEditBtn.textContent = "Edit";
        optionEditBtn.onclick = async function (event) {
            event.stopPropagation();
            if(optionId) await openEditOptionModal(optionPara, optionId);
        }

        let optionDeleteBtn = document.createElement("button");
        optionDeleteBtn.classList.add("delete-variation-btn");
        optionDeleteBtn.textContent = "Delete";
        optionDeleteBtn.onclick = async function (event) {
            event.stopPropagation();
            if(optionId) await deleteOptionHandler(optionId, optionDiv);
        }

        optionBtnGroup.append(optionEditBtn, optionDeleteBtn);
        optionDiv.append(optionPara, optionBtnGroup);

        return optionDiv;
    }

    // *************** Add/Edit Modal handling ***************

    let editingVariationId = null;
    let editingParaEl = null;

    // Open modal for variation add/edit
    function openVariationModal(paraEl = null, variationId = null) {
        const modal = document.getElementById("variation-modal");
        const input = document.getElementById("modal-variation-input");

        editingVariationId = variationId;
        editingParaEl = paraEl;

        document.getElementById("modal-title").textContent = variationId ? "Edit Variation" : "Add Variation";
        input.value = paraEl ? paraEl.textContent : "";
        modal.style.display = "flex";

        input.focus();
    }

    // Open Add Option modal
    function openAddOptionModal(variationId = null) {
        const modal = document.getElementById("option-modal");
        const input = document.getElementById("option-modal-input");
        const saveBtn = document.getElementById("save-option-btn");

        document.getElementById("option-modal-title").textContent = "Add Option";
        input.value = "";
        modal.style.display = "flex";

        input.focus();

        saveBtn.onclick = async function (event) {
            event.stopPropagation();
            if (variationId) await saveAddOptionModal(variationId);
        }
    }

    // Open Edit option modal
    function openEditOptionModal(paraEl = null, optionId = null) {
        const modal = document.getElementById("option-modal");
        const input = document.getElementById("option-modal-input");
        const saveBtn = document.getElementById("save-option-btn");

        document.getElementById("option-modal-title").textContent = "Edit Option";
        input.value = paraEl.textContent;
        modal.style.display = "flex";

        input.focus();

        saveBtn.onclick = async function (event) {
            event.stopPropagation();
            const name = document.getElementById("option-modal-input").value.trim();
            console.log("Sending edit option data with id: ", optionId);
            if (!name) return;
            if (optionId) await saveEditOptionModal(paraEl, name, optionId);
        }
    }

    // Save from variation modal
    document.getElementById("save-variation-btn").onclick = async function () {
        const name = document.getElementById("modal-variation-input").value.trim();
        if (!name) return;

        try {
            if (editingVariationId) {
                const response = await updateVariationHandler(name, editingVariationId);
                if (editingParaEl && response.ok) editingParaEl.textContent = name;
            } else {
                const response = await saveVariation(name);
                const saved = await response.json();
                addVariation(saved.variationId, name, []);
            }
        } catch (e) {
            console.error("Modal Save Error", e);
            alert("Failed to save variation.");
        } finally {
            closeVariationModal();
        }
    };

    // Save from Add option modal
    async function saveAddOptionModal(variationId = null) {
        const name = document.getElementById("option-modal-input").value.trim();
        if (!name) return;
        try {
            if (variationId) {
                const response = await saveOption(name, variationId);
                const savedSub = await response.json();
                const optionDiv = addOption(savedSub.variationId, name);
                const parentAccordion = document.querySelector(`.accordion[data-id='${variationId}'] .accordion-content`);
                if (parentAccordion) {
                    parentAccordion.appendChild(optionDiv);
                }
            }
        } catch (e) {
            console.error("Add option modal - save error", e);
            alert("Failed to Save option.");
        } finally {
            closeOptionModal();
        }
    }

    // Save from Edit option modal
    async function saveEditOptionModal(editingOptionParaEl = null, name = null, optionId = null) {
        try {
            if (optionId) {
                const response = await updateOptionHandler(name, optionId);
                if (editingOptionParaEl && response.ok) editingOptionParaEl.textContent = name;
            }
        } catch (e) {
            console.error("Modal Update Error", e);
            alert("Failed to update option.");
        } finally {
            closeOptionModal();
        }
    }

    // 'Enter' key listener for modal save buttons
    document.addEventListener("keydown", function (e) {
        if(e.key === "Enter") {
            const variationModal = document.getElementById("variation-modal");
            const optionModal = document.getElementById("option-modal");

            if (variationModal && variationModal.style.display === "flex") {
                document.getElementById("save-variation-btn")?.click();
            } else if (optionModal && optionModal.style.display === "flex") {
                document.getElementById("save-option-btn")?.click();
            }
        }
    });

    // Close modal
    function closeVariationModal() {
        document.getElementById("variation-modal").style.display = "none";
        editingVariationId = null;
        editingParaEl = null;
    }

    // Close option modal
    function closeOptionModal() {
        document.getElementById("option-modal").style.display = "none";
    }

    // 'Escape' key listener for closing modals
    document.addEventListener("keydown", function (e) {
        if (e.key === "Escape") {
            const variationModal = document.getElementById("variation-modal");
            const optionModal = document.getElementById("option-modal");
            if (variationModal && variationModal.style.display === "flex") {
                closeVariationModal();
            }
            if (optionModal && optionModal.style.display === "flex") {
                closeOptionModal();
            }
        }
    });

    // Load existing variations and options on page load
    window.onload = loadVariations;
//});