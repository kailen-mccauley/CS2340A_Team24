// JavaScript for image scrolling
let currentImageIndex = 0;
const container = document.querySelector('.diagram-image-container');
const images = container.querySelectorAll('.diagram-image');

// Display the first image and mark it as active initially
images[currentImageIndex].classList.add('active');

function scrollImage(direction) {
    // Remove 'active' class from the current active image
    images[currentImageIndex].classList.remove('active');

    // Update the currentImageIndex
    currentImageIndex = (currentImageIndex + direction + images.length) % images.length;

    // Add 'active' class to the new active image
    images[currentImageIndex].classList.add('active');
}
