// function getName(){
//     $.ajax({
//         data: null,
//         url: "currentUserName",
//         timeout: 2000,
//         error: function (err){
//             console.error("Fail to fetch name");
//         },
//         success: function (name){
//             $('#nameLabel').empty().append("Hello, "  + name);
//         }
//     });
// }
//
// const refreshRate = 2000;
// const USER_LIST_URL = buildUrlWithContextPath("userslist");
//
// function refreshUsersList(users) {
//     //clear all current users
//     $("#userslist").empty();
//
//     // rebuild the list of users: scan all users and add them to the list of users
//     $.each(users || [], function(index, username) {
//         console.log("Adding user #" + index + ": " + username);
//
//         //create a new <li> tag with a value in it and append it to the #userslist (div with id=userslist) element
//         $('<li>' + username + '</li>')
//             .appendTo($("#userslist"));
//     });
// }
//
// function getUserList(){
//     $.ajax({
//         url: USER_LIST_URL,
//         timeout: 2000,
//         success: function(users) {
//             refreshUsersList(users);
//         }
//     });
// }
//
// $(function () {
//
//     setTimeout(getName, 2000);
//     setInterval(getUserList, refreshRate);
//
//     $('#logout').on("click", function () {
//         $.ajax({
//             data: null,
//             url: "logout",
//             timeout: 2000,
//             error: function(errorObject) {
//                 console.error("Failed to logout !");
//                 alert(errorObject.responseText);
//             },
//             success: function(primaryUrl) {
//                 window.location.replace(primaryUrl);
//                 console.log(primaryUrl);
//             }
//         });
//
//         // by default - we'll always return false so it doesn't redirect the user.
//         return false;
//     });
// });

const refreshRate = 2000;
const USER_LIST_URL = "userslist";
const LOGOUT_URL = "logout";
const PROBLEMS_URL = "problems";
const PROBLEMS_LIST_URL = "problemslist";

$(function () {
    ajaxLoggedInUsername();
    ajaxUsersList();
    ajaxProblemList();

    formUploadFileSetEvents();

    setInterval(ajaxUsersList, refreshRate);
    setInterval(ajaxProblemList, refreshRate);
})

// TODO - Move to different more general js file
function ajaxUsersList() {
    $.ajax({
        url: USER_LIST_URL,
        data: {
            action: "userList"
        },
        success: refreshUsersList,
        error: function(object) {
            console.log("Couldn't pull the users from the server. Sent: ");
            console.log(object);
        }
    });
}

function refreshUsersList(users) {
    console.log("pulled new user list: " + users);
    $("#users-list").empty();

    $.each($(".users-counter"), function(index, element) {
        element.innerText = (users || []).length;
    });

    $.each(users || [], function(index, username) {
        $('<div class="user">' +
            '<label id="user-name" class="cut-text">' +
            username +
            '</label>' +
            '</div>')
            .appendTo($("#users-list"));
    });
}

// TODO - Move to different more general js file
function ajaxLoggedInUsername() {
    $.ajax({
        url: USER_LIST_URL,
        data: {
            action: "username"
        },
        success: function(username) {
            $.each($(".username-logged-in") || [], function(index, element) {
                element.innerHTML = username;
            });
        },
        error: function(relocation) {
            window.location.href = buildUrlWithContextPath(relocation.responseText);
        }
    });
}

function ajaxProblemList() {
    $.ajax({
        url: PROBLEMS_LIST_URL,
        success: refreshProblemList,
        error: function(object) {
            console.log("Couldn't pull the problems from the server. Sent: ");
            console.log(object);
        }
    });
}

