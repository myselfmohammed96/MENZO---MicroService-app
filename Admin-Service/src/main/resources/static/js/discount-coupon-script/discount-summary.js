
const getSummary = "http://localhost:8080/discount/summary";
const getMappedContent = "http://localhost:8080/discount/mapped-content";

let mappedListWrapper;

let discountId;
let discountAddSuccess;



//  ********* FETCH methods *********

//  FETCH - discount summary
async function  fetchSummary(id) {
    try {
        const response = await fetch(`${getSummary}?id=${id}`, {
            method: "GET",
            credentials: "include"
        });
        if (!response.ok) {
            throw new Error("Error fetching discount summary: ", response.status);
        }
        return await response.json();
    } catch (error) {
        console.error("Fetching discount summary failed: ", error);
    }
}

//  FETCH - mapped content
async function fetchMappedContent(id) {
    try {
        const response = await fetch(`${getMappedContent}?id=${id}`, {
            method: "POST",
            credentials: "include"
        });
        if (!response.ok) {
            throw new Error("Error fetching mapped content: ", response.status);
        }
        return await response.json();
    } catch (error) {
        console.error("Fetching mapped content failed: ", error);
    }
}



//  ********* POPULATE methods *********

//  POPULATE - discount summary
function populateSummary(summary) {
    try {
        const generalDetails = {
            name: document.getElementById("name"),
            description: document.getElementById("description"),
            code: document.getElementById("code"),
            level: document.getElementById("level"),
            type: document.getElementById("type"),
            value: document.getElementById("value"),
            capType: document.getElementById("cap-type"),
            capValue: document.getElementById("cap-value"),
            priority: document.getElementById("priority")
        };
        const validityDetails = {
            startDate: document.getElementById("start-at"),
            endDate: document.getElementById("end-at"),
            status: document.getElementById("status"),
            resumeDate: document.getElementById("resume-at"),
            timeZone: document.getElementById("time-zone")
        };
        const otherDetails = {
            createdAt: document.getElementById("created-at"),
            createdBy: document.getElementById("created-by"),
            updatedAt: document.getElementById("updated-at"),
            updatedBy: document.getElementById("updated-by")
        };

        //  populate general details
        if (summary.discountName) {
            generalDetails.name.querySelector('p').textContent = summary.discountName;
        }

        if (summary.discountDescription && summary.discountDescription !== "") {
            generalDetails.description.querySelector('p').textContent = summary.discountDescription;
        } else {
            generalDetails.description.style.display = 'none';
        }

        if (summary.discountCode) {
            generalDetails.code.querySelector('p').textContent = summary.discountCode;
        }

        if (summary.level) {
            const level = summary.level.charAt(0).toUpperCase() + summary.level.slice(1).toLowerCase().replaceAll('_', '-');
            generalDetails.level.querySelector('p').textContent = level;
        }

        if (summary.type) {
            const type = summary.type.charAt(0).toUpperCase() + summary.type.slice(1).toLowerCase();
            generalDetails.type.querySelector('p').textContent = type;
        }

        if (summary.value !== null && summary.value !== undefined) {
            let value;
            if (summary.type === "PERCENT") {
                value = summary.value + "%";
            } else if (summary.type === "FLAT") {
                value = "₹" + summary.value;
            }
            generalDetails.value.querySelector('p').textContent = value;
        }

        if (summary.capType) {
            const capType = summary.capType.charAt(0).toUpperCase() + summary.capType.slice(1).toLowerCase();
            generalDetails.capType.querySelector('p').textContent = capType;
        }

        if (summary.capValue !== null && summary.capValue !== undefined && summary.capType !== "NONE") {
            let capValue;
            if (summary.capType === "PERCENT") {
                capValue = summary.capValue + "%";
            } else if (summary.capType === "AMOUNT") {
                capValue = "₹" + summary.capValue;
            }
            generalDetails.capValue.querySelector('p').textContent = capValue;
        } else {
            generalDetails.capValue.style.display = 'none';
        }

        if (summary.priority !== null && summary.priority !== undefined) {
            generalDetails.priority.querySelector('p').textContent = summary.priority;
        } else {
            generalDetails.priority.style.display = 'none';
        }

        //  populate validity details
        if (summary.startAt) {
            const startAt = new Date(summary.startAt);
            validityDetails.startDate.querySelector('p').textContent = startAt.toLocaleString("en-GB", {
                day: "2-digit",
                month: "short",
                year: "numeric",
                hour: "2-digit",
                minute: "2-digit",
                hour12: true
            });
        }

        if (summary.endAt) {
            const endAt = new Date(summary.endAt);
            validityDetails.endDate.querySelector('p').textContent = endAt.toLocaleString("en-GB", {
                day: "2-digit",
                month: "short",
                year: "numeric",
                hour: "2-digit",
                minute: "2-digit",
                hour12: true
            });
        }

        if (summary.status) {
            const statusColor = {
                'ACTIVE': 'status-green',
                'INACTIVE': 'status-red',
                'SCHEDULED': 'status-blue',
                'PAUSED': 'status-amber',
                'EXPIRED': 'status-grey',
                'CANCELLED': 'status-maroon'
            }[summary.status] || '';
            const status = summary.status.charAt(0).toUpperCase() + summary.status.slice(1).toLowerCase();
            const statusElement = validityDetails.status.querySelector('p span');

            statusElement.textContent = status;
            statusElement.classList.add(statusColor);
        }

        if (summary.resumeAt && summary.status === "PAUSED") {
            const resumeAt = new Date(summary.resumeAt);
            validityDetails.resumeDate.querySelector('p').textContent = resumeAt.toLocaleString("en-GB", {
                day: "2-digit",
                month: "short",
                year: "numeric",
                hour: "2-digit",
                minute: "2-digit",
                hour12: true
            });
        } else {
            validityDetails.resumeDate.style.display = 'none';
        }

        if (summary.timeZone) {
            validityDetails.timeZone.querySelector('p').textContent = summary.timeZone;
        }

        //  populate other details
        if (summary.createdAt) {
            const createdAt = new Date(summary.createdAt);
            otherDetails.createdAt.querySelector('p').textContent = createdAt.toLocaleDateString("en-GB", {
                day: "2-digit",
                month: "short",
                year: "numeric"
            });
        }

        if (summary.createdBy) {
            otherDetails.createdBy.querySelector('p').textContent = summary.createdBy;
        } else {
            otherDetails.createdBy.style.display = 'none';
        }

        if (summary.updatedAt) {
            const updatedAt = new Date(summary.updatedAt);
            otherDetails.updatedAt.querySelector('p').textContent = updatedAt.toLocaleDateString("en-GB", {
                day: "2-digit",
                month: "short",
                year: "numeric"
            });
        } else {
            otherDetails.updatedAt.style.display = 'none';
        }

        if (summary.updatedBy) {
            otherDetails.updatedBy.querySelector('p').textContent = summary.updatedBy;
        } else {
            otherDetails.updatedBy.style.display = 'none';
        }

    } catch (error) {
        console.error("Error populating discount summary: ", error);
    }
}

