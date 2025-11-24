//  ********* Modal - Add product item form toggle *********
const toggleFormModal = (modalStatus) => {
    const formModal = document.getElementById('add-item-form-modal');
    const modalDisplay = getComputedStyle(formModal).display;

    if(modalStatus === "open" && modalDisplay === "none") {
        formModal.style.display = "flex"
    } else if(modalStatus === "close" && modalDisplay === "flex" ){
        formModal.style.display = "none";
    }
}


//  ********* DOM Loading event listener *********
document.addEventListener("DOMContentLoaded", () => {

    //  product item form - modal toggle
    document.getElementById('add-item-button').addEventListener('click', () => {
        toggleFormModal('open');
    });

    document.getElementById('modal-form-close').addEventListener('click', () => {
        toggleFormModal('close');
    });



    //  ******* Image upload - FilePond *******

        FilePond.registerPlugin(
            FilePondPluginImagePreview,
            FilePondPluginImageValidateSize,
            FilePondPluginFileValidateType,
            FilePondPluginImageCrop
        );

        const pond = FilePond.create(document.querySelector('#product-images'), {
            allowMultiple: true,
            minFiles: 3,
            maxFiles: 9,
            acceptedFileTypes: ['image/jpg', 'image/jpeg', 'image/png'],
            imageValidateSizeMinWidth: 300,
            imageValidateSizeMinHeigth: 300,
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
            document.getElementById('image-stats').innerText = `Images: ${count} | Total Size: ${totalSizeMB} MB`;
        }

});