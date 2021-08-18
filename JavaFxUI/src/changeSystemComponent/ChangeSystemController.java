package changeSystemComponent;

import commands.EngineWrapper;
import crossover.CrossoverTypes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.TimeTable;
import models.TimeTableSystemDataSupplier;
import mutation.Mutation;
import selection.SelectionTypes;
import utils.AlertUtils;

import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ChangeSystemController {
    private final EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper;
    @FXML
    private VBox vBox;

    @FXML
    private Label mainTitle;

    private TextField elita;
    private List<VBox> mutations;

    public ChangeSystemController(EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper){
        this.wrapper = wrapper;
    }

    public void setView(){
        elita = new TextField();
        elita.setPromptText("elitism");
        elita.setText(String.valueOf(wrapper.getEngine().getElitism()));
        ComboBox<SelectionTypes> selections = new ComboBox<>();
        selections.setPromptText("Selections");
        selections.getItems().addAll(SelectionTypes.values());
        selections.setValue(wrapper.getEngine().getSelection().getType());
        ComboBox<CrossoverTypes> crossovers = new ComboBox<>();
        crossovers.getItems().addAll(CrossoverTypes.values());
        crossovers.setPromptText("Crossovers");
        crossovers.setValue(wrapper.getEngine().getCrossover().getType());
        mutations = new ArrayList<>();
        List<Mutation<TimeTable, TimeTableSystemDataSupplier>> mutationInEngine = wrapper.getEngine().getMutations();
        mutationInEngine.forEach(mutation ->{
            VBox currentV = new VBox();
            Label label = new Label(mutation.getType().toString());
            TextField current = new TextField();
            current.setPromptText("probability");
            current.setText(String.valueOf(mutation.getProbability()));
            currentV.getChildren().add(label);
            currentV.getChildren().add(current);
            mutations.add(currentV);
        });

        vBox.getChildren().add(elita);
        vBox.getChildren().add(selections);
        vBox.getChildren().add(crossovers);
        vBox.getChildren().addAll(mutations);
        Button submit = new Button("submit");
        submit.setOnAction(this::submitListener);
        vBox.getChildren().add(submit);
    }

    private void setElitism(){
        try{
            int elitaNum = Integer.parseInt(elita.getText());
            wrapper.getEngine().setElitism(elitaNum);
        }catch (NumberFormatException e){
            throw new IllegalArgumentException("elitism should be a number only");
        }
    }

    private void setSelection(){

    }

    private void setCrossover(){

    }

    private void setMutations(){
        List<Mutation<TimeTable, TimeTableSystemDataSupplier>> mutationInEngine = wrapper.getEngine().getMutations();
        IntStream.range(0, mutationInEngine.size()).forEach(i -> {
            try{
                TextField currentTextField = (TextField) mutations.get(i).getChildren().get(1);
                double currentProb = Double.parseDouble(currentTextField.getText());
                mutationInEngine.get(i).setProbability(currentProb);
            }catch (NumberFormatException e){
                throw new IllegalArgumentException("probability for " + mutationInEngine.get(i).getType() + " should be a float between 0 - 1");
            }
        });
    }

    private void submitListener(ActionEvent actionEvent){
        try{
            setElitism();
            setCrossover();
            setSelection();
            setMutations();
        }catch (Exception e){
            AlertUtils.displayAlert(Alert.AlertType.ERROR, "Failed", e.getMessage());
        }
    }
}