//  POPULATE - mapped content
//  ## take care of itemCount.. make it local if necessary.. populate or repopulate design stuff
let itemCount = 1;
window.populateMappedContent = function (content = [], level, rePopulate = false) {
    try {
        const heading = level.split('_')
                            .filter(Boolean)
                            .map(s => s.charAt(0).toUpperCase() + s.slice(1).toLowerCase())
                            .join('-');

        const listHeading = document.getElementById('mapping-list-heading');
        listHeading.textContent = heading;
    } catch (error) {
        console.error("Error loading mapped list heading: ", error);
    }
    try {
        mappedListWrapper.innerHTML = '';

        if (rePopulate) {
            itemCount = 1;
        }

        if (level === "GLOBAL") {
            mappedListWrapper.innerHTML = `
                <div class="empty-list-container">
                    <span>No mapping for global discount</span>
                </div>
            `;
            return;
        }

        if (content.length === 0) {
            const levelText = level.toLowerCase().replaceAll('_', '-');
            mappedListWrapper.innerHTML = `
                <div class="empty-list-container">
                    <span>No ${levelText} mapped yet</span>
                </div>
            `;
            return;
        }

        content.forEach(c => {
            const discountMapElement = document.createElement('div');
            discountMapElement.classList.add("discount-map-element");
            discountMapElement.setAttribute('data-mappingId', c.mappingId);

            const listElementContent = document.createElement('div');
            listElementContent.classList.add("list-element-content");

            //  ------- counter & checkbox container -------
            const counterCheckboxContainer = document.createElement('div');
            counterCheckboxContainer.classList.add('counter-checkbox-container');

            const ccSelect = document.createElement('label');
            ccSelect.classList.add('cc-select');

            const checkbox = document.createElement('input');
            checkbox.type = 'checkbox';
            checkbox.addEventListener('click', (e) => {
                e.stopPropagation();
            });

            const counter = document.createElement('span');
            counter.textContent = itemCount;
            itemCount++;

            ccSelect.append(
                checkbox,
                counter
            );
            counterCheckboxContainer.appendChild(ccSelect);

            //  ------- text container -------
            const listTextContainer = document.createElement('div');
            listTextContainer.classList.add('list-text-container');

            //  element text 1
            const elementText1 = document.createElement('div');
            elementText1.classList.add('element-text-1');

            const textWrapper = document.createElement('div');
            textWrapper.classList.add('element-text-wrapper');

            const text = document.createElement('p');
            text.textContent = c.textContent;

            textWrapper.appendChild(text);
            elementText1.appendChild(textWrapper);

            //  element text 2
            const elementText2 = document.createElement('div');
            elementText2.classList.add('element-text-2');

            const elementText2Row = document.createElement('div');
            elementText2Row.classList.add('element-text-2-row');

            const elementText2Wrapper = document.createElement('div');
            elementText2Wrapper.classList.add('element-text-2-wrapper');

            if (level === "VARIANT") {
                const text2Key1 = document.createElement('p');
                text2Key1.classList.add('element-text-2-key');
                text2Key1.textContent = "Size: ";

                const text2Value1 = document.createElement('p');
                text2Value1.classList.add('element-text-2-value');
                text2Value1.textContent = c.size + " |";
                //  ## replace the " |" with a vertical short <hr>

                const text2Key2 = document.createElement('p');
                text2Key2.classList.add('element-text-2-key');
                text2Key2.textContent = "Color: ";

                const text2Value2 = document.createElement('p');
                text2Value2.classList.add('element-text-2-value');
                text2Value2.textContent = c.color;

                const colorIcon = document.createElement('div');
                colorIcon.classList.add('element-color-icon');
                colorIcon.style.background = c.hexCode;

                elementText2Wrapper.append(
                    text2Key1,
                    text2Value1,
                    text2Key2,
                    text2Value2,
                    colorIcon
                );
            }

            if (level !== "VARIANT" && c.exclusionCount > 0) {
                const text2Key = document.createElement('p');
                text2Key.classList.add('element-text-2-key');
                text2Key.textContent = "Exclusion: ";

                const text2Value = document.createElement('p');
                text2Value.classList.add('element-text-2-value');
                text2Value.textContent = c.exclusionCount;

                elementText2Wrapper.append(
                    text2Key,
                    text2Value
                );
            }

            elementText2Row.appendChild(elementText2Wrapper);
            elementText2.appendChild(elementText2Row);
            listTextContainer.append(
                elementText1,
                elementText2
            );

            //  ------- button container -------
            const listButtonContainer = document.createElement('div');
            listButtonContainer.classList.add('list-button-container');

            //  exclude button
            if (level !== "VARIANT") {
                const excludeButton = document.createElement('button');
                excludeButton.classList.add('list-item-button', 'list-item-exclude-button');
                excludeButton.title = 'Exclude';

                const excludeButtonIcon = document.createElement('img');
                excludeButtonIcon.src = '../media/traffic-signal.png';

                excludeButton.appendChild(excludeButtonIcon);
                excludeButton.addEventListener('click', (e) => {
                    e.stopPropagation();
                    //  exclude logic
                });

                listButtonContainer.appendChild(excludeButton);
            }

            //  delete button
            const deleteButton = document.createElement('button');
            deleteButton.classList.add('list-item-button', 'list-item-delete-button');
            deleteButton.title = 'Delete';

            const deleteButtonIcon = document.createElement('img');
            deleteButtonIcon.src = '../media/delete.png';

            deleteButton.appendChild(deleteButtonIcon);
            deleteButton.addEventListener('click', () => {
                e.stopPropagation();
                //  delete logic
            });

            listButtonContainer.appendChild(deleteButton);

            //  ------- final append -------
            listElementContent.append(
                counterCheckboxContainer,
                listTextContainer,
                listButtonContainer
            );
            discountMapElement.appendChild(listElementContent);

            discountMapElement.addEventListener('click', () => {
                console.log("Propagating discount map element click event");
            });

            mappedListWrapper.appendChild(discountMapElement);
        });
    } catch (error) {
        console.error("Error populating mapped content: ", error);
    }
}



