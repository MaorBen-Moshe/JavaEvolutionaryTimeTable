function ajaxLogin(){
    //add a function to the submit event
    $("#loginForm").submit(function() {
        $.ajax({
            data: $(this).serialize(),
            url: this.action,
            timeout: 2000,
            error: function(errorObject) {
                console.error("Failed to login !");
                $("#error-placeholder").empty().append(errorObject.responseText);
            },
            success: function(nextPageUrl) {
                window.location.replace(nextPageUrl);
            }
        });

        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    });
}

$(function() { // onload...do
    ajaxLogin();
});