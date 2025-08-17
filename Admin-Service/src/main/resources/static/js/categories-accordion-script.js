// *************** API URLs ***************

const apiBase = "http://localhost:8080/categories";

const urls = {
    getAll: `${apiBase}/get-all`,
    addParent: `${apiBase}/add-parent`,
    updateParent: `${apiBase}/update-parent`,
    deleteParent: `${apiBase}/delete-parent`,
    addSub: `${apiBase}/add-sub`,
    updateSub: `${apiBase}/update-sub`,
    deleteSub: `${apiBase}/delete-sub`
};

// *************** CRUD functions on categories ***************

//window.addEventListener("DOMContentLoaded", () => {

    // Load categories from backend on page load
    async function loadCategories() {
        try{
            const response = await fetch(urls.getAll, { credentials: "include" });
            if (!response.ok) throw new Error("Failed to load categories");
            const categories = await response.json();
            categories.forEach(c => addCategory(c.id, c.categoryName, c.subCategories));
        } catch (error) {
            console.error("Load Error: ", error);
            alert("Unable to load categories.");
        }
    }

    // Save new parent category
    async function saveCategories(name) {
        try{
            const response = await fetch(urls.addParent, {
                method: "POST",
                credentials: "include",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ categoryName: name })
            });
            if(!response.ok) {
                const err = await response.json.catch(() => ({}));
                throw new Error(err.message || "Failed to save parent category");
            }
            return await response;
        } catch (error){
            console.error("Save error: ", error);
            alert("Failed to save category.");
            throw error;
        }
    }

    // Update parent category by categoryId
    async function updateCategory(name, categoryId) {
        try{
            const response = await fetch(`${urls.updateParent}?id=${categoryId}`, {
                method: "PUT",
                credentials: "include",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ categoryName: name })
            });
            if(response.status === 400) {
                throw new Error("Invalid data");
            } else if (response.status === 404) {
                throw new Error("Category not found");
            } else if (!response.ok) {
                const err = await response.json().catch(() => ({}));
                throw new Error(err.message || "Failed to update parent category");
            }
            return await response;
        } catch (error){
            console.error("Error updating parent category: ", error);
            alert("Failed to update category.");
            throw error;
        }
    }

    // Delete parent category by id
    async function deleteParentCategory(parentCategoryId, element) {
        try{
            const response = await fetch(`${urls.deleteParent}?id=${parentCategoryId}`, {
                method: "DELETE",
                credentials: "include"
            });
            if (!response.ok){
                const err = await response.json().catch(() => ({}));
                throw new Error(err.message || "Failed to delete category");
            }
            element.remove();
        } catch (error) {
            console.error("Deletion failed: ", error);
            alert("Failed to delete category.");
        }
    }

    // Save new sub-option
    async function saveSubCategory(name, categoryId, selectedVariations) {
        try {
            const response = await fetch(urls.addSub, {
                method: "POST",
                credentials: "include",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ parentCategoryId: categoryId, categoryName: name, variationIds: selectedVariations })
            });
            if (!response.ok) {
                const err = await response.json().catch(() => ({}));
                throw new Error(err.message || "Failed to save sub-category");
            }
            window.resetVariationSelect();
            return await response;
        } catch (error) {
            console.error("Save sub-category error: ", error);
            alert("Failed to save sub-category.");
            throw error;
        }
    }

    // Update Sub category by subCategoryId
    async function updateSubCategory(name, subCategoryId) {
        try{
            const response = await fetch(`${urls.updateSub}?id=${subCategoryId}`, {
                method: "PUT",
                credentials: "include",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ categoryName: name })
            });
            if (response.status === 400) {
                throw new Error("Invalid data");
            } else if (response.status === 404) {
                throw new Error("Sub-category not found");
            } else if (!response.ok) {
                const err = await response.json().catch(() => ({}));
                throw new Error(err.message || "Failed to update sub-category");
            }
            return await response;
        } catch (error) {
            console.error("Error updating sub-category: ", error);
            alert("Failed to update sub-category.");
            throw error;
        }
    }

    // Delete sub category
    async function deleteSubCategory(subCategoryId, subCategoryDiv) {
        try{
            const response = await fetch(`${urls.deleteSub}?id=${subCategoryId}`, {
                method: "DELETE",
                credentials: "include"
            });
            if (!response.ok) {
                const err = await response.json().catch(() => ({}));
                throw new Error(err.message || "Failed to delete sub-category");
            }
            subCategoryDiv.remove();
        } catch (error) {
            console.error("Deletion failed: ", error);
            alert("Failed to delete sub-category.");
        }
    }

    // Add new category - UI functions
    function addCategory(categoryId = null, categoryName = "", subCategories = []) {
        let container = document.getElementById("accordion-container");

        let accordion = document.createElement("div");
        accordion.classList.add("accordion");
        accordion.setAttribute("data-id", categoryId);

        // Accordion header
        let header = document.createElement("div");
        header.classList.add("accordion-header");

        let categoryNamePara = document.createElement("p");
        categoryNamePara.textContent = categoryName;
        categoryNamePara.classList.add("category-name-para");

        let buttonGroup = document.createElement("div");
        buttonGroup.classList.add("button-group");

        let updateBtn = document.createElement("button");
        updateBtn.classList.add("update-category-btn");
        updateBtn.textContent = "Edit";
        updateBtn.onclick = async function (event) {
            event.stopPropagation();
            if(categoryId) await openCategoryModal(categoryNamePara, categoryId);
        };

        let deleteBtn = document.createElement("button");
        deleteBtn.classList.add("delete-category-btn");
        deleteBtn.textContent = "Delete";
        deleteBtn.onclick = async function (event) {
            event.stopPropagation();
            if (categoryId) await deleteParentCategory(categoryId, accordion);
        };

        buttonGroup.append(updateBtn, deleteBtn);
        header.append(categoryNamePara, buttonGroup);

        // Accordion content - sub categories list of parent
        let content = document.createElement("div");
    //    content.id = "accordion-content"
        content.classList.add("accordion-content");

        // Add Option button container
        let addOptionContainer = document.createElement("div");
        addOptionContainer.classList.add("add-option-btn-container");

        let addOptionBtn = document.createElement("button");
        addOptionBtn.textContent = "Add Option";
        addOptionBtn.classList.add("add-option-btn");
        addOptionBtn.onclick = async function (event) {
            event.stopPropagation();
            if(categoryId) await openAddSubCategoryModal(categoryId);
        };

        addOptionContainer.appendChild(addOptionBtn);
        content.appendChild(addOptionContainer);

        // Add existing sub-categories from backend
        subCategories.forEach(sub => {
            const subCategoryDiv = addSubCategory(sub.id, sub.categoryName);
            content.appendChild(subCategoryDiv);
        });

        accordion.append(header, content);
        container.appendChild(accordion);

        // Toggle visibility on header click
        header.onclick = () => {
            const allContents = document.querySelectorAll(".accordion-content");
            allContents.forEach(c => {
                if (c !== content) {
                    c.style.display = "none";
                }
            });
            content.style.display = content.style.display === "block" ? "none" : "block";
        };
    }

    // Add new sub category - UI functions
    function addSubCategory(subCategoryId = null, subCategoryName = "") {
        let subCategoryDiv = document.createElement("div");
        subCategoryDiv.classList.add("option");
        subCategoryDiv.dataset.id = subCategoryId;

        let subPara = document.createElement("p");
        subPara.classList.add("sub-category-name-para");
        subPara.textContent = subCategoryName;

        let subCategoryBtnGroup = document.createElement("div");
        subCategoryBtnGroup.classList.add("button-group");

        let subCategoryEditBtn = document.createElement("button");
        subCategoryEditBtn.classList.add("update-category-btn");
        subCategoryEditBtn.textContent = "Edit";
        subCategoryEditBtn.onclick = async function (event) {
            event.stopPropagation();
            if(subCategoryId) await openEditSubCategoryModal(subPara, subCategoryId);
        }

        let subCategoryDeleteBtn = document.createElement("button");
        subCategoryDeleteBtn.classList.add("delete-category-btn");
        subCategoryDeleteBtn.textContent = "Delete";
        subCategoryDeleteBtn.onclick = async function (event) {
            event.stopPropagation();
            if(subCategoryId) await deleteSubCategory(subCategoryId, subCategoryDiv);
        }

        subCategoryBtnGroup.append(subCategoryEditBtn, subCategoryDeleteBtn);
        subCategoryDiv.append(subPara, subCategoryBtnGroup);

        return subCategoryDiv;
    }

    // *************** Add/Edit Modal handling ***************

    let editingCategoryId = null;
    let editingParaEl = null;

    // Open modal for parent category add/edit
    function openCategoryModal(paraEl = null, categoryId = null) {
        const modal = document.getElementById("category-modal");
        const input = document.getElementById("modal-category-input");

        editingCategoryId = categoryId;
        editingParaEl = paraEl;

        document.getElementById("modal-title").textContent = categoryId ? "Edit Category" : "Add Category";
        input.value = paraEl ? paraEl.textContent : "";
        modal.style.display = "flex";

        input.focus();
    }

    function getSelectedVariationIds() {
        return new Promise((resolve, reject) => {
            try {
                setTimeout(() => {
                    const ids = window.selectedVariationIds;
                    if (!ids || !Array.isArray(ids)) {
                        return reject("Selected variations not available or invalid.");
                    }
                    resolve(ids);
                }, 0);
            } catch (err) {
                reject(err);
            }
        });
    }

    // Open Add Sub-category modal
    function openAddSubCategoryModal(categoryId = null) {
        const modal = document.getElementById("sub-category-modal");
        const input = document.getElementById("sub-modal-category-input");
        const saveBtn = document.getElementById("save-sub-category-btn");

        document.getElementById("sub-modal-title").textContent = "Add Sub-Category";
        input.value = "";
        modal.style.display = "flex";

        input.focus();

        saveBtn.onclick = async function (event) {
            event.stopPropagation();
            try {
                const selectedVariations = await getSelectedVariationIds();
                if (categoryId) await saveAddSubCategoryModal(categoryId, selectedVariations);
            } catch (error) {
                console.error("Variation selection failed", error);
                alert("Failed to fetch selected variations. Cannot save sub-category.");
        }
        };
    }

    // Open Edit Sub-category modal
    function openEditSubCategoryModal(paraEl = null, subCategoryId = null) {
        const modal = document.getElementById("sub-category-modal");
        const input = document.getElementById("sub-modal-category-input");
        const saveBtn = document.getElementById("save-sub-category-btn");

        document.getElementById("sub-modal-title").textContent = "Edit Sub Category";
        input.value = paraEl.textContent ;
        modal.style.display = "flex";

        input.focus();

        saveBtn.onclick = async function (event) {
            event.stopPropagation();
            const name = document.getElementById("sub-modal-category-input").value.trim();
            if (!name) return;
            if (subCategoryId) await saveEditSubCategoryModal(paraEl, name, subCategoryId);
        }
    }

    // Save from parent modal
    document.getElementById("save-category-btn").onclick = async function () {
        const name = document.getElementById("modal-category-input").value.trim();
        if (!name) return;

        try {
            if (editingCategoryId) {
                const response = await updateCategory(name, editingCategoryId);
                if (editingParaEl && response.ok) editingParaEl.textContent = name;
            } else {
                const response = await saveCategories(name);
                const saved = await response.json();
                addCategory(saved.categoryId, name, []);
            }
        } catch (e) {
            console.error("Modal Save Error", e);
            alert("Failed to save category.");
        } finally {
            closeCategoryModal();
        }
    };

    // Save from Add sub category modal
    async function saveAddSubCategoryModal(categoryId = null, selectedVariations = []) {
        const name = document.getElementById("sub-modal-category-input").value.trim();
        if (!name) return;
        try {
            if (categoryId) {
                const response = await saveSubCategory(name, categoryId, selectedVariations);
                const savedSub = await response.json();
                const subCategoryDiv = addSubCategory(savedSub.categoryId, name);
                const parentAccordion = document.querySelector(`.accordion[data-id='${categoryId}'] .accordion-content`);
                if (parentAccordion) {
                    parentAccordion.appendChild(subCategoryDiv);
                }
            }
        } catch (e) {
            console.error("Add sub modal - save error", e);
            alert("Failed to Save sub-category.");
        } finally {
            closeSubCategoryModal();
        }
    }

    // Save from Edit sub category modal
    async function saveEditSubCategoryModal(editingSubParaEl = null, name = null, subCategoryId = null) {
        try {
            if (subCategoryId) {
                const response = await updateSubCategory(name, subCategoryId);
                if (editingSubParaEl && response.ok) editingSubParaEl.textContent = name;
            }
        } catch (e) {
            console.error("Modal Update Error", e);
            alert("Failed to update sub-category.");
        } finally {
            closeSubCategoryModal();
        }
    }

    // 'Enter' key listener for modal save buttons
    document.addEventListener("keydown", function (e) {
        if(e.key === "Enter") {
            const categoryModal = document.getElementById("category-modal");
            const subCategoryModal = document.getElementById("sub-category-modal");

            if (categoryModal && categoryModal.style.display === "flex") {
                document.getElementById("save-category-btn")?.click();
            } else if (subCategoryModal && subCategoryModal.style.display === "flex") {
                document.getElementById("save-sub-category-btn")?.click();
            }
        }
    });

    // Close modal
    function closeCategoryModal() {
        document.getElementById("category-modal").style.display = "none";
        editingCategoryId = null;
        editingParaEl = null;
    }

    // Close Sub-category modal
    function closeSubCategoryModal() {
        document.getElementById("sub-category-modal").style.display = "none";
    }

    // 'Escape' key listener for closing modals
    document.addEventListener("keydown", function (e) {
        if (e.key === "Escape") {
            const categoryModal = document.getElementById("category-modal");
            const subCategoryModal = document.getElementById("sub-category-modal");
            if (categoryModal && categoryModal.style.display === "flex") {
                closeCategoryModal();
            }
            if (subCategoryModal && subCategoryModal.style.display === "flex") {
                closeSubCategoryModal();
            }
        }
    });

    // Load existing categories on page load
    window.onload = loadCategories;
//});
