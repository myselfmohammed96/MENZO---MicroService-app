const getLevelElements = "http://localhost:8080/discount/level-elements";
const saveDiscountMapping = "http://localhost:8080/discount/mapping";

//  ## file name chng - Hierarchical mapping/ drill down selection
//  ## add back button
//  ## explore more robust way for this.. adding session based drill down if needed.

let addMappingModal;
let modalOptionsContainer;
let saveMappingButton;

const levelHierarchy = ['CATEGORY', 'SUB_CATEGORY', 'PRODUCT', 'VARIANT'];
let currentLevel;

let targetLevelElements;



/*
*   ---------------------------------
*   ********* FETCH methods *********
*   ---------------------------------
*/

//  FETCH - level elements (for drill-down selection) -------------------
async function fetchLevelElements(cLevel, previousId) {
    try {
        const params = new URLSearchParams({ cLevel });
        if (previousId) params.append("previousId", previousId);

        const response = await fetch(`${getLevelElements}?${params}`, {
            method: "GET",
            credentials: "include"
        });
        if (!response.ok) {
            throw new Error("Error fetching level elements: " + response.status);
        }
        return await response.json();
    } catch (error) {
        console.error('Fetching level elements failed: ', error);
        return null;
    }
}



/*
*   ----------------------------------------
*   ********* SAVE mapping methods *********
*   ----------------------------------------
*/

//  collect & save mapping
async function saveMappedElements(cLevel) {
    try {
        const selectedIds = [];
        modalOptionsContainer
            .querySelectorAll('.discount-map-element')
            .forEach(e => {
                if (e.querySelector('.cc-select input').checked) {
                    selectedIds.push(e.dataset.id);
                }
            });

        if (selectedIds.length === 0) {
            const level = cLevel.charAt(0).toUpperCase()
                        + cLevel.slice(1).toLowerCase().replaceAll('_', '-');
            window.showToast(`No ${level} selected.`, false);
            return;
        }

        //  building mapping object - for POST
        const mappingData = {
            discountId: window.discountId,
            level: window.discountLevel,
            selectionList: selectedIds
        };

        //  POST request - to backend
        const response = await fetch(saveDiscountMapping, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            credentials: "include",
            body: JSON.stringify(mappingData)
        });

        const data = await response.json().catch(() => ({}));

        if (response.ok) {
            //  populate selected elements in mapping list
            populateMappedElements(data.mappedContent, cLevel);

            //  close modal
            addMappingModal.style.display = 'none';

            //  show success toast
            const levelPluralMap = {
                CATEGORY: 'Categories',
                SUB_CATEGORY: 'Sub-categories',
                PRODUCT: 'Products',
                VARIANT: 'Variants'
            };
            const levelPlural = levelPluralMap[cLevel] || 'Elements';

            window.showToast(`${levelPlural} mapped successfully.!`, true);
        } else if (response.status >= 500) {
            console.error("Server error: ", data.message);

            //  close modal
            addMappingModal.style.display = 'none';

            //  show failure toast
            window.showToast("Server error. Try again.", false);
        } else {
            console.error("Error: ", data.message);

            //  close modal
            addMappingModal.style.display = 'none';

            //  show failure toast
            window.showToast(data.message || "Something went wrong. Try again.", false);
        }
//        console.log(mappingData);
    } catch (error) {
        console.error("Error saving discount mapping: ", error);
        window.showToast("Error saving discount mapping: ", false);
    }
}

//  populate mapped elements to mapping list
function populateMappedElements(mappedContent = [], cLevel) {
    try {
        if (!Array.isArray(mappedContent)) {
            throw new Error("Invalid data format: mapped content should be array.");
        }
        window.populateMappedContent(mappedContent, cLevel, true);
    } catch (error) {
        console.error("Error loading mapped content on save: ", error);
    }
}



/*
*   -------------------------------------------------
*   ********* DRILL-DOWN navigation methods *********
*   -------------------------------------------------
*/

//  change level - on selecting a drill-down element -------------------------------
function changeLevel(selectedLevel, selectedId) {
    try {
        if (!levelHierarchy.includes(selectedLevel)) {
            throw new Error("Invalid current level: " + selectedLevel);
        }
        const currentIndex = levelHierarchy.indexOf(selectedLevel);

        if (currentIndex === levelHierarchy.length - 1) {
            throw new Error("Final level reached: " + selectedLevel);
        }

        currentLevel = levelHierarchy[currentIndex + 1];
        const isFinal = currentLevel === window.discountLevel;

        drillDownStarter(false, isFinal, selectedId);
    } catch (error) {
        console.error("Error changing level: ", error);
    }
}

//  drill-down select starter/navigator (hierarchical/cascade mapping begins here) -----------------
async function drillDownStarter(isInitialOpen = true, isFinal = false, selectedId = null) {
    try {
        //  set initial level
        if (isInitialOpen) {
            currentLevel = levelHierarchy[0];
        } else if (selectedId == null) {
            throw new Error("Previous level ID is required.");
        }

        if (currentLevel === window.discountLevel) {
            isFinal = true;
        }

        const levelElements = await fetchLevelElements(currentLevel, selectedId);

        if (!Array.isArray(levelElements)) {
            throw new Error("Invalid data format: Level elements should be array");
        }

        //  Load modal content
        await loadLevelElements(levelElements, currentLevel, isFinal)
    } catch (error) {
        console.error("Error loading drill-down select:", error);
    }
}



