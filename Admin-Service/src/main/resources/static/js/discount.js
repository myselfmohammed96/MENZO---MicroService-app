
let modalForm;

function openModalForm() {
    modalForm.style.display = "flex";
}

const closeModalForm = () => {
    modalForm.style.display = "none";
};



document.addEventListener('DOMContentLoaded', () => {

    modalForm = document.getElementById('discount-form-modal');

    //  open form modal
    document.getElementById('add-button').addEventListener('click', () => {
         openModalForm();
    });

    //  close form modal
    document.getElementById('close-form-modal-btn').addEventListener('click', () => {
        closeModalForm();
    });
});