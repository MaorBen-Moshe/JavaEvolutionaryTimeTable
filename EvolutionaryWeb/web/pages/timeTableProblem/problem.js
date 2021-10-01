const refreshRate = 2000;
const USER_LIST_URL = buildUrlWithContextPath("userslist");
const LOGOUT_URL = "logout";
const PROBLEM_URL = buildUrlWithContextPath("problem");
const PROBLEMS_LIST_URL = buildUrlWithContextPath("problemslist");

$(function () {
    ajaxLoggedInUsername();
    ajaxUsersList();
    ajaxProblemList();
    formUploadFileSetEvents();

    setInterval(ajaxUsersList, refreshRate);
    setInterval(ajaxProblemList, refreshRate);
})

function ajaxUsersList() {
        $.ajax({
        url: USER_LIST_URL,
        timeout: 2000,
        success: function(users) {
            refreshUsersList(users);
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

function ajaxLoggedInUsername() {
        $.ajax({
        data: null,
        url: "currentUserName",
        timeout: 2000,
        error: function (err){
            console.error("Fail to fetch name");
        },
        success: function (name){
            $('#nameLabel').empty().append(name);
        }
    });
}

function ajaxProblemList() {
    $.ajax({
        url: PROBLEMS_LIST_URL,
        success: refreshProblemList,
        error: function(object) {
            console.log("Couldn't pull the problems from the server. Sent: ");
            console.log(object.responseText);
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

        tdID.innerText = problem.problemId;
        tdUploader.innerText = problem.creatorName;
        tdProblemInfo.appendChild(createSectionProblemInfo(problem));
        tdRules.appendChild(createSectionRulesInfo(problem));
        tdUsers.innerText = problem.usersSolveProblemSize;
        tdBestFitness.innerText = problem.currentBestFitnessOfProblem;

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
    var tdCourses = $(document.createElement("label")).text("Courses:" + problem.subjects)[0];

    sectionInfo.appendChild(tdDays);
    sectionInfo.appendChild(tdHours);
    sectionInfo.appendChild(tdTeachers);
    sectionInfo.appendChild(tdClasses);
    sectionInfo.appendChild(tdCourses);

    return sectionInfo;
}

function createSectionRulesInfo(problem) {
    var sectionInfo = $(document.createElement("section")).addClass("grid")[0];

    var tdHard = $(document.createElement("label")).text("Hard rules:" + problem.numberOfHardRules)[0];
    var tdSoft = $(document.createElement("label")).text("Soft rules:" + problem.numberOfSoftRules)[0];

    sectionInfo.appendChild(tdSoft);
    sectionInfo.appendChild(tdHard);

    return sectionInfo;
}

function logout() {
    $.ajax({
            data: null,
            url: LOGOUT_URL,
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
    const id = this.children[0].innerText;
    alert("You are entering to problem with id: " + id);

    $.ajax({
        data: {"id" : id},
        url: PROBLEM_URL,
        timeout: 2000,
        success: function (path) {
            window.location.replace(path);
        },
        error: function (errObject) {
            alert(errObject.responseText);
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
            success: function(message) {
                $(".upload-result")[0].style.color = "limegreen";
                $(".upload-result")[0].innerText = "Upload the file successfully!";
                $("#file-input")[0].value = "";
                $(".file-path").empty();
                $(".upload-button")[0].disabled = true;
                alert(message);
            },
            error: function (message){
                $(".upload-result")[0].style.color = "red";
                $(".upload-result")[0].innerText = "Failed upload file";
                $("#file-input")[0].value = "";
                $(".file-path").empty();
                $(".upload-button")[0].disabled = true;
                alert(message.responseText);
            }
        });
        return false;
    });
}