/*
*   --------------------------------
*   ********* LOAD methods *********
*   --------------------------------
*/

//  LOAD - Level elements in modal ------------------
async function loadLevelElements(elements = [], cLevel, isFinal = false) {
    try {
        if (!elements.length) {
            throw new Error("Level elements cannot be empty");
        }

        modalOptionsContainer.innerHTML = '';

        elements.forEach(el => {
            const discountMapElement = document.createElement('div');
            discountMapElement.classList.add('discount-map-element');
            discountMapElement.dataset.id = el.id;

            const listElementContent = document.createElement('div');
            listElementContent.classList.add('list-element-content');

            //  checkbox
            const counterCheckboxContainer = document.createElement('div');
            counterCheckboxContainer.classList.add('counter-checkbox-container');

            const ccSelect = document.createElement('label');
            ccSelect.classList.add('cc-select');

            const checkBox = document.createElement('input');
            checkBox.type = 'checkbox';
            checkBox.disabled = !isFinal;

            checkBox.addEventListener('click', e => e.stopPropagation());

            const checkBoxSpan = document.createElement('span');

            ccSelect.append(
                checkBox,
                checkBoxSpan
            );
            counterCheckboxContainer.appendChild(ccSelect);
            listElementContent.appendChild(counterCheckboxContainer);

            //  text container
            const image = (cLevel === 'PRODUCT' || cLevel === 'VARIANT') && el.imageIcon
                        ? el.imageIcon : null;
            const size = cLevel === 'VARIANT' && el.size ? el.size : null;
            const color = cLevel === 'VARIANT' && el.color ? el.color : null;
            const hexCode = cLevel === 'VARIANT' && el.hexCode ? el.hexCode : null;

            const listTextContainer = levelElementsTextContent(
                cLevel,
                el.text,
                image,
                size,
                color,
                hexCode
            );

            listElementContent.insertAdjacentHTML('beforeend', listTextContainer);

            const hr = document.createElement('hr');
            hr.classList.add('line', 'line-86');

            discountMapElement.append(
                listElementContent,
                hr
            );

            //  action on select event (change level / )
            if (!isFinal) {
                discountMapElement.addEventListener('click', () => {
                    const selectedId = discountMapElement.dataset.id;
                    changeLevel(cLevel, selectedId);
                });
                saveMappingButton.disabled = true;
            } else {
                discountMapElement.addEventListener('click', () => {
                    const checkbox = discountMapElement.querySelector('.cc-select input');
                    checkbox.checked = !checkbox.checked;
                });
                discountMapElement.querySelector('.cc-select')
                    .addEventListener('click', e => e.stopPropagation());
            }
            modalOptionsContainer.appendChild(discountMapElement);
        });
        if (isFinal) {
            saveMappingButton.disabled = false;
            saveMappingButton.addEventListener('click', async () => {
                await saveMappedElements(cLevel);
            });
        }
    } catch (error) {
        console.error("Level details loading failed: ", error);
    }
}

//  Level details - options builder --------------------
function levelElementsTextContent(cLevel, text = null, image = null,
                            size = null, color = null, hexCode = null) {
    try {
        if (!text) {
           throw new Error('Text content is required');
        }
        if ((cLevel === 'PRODUCT' || cLevel === 'VARIANT') && !image) {
            throw new Error('Icon image is required');
        }
        if (cLevel === 'VARIANT' && (!size || !color || !hexCode)) {
            throw new Error('Variant details required');
        }

        const elementText = `
            <div class="element-text-1">
                <div class="element-sku-container">
                    <p>${text}</p>
                </div>
            </div>
        `;

        switch(cLevel) {
            case 'CATEGORY':
            case 'SUB_CATEGORY':
                return `
                    <div class="list-text-container list-options">
                        <div>${elementText}</div>
                    </div>
                `;

            case 'PRODUCT':
                return `
                    <div class="list-text-container list-options">
                    	<div class="list-option-image-container">
                    		<img src="/${image}" alt="">
                    	</div>
                    	<div>${elementText}</div>
                    </div>
                `;

            case 'VARIANT':
                return `
                    <div class="list-text-container list-options">
                    	<div class="list-option-image-container">
                    		<img src="/${image}" alt="">
                    	</div>
                    	<div>
                    		${elementText}
                    		<div class="element-text-2">
                    			<div class="element-text-2-row">
                    				<div class="element-color">
                    					<p class="element-color-key">Size: </p>
                    					<p class="element-color-value">${size} |</p>
                    					<p class="element-color-key">Color: </p>
                    					<p class="element-color-value">${color}</p>
                    					<div class="element-color-icon" style="background:${hexCode};"></div>
                    				</div>
                    			</div>
                    		</div>
                    	</div>
                    </div>
                `;

            default: throw new Error(`Invalid discount level: ${cLevel}`);
        }
    } catch (error) {
        console.error("Error loading level text content: ", error);
        return "";
    }
}


//  DOM Loading event
document.addEventListener('DOMContentLoaded', () => {
    addMappingModal = document.getElementById('add-discount-mapping-modal');
    modalOptionsContainer = document.querySelector('.modal-options-container');
    saveMappingButton = document.getElementById('save-mapping-button');

    //  ------- Modal open & close -------
    document.getElementById('add-mapping-button').addEventListener('click', () => {
        drillDownStarter();
        addMappingModal.style.display = 'flex';
    });
    document.getElementById('close-discount-mapping-modal').addEventListener('click', () => {
        addMappingModal.style.display = 'none';
    });
});