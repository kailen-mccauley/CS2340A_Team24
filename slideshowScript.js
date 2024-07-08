// Get the slides
var slides = document.querySelectorAll('.slide');

// Initialize current index
var currentIndex = 0;

// Function to update slides display
function updateSlides() {
    // Hide all slides
    slides.forEach(function(slide) {
        slide.style.display = 'none';
    });

    // Display the current and next slide
    for (var i = currentIndex; i < currentIndex + 2; i++) {
        slides[i].style.display = 'block';
    }
}

// Function to show the next pair of images
function nextSlides() {
    currentIndex += 2;
    if (currentIndex >= slides.length) {
        currentIndex = 0; // Wrap around to the beginning
    }
    updateSlides();
}

// Function to show the previous pair of images
function prevSlides() {
    currentIndex -= 2;
    if (currentIndex < 0) {
        currentIndex = slides.length - 2; // Wrap around to the end
    }
    updateSlides();
}

// Initially update slides
updateSlides();
