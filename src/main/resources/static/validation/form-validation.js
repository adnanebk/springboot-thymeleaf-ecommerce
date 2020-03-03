// Example starter JavaScript for disabling form submissions if there are invalid fields
(function () {
  'use strict'

  window.addEventListener('load', function () {
    // Fetch all the forms we want to apply custom Bootstrap validation styles to
    var forms = document.getElementsByClassName('needs-validation')

    // Loop over them and prevent submission
    Array.prototype.filter.call(forms, function (form) {
      form.addEventListener('submit', function (event) {
        if (form.checkValidity() === false) {
          event.preventDefault()
          event.stopPropagation()
        }
        form.classList.add('was-validated')
      }, false)
    })
  }, false)
}());












//var http = require("http");
/*setInterval(function() {
    // http.get("http://<your app name>.herokuapp.com");
    $("#cc").text("yyyyy");

}, 6000); // every 5 minutes (300000)*/