//  ********* Data Loader methods *********

//  LOADER - discount details
async function loadDiscountDetails() {
    try {
        const summaryResult = await fetchSummary(discountId);
        if (!summaryResult || !summaryResult.summary) {
            console.error("Discount summary not found");
            return;
        }
        window.discountId = summaryResult.summary.discountId;
        window.discountLevel = summaryResult.summary.level;
        populateSummary(summaryResult.summary);

        const mappedContentResult = await fetchMappedContent(discountId);
        if (!mappedContentResult || !mappedContentResult.content) {
            console.error("Discount mapped content not found");
            return;
        }
        if (!Array.isArray(mappedContentResult.content)) {
            throw new Error("Invalid data format: mapped content should be array");
        }
        populateMappedContent(mappedContentResult.content, window.discountLevel);

    } catch (error) {
        console.error("Error loading discount details: ", error);
    }
}


//  DOM Loading event
document.addEventListener("DOMContentLoaded", async () => {

    mappedListWrapper = document.getElementById('list-wrapper-body');

    //  ------- Get - param data -------
    const params = new URLSearchParams(window.location.search);
    discountId = params.get('id');
    discountAddSuccess = params.get('success') === 'true';

    //  ------- Load - initial data -------
    await loadDiscountDetails();

    //  ------- Success toast (for add new discount) -------
    if (discountAddSuccess) {
        window.showToast("Discount successfully added.", true);
    }

});