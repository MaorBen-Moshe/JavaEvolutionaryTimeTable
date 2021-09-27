const START_PROBLEM_URL = buildUrlWithContextPath("startProblem");
const SET_ENGINE_INFO_URL = buildUrlWithContextPath("setEngineInfo");

function startAlgorithm(){

}

function pauseAlgorithm(){

}

function resumeAlgorithm(){

}

function stopAlgorithm(){

}

var x = 0;
function addMutationCell(){
    x = x + 1;
    var mut_list = document.getElementById("mut-list");
    if(mut_list === null){
        mut_list = document.createElement("div");
        mut_list.id = "mut-list";
        $("#mut-section").append(mut_list);
    }

    var idPrefix = "mut" + x + "-";
    var div = document.createElement("div");
    div.id = "mut" + x;
    mut_list.appendChild(div);
    var titleLabel = document.createElement("label");
    titleLabel.innerHTML = "mutation:";
    div.appendChild(titleLabel);
    var select = document.createElement("select");
    select.id = idPrefix + "type";
    var flipping = document.createElement("option");
    flipping.value = "Flipping";
    flipping.text = "Flipping";
    var sizer = document.createElement("option");
    sizer.value = "Sizer";
    sizer.text = "Sizer";
    select.appendChild(flipping);
    select.appendChild(sizer);
    div.appendChild(select);
    var probLabel = document.createElement("label");
    probLabel.innerHTML = "probability:";
    div.appendChild(probLabel);
    var probInput = document.createElement("input");
    probInput.id = idPrefix + "probability";
    probInput.className = "prob";
    probInput.type = "number";
    probInput.name = "probability";
    probInput.min = "0";
    probInput.max = "1";
    probInput.step = "0.01";
    div.appendChild(probInput);
    var tupplesLabel = document.createElement("label");
    tupplesLabel.innerHTML = "Tupples:";
    div.appendChild(tupplesLabel);
    var tupplesInput = document.createElement("input");
    tupplesInput.id = idPrefix + "tupples";
    tupplesInput.className = "tupples";
    tupplesInput.name = "tupples";
    tupplesInput.type = 'number';
    div.appendChild(tupplesInput);

    var componentLabel = document.createElement("label");
    componentLabel.innerHTML = "Component:";
    div.appendChild(componentLabel);

    var componentSelect = document.createElement("select");
    componentSelect.id = idPrefix + "component";
    var S = document.createElement("option");
    S.value = "S";
    S.text = "S";
    var T = document.createElement("option");
    T.value = "T";
    T.text = "T";
    var C = document.createElement("option");
    C.value = "C";
    C.text = "C";
    var H = document.createElement("option");
    H.value = "H";
    H.text = "H";
    var D = document.createElement("option");
    D.value = "D";
    D.text = "D";
    componentSelect.appendChild(S);
    componentSelect.appendChild(T);
    componentSelect.appendChild(C);
    componentSelect.appendChild(H);
    componentSelect.appendChild(D);
    componentSelect.name = "component";
    div.appendChild(componentSelect);
}

function ajaxOnSendInfo(){
    $("#info-form").submit(function (){
       const json = JSON.stringify(createObj());
       $.ajax({
           data: {'info': json},
           method: this.method,
           type: "POST",
           url: SET_ENGINE_INFO_URL,
           timeout: 2000,
           error: function (errObj){
               alert(errObj.responseText);
           },success: function (){
               $(".header-label").css("color", "green");
               alert("Info sent successfully. You can now run the problem");
           }
       });

       return false;
    });
}

function createObj(){
    var ret = {};

    ret.population = $("#population").val();
    ret.elitism = $("#elita").val();
    ret.selectionType = $("#selection").val();
    ret.selectionInput = $("#selection-input-tag").val();
    ret.crossoverType = $("#crossover").val();
    ret.crossoverCutting = $("#cutting").val();
    ret.crossoverAspect = $("#aspect").val();
    ret.mutations = [];
    // mutation created
    for(var i = 1; i <= x; i++){
        var mutPrefix = "#mut" + i + "-";
        var mut = {};
        mut.type = $(mutPrefix + "type").val();
        mut.probability = $(mutPrefix + "probability").val();
        mut.tupples = $(mutPrefix + "tupples").val();
        mut.component = $(mutPrefix + "component").val();
        ret.mutations.push(mut);
    }

    ret.jumps = $("#jumps").val();
    ret.gensChecked = $("#gens-check").is(":checked");
    ret.gensInput = $("#gens-input").val();
    ret.fitnessChecked = $("#fit-check").is(":checked");;
    ret.fitnessInput = $("#fit-input").val();
    ret.timeChecked = $("#time-check").is(":checked");
    ret.timeInput = $("#time-input").val();

    return ret;
}
