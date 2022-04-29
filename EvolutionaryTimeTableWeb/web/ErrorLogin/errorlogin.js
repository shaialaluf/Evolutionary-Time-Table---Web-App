
// (function($) {
//     $(function() {
//         $('.update').live('change', function() {
//             formObject.run($(this));
//         });
//     });
// })(jQuery);


$(function() { // onload...do
    //add a function to the submit event
    $("#loginForm").submit(function() {
        $.ajax({
            data: $(this).serialize(),
            url: this.action,
            timeout: 2000,
            error: function(errorObject) {
                $("#errorMessage").text(errorObject.responseText);
                console.log("fail");

            },
            success: function(nextPageUrl) {
                console.log("gooodddd");
                window.location.replace(nextPageUrl);
            }
        });
        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    });
});