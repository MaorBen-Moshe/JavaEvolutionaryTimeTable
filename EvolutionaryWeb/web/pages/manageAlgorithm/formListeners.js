const SET_ENGINE_INFO_URL = buildUrlWithContextPath("setEngineInfo");
const TABLE_DISPLAY_URL = buildUrlWithContextPath("tableDisplay");
const TIMEOUT = 2000;

function ajaxOnSendInfo(){
    $("#info-form").submit(function (){
        const json = JSON.stringify(createObj());
        $.ajax({
            data: {'info': json},
            method: this.method,
            type: "POST",
            url: SET_ENGINE_INFO_URL,
            timeout: TIMEOUT,
            error: function (errObj){
                alert(errObj.responseText);
            },success: function (properties){
                $(".header-label").css("color", "green");
                alert("Info sent successfully. You can now run the problem");
                addPropertiesInfo(properties);
            }
        });

        return false;
    });
}

function ajaxOnDisplayProblem(){
    $("#display-problem-form").submit(function () {
       const orientation =  $("#orientation-display").val();
       // the format is: 'id=?, name=?' and we want the id value
       const object_chosen = $("#object-display").val().split(",")[0].trim().split("=")[1].trim();
        $.ajax({
           data: {
               orientation: orientation,
               id: object_chosen
           },
            url: TABLE_DISPLAY_URL,
            method: this.method,
            timeout: TIMEOUT,
            error: function(err){
               alert(err.responseText);
            },
            success: function(data){
               displayTable(data);
            }
        });

        return false;
    });
}

function displayTable(data){
    setRulesScore(data.rules);
    setGenericSolutionInfo(data);
    console.log(data.tableInfo);
    setTable(data.tableInfo);
}

function setTable(tableInfo){
    var tableHead = $(".problem-table-head")[0];
    var tableBody = $(".problem-table-body")[0];
}

function setGenericSolutionInfo(data){
    var tableBody = $(".generic-problem-table-body")[0];

    tableBody.innerHTML = "";

    tableBody.appendChild(setGenericHelper("Soft Rules Average", data.softRulesAvg));
    tableBody.appendChild(setGenericHelper("Hard Rules Average", data.hardRulesAvg));
    tableBody.appendChild(setGenericHelper("Generation Created", data.generationCreated));
    tableBody.appendChild(setGenericHelper("Fitness Score", data.fitnessScore));
}

function setGenericHelper(name, value){
    var trRow = document.createElement("tr");
    var tdName = document.createElement("td");
    var tdValue = document.createElement("td");

    tdName.innerText = name;
    tdValue.innerText = value;

    trRow.appendChild(tdName);
    trRow.appendChild(tdValue);
    return trRow;
}

function setRulesScore(rules){
    var tableBody = $(".rules-problem-table-body")[0];

    tableBody.innerHTML = "";
    $.each(rules || [], function(index, rule) {
        var trRow = document.createElement("tr");

        var tdName = document.createElement("td");
        var tdStrength = document.createElement("td");
        var tdScore = document.createElement("td");

        tdName.innerText = rule.name;
        tdStrength.innerText = rule.strength;
        tdScore.innerText = rule.score;

        trRow.appendChild(tdName);
        trRow.appendChild(tdStrength);
        trRow.appendChild(tdScore);

        tableBody.appendChild(trRow);
    });
}

function addPropertiesInfo(properties){
    var tableBody = $(".engine-table-body")[0];

    tableBody.innerHTML = "";
    $.each(properties || [], function(index, property) {
        var trRow = document.createElement("tr");

        var tdName = document.createElement("td");
        var tdValue = document.createElement("td");

        tdName.innerText = property.name;
        tdValue.innerText = property.value;

        trRow.appendChild(tdName);
        trRow.appendChild(tdValue);

        tableBody.appendChild(trRow);
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

function setSelectionListener(){
    const selectElement = document.querySelector("#selection");
    var div = document.getElementById("selection-input");
    div.style.visibility = 'hidden';
    $("#selection-input-tag-label").empty();
    selectElement.addEventListener('change', (event) => {
        var input = document.getElementById("selection-input-tag");
        var div = document.getElementById("selection-input");
        const value = event.target.value;
        if(value === 'RouletteWheel') {
            div.style.visibility = 'hidden';
            $("#selection-input-tag-label").empty();
        }else if(value === 'Tournament'){
            div.style.visibility = 'visible';
            $("#selection-input-tag-label").empty().append("PTE:");
            input.min = "0";
            input.max = "1";
            input.step = "0.01";
        }else if(value === 'Truncation'){
            div.style.visibility = 'visible';
            $("#selection-input-tag-label").empty().append("Top percent:");
            input.min = "1";
            input.max = "100";
            input.step = "1";
        }
    });
}

function setCrossoverListener(){
    const crossElement = document.querySelector("#crossover");
    var div = document.getElementById("orientation");
    div.style.visibility = 'hidden';
    crossElement.addEventListener('change', (event) => {
        const value = event.target.value;
        var div = document.getElementById("orientation");
        if(value === 'Aspect') {
            div.style.visibility = 'visible';
        }else if(value === 'DayTime'){
            div.style.visibility = 'hidden';
        }
    });
}

function setMutationListener(event){
    var type = $("#" + event.target.id).val();
    var mutIdPrefix = event.target.id.split("-")[0] + "-";
    var isFlipping = type === "Flipping";

    $("#" + mutIdPrefix + "component-label").css("visibility", (isFlipping ? "visible" : "hidden"));
    $("#" + mutIdPrefix + "component").css("visibility", (isFlipping ? "visible" : "hidden"));
    $("#" + mutIdPrefix + "tupplesLabel").empty().append((isFlipping ? "Max Tupples: (Positive only)" : "Total Tupples:"));
}

function callListeners(){
    document.getElementById('mut-add').onclick = addMutationCell;
    setCrossoverListener();
    setSelectionListener();
    ajaxOnSendInfo();
    ajaxOnDisplayProblem();
}