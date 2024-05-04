document.addEventListener("DOMContentLoaded", function() {
    var colorButton = document.getElementById("colorButton");
    var mainSections = document.querySelectorAll("main > section");
    var sprintParagraphs = document.querySelectorAll('details > p');
    var textColor = '#000000';
    var backgroundColor = '#f0ead2';
    var white = '#ffffff';
    var black = '#000000';
    var standardBackgrounColor = '#f0ead2';
    var darkModeBackgroundColor = '#262423';
  
    colorButton.addEventListener("click", function() {
      changeBackgroundColor();
    });
  
    function changeBackgroundColor() {
      if (textColor == black) {
        textColor = white;
        backgroundColor = darkModeBackgroundColor;
        colorButton.textContent = 'Light Mode';
      } else {
        textColor = black;
        backgroundColor = standardBackgrounColor;
        colorButton.textContent = 'Dark Mode';
      }
      mainSections.forEach(function(section) {
        section.style.color = textColor;
      });
      sprintParagraphs.forEach(function(sprintParagraphs) {
        sprintParagraphs.style.color = textColor;
      });
      document.body.style.backgroundColor = backgroundColor;
    }
  });
  /* colorButton.addEventListener("click", function() {
    console.log("Button clicked"); // Check if button click event is captured
    changeBackgroundColor();
  }); */