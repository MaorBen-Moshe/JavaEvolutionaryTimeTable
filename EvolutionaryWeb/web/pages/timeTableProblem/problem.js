$(function () {
    $('#logout').on("click", function () {
        $.ajax({
            data: null,
            url: "logout",
            timeout: 2000,
            error: function(errorObject) {
                console.error("Failed to logout !");
                alert(errorObject.responseText);
            },
            success: function(primaryUrl) {
                window.location.replace(primaryUrl);
                console.log(primaryUrl);
            }
        });

        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    });
});