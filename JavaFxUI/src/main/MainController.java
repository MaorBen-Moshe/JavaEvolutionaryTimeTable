package main;

import DTO.*;
import bestItemComponent.BestItemController;
import changeSystemComponent.ChangeSystemController;
import commands.CommandResult;
import commands.EngineWrapper;
import commands.LoadCommand;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.TimeTable;
import models.TimeTableSystemDataSupplier;
import systemInfoComponent.SystemInfoController;
import tasks.StartAlgorithmTask;
import utils.AlertUtils;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class MainController {
    private Stage primaryStage;
    private final SimpleStringProperty selectedFileProperty;
    private final SimpleBooleanProperty isFileSelected;
    private EngineWrapper<TimeTable, TimeTableSystemDataSupplier> engineWrapper;
    private LoadCommand loadCommand;
    private StartAlgorithmTask startTask;
    private boolean paused = false;

    @FXML
    private Button loadButton;
    @FXML
    private Label filePathLabel;
    @FXML
    private ComboBox<String> themesComboBox;
    @FXML
    private CheckBox animationsCheckBox;
    @FXML
    private Button stopButton;
    @FXML
    private Button startButton;
    @FXML
    private Button pauseButton;
    @FXML
    private ProgressBar generationsProgressBar;
    @FXML
    private Label generationsLabel;
    @FXML
    private ProgressBar fitnessProgressBar;
    @FXML
    private Label fitnessLabel;
    @FXML
    private ProgressBar timeProgressBar;
    @FXML
    private Label timeLabel;
    @FXML
    private ScrollPane systemInfo;
    @FXML
    private SystemInfoController systemInfoController;
    @FXML
    private ScrollPane bestItem;
    @FXML
    private BestItemController bestItemController;
    @FXML
    private ScrollPane changeSystemInfo;
    @FXML
    private ChangeSystemController changeSystemInfoController;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Label fitnessRunningLabel;
    @FXML
    private Label generationsRunningLabel;
    @FXML
    private TextField generationsTextField;
    @FXML
    private Label genTextLabel;
    @FXML
    private TextField fitnessTextField;
    @FXML
    private Label fitTextLabel;
    @FXML
    private TextField timeTextField;
    @FXML
    private Label timeTextLabel;
    @FXML
    private TextField jumpsTextField;
    @FXML
    private Label jumpsTextLabel;

    public MainController(){
        selectedFileProperty = new SimpleStringProperty();
        isFileSelected = new SimpleBooleanProperty(false);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public  void onLoadFile(ActionEvent event){
        try{
            loadCommand.execute();
        } catch (Exception e){
            AlertUtils.displayAlert(Alert.AlertType.ERROR, "Fail", "file load failed: "+ e.getMessage());
        }
    }

    @FXML
    public void onPause(ActionEvent event) {
        try {
            if(paused){
                changeSystemInfo.setVisible(false);
                startTask.resume();
                pauseButton.setText("Pause");
                paused = false;
            }
            else{
                startTask.pause();
                pauseButton.setText("Resume");
                paused = true;
                changeSystemInfoController.setView();
                changeSystemInfo.setVisible(true);
            }
        } catch (Exception e) {
            AlertUtils.displayAlert(Alert.AlertType.ERROR, "Fail", e.getMessage());
        }
    }

    @FXML
    public void onStart(ActionEvent event) {
        try{
            if(isFileSelected.get()){
                StartSystemInfoDTO info = createRules();
                startTask = new StartAlgorithmTask(engineWrapper, info, (result) -> {
                    startButton.setDisable(false);
                    paused = false;
                    bestItemController.setView();
                    bestItem.setVisible(true);
                });

                startButton.setDisable(true);
                bestItem.setVisible(false);
                generationsRunningLabel.textProperty().bind(Bindings.concat("Generation number: ", startTask.getCurrentGenerationsProperty()));
                fitnessRunningLabel.textProperty().bind(Bindings.concat("Fitness number: ", startTask.getCurrentFitnessProperty()));
                generationsProgressBar.progressProperty().bind(startTask.getGenerationsProperty());
                fitnessProgressBar.progressProperty().bind(startTask.getFitnessProperty());
                timeProgressBar.progressProperty().bind(startTask.getTimeProperty());
                startTask.exceptionProperty().addListener((obs, oldVal, newVal) -> {
                    if(newVal != null){
                        AlertUtils.displayAlert(Alert.AlertType.ERROR, "Failure", newVal.getMessage());
                        startButton.setDisable(false);
                    }
                });

                generationsRunningLabel.setVisible(true);
                fitnessRunningLabel.setVisible(true);
                new Thread(startTask).start();
            }
            else{
                AlertUtils.displayAlert(Alert.AlertType.INFORMATION, "Note", "Please load a file first!");
            }
        }catch (Exception e){
            AlertUtils.displayAlert(Alert.AlertType.ERROR, "Failure", e.getMessage());
        }
    }

    @FXML
    public void onStop(ActionEvent event) {
        try {
            startTask.stop();
            startTask.cancel();
            bestItemController.setView();
            bestItem.setVisible(true);
        } catch (Exception e) {
            AlertUtils.displayAlert(Alert.AlertType.ERROR, "Failure", e.getMessage());
        }
    }

    @FXML
    private void initialize() {
        filePathLabel.textProperty().bind(selectedFileProperty);
        engineWrapper = new EngineWrapper<>();
        loadCommand = new LoadCommand(engineWrapper, () -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select evolutionary logic file");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile == null) {
                return null;
            }

            return selectedFile.getAbsolutePath();
        }, (path) -> {
            selectedFileProperty.set(path);
            isFileSelected.set(engineWrapper.isFileLoaded());
        }, () -> false);

        themesComboBox.getItems().addAll("default", "theme 1", "theme 2");
        themesComboBox.valueProperty().addListener((obs, oldItem, newItem) -> {
            // load new css according to newItem
        });

        bestItemController.setWrapper(engineWrapper);
        bestItem.setVisible(false);
        systemInfoController.setEngineWrapper(engineWrapper);
        changeSystemInfo.setVisible(false);
        changeSystemInfoController.setWrapper(engineWrapper);
        changeSystemInfoController.setAfterSubmit(x -> systemInfoController.refreshView());
        systemInfo.visibleProperty().bind(isFileSelected);
        stopButton.disableProperty().bind(startButton.disableProperty().not());
        pauseButton.disableProperty().bind(startButton.disableProperty().not());
        generationsTextField.visibleProperty().bind(startButton.disableProperty().not());
        genTextLabel.visibleProperty().bind(startButton.disableProperty().not());
        fitnessTextField.visibleProperty().bind(startButton.disableProperty().not());
        fitTextLabel.visibleProperty().bind(startButton.disableProperty().not());
        timeTextField.visibleProperty().bind(startButton.disableProperty().not());
        timeTextLabel.visibleProperty().bind(startButton.disableProperty().not());
        jumpsTextField.visibleProperty().bind(startButton.disableProperty().not());
        jumpsTextLabel.visibleProperty().bind(startButton.disableProperty().not());
        loadButton.disableProperty().bind(startButton.disableProperty());
        generationsRunningLabel.setVisible(false);
        fitnessRunningLabel.setVisible(false);
        generationsLabel.setVisible(false);
        fitnessLabel.setVisible(false);
        timeLabel.setVisible(false);
        generationsProgressBar.setVisible(false);
        fitnessProgressBar.setVisible(false);
        timeProgressBar.setVisible(false);
        generationsLabel.textProperty().bind(Bindings.concat("generations ", Bindings.multiply(generationsProgressBar.progressProperty(), 100), "%"));
        fitnessLabel.textProperty().bind(Bindings.concat("fitness ", Bindings.multiply(fitnessProgressBar.progressProperty(), 100), "%"));
        timeLabel.textProperty().bind(Bindings.concat("time ", Bindings.multiply(timeProgressBar.progressProperty(), 100), "%"));
    }

    private StartSystemInfoDTO createRules(){
        int jumps;
        if(!jumpsTextField.getText().trim().isEmpty()){
            jumps = Integer.parseInt(jumpsTextField.getText());
        }
        else{
            throw new IllegalArgumentException("You should pick the jumps in generations");
        }

        Set<TerminateRuleDTO> rules = new HashSet<>();
        if(!generationsTextField.getText().trim().isEmpty()) {
            int generations = Integer.parseInt(generationsTextField.getText());
            if(generations < 100){
                throw new IllegalArgumentException("You should run the algorithm for at least 100 generations");
            }

            rules.add(new GenerationsTerminateRuleDTO(generations));
            generationsProgressBar.setVisible(true);
            generationsLabel.setVisible(true);
        }
        else{
            generationsProgressBar.setVisible(false);
            generationsLabel.setVisible(false);
        }

        if(!fitnessTextField.getText().trim().isEmpty()) {
            double fitness = Double.parseDouble(fitnessTextField.getText());
            rules.add(new FitnessTerminateRuleDTO(fitness));
            fitnessProgressBar.setVisible(true);
            fitnessLabel.setVisible(true);
        }
        else{
            fitnessProgressBar.setVisible(false);
            fitnessLabel.setVisible(false);
        }

        if(!timeTextField.getText().trim().isEmpty()){
            long time = Long.parseLong(timeTextField.getText());
            rules.add(new TimeTerminateRuleDTO(time));
            timeProgressBar.setVisible(true);
            timeLabel.setVisible(true);
        }
        else{
            timeProgressBar.setVisible(false);
            timeLabel.setVisible(false);
        }

        return new StartSystemInfoDTO(rules, jumps);
    }
}