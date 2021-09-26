const START_PROBLEM_URL = buildUrlWithContextPath("startProblem");

function startAlgorithm(){

}

function pauseAlgorithm(){

}

function resumeAlgorithm(){

}

function stopAlgorithm(){

}

var x = 1;
function addMutationCell(){
    var div = document.createElement("div");
    div.id = "mut" + x;
    $("#mut-list").appendChild(div);
    var titleLabel = document.createElement("label");
    titleLabel.innerHTML = "mutation:";
    $("#mut" + x).appendChild(titleLabel);
    var select = document.createElement("select");
    var flipping = document.createElement("option");
    flipping.value = "Flipping";
    flipping.text = "Flipping";
    var sizer = document.createElement("option");
    sizer.value = "Sizer";
    sizer.text = "Sizer";
    select.appendChild(flipping);
    select.appendChild(sizer);
    $("#mut" + x).appendChild(select);
    var probLabel = document.createElement("label");
    probLabel.innerHTML = "probability:";
    $("#mut" + x).appendChild(probLabel);
    var probInput = document.createElement("input");
    probInput.className = "prob";
    probInput.type = "number";
    probInput.name = "probability"
    probInput.min = '0';
    probInput.max = '1';
    $("#mut" + x).appendChild(probInput);
    var tupplesLabel = document.createElement("label");
    tupplesLabel.innerHTML = "Tupples";
    $("#mut" + x).appendChild(tupplesLabel);
    var tupplesInput = document.createElement("input");
    tupplesInput.className = "tupples";
    tupplesInput.name = "tupples";
    tupplesInput.min = '0';
    tupplesInput.type = 'number';
    $("#mut" + x).appendChild(tupplesInput);

    var componentLabel = document.createElement("label");
    componentLabel.innerHTML = "Component";
    $("#mut" + x).appendChild(componentLabel);

    var componentSelect = document.createElement("select");
    var S = document.createElement("option");
    sizer.value = "S";
    sizer.text = "S";
    var T = document.createElement("option");
    sizer.value = "T";
    sizer.text = "T";
    var C = document.createElement("option");
    sizer.value = "C";
    sizer.text = "C";
    var H = document.createElement("option");
    sizer.value = "H";
    sizer.text = "H";
    var D = document.createElement("option");
    sizer.value = "D";
    sizer.text = "D";
    componentSelect.appendChild(S);
    componentSelect.appendChild(T);
    componentSelect.appendChild(C);
    componentSelect.appendChild(H);
    componentSelect.appendChild(D);
    componentSelect.name = "component";
    $("#mut" + x).appendChild(componentSelect);

    x = x + 1;
}
