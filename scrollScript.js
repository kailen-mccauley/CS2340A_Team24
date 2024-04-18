let currentImageIndex = 0;
const images = document.querySelectorAll('.image-container');

function scrollImage(direction) {
    images[currentImageIndex].classList.remove('active');
    currentImageIndex = (currentImageIndex + direction + images.length) % images.length;
    images[currentImageIndex].classList.add('active');
}
