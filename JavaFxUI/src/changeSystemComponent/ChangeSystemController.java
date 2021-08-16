package changeSystemComponent;

import commands.EngineWrapper;
import crossover.CrossoverTypes;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.TimeTable;
import models.TimeTableSystemDataSupplier;
import selection.SelectionTypes;

public class ChangeSystemController {
    private final EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper;
    @FXML
    private VBox vBox;

    @FXML
    private Label mainTitle;

    public ChangeSystemController(EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper){
        this.wrapper = wrapper;
    }

    public void setView(){
        ComboBox<SelectionTypes> selections = new ComboBox<>();
        selections.getItems().addAll(SelectionTypes.values());
        ComboBox<CrossoverTypes> crossovers = new ComboBox<>();
        crossovers.getItems().addAll(CrossoverTypes.values());

    }
}