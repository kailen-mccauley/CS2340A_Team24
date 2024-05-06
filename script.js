document.addEventListener("DOMContentLoaded", function() {
  var colorButton = document.getElementById("colorButton");
  var mainSections = document.querySelectorAll("main > section");
  var summaryElements = document.querySelectorAll("summary");
  var textColor = '#000000';
  var backgroundColor = '#f0ead2';
  var summaryColor = '#f0f0f0';
  var white = '#ffffff';
  var black = '#000000';
  var standardBackgrounColor = '#f0ead2';
  var darkModeBackgroundColor = '#262423';
  var lightBoxColor = '#f0f0f0';
  var grayBoxColor = '#A9A9A9';

  colorButton.addEventListener("click", function() {
    changeBackgroundColor();
  });

  function changeBackgroundColor() {
    if (textColor == black) {
      textColor = white;
      backgroundColor = darkModeBackgroundColor;
      summaryColor = grayBoxColor;
      colorButton.textContent = 'Light Mode';
    } else {
      textColor = black;
      backgroundColor = standardBackgrounColor;
      summaryColor = lightBoxColor;
      colorButton.textContent = 'Dark Mode';
    }
    mainSections.forEach(function(section) {
      section.style.color = textColor;
    });
    document.body.style.backgroundColor = backgroundColor;
    summaryElements.forEach(function(summary) {
      summary.style.backgroundColor = summaryColor;
    });
  }
});
/* colorButton.addEventListener("click", function() {
  console.log("Button clicked"); // Check if button click event is captured
  changeBackgroundColor();
}); */