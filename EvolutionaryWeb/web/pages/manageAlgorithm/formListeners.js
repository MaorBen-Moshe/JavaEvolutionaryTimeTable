function setInfoFormListener() {

}

function setSelectionListener(){
    const selectElement = document.querySelector("#selection");
    var div = document.getElementById("selection-input");
    div.style.visibility = 'hidden';
    $("#selection-input-tag-label").empty();
    selectElement.addEventListener('change', (event) => {
        var input = document.getElementById("selection-input-tag");
        const value = event.target.value;
        if(value === 'RouletteWheel') {
            var div = document.getElementById("selection-input");
            div.style.visibility = 'hidden';
            $("#selection-input-tag-label").empty();
        }else if(value === 'Tournament'){
            var div = document.getElementById("selection-input");
            div.style.visibility = 'visible';
            $("#selection-input-tag-label").empty().append("PTE:");
            input.min = "0";
            input.max = "1";
            input.step = "0.01";
        }else if(value === 'Truncation'){
            var div = document.getElementById("selection-input");
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
        if(value === 'Aspect') {
            var div = document.getElementById("orientation");
            div.style.visibility = 'visible';
        }else if(value === 'DayTime'){
            var div = document.getElementById("orientation");
            div.style.visibility = 'hidden';
        }
    });
}