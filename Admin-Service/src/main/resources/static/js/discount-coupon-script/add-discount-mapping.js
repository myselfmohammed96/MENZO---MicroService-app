const getLevelDetails = "http://localhost:8080/discount/level-details";
const saveDiscountMapping = "http://localhost:8080/discount/mapping";

let addMappingModal;
let modalOptionsContainer;
let saveMappingButton;



//  ********* fetch methods *********

//  FETCH - level details
async function fetchLevelDetails(cLevel, previousId) {
    try {
        const params = new URLSearchParams({ cLevel });
        if (previousId) params.append("previousId", previousId);

        const response = await fetch(`${getLevelDetails}?${params}`, {
            method: "GET",
            credentials: "include"
        });
        if (!response.ok) {
            throw new Error("Error fetching level details: " + response.status);
        }
        return await response.json();
    } catch (error) {
        console.error('Fetching level details failed: ', error);
        return null;
    }
}

async function postDiscountMapping(mappingData) {
    try {
        const response = await fetch(saveDiscountMapping, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            credentials: "include",
            body: JSON.stringify(mappingData)
        });
        if (!response.ok) throw new Error("Failed to save discount mapping");
        return response.json();
    } catch (error) {
        console.error("Error saving discount mapping: ", error);
        return null;
    }
}


function saveMappedElements() {
    const selectedIds = [];
    modalOptionsContainer
        .querySelectorAll('.discount-map-element')
        .forEach(e => {
            if (e.querySelector('.cc-select input').checked) {
                selectedIds.push(e.dataset.id);
            }
        });

    const mappingData = {
        discountId: window.discountId,
        level: window.discountLevel,
        selectionList: selectedIds
    }

        console.log(mappingData);
}

//  ********* Load methods *********

//  Level details - options builder
function levelDetailsTextContent(cLevel, text = null, image = null,
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



//  LOAD - Level details in modal
function loadLevelDetails(details = [], cLevel, isFinal = false) {
    try {
        if (!details.length) {
            throw new Error("Level details cannot be empty");
        }

        modalOptionsContainer.innerHTML = '';

        details.forEach(d => {
            const discountMapElement = document.createElement('div');
            discountMapElement.classList.add('discount-map-element');
            discountMapElement.dataset.id = d.id;

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
            const image = (cLevel === 'PRODUCT' || cLevel === 'VARIANT') && d.imageIcon
                        ? d.imageIcon : null;
            const size = cLevel === 'VARIANT' && d.size ? d.size : null;
            const color = cLevel === 'VARIANT' && d.color ? d.color : null;
            const hexCode = cLevel === 'VARIANT' && d.hexCode ? d.hexCode : null;

            const listTextContainer = levelDetailsTextContent(
                cLevel,
                d.text,
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

            //  change level on select event
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

//                saveMappingButton.disabled = false;
//                saveMappingButton.addEventListener('click', () => {
//                    saveMappedElements();
//                });
            }
            modalOptionsContainer.appendChild(discountMapElement);
        });
        if (isFinal) {
            saveMappingButton.disabled = false;
            saveMappingButton.addEventListener('click', () => {
                saveMappedElements();
            });
        }
    } catch (error) {
        console.error("Level details loading failed: ", error);
    }
}


//  LOAD - mapping modal
const levelHierarchy = ['CATEGORY', 'SUB_CATEGORY', 'PRODUCT', 'VARIANT'];
let currentLevel;

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

        navigateMappingModal(false, isFinal, selectedId);
    } catch (error) {
        console.error("Error changing level: ", error);
    }
}

async function navigateMappingModal(isInitialOpen = true, isFinal = false, selectedId = null) {
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

        const levelDetails = await fetchLevelDetails(currentLevel, selectedId);

        if (!Array.isArray(levelDetails)) {
            throw new Error("Invalid data format: Level details should be array");
        }

        //  Load modal content
        loadLevelDetails(levelDetails, currentLevel, isFinal)
    } catch (error) {
        console.error("Error loading mapping modal:", error);
    }
}



//  DOM Loading event
document.addEventListener('DOMContentLoaded', () => {

    addMappingModal = document.getElementById('add-discount-mapping-modal');
    modalOptionsContainer = document.querySelector('.modal-options-container');
    saveMappingButton = document.getElementById('save-mapping-button');

    //  ------- Modal open & close -------
    document.getElementById('add-mapping-button').addEventListener('click', () => {
        navigateMappingModal();
        addMappingModal.style.display = 'flex';
    });
    document.getElementById('close-discount-mapping-modal').addEventListener('click', () => {
        addMappingModal.style.display = 'none';
    });
});