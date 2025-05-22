// *************** API URLs ***************

const apiBase = "http://localhost:8080/variations";

const urls = {
    getAll: `${apiBase}/get-all`,
    addNewVariation: `${apiBase}/add-variation`
};

// *************** CRUD functions on variations and options ***************

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
        alert("Failed to save category.");
        throw error;
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
    // update btn onclick handler

    let deleteBtn = document.createElement("button");
    deleteBtn = classList.add("delete-variation-btn");
    deleteBtn.textContent = "Delete";
    // delete btn onclick handler

    buttonGroup.append(updateBtn, deleteBtn);
    header.append(variationNamePara, buttonGroup);

    // Accordion content - options list of variation
    let content = document.createElement("div");
    content.id = "accordion-content";
    content.classList.add("accorion-content");

    //Add Option button container
    let addOptionContainer = document.createElement("div");
    addOptionContainer.classList.add("add-option-btn-container");

    let addOptionBtn = document.createElement("button");
    addOptionBtn.textContent = "Add Option";
    addOptionBtn.classList.add("add-option-btn");
    // add option onclick handler

    addOptionContainer.appendChild(addOptionBtn);
    content.appendChild(addOptionContainer);

    // Add existing options from backend
    options.forEach(option => {
        const optionDiv = addOption(option.id, option.optionName);
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
function addOption(optionId = null, optionName = "") {
    let optionDiv = document.createElement("div");
    optionDiv.classList.add("option");
    optionDiv.dataset.id = optionId;

    let optionPara = document.createElement("p");
    optionPara.classList.add("option-name-para");
    optionPara.textContent = optionName;

    let optionBtnGroup = document.createElement("div");
    optionBtnGroup.classList.add("button-group");

    let optionEditBtn = document.createElement("button");
    optionEditBtn.classList.add("update-variation-btn");
    optionEditBtn.textContent = "Edit";
    // optionEdit btn onclick handler

    let optionDeleteBtn = document.createElement("button");
    optionDeleteBtn.classList.add("delete-variation-btn");
    optionDeleteBtn.textContent = "Delete";
    // optionDelete btn onclick handler

    optionBtnGroup.append(optionEditBtn, optionDeleteBtn);
    optionDiv.append(optionPara, optionBtnGroup);

    return optionDiv;
}

// *************** Add/Edit Modal handling ***************