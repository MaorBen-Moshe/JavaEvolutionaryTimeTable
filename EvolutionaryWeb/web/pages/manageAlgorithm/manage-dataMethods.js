const TIMEOUT = 2000;
const RULES_URL = buildUrlWithContextPath("");
const TEACHERS_URL = buildUrlWithContextPath("");
const CLASSES_URL = buildUrlWithContextPath("");
const SUBJECTS_URL = buildUrlWithContextPath("");

function ajaxRulesList(){

}

function ajaxTeachersList(){

}

function ajaxClassesList(){

}

function ajaxSubjectsList(){

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


function getProblemData(){
    ajaxRulesList();
    ajaxTeachersList();
    ajaxClassesList();
    ajaxSubjectsList();
}