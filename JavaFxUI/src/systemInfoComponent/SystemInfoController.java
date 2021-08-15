package systemInfoComponent;

import DTO.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import models.TimeTable;
import models.TimeTableSystemDataSupplier;
import mutation.Mutation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class SystemInfoController {
    @FXML
    private VBox vBox;
    @FXML
    private Label systemInfoTitle;
    @FXML
    private Label timeTableTitle;
    @FXML
    private Label daysPrefix;
    @FXML
    private Label days;
    @FXML
    private Label hoursPrefix;
    @FXML
    private Label hours;
    @FXML
    private Label engineTitle;
    @FXML
    private Label populationPrefix;
    @FXML
    private Label population;
    @FXML
    private Label selectionPrefix;
    @FXML
    private Label selection;
    @FXML
    private Label crossoverPrefix;
    @FXML
    private Label crossover;

    public <T extends SerialItemDTO> List<Label> setSerialItems(String title, Set<T> items){
        List<Label> labels = new ArrayList<>();
        Label serialTitle = new Label(title);
        serialTitle.setVisible(true);
        serialTitle.getStyleClass().add("title");
        labels.add(serialTitle);
        items.stream().sorted(Comparator.comparing(T::getId)).forEach(item ->{
            Label label = new Label();
            label.setVisible(true);
            label.setText(item.toString());
            labels.add(label);
        });

        return labels;
    }

    public List<Label> setRulesComponent(RulesDTO rules){
        List<Label> labels = new ArrayList<>();
        Label rulesTitle = new Label("Rules:");
        rulesTitle.setVisible(true);
        rulesTitle.getStyleClass().add("title");
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

    public <T extends Mutation<TimeTable, TimeTableSystemDataSupplier>> List<Label> setMutations(String title, List<T> items){
        List<Label> labels = new ArrayList<>();
        Label mutationsTitle = new Label(title);
        mutationsTitle.setVisible(true);
        mutationsTitle.getStyleClass().add("title");
        labels.add(mutationsTitle);
        items.forEach(item ->{
            Label label = new Label();
            label.setVisible(true);
            label.setText(item.toString());
            labels.add(label);
        });

        return labels;
    }

    public void setView(SystemInfoDTO<TimeTable, TimeTableSystemDataSupplier> result) {
        days.setText(String.valueOf(result.getDays()));
        hours.setText(String.valueOf(result.getHours()));
        population.setText(String.valueOf(result.getInitialSize()));
        selection.setText(result.getSelection().toString());
        crossover.setText(result.getCrossover().toString());

        int initIndex = vBox.getChildren().indexOf(hours) + 1;
        vBox.getChildren().addAll(initIndex, setSerialItems("Subjects: ", result.getSubjects()));
        initIndex += result.getSubjects().size();
        vBox.getChildren().addAll(initIndex, setSerialItems("Teachers: ", result.getTeachers()));
        initIndex += result.getTeachers().size();
        vBox.getChildren().addAll(initIndex, setSerialItems("Classes: ", result.getClasses()));
        initIndex += result.getClasses().size();
        vBox.getChildren().addAll(initIndex, setRulesComponent(result.getRules()));
        initIndex = vBox.getChildren().indexOf(crossover) + 1;
        vBox.getChildren().addAll(initIndex, setMutations("Mutations: ", result.getMutations()));
    }
}