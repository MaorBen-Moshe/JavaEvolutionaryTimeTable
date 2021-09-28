
const GENERIC_INFO_URL = buildUrlWithContextPath("genericInfo");
const RULES_URL = buildUrlWithContextPath("rules");
const TEACHERS_URL = buildUrlWithContextPath("teachers");
const CLASSES_URL = buildUrlWithContextPath("classes");
const SUBJECTS_URL = buildUrlWithContextPath("subjects");

function ajaxRulesList(){
    $.ajax({
        url: RULES_URL,
        timeout: TIMEOUT,
        success: function(rulesObj) {
            setRulesTable(rulesObj);
        }
    });
}

function setRulesTable(rulesObj){
    var tableBody = $(".rules-table-body")[0];
    var rules_List = rulesObj.rules;

    tableBody.innerHTML = "";
    $.each(rules_List || [], function(index, rule) {
        var trRow = document.createElement("tr");

        var tdName = document.createElement("td");
        var tdStrength = document.createElement("td");
        var tdConfigs = document.createElement("td");

        tdName.innerText = rule.type;
        tdStrength.innerText = rule.strength;
        tdConfigs.appendChild(getConfigurationsOfRules(rule.configurations));

        trRow.appendChild(tdName);
        trRow.appendChild(tdStrength);
        trRow.appendChild(tdConfigs);

        tableBody.appendChild(trRow);
    });
}

function getConfigurationsOfRules(configurationsObj){
    var hasConfigs = false;
    var sectionInfo = $(document.createElement("section")).addClass("grid")[0];
    for(var key in configurationsObj){
        hasConfigs = true;
        var configLabel = $(document.createElement("label")).text("Config: " + key + " = " + configurationsObj[key])[0];
        sectionInfo.appendChild(configLabel);
    }

    if(hasConfigs === false){
        sectionInfo.appendChild($(document.createElement("label")).text("No Configurations")[0]);
    }

    return sectionInfo;
}

function ajaxTeachersList(){
    $.ajax({
        url: TEACHERS_URL,
        timeout: TIMEOUT,
        success: function(teachers) {
            setTeachersTable(teachers);
        }
    });
}

function setTeachersTable(teachers){
    var tableBody = $(".teachers-table-body")[0];

    tableBody.innerHTML = "";
    $.each(teachers || [], function(index, teacher) {
        var trRow = document.createElement("tr");

        var tdID = document.createElement("td");
        var tdName = document.createElement("td");
        var tdHoursPref = document.createElement("td");
        var tdSubjects = document.createElement("td");

        tdID.innerText = teacher.id;
        tdName.innerText = teacher.name;
        tdHoursPref.innerText = teacher.workingHoursPref;
        tdSubjects.appendChild(createSectionSubjectsInfo(teacher.subjects));

        trRow.appendChild(tdID);
        trRow.appendChild(tdName);
        trRow.appendChild(tdHoursPref);
        trRow.appendChild(tdSubjects);

        tableBody.appendChild(trRow);
    });
}

function createSectionSubjectsInfo(subjects) {
    var sectionInfo = $(document.createElement("section")).addClass("grid")[0];

    $.each(subjects || [], function (index, subject){
       var subjectLabel = $(document.createElement("label")).text("Subject: id=" + subject.id + ", name="+ subject.name + ".")[0];
        sectionInfo.appendChild(subjectLabel);
    });

    return sectionInfo;
}

function ajaxClassesList(){
    $.ajax({
        url: CLASSES_URL,
        timeout: TIMEOUT,
        success: function(classes) {
            setClassesTable(classes);
        }
    });
}

function setClassesTable(classes){
    var tableBody = $(".klass-table-body")[0];

    tableBody.innerHTML = "";
    $.each(classes || [], function(index, klass) {
        var trRow = document.createElement("tr");

        var tdID = document.createElement("td");
        var tdName = document.createElement("td");
        var tdTotalHours = document.createElement("td");
        var tdSubjects = document.createElement("td");

        tdID.innerText = klass.id;
        tdName.innerText = klass.name;
        tdTotalHours.innerText = klass.totalNumberOfHours;
        tdSubjects.appendChild(getSubjectsFromObject(klass.subjectsNeeded));

        trRow.appendChild(tdID);
        trRow.appendChild(tdName);
        trRow.appendChild(tdTotalHours);
        trRow.appendChild(tdSubjects);

        tableBody.appendChild(trRow);
    });
}

function getSubjectsFromObject(subjectObj){
    var sectionInfo = $(document.createElement("section")).addClass("grid")[0];
    for(var key in subjectObj){
        var subjectLabel = $(document.createElement("label")).text("Subject: " + key + " hours= " + subjectObj[key])[0];
        sectionInfo.appendChild(subjectLabel);
    }

    return sectionInfo;
}

function ajaxSubjectsList(){
    $.ajax({
        url: SUBJECTS_URL,
        timeout: TIMEOUT,
        success: function(subjects) {
            setSubjectsTable(subjects);
        }
    });
}

function setSubjectsTable(subjects){
    var tableBody = $(".subjects-table-body")[0];

    tableBody.innerHTML = "";
    $.each(subjects || [], function(index, subject) {
        var trRow = document.createElement("tr");

        var tdID = document.createElement("td");
        var tdName = document.createElement("td");

        tdName.innerText = subject.name;
        tdID.innerText = subject.id;

        trRow.appendChild(tdID);
        trRow.appendChild(tdName);

        tableBody.appendChild(trRow);
    });
}

function ajaxGenericProblemInfo(){
    $.ajax({
        url: GENERIC_INFO_URL,
        timeout: TIMEOUT,
        success: function(generic) {
            setGenericInfoTable(generic);
        }
    });
}

function setGenericInfoTable(generic){
    var tableBody = $(".generic-table-body")[0];

    tableBody.innerHTML = "";
    for(var key in generic){
        var trRow = document.createElement("tr");

        var tdKey = document.createElement("td");
        var tdVal = document.createElement("td");

        tdKey.innerText = key;
        tdVal.innerText = generic[key];

        trRow.appendChild(tdKey);
        trRow.appendChild(tdVal);

        tableBody.appendChild(trRow);
    }
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
    ajaxGenericProblemInfo();
    ajaxRulesList();
    ajaxTeachersList();
    ajaxClassesList();
    ajaxSubjectsList();
}