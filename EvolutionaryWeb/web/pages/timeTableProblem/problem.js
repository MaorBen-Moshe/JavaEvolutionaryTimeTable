function getName(){
    $.ajax({
        data: null,
        url: "currentUserName",
        timeout: 2000,
        error: function (err){
            console.error("Fail to fetch name");
        },
        success: function (name){
            $('#nameLabel').empty().append("Hello, "  + name);
        }
    });
}

const refreshRate = 2000;
const USER_LIST_URL = buildUrlWithContextPath("userslist");

function refreshUsersList(users) {
    //clear all current users
    $("#userslist").empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(users || [], function(index, username) {
        console.log("Adding user #" + index + ": " + username);

        //create a new <li> tag with a value in it and append it to the #userslist (div with id=userslist) element
        $('<li>' + username + '</li>')
            .appendTo($("#userslist"));
    });
}

function getUserList(){
    $.ajax({
        url: USER_LIST_URL,
        timeout: 2000,
        success: function(users) {
            refreshUsersList(users);
        }
    });
}

$(function () {

    setTimeout(getName, 2000);
    setInterval(getUserList, refreshRate);

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