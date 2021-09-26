const refreshRate = 2000;
const USERS_RUN_PROBLEM_LIST_URL = buildUrlWithContextPath("usersRunProblemList");
const GET_PROBLEM_URL = buildUrlWithContextPath("getProblem");
var id;

$(function () {
    ajaxGetProblemInfo();
    ajaxUsersList();
    //ajaxProblemList();
    setInterval(ajaxUsersList, refreshRate);
    document.getElementById('mut-add').onclick = function (event){
        addMutationCell();
    };

    setInfoFormListener();
    setSelectionListener();
    setCrossoverListener();
})


function ajaxGetProblemInfo(){
    $.ajax({
        url: GET_PROBLEM_URL,
        timeout: 2000,
        error: function (errObject){
            alert(errObject.responseText);
        },
        success: problemInfoSuccess
    })
}

function problemInfoSuccess(probJson){

}

function ajaxUsersList() {
    $.ajax({
        url: USERS_RUN_PROBLEM_LIST_URL,
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
        tdUsers.innerText = problem.usersSolveProblem.length;
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

function goBack() {
    window.location.replace("../timeTableProblem/timeTableProblem.html");
}

function createProblemDialog(event) {
    // TODO: display a nice dialog if the user want to solve this problem or not.

    var id = this.children[0].innerText;

    $.ajax({
        data: id,
        url: PROBLEMS_URL,
        timeout: 2000,
        success: function (path) {
            window.location.replace = path;
        }
    });
}
