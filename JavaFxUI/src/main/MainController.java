package main;

import DTO.SystemInfoDTO;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.TimeTable;
import models.TimeTableSystemDataSupplier;
import systemInfoComponent.SystemInfoController;

import java.io.File;

public class MainController {
    private Stage primaryStage;
    private final SimpleStringProperty selectedFileProperty;
    private final SimpleBooleanProperty isFileSelected;
    private EngineWrapper<TimeTable, TimeTableSystemDataSupplier> engineWrapper;
    private LoadCommand loadCommand;

    @FXML
    private Button loadButton;
    @FXML
    private Label filePathLabel;
    @FXML
    private ComboBox<String> themesComboBox;
    @FXML
    private CheckBox animationsCheckBox;
    @FXML
    private Rectangle stopButton;
    @FXML
    private Polygon startButton;
    @FXML
    private HBox pauseHBox;
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
    private Label errorLabel;
    @FXML
    private ScrollPane systemInfo;
    @FXML
    private SystemInfoController systemInfoController;
    @FXML
    private BorderPane borderPane;

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
            errorLabel.setVisible(true);
            errorLabel.setText("file load failed: "+ e.getMessage());
        }
    }

    @FXML
    public void onPause(MouseEvent event) {

    }

    @FXML
    public void onStart(MouseEvent event) {

    }

    @FXML
    public void onStop(MouseEvent event) {

    }

    @FXML
    private void initialize() {
        filePathLabel.textProperty().bind(selectedFileProperty);
        EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper = new EngineWrapper<>();
        SystemInfoCommand systemInf = new SystemInfoCommand(wrapper, this::setSystemInfo);
        loadCommand = new LoadCommand(wrapper, () -> {
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
            isFileSelected.set(wrapper.isFileLoaded());
            errorLabel.setVisible(false);
            systemInf.execute();
        }, () -> false);

        themesComboBox.getItems().addAll("default", "theme 1", "theme 2");
        themesComboBox.valueProperty().addListener((obs, oldItem, newItem) -> {
            // load new css according to newItem
        });

        generationsLabel.textProperty().bind(Bindings.concat("generations ", generationsProgressBar.progressProperty(), "%"));
        fitnessLabel.textProperty().bind(Bindings.concat("fitness ", fitnessProgressBar.progressProperty(), "%"));
        timeLabel.textProperty().bind(Bindings.concat("time ", timeProgressBar.progressProperty(), "%"));
        //emptyInfoLabel.visibleProperty().bind(isFileSelected.not());
        systemInfo.visibleProperty().bind(isFileSelected);
    }

    private void setSystemInfo(CommandResult<SystemInfoDTO<TimeTable, TimeTableSystemDataSupplier>> infoResult){
        if(infoResult.isFailed()){
            errorLabel.setVisible(true);
            errorLabel.setText(infoResult.getErrorMessage());
            return;
        }


        systemInfoController.setView(infoResult.getResult());
    }

}