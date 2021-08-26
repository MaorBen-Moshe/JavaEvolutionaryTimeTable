package changeSystemComponent;

import commands.EngineWrapper;
import crossover.AspectOrientedCrossover;
import crossover.CrossoverTypes;
import crossover.DayTimeOrientedCrossover;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.TimeTable;
import models.TimeTableSystemDataSupplier;
import mutation.Mutation;
import selection.RouletteWheelSelection;
import selection.SelectionTypes;
import selection.TournamentSelection;
import selection.TruncationSelection;
import utils.AlertUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class ChangeSystemController {
    private EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper;
    @FXML
    private VBox vBox;
    private Label elitaLabel;
    private ComboBox<SelectionTypes> selections;
    private ComboBox<CrossoverTypes> crossovers;
    private Label selectionLabel;
    private TextField selectionTextField;
    private TextField cuttingTextField;
    private ComboBox<AspectOrientedCrossover.Orientation> orientationBox;
    private TextField elita;
    private List<VBox> mutations;
    private Button submit;
    private Label selectionTitle;
    private Label mutationTitle;
    private Label crossoverTitle;
    private Consumer<?> after;
    private final Pattern floatPattern;
    private final Pattern numberPattern;

    public ChangeSystemController(){
        floatPattern = Pattern.compile("[0](\\.[0-9]*)?|[1]?");
        numberPattern = Pattern.compile("[0-9]*");
    }

    public void setWrapper(EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper){
        this.wrapper = wrapper;
    }

    public void setAfterSubmit(Consumer<?> after){
        this.after = after;
    }

    public void initialize(){
        initial();
    }

    public void setView(){
        elitaLabel.setText("Elitism" + " (0-" + wrapper.getEngine().getInitialPopulationSize() + ").");
        elita.setText(String.valueOf(wrapper.getEngine().getElitism()));
        selections.setValue(wrapper.getEngine().getSelection().getType());
        crossovers.setValue(wrapper.getEngine().getCrossover().getType());
        if(mutations == null || mutations.size() == 0){
            mutations = new ArrayList<>();
            List<Mutation<TimeTable, TimeTableSystemDataSupplier>> mutationInEngine = wrapper.getEngine().getMutations();
            mutationInEngine.forEach(mutation ->{
                VBox currentV = new VBox();
                currentV.setAlignment(Pos.CENTER);
                currentV.getStyleClass().add("vBox");
                Label label = new Label(mutation.getType().toString() + " probability (0-1)");
                TextField current = new TextField();
                current.setPromptText("probability");
                current.setText(String.valueOf(mutation.getProbability()));
                current.textProperty().addListener((item, old, newVal) -> {
                    if(newVal.trim().isEmpty()){
                        return;
                    }

                    if(!floatPattern.matcher(newVal).matches()){
                        current.setText(old);
                    }
                });
                currentV.getChildren().add(label);
                currentV.getChildren().add(current);
                VBox.setMargin(current, new Insets(5, 10,10,10));
                mutations.add(currentV);
            });

            vBox.getChildren().add(vBox.getChildren().indexOf(submit), mutationTitle);
            vBox.getChildren().addAll(vBox.getChildren().indexOf(mutationTitle) + 1, mutations);
        }else{
            List<Mutation<TimeTable, TimeTableSystemDataSupplier>> mutationInEngine = wrapper.getEngine().getMutations();
            IntStream.range(0, mutationInEngine.size()).forEach(i -> {
                TextField current = (TextField) mutations.get(i).getChildren().get(1);
                current.setText(String.valueOf(mutationInEngine.get(i).getProbability()));
            });
        }
    }

    private void initial(){
        elitaLabel = new Label();
        elita = new TextField();
        VBox.setMargin(elita, new Insets(5, 10,10,10));
        elita.setPromptText("elitism");
        selections = new ComboBox<>();
        selections.setPromptText("Selections");
        selections.getItems().addAll(SelectionTypes.values());
        selections.getSelectionModel().selectedItemProperty().addListener((options, oldVal, newVal) -> showSelection(newVal));
        selectionLabel = new Label();
        selectionTextField = new TextField();
        VBox.setMargin(selectionTextField, new Insets(5, 10,10,10));
        crossovers = new ComboBox<>();
        crossovers.getSelectionModel().selectedItemProperty().addListener((options, oldVal, newVal) -> showCrossover(newVal));
        crossovers.getItems().addAll(CrossoverTypes.values());
        crossovers.setPromptText("Crossovers");
        Label cuttingLabel = new Label("cutting Points: (positive number)");
        cuttingLabel.setVisible(true);
        cuttingTextField = new TextField();
        VBox.setMargin(cuttingTextField, new Insets(5, 10,10,10));
        cuttingTextField.setVisible(true);
        orientationBox = new ComboBox<>();
        orientationBox.getItems().addAll(AspectOrientedCrossover.Orientation.values());
        orientationBox.setVisible(false);
        orientationBox.setPromptText("Orientation");

        mutationTitle = new Label("Mutations");
        mutationTitle.getStyleClass().add("sub");
        selectionTitle = new Label("Selection");
        selectionTitle.getStyleClass().add("sub");
        crossoverTitle = new Label("Crossover");
        crossoverTitle.getStyleClass().add("sub");

        vBox.getChildren().add(elitaLabel);
        vBox.getChildren().add(elita);
        vBox.getChildren().add(selectionTitle);
        vBox.getChildren().add(selections);
        vBox.getChildren().addAll(selectionLabel, selectionTextField);
        vBox.getChildren().add(crossoverTitle);
        vBox.getChildren().add(crossovers);
        vBox.getChildren().addAll(cuttingLabel, cuttingTextField, orientationBox);
        submit = new Button("submit");
        submit.setOnAction(this::submitListener);
        vBox.getChildren().add(submit);
        vBox.setAlignment(Pos.CENTER);
        vBox.getStyleClass().add("vBox");

        textFieldsListeners();
    }

    private void textFieldsListeners(){
        elita.textProperty().addListener((item, old, newVal) ->{
            try{
                if(newVal.trim().isEmpty()) return;
                int elita = Integer.parseInt(newVal);
                if(elita < 0 || elita > wrapper.getEngine().getInitialPopulationSize()){
                    this.elita.setText(old);
                }
            }catch (NumberFormatException e){
                this.elita.setText(old);
            }
        });

        cuttingTextField.textProperty().addListener((item, old, newVal) ->{
            if(numberPattern.matcher(newVal).matches()) return;
            this.cuttingTextField.setText(old);
        });
    }

    private void showSelection(SelectionTypes chosen){
        String valText;
        switch (chosen){
            case Truncation:
                valText = null;
                if(wrapper.getEngine().getSelection().getType().equals(chosen)){
                    valText = String.valueOf(((TruncationSelection)wrapper.getEngine().getSelection()).getTopPercent());
                }

                showSelectionHelper("Top percent: (0-100)", valText);
                break;
            case Tournament:
                valText = null;
                if(wrapper.getEngine().getSelection().getType().equals(chosen)){
                    valText = String.valueOf(((TournamentSelection)wrapper.getEngine().getSelection()).getPte());
                }

                showSelectionHelper("PTE: (0-1)", valText);
                break;
            default:
                selectionTextField.setVisible(false);
                selectionLabel.setVisible(false);
                break;
        }
    }

    private void showSelectionHelper(String labelText, String valueText){
        selectionLabel.setText(labelText);
        selectionLabel.setVisible(true);
        if(valueText != null){
            selectionTextField.setText(valueText);
        }

        selectionTextField.setVisible(true);
    }

    private void showCrossover(CrossoverTypes chosen){
        orientationBox.setVisible(chosen.equals(CrossoverTypes.AspectOriented));
        if(chosen.equals(CrossoverTypes.AspectOriented)) orientationBox.setValue(((AspectOrientedCrossover)wrapper.getEngine().getCrossover()).getOrientation());
        if(wrapper.getEngine().getCrossover().getType().equals(chosen)) {
            cuttingTextField.setText(String.valueOf(wrapper.getEngine().getCrossover().getCuttingPoints()));
        }
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
        SelectionTypes current = selections.getValue();
        if(wrapper.getEngine().getSelection().getType().equals(current)){
            switch (current){
                case Tournament:
                    ((TournamentSelection)wrapper.getEngine().getSelection()).setPte(Float.parseFloat(selectionTextField.getText()));
                    break;
                case Truncation:
                    ((TruncationSelection)wrapper.getEngine().getSelection()).setTopPercent(Integer.parseInt(selectionTextField.getText()));
                    break;
            }
        }
        else{
            switch (current){
                case RouletteWheel:
                    wrapper.getEngine().setSelection(new RouletteWheelSelection());
                    break;
                case Truncation:
                    wrapper.getEngine().setSelection(new TruncationSelection(Integer.parseInt(selectionTextField.getText())));
                    break;
                case Tournament:
                    wrapper.getEngine().setSelection(new TournamentSelection(Float.parseFloat(selectionTextField.getText())));
                    break;
            }
        }
    }

    private void setCrossover(){
        CrossoverTypes current = crossovers.getValue();
        if(wrapper.getEngine().getCrossover().getType().equals(current)){
            if(current.equals(CrossoverTypes.AspectOriented)){
                ((AspectOrientedCrossover)wrapper.getEngine().getCrossover()).setOrientation(orientationBox.getValue());
            }

            wrapper.getEngine().getCrossover().setCuttingPoints(Integer.parseInt(cuttingTextField.getText()));
        }
        else{
            int cutting = Integer.parseInt(cuttingTextField.getText());
            switch (current){
                case AspectOriented:
                    wrapper.getEngine().setCrossover(new AspectOrientedCrossover(cutting, orientationBox.getValue()));
                    break;
                case DayTimeOriented:
                    wrapper.getEngine().setCrossover(new DayTimeOrientedCrossover(cutting));
                    break;
            }
        }
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
            AlertUtils.displayAlert(Alert.AlertType.INFORMATION, "Success", "your changes saved");
            if(after != null) after.accept(null);
        }catch (NumberFormatException ex){
          AlertUtils.displayAlert(Alert.AlertType.ERROR, "Input failure", "please check the values you inserted");
        } catch (Exception e){
            AlertUtils.displayAlert(Alert.AlertType.ERROR, "Failed", e.getMessage());
        }
    }
}