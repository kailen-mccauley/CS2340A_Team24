document.addEventListener("DOMContentLoaded", function() {
  var mainText = document.querySelectorAll("main section p, main section h4, main section details p");
  var summaryElements = document.querySelectorAll("summary");
  var summaryColor = '#f0f0f0';
  var textColor = '#000000';
  var backgroundColor = '#f0ead2';
  var white = '#ffffff';
  var black = '#000000';
  var standardBackgrounColor = '#f0ead2';
  var darkModeBackgroundColor = '#262423';
  var lightBoxColor = '#f0f0f0';
  var grayBoxColor = '#A9A9A9';

  function changeBackgroundColor() {
    var darkModeQuery = window.matchMedia('(prefers-color-scheme: dark)');
    if (darkModeQuery.matches) {
      textColor = white;
      backgroundColor = darkModeBackgroundColor;
      summaryColor = grayBoxColor;
    } else {
      textColor = black;
      backgroundColor = standardBackgrounColor;
      summaryColor = lightBoxColor;
    }
    mainText.forEach(function(mainText) {
      mainText.style.color = textColor;
    });
    document.body.style.backgroundColor = backgroundColor;
    summaryElements.forEach(function(summary) {
      summary.style.backgroundColor = summaryColor;
    });
  }
  changeBackgroundColor();
  window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', changeBackgroundColor);
}); 