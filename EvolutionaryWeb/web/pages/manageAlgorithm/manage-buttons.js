const START_PROBLEM_URL = buildUrlWithContextPath("startProblem");
const PAUSE_PROBLEM_URL = buildUrlWithContextPath("pauseProblem");
const RESUME_PROBLEM_URL = buildUrlWithContextPath("resumeProblem");
const STOP_PROBLEM_URL = buildUrlWithContextPath("stopProblem");

function runningProcessInfo(){

}

function startAlgorithm(){
    $.ajax({
       url: START_PROBLEM_URL,
       timeout: TIMEOUT,
       method: "POST",
       data: null,
       success: function(){
           alert("Algorithm start running!");
       },
        error: function (errObj){
           alert(errObj.responseText);
        }
    });
}

function pauseAlgorithm(){
    $.ajax({
        url: PAUSE_PROBLEM_URL,
        timeout: TIMEOUT,
        method: "POST",
        data: null,
        success: function(){
            alert("Algorithm paused!");
        },
        error: function (errObj){
            alert(errObj.responseText);
        }
    });
}

function resumeAlgorithm(){
    $.ajax({
        url: RESUME_PROBLEM_URL,
        timeout: TIMEOUT,
        method: "POST",
        data: null,
        success: function(){
            alert("Algorithm resumed!");
        },
        error: function (errObj){
            alert(errObj.responseText);
        }
    });
}

function stopAlgorithm(){
    $.ajax({
        url: STOP_PROBLEM_URL,
        timeout: TIMEOUT,
        method: "POST",
        data: null,
        success: function(){
            alert("Algorithm stopped!");
        },
        error: function (errObj){
            alert(errObj.responseText);
        }
    });
}
