package main;

import DTO.*;
import commands.CommandResult;
import commands.EngineWrapper;
import commands.LoadCommand;
import commands.SystemInfoCommand;
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
    private BorderPane borderPane;
    @FXML
    private Label fitnessRunningLabel;
    @FXML
    private Label generationsRunningLabel;
    @FXML
    private TextField generationsTextField;
    @FXML
    private TextField fitnessTextField;
    @FXML
    private TextField timeTextField;
    @FXML
    private TextField jumpsTextField;

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
            startTask.pause();
        } catch (Exception e) {
            AlertUtils.displayAlert(Alert.AlertType.ERROR, "Fail", e.getMessage());
        }
    }

    @FXML
    public void onStart(ActionEvent event) {
        try{
            if(isFileSelected.get()){
                StartSystemInfoDTO info = createRules();
                startTask = new StartAlgorithmTask(engineWrapper, info, (result) -> startButton.setDisable(false));

                startButton.setDisable(true);
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
                startTask.run();
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
        } catch (Exception e) {
            AlertUtils.displayAlert(Alert.AlertType.ERROR, "Failure", e.getMessage());
        }
    }

    @FXML
    private void initialize() {
        filePathLabel.textProperty().bind(selectedFileProperty);
        engineWrapper = new EngineWrapper<>();
        SystemInfoCommand systemInf = new SystemInfoCommand(engineWrapper, this::setSystemInfo);
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
            systemInf.execute();
        }, () -> false);

        themesComboBox.getItems().addAll("default", "theme 1", "theme 2");
        themesComboBox.valueProperty().addListener((obs, oldItem, newItem) -> {
            // load new css according to newItem
        });

        generationsLabel.textProperty().bind(Bindings.concat("generations ", Bindings.multiply(generationsProgressBar.progressProperty(), 100), "%"));
        fitnessLabel.textProperty().bind(Bindings.concat("fitness ", Bindings.multiply(fitnessProgressBar.progressProperty(), 100), "%"));
        timeLabel.textProperty().bind(Bindings.concat("time ", Bindings.multiply(timeProgressBar.progressProperty(), 100), "%"));
        systemInfo.visibleProperty().bind(isFileSelected);
        stopButton.disableProperty().bind(startButton.disableProperty().not());
        pauseButton.disableProperty().bind(startButton.disableProperty().not());
        generationsTextField.visibleProperty().bind(startButton.disableProperty().not());
        fitnessTextField.visibleProperty().bind(startButton.disableProperty().not());
        timeTextField.visibleProperty().bind(startButton.disableProperty().not());
        jumpsTextField.visibleProperty().bind(startButton.disableProperty().not());
        loadButton.disableProperty().bind(startButton.disableProperty());
        generationsRunningLabel.visibleProperty().bind(startButton.disableProperty());
        fitnessRunningLabel.visibleProperty().bind(startButton.disableProperty());
    }

    private void setSystemInfo(CommandResult<SystemInfoDTO<TimeTable, TimeTableSystemDataSupplier>> infoResult){
        if(infoResult.isFailed()){
            AlertUtils.displayAlert(Alert.AlertType.ERROR, "Fail", infoResult.getErrorMessage());
            return;
        }

        systemInfoController.setView(infoResult.getResult());
    }

    private StartSystemInfoDTO createRules(){
        int jumps = 1;
        if(!jumpsTextField.getText().trim().isEmpty()){
            jumps = Integer.parseInt(jumpsTextField.getText());
        }

        Set<TerminateRuleDTO> rules = new HashSet<>();
        if(!generationsTextField.getText().trim().isEmpty()) {
            int generations = Integer.parseInt(generationsTextField.getText());
            if(generations < 100){
                throw new IllegalArgumentException("You should run the algorithm for at least 100 generations");
            }

            rules.add(new GenerationsTerminateRuleDTO(generations));
        }

        if(!fitnessTextField.getText().trim().isEmpty()) {
            double fitness = Double.parseDouble(fitnessTextField.getText());
            rules.add(new FitnessTerminateRuleDTO(fitness));
        }

        if(!timeTextField.getText().trim().isEmpty()){
            long time = Long.parseLong(timeTextField.getText());
            rules.add(new TimeTerminateRuleDTO(time));
        }

        return new StartSystemInfoDTO(rules, jumps);
    }
}