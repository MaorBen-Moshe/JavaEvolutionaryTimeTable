package systemInfoComponent;

import DTO.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
        Label timeTableLabel = new Label("Time table info: ");
        timeTableLabel.getStyleClass().addAll("subTitle", "title");
        vBox.getChildren().add(timeTableLabel);
        vBox.getChildren().add(new Label("Days: " + result.getDays()));
        vBox.getChildren().add(new Label("Hours: " + result.getHours()));
        vBox.getChildren().addAll(setSerialItems("Subjects: ", result.getSubjects()));
        vBox.getChildren().addAll(setSerialItems("Teachers: ", result.getTeachers()));
        vBox.getChildren().addAll(setSerialItems("Classes: ", result.getClasses()));
        vBox.getChildren().addAll(setRulesComponent(result.getRules()));
        Label engineInfoLabel = new Label("Engine info: ");
        engineInfoLabel.getStyleClass().addAll("subTitle", "title");
        vBox.getChildren().add(engineInfoLabel);
        vBox.getChildren().add(new Label("Population: " + result.getInitialSize()));
        Label selection = new Label("Selection:");
        selection.getStyleClass().add("title");
        vBox.getChildren().add(selection);
        vBox.getChildren().add(new Label(result.getSelection().toString()));
        Label crossover = new Label("Crossover:");
        crossover.getStyleClass().add("title");
        vBox.getChildren().add(crossover);
        vBox.getChildren().add(new Label(result.getCrossover().toString()));
        vBox.getChildren().addAll(setMutations("Mutations: ", result.getMutations()));
    }
}