function refreshProblemList(problems) {
    var tableBody = $(".problems-table-body")[0];

    tableBody.innerHTML = "";
    $.each(problems || [], function(index, problem) {
        var trRow = document.createElement("tr");

        var tdID = document.createElement("td");
        var tdUploader = document.createElement("td");
        var tdProblemInfo = document.createElement("td");
        var tdRules = document.createElement("td");
        var tdUsers = document.createElement("td");
        var tdBestFitness = document.createElement("td");

        tdID.innerText = problem.problemID;
        tdUploader.innerText = problem.uploader;
        tdProblemInfo.appendChild(createSectionProblemInfo(problem));
        tdRules.appendChild(createSectionRulesInfo(problem));
        tdUsers.innerText = problem.users.length;
        tdBestFitness.innerText = problem.bestFitness;

        trRow.appendChild(tdID);
        trRow.appendChild(tdUploader);
        trRow.appendChild(tdProblemInfo);
        trRow.appendChild(tdRules);
        trRow.appendChild(tdUsers);
        trRow.appendChild(tdBestFitness);

        trRow.onclick = createProblemDialog;

        tableBody.appendChild(trRow);
    });
}

function createSectionProblemInfo(problem) {
    var sectionInfo = $(document.createElement("section")).addClass("grid")[0];

    var tdDays = $(document.createElement("label")).text("Days:" + problem.days)[0];
    var tdHours = $(document.createElement("label")).text("Hours:" + problem.hours)[0];
    var tdTeachers = $(document.createElement("label")).text("Teachers:" + problem.teachers)[0];
    var tdClasses = $(document.createElement("label")).text("Classes:" + problem.classes)[0];
    var tdCourses = $(document.createElement("label")).text("Courses:" + problem.courses)[0];

    sectionInfo.appendChild(tdDays);
    sectionInfo.appendChild(tdHours);
    sectionInfo.appendChild(tdTeachers);
    sectionInfo.appendChild(tdClasses);
    sectionInfo.appendChild(tdCourses);

    return sectionInfo;
}

function createSectionRulesInfo(problem) {
    var sectionInfo = $(document.createElement("section")).addClass("grid")[0];

    var tdHard = $(document.createElement("label")).text("Hard rules:" + problem.hardRules)[0];
    var tdSoft = $(document.createElement("label")).text("Soft rules:" + problem.softRules)[0];

    sectionInfo.appendChild(tdSoft);
    sectionInfo.appendChild(tdHard);

    return sectionInfo;
}

function logout() {
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
}

function createProblemDialog(event) {
    // TODO: display a nice dialog if the user want to solve this problem or not.

    var id = this.children[0].innerText;

    $.ajax({
        url: PROBLEMS_URL,
        data: {
            problemId: id,
            action: "add"
        },
        success: function (json) {
            window.location.href = buildUrlWithContextPath(json.url);
        }
    });
}

function formUploadFileSetEvents() {
    $("#file-input")[0].onchange = function () {
        $(".upload-result")[0].innerHTML = "";
        $(".upload-button")[0].disabled = false;
        $(".file-path")[0].innerText = "File Name: " + this.files[0].name;
    };

    $("#upload-form").submit(function () {
        var formData = new FormData();
        var file = $("#file-input")[0].files[0];
        if (file === undefined) {
            $(".upload-result")[0].style.color = "red";
            $(".upload-result")[0].innerText = "Please choose a file and don't press 'cancel'";
            $(".file-path").empty();
            $(".upload-button")[0].disabled = true;
            return false;
        }
        formData.append("file", file);

        $.ajax({
            method: 'POST',
            data: formData,
            url: this.action,
            processData: false, // Don't process the files
            contentType: false, // Set content type to false as jQuery will tell the server its a query string request
            timeout: 4000,
            success: validateFile
        });
        return false;
    });
}

function validateFile(json) {
    // json object:
    //  isFileCorrupted - indicate if the file successfully loaded or not.
    //  errorMessage - the message what's the problem in the file.
    if (json.isFileCorrupted) {
        $(".upload-result")[0].style.color = "red";
        $(".upload-result")[0].innerText = json.errorMessage;
    } else {
        $(".upload-result")[0].style.color = "limegreen";
        $(".upload-result")[0].innerText = "Upload the file successfully!";
        $("#file-input")[0].value = "";
        $(".file-path").empty();
        $(".upload-button")[0].disabled = true;
    }
}