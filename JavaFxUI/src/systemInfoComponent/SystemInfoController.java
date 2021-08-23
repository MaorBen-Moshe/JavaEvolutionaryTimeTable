package systemInfoComponent;

import DTO.*;
import commands.CommandResult;
import commands.EngineWrapper;
import commands.SystemInfoCommand;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import models.TimeTable;
import models.TimeTableSystemDataSupplier;
import mutation.Mutation;
import utils.AlertUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class SystemInfoController {
    @FXML
    private Button showButton;
    @FXML
    private VBox vBox;
    @FXML
    private Label mainTitle;
    private Label timeTableLabel;
    private Label daysLabel;
    private Label hoursLabel;
    private Label engineInfoLabel;
    private Label populationLabel;
    private Label selectionTitle;
    private Label selection;
    private Label elitism;
    private Label crossoverTitle;
    private Label crossover;
    private Label subjectsTitle;
    private Label teachersTitle;
    private Label classesTitle;
    private Label rulesTitle;
    private Label mutationsTitle;
    private TitledPane timeTable;
    private VBox timeTableVBox;
    private TitledPane engine;
    private VBox engineVBox;
    private boolean shown;
    private SystemInfoCommand systemInf;

    public void setEngineWrapper(EngineWrapper<TimeTable, TimeTableSystemDataSupplier> engineWrapper) {
        systemInf = new SystemInfoCommand(engineWrapper, this::setSystemInfo);
    }

    public void initialize(){
        shown = false;
        timeTableLabel = new Label("Time table info: ");
        timeTableLabel.getStyleClass().addAll("subTitle", "title");
        daysLabel = new Label();
        hoursLabel = new Label();
        engineInfoLabel = new Label("Engine info: ");
        engineInfoLabel.getStyleClass().addAll("subTitle", "title");
        populationLabel = new Label();
        selectionTitle = new Label("Selection:");
        selectionTitle.getStyleClass().add("title");
        selection = new Label();
        elitism = new Label();
        crossoverTitle = new Label("Crossover:");
        crossoverTitle.getStyleClass().add("title");
        crossover = new Label();
        subjectsTitle = setTitleLabel("Subjects: ");
        teachersTitle = setTitleLabel("Teachers: ");
        classesTitle = setTitleLabel("Classes: ");
        rulesTitle = setTitleLabel("Rules: ");
        mutationsTitle = setTitleLabel("Mutations: ");

        timeTableVBox = new VBox();
        timeTable = new TitledPane("Time Table info", timeTableVBox);
        timeTable.setVisible(false);
        timeTable.setExpanded(false);
        engineVBox = new VBox();
        engine = new TitledPane("Engine Info", engineVBox);
        engine.setExpanded(false);
        engine.setVisible(false);
        vBox.getChildren().addAll(timeTable, engine);
        mainTitle.setVisible(false);
    }

    public void refreshView(){
        if(shown){
            systemInf.execute();
        }
    }

    private void setView(SystemInfoDTO<TimeTable, TimeTableSystemDataSupplier> result) {
        setTimeTableInfo(result);
        setEngineInfo(result);
    }

    private void setSystemInfo(CommandResult<SystemInfoDTO<TimeTable, TimeTableSystemDataSupplier>> infoResult){
        if(infoResult.isFailed()){
            AlertUtils.displayAlert(Alert.AlertType.ERROR, "Fail", infoResult.getErrorMessage());
            return;
        }

        setView(infoResult.getResult());
    }

    @FXML
    private void onShowInfo(ActionEvent event) {
        if(shown){
            timeTable.setVisible(false);
            engine.setVisible(false);
            mainTitle.setVisible(false);
            showButton.setText("Show info");
        }
        else{
            systemInf.execute();
            timeTable.setVisible(true);
            engine.setVisible(true);
            mainTitle.setVisible(true);
            showButton.setText("Hide info");
        }

        shown = !shown;
    }

    private void setTimeTableInfo(SystemInfoDTO<TimeTable, TimeTableSystemDataSupplier> result){
        timeTableVBox.getChildren().clear();
        daysLabel.setText("Days: " + result.getDays());
        hoursLabel.setText("Hours: " + result.getHours());
        timeTableVBox.getChildren().addAll(timeTableLabel,
                                           daysLabel,
                                           hoursLabel);
        timeTableVBox.getChildren().addAll(setSerialItems(subjectsTitle, result.getSubjects()));
        timeTableVBox.getChildren().addAll(setSerialItems(teachersTitle, result.getTeachers()));
        timeTableVBox.getChildren().addAll(setSerialItems(classesTitle, result.getClasses()));
        timeTableVBox.getChildren().addAll(setRulesComponent(result.getRules()));
    }

    private void setEngineInfo(SystemInfoDTO<TimeTable, TimeTableSystemDataSupplier> result){
        engineVBox.getChildren().clear();
        engineVBox.getChildren().add(engineInfoLabel);
        populationLabel.setText("Population: " + result.getInitialSize());
        elitism.setText("Elitism: " + result.getElitism());
        selection.setText(result.getSelection().toString());
        crossover.setText(result.getCrossover().toString());
        engineVBox.getChildren().addAll(populationLabel,
                                        elitism,
                                        selectionTitle,
                                        selection,
                                        crossoverTitle,
                                        crossover);
        engineVBox.getChildren().addAll(setMutations(result.getMutations()));
    }

    private Label setTitleLabel(String text){
        Label serialTitle = new Label(text);
        serialTitle.getStyleClass().add("title");
        return serialTitle;
    }

    private <T extends SerialItemDTO> List<Label> setSerialItems(Label title, Set<T> items){
        List<Label> labels = new ArrayList<>();
        labels.add(title);
        items.stream().sorted(Comparator.comparing(T::getId)).forEach(item ->{
            Label label = new Label();
            label.setVisible(true);
            label.setText(item.toString());
            labels.add(label);
        });

        return labels;
    }

    private List<Label> setRulesComponent(RulesDTO rules){
        List<Label> labels = new ArrayList<>();
        labels.add(rulesTitle);
        for(RuleDTO rule : rules.getRules()){
            Label label = new Label();
            label.setVisible(true);
            String configurations = rule.getConfigurations().size() > 0 ? ", configurations: " + rule.getConfigurations().toString() : "";
            label.setText("Rule name: " + rule.getType() + ", rule strength: " + rule.getStrength() + configurations);
            labels.add(label);
        }

        return labels;
    }

    private <T extends Mutation<TimeTable, TimeTableSystemDataSupplier>> List<Label> setMutations(List<T> items){
        List<Label> labels = new ArrayList<>();
        labels.add(mutationsTitle);
        items.forEach(item ->{
            Label label = new Label();
            label.setVisible(true);
            label.setText(item.toString());
            labels.add(label);
        });

        return labels;
    }
}