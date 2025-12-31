//let imageZoom = document.getElementById('image-zoom');
//if (imageZoom) {
//    imageZoom.addEventListener('mousemove', (event) => {
//        imageZoom.style.setProperty('--display', 'block');

//        let pointer = {
//            x: (event.offsetX * 100) / imageZoom.offsetWidth,
//            y: (event.offsetY * 100) / imageZoom.offsetHeight
//        }
//
//        imageZoom.style.setProperty('--zoom-x', pointer.x + '%');
//        imageZoom.style.setProperty('--zoom-y', pointer.y + '%');
//        // console.log(pointer);
//    });
//    imageZoom.addEventListener('mouseout', () => {
//        imageZoom.style.setProperty('--display', 'none');
//    });
//}


document.addEventListener('DOMContentLoaded', () => {
    const wrappers = document.querySelectorAll('.image-preview-wrapper');
    const previewModal = document.getElementById('open-image-preview-modal');
    const modalImage = document.getElementById('modal-image');
    const modalCloseIcon = document.getElementById('modal-close');
    const leftImageNavBtn = document.querySelector('.left-nav-btn');
    const rightImageNavBtn = document.querySelector('.right-nav-btn');

    const images = ["../media/red-cross.jpg", "../media/MENZO_logo.jpg", "../media/product_image_sample.jpg"];

    let currentImage;

    wrappers.forEach(wrapper => {
        const overlay = wrapper.querySelector('.image-overlay');

        wrapper.addEventListener('mousedown', () => {
            overlay.classList.add('active');
        });

        wrapper.addEventListener('mouseup', () => {
            overlay.classList.remove('active');
        });

        wrapper.addEventListener('mouseleave', () => {
            overlay.classList.remove('active');
        });

        wrapper.addEventListener('touchstart', () => {
            overlay.classList.add('active');
        });

        wrapper.addEventListener('touchend', () => {
            overlay.classList.remove('active');
        });

        wrapper.addEventListener('click', () => {
            const imageSrc = wrapper.querySelector('.image-preview').getAttribute('src');
            console.log(imageSrc);
            modalImage.setAttribute('src', imageSrc);

            currentImage = images.indexOf(imageSrc);

            previewModal.style.display = "flex";
        });
    });

    leftImageNavBtn.addEventListener('click', () => {
        if(currentImage === null || currentImage === undefined) return;

        if(currentImage === 0) {
            currentImage = images.length-1;
            modalImage.setAttribute('src', images[currentImage]);
        } else {
            currentImage--
            modalImage.setAttribute('src', images[currentImage]);
        }
    });

    rightImageNavBtn.addEventListener('click', () => {
        if(currentImage === null || currentImage === undefined) return;

        if(currentImage === images.length-1) {
            currentImage = 0;
            modalImage.setAttribute('src', images[currentImage]);
        } else {
            currentImage++;
            modalImage.setAttribute('src', images[currentImage]);
        }
    })

    modalCloseIcon.addEventListener('click', () => {
        currentImage = null;
        previewModal.style.display = 'none';
    });

});