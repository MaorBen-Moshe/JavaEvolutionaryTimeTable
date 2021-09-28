const refreshRate = 2000;
const USERS_RUN_PROBLEM_LIST_URL = buildUrlWithContextPath("usersRunProblemList");
const USER_IN_PROBLEM_URL = buildUrlWithContextPath("inProblemConfigsServlet");
const PROCESS_INFO_URL = buildUrlWithContextPath("processInfo");

$(function () {
    ajaxUserInProblem();
    ajaxUsersList();
    getProblemData();
    callListeners(); // include form engine info listener
    setInterval(ajaxUsersList, refreshRate);
    setInterval(ajaxProcessingInfo, refreshRate);
})

function ajaxProcessingInfo(){
    $.ajax({
        url: PROCESS_INFO_URL,
        timeout: TIMEOUT,
        success: handleProcessInfo,
        error: function (errObj){
            alert(errObj.responseText);
        }
    });
}

function handleProcessInfo(processInfoObj){
    $("#startButton").attr("disabled", processInfoObj.isRunning || processInfoObj.isPaused);
    $("#pauseButton").attr("disabled", !processInfoObj.isRunning);
    $("#resumeButton").attr("disabled", !processInfoObj.isPaused);
    $("#stopButton").attr("disabled", !processInfoObj.isRunning);

    $("#generationRunning").css("visibility", processInfoObj.isRunning ? "visible" : "hidden");
    $("#fitnessRunning").css("visibility", processInfoObj.isRunning ? "visible" : "hidden");

    $("#generationRunning").empty().append("Generations: " + processInfoObj.userGenerations);
    $("#fitnessRunning").empty().append("Fitness: " + processInfoObj.userFitness);
}

function ajaxUserInProblem(){
    $.ajax({
        url: USER_IN_PROBLEM_URL,
        timeout: TIMEOUT,
        error: function (errObject){
            alert(errObject.responseText);
        },
        success: function (response){
            if(response.userInProblem){
                addPropertiesInfo(response.properties);
                $(".header-label").css("color", "green");
            }else{
                $(".header-label").css("color", "red");
            }
        }
    })
}

function ajaxUsersList() {
    $.ajax({
        url: USERS_RUN_PROBLEM_LIST_URL,
        timeout: TIMEOUT,
        success: function(users) {
            refreshUsersList(users);
        }
    });
}

function refreshUsersList(users) {
    console.log("pulled new user list: " + users);
    var tableBody = $(".users-table-body")[0];

    tableBody.innerHTML = "";
    $.each(users || [], function(index, user) {
        var trRow = document.createElement("tr");

        var tdName = document.createElement("td");
        var tdGenerations = document.createElement("td");
        var tdFitness = document.createElement("td");

        tdName.innerText = user.name;
        tdGenerations.innerText = user.generations;
        tdFitness.innerText = user.fitness;

        trRow.appendChild(tdName);
        trRow.appendChild(tdGenerations);
        trRow.appendChild(tdFitness);

        tableBody.appendChild(trRow);
    });
}

function goBack() {
    window.location.replace("../timeTableProblem/timeTableProblem.html");
}
