const getAllVariations = "http://localhost:8080/variations/get-variations";

document.addEventListener('DOMContentLoaded', async () => {
    const subCategoryId = document.getElementById('sub-category-id').value;
    const variationsFieldSet = document.getElementById('variations-fieldset');

    //  Load variations
    async function loadVariations(subCategoryId) {
        try {
            const response = await fetch(`${getAllVariations}?id=${subCategoryId}`, {
                method: "GET",
                credentials: "include"
            });
            if(!response.ok) {
                throw new Error(`HTTP error ${response.status}`);
            }
            const variations = await response.json();
            if (!Array.isArray(variations)) {
                throw new Error("Invalid variation format");
            }

            populateVariations(variations);
        } catch (error) {
            console.error("Variation Load Error: ", error);
            alert("Unable to load variations. Please try again.");
        }
    }

    function populateVariations(variations) {
        variations.forEach(variation => {
            if(variation && variation.variationName && Array.isArray(variation.options)) {
                const label = document.createElement('label');
                label.classList.add("general-info-label");
                label.textContent = variation.variationName + ':';

                const select = document.createElement('select');
                select.name = variation.variationName;
                select.required = true;

                const placeholderOption = document.createElement('option');
                placeholderOption.value = '';
                placeholderOption.textContent = `Select ${variation.variationName}`;
                placeholderOption.disabled = true;
                placeholderOption.selected = true;
                select.appendChild(placeholderOption);

                variation.options.forEach(option => {
                    const optionElement = document.createElement('option');
                    optionElement.value = option.id;
                    optionElement.textContent = option.optionValue;
                    select.appendChild(optionElement);
                });
                label.appendChild(select);
                variationsFieldSet.appendChild(label);

                new Choices(select, {
                    placeholder: true,
                    searchEnabled: true,
                    searchPlaceholderValue: 'Search...',
                    itemSelectText: '',
                    shouldSort: true
                });
            } else {
                console.warn("Skipping invalid variation: ", variation);
            }
        });
    }

//    const subCategoryId = 121;
    await loadVariations(subCategoryId);

    //  Image adding - FilePond
    FilePond.registerPlugin(
        FilePondPluginImagePreview,
        FilePondPluginImageValidateSize,
        FilePondPluginFileValidateType,
        FilePondPluginImageCrop
    );

    const pond = FilePond.create(document.querySelector('#product-item-images'), {
        allowMultiple: true,
        minFiles: 3,
        maxFiles: 9,
        acceptedFileTypes: ['image/jpg', 'image/jpeg', 'image/png'],
        imageValidateSizeMinWidth: 300,
        imageValidateSizeMinHeight: 300,
        imagePreviewHeight: 150,
        stylePanelLayout: 'compact',
        instantUpload: false,
        storeAsFile: true,
        labelIdle: 'Drag & Drop or <span class="filepond--label-action">Browse</span> to upload',
        credits: false
    });

    pond.on('addfile', updateImageStats);
    pond.on('removefile', updateImageStats);

    function updateImageStats() {
        const files = pond.getFiles();
        const count = files.length;
        const totalSizeMB = (files.reduce((sum, f) => sum + f.file.size, 0) / (1024 * 1024)).toFixed(2);
        document.getElementById('image-stats').innerText = `Images: ${count} | Total size: ${totalSizeMB} MB`;
    }
});