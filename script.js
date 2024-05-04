document.addEventListener("DOMContentLoaded", function() {
    var mainText = document.querySelectorAll("main section p, main section h4, main section details p");
    var textColor = '#000000';
    var backgroundColor = '#f0ead2';
    var white = '#ffffff';
    var black = '#000000';
    var standardBackgrounColor = '#f0ead2';
    var darkModeBackgroundColor = '#262423';

    function changeBackgroundColor() {
      var darkModeQuery = window.matchMedia('(prefers-color-scheme: dark)');
      if (darkModeQuery.matches) {
        textColor = white;
        backgroundColor = darkModeBackgroundColor;
      } else {
        textColor = black;
        backgroundColor = standardBackgrounColor;
      }
      mainText.forEach(function(mainText) {
      mainText.style.color = textColor;
      });
      document.body.style.backgroundColor = backgroundColor;
    }
    changeBackgroundColor();
    window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', changeBackgroundColor);
  });