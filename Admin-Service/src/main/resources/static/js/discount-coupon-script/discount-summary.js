
const getSummary = "";
const getMappedContent = "";

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
            method: "GET",
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
function populateMappedContent(content = []) {

}



//  ********* Data Loader methods *********

//  LOADER - discount details
async function loadDiscountDetails() {
    try {
        const summary = await fetchSummary(discountId);
        const mappedContent = await fetchMappedContent(discountId);
        if (!summary) {
            console.error("Discount summary not found");
            return;
        }
        if (!mappedContent) {
            console.error("Discount mapped content not found");
            return;
        }
        if (!Array.isArray(mappedContent)) {
            throw new Error("Invalid data format: mapped content should be array");
        }
        populateSummary(summary);
        populateMappedContent(mappedContent);
    } catch (error) {
        console.error("Error loading discount details");
    }
}


//  DOM Loading event
document.addEventListener("DOMContentLoaded", () => {

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