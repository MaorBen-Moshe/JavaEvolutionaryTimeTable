package bestItemComponent;

import DTO.*;
import commands.BestSolutionCommand;
import commands.CommandResult;
import commands.EngineWrapper;
import commands.ProcessCommand;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import models.SerialItem;
import models.TimeTable;
import models.TimeTableSystemDataSupplier;
import utils.AlertUtils;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BestItemController {
    private static class ComboItem{
        private int id;
        private String name;

        private ComboItem(int id, String name){
            this.id = id;
            this.name = name;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ComboItem comboItem = (ComboItem) o;
            return id == comboItem.id && Objects.equals(name, comboItem.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name);
        }

        @Override
        public String toString() {
            return  "id=" + id +
                    ", name=" + name;
        }
    }

    private BestSolutionCommand bestCommand;
    private ProcessCommand processCommand;
    private List<FitnessHistoryItemDTO<TimeTableDTO>> history;
    private int days;
    private int hours;
    private int currentDisplayItemIndex;
    private EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper;
    private enum Aspect {CLASS, TEACHER}

    @FXML
    private ComboBox<Aspect> aspectComboBox;

    @FXML
    private ComboBox<ComboItem> itemComboBox;

    @FXML
    private Label displayLabel;

    @FXML
    private Label itemLabel;

    @FXML
    private Button prevButton;

    @FXML
    private Button nextButton;

    @FXML
    private LineChart<NumberAxis, NumberAxis> fitnessChart;

    @FXML
    private NumberAxis xAxis ;

    @FXML
    private NumberAxis yAxis ;

    @FXML
    private VBox rulesVBox;

    @FXML
    private GridPane tablePos;
    @FXML
    private Label fitnessLabel;
    @FXML
    private Label generationLabel;
    @FXML
    private Label softRulesLabel;
    @FXML
    private Label hardRulesLabel;
    @FXML
    private Label rulesTitleLabel;
    @FXML
    private Label mainTitleLabel;

    public BestItemController(){
    }

    public void setWrapper(EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper){
        bestCommand = new BestSolutionCommand(wrapper, (result) -> {
            try {
                display(result);
            } catch (Exception e) {
                AlertUtils.displayAlert(Alert.AlertType.ERROR, "Failed", e.getMessage());
            }
        });

        processCommand = new ProcessCommand(wrapper,  (result) -> {
            try {
                process(result);
            } catch (Exception e) {
                AlertUtils.displayAlert(Alert.AlertType.ERROR, "Failed", e.getMessage());
            }
        });

        this.wrapper = wrapper;
    }

    public void setView(){
        bestCommand.execute();
        processCommand.execute();
    }

    @FXML
    private void initialize(){
        mainTitleLabel = new Label("Solution Info:");
        rulesTitleLabel = new Label("Rules Score:");
        generationLabel = new Label();
        fitnessLabel = new Label();
        softRulesLabel = new Label();
        hardRulesLabel = new Label();
        itemComboBox.setVisible(false);
        itemLabel.setVisible(false);
        aspectComboBox.getItems().addAll(Aspect.values());
        aspectComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldVal, newVal) -> {
            itemComboBox.setVisible(true);
            itemLabel.setVisible(true);
            itemComboBox.getItems().clear();
            switch (newVal){
                case CLASS: setItemsComboBox(wrapper.getEngine().getSystemInfo().getClasses()); break;
                case TEACHER: setItemsComboBox(wrapper.getEngine().getSystemInfo().getTeachers()); break;
            }
        });
    }

    private void setItemsComboBox(Map<Integer, ? extends SerialItem> items){
        items.forEach((key, val) -> itemComboBox.getItems().add(new ComboItem(key, val.getName())));
    }

    private void process(CommandResult<List<FitnessHistoryItemDTO<TimeTableDTO>>> result) throws Exception {
        if(result.isFailed()){
            throw new Exception(result.getErrorMessage());
        }

        history = result.getResult();
        currentDisplayItemIndex = history.size() - 1;
        fillChart(result.getResult());
    }

    private void display(CommandResult<BestSolutionDTO> result) throws Exception {
        if(result.isFailed()){
            throw new Exception(result.getErrorMessage());
        }

        BestSolutionDTO solution = result.getResult();
        days = solution.getSupplier().getDays();
        hours = solution.getSupplier().getHours();
        createInitialGrid();
        displayRules(solution.getGenerationCreated(), solution.getFitness(), solution.getSolution().getSoftRulesAvg(), solution.getSolution().getHardRulesAvg(), solution.getSolution().getRulesScore());
    }

    private void createInitialGrid() {
    }

    private void fillChart(List<FitnessHistoryItemDTO<TimeTableDTO>> result) {
        fitnessChart.setTitle("Fitness by generation");
        Series series = new Series();
        result.forEach(item -> series.getData().add(new XYChart.Data<>(item.getGenerationNumber(), item.getCurrentGenerationFitness())));

        fitnessChart.getData().clear();
        fitnessChart.getData().addAll(series);
    }

    private void displayRules(int generations, double fitness, double softAvg, double hardAvg, Map<RuleDTO, Double> rules){
        rulesVBox.getChildren().clear();
        generationLabel.setText("Generation: " + generations);
        fitnessLabel.setText("Fitness: " + fitness);
        softRulesLabel.setText("Soft Rules: " + (softAvg < 0 ? "no soft rules" : softAvg));
        hardRulesLabel.setText("Hard Rules: " + (hardAvg < 0 ? "no hard rules" : hardAvg));

        rulesVBox.getChildren().addAll(mainTitleLabel, generationLabel, fitnessLabel, softRulesLabel, hardRulesLabel, rulesTitleLabel);
        rules.forEach((rule, ruleScore) -> {
            Label name = new Label("Rule name: " + rule.getType());
            Label strength = new Label("Rule strength: " + rule.getStrength());
            Label scoreLabel = new Label("Rule score: " + ruleScore);
            rulesVBox.getChildren().addAll(name, strength, scoreLabel);
        });
    }

    private void displayTable(TimeTableDTO table){
        tablePos.getChildren().clear();
        ComboItem currentChoice = itemComboBox.getValue();
    }

    @FXML
    void onNext(ActionEvent event) {
        if(currentDisplayItemIndex < history.size() - 1){
            FitnessHistoryItemDTO<TimeTableDTO> current = history.get(++currentDisplayItemIndex);
            displayRules(current.getGenerationNumber(), current.getCurrentGenerationFitness(), current.getSolution().getSoftRulesAvg(), current.getSolution().getHardRulesAvg(), current.getSolution().getRulesScore());
            displayTable(current.getSolution());
        }
    }

    @FXML
    void onPrev(ActionEvent event) {
        if(currentDisplayItemIndex > 0){
            FitnessHistoryItemDTO<TimeTableDTO> current = history.get(--currentDisplayItemIndex);
            displayRules(current.getGenerationNumber(), current.getCurrentGenerationFitness(), current.getSolution().getSoftRulesAvg(), current.getSolution().getHardRulesAvg(), current.getSolution().getRulesScore());
            displayTable(current.getSolution());
        }
    }
}
