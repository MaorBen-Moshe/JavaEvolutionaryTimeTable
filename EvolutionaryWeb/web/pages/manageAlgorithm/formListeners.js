function setInfoFormListener() {

}

function setSelectionListener(){
    const selectElement = document.querySelector("#selection");
    var div = document.getElementById("selection-input");
    div.style.visibility = 'hidden';
    $("#selection-input-tag-label").empty();
    selectElement.addEventListener('change', (event) => {
        const value = event.target.value;
        if(value === 'RouletteWheel') {
            var div = document.getElementById("selection-input");
            div.style.visibility = 'hidden';
            $("#selection-input-tag-label").empty();
        }else if(value === 'Tournament'){
            var div = document.getElementById("selection-input");
            div.style.visibility = 'visible';
            $("#selection-input-tag-label").empty().append("PTE:");
        }else if(value === 'Truncation'){
            var div = document.getElementById("selection-input");
            div.style.visibility = 'visible';
            $("#selection-input-tag-label").empty().append("Top percent:");
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

$(function (){
    setInfoFormListener();
    setSelectionListener();
    setCrossoverListener();
})