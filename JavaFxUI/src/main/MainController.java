package main;

import DTO.*;
import Interface.ThemesChanger;
import bestItemComponent.BestItemController;
import changeSystemComponent.ChangeSystemController;
import commands.EngineWrapper;
import commands.LoadCommand;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import models.TimeTable;
import models.TimeTableSystemDataSupplier;
import systemInfoComponent.SystemInfoController;
import tasks.StartAlgorithmTask;
import utils.AlertUtils;

import java.io.File;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

public class MainController implements ThemesChanger {
    private Stage primaryStage;
    private final SimpleStringProperty selectedFileProperty;
    private final SimpleBooleanProperty isFileSelected;
    private EngineWrapper<TimeTable, TimeTableSystemDataSupplier> engineWrapper;
    private LoadCommand loadCommand;
    private StartAlgorithmTask startTask;
    private boolean paused = false;
    private final Pattern numberPattern;
    private final Pattern fitnessPattern;

    @FXML
    private Button loadButton;
    @FXML
    private Label filePathLabel;
    @FXML
    private ComboBox<ThemesChanger.Themes> themesComboBox;
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
    @FXML
    private ImageView startImage;
    @FXML
    private BorderPane root;

    public MainController(){
        selectedFileProperty = new SimpleStringProperty();
        isFileSelected = new SimpleBooleanProperty(false);
        numberPattern = Pattern.compile("[0-9]*");
        fitnessPattern = Pattern.compile("^[1-9]?[0-9]?$|^100$");
    }

    @Override
    public void setNewTheme(Themes theme) {
        root.getStylesheets().clear();
        String sheet;
        switch(theme){
            case Theme_1: sheet = "mainTheme1.css"; break;
            case Theme_2: sheet = "mainTheme2.css"; break;
            default: sheet = "main.css"; break;
        }

        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource(sheet)).toExternalForm());
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, (event) ->{
            try {
                if(startTask != null){
                    startTask.stop();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public  void onLoadFile(){
        try{
            loadCommand.execute();
        } catch (Exception e){
            AlertUtils.displayAlert(Alert.AlertType.ERROR, "Fail", "file load failed: "+ e.getMessage());
        }
    }

    @FXML
    public void onPause() {
        try {
            if(paused){
                resume(true);
            }
            else{
                pause();
            }
        } catch (Exception e) {
            AlertUtils.displayAlert(Alert.AlertType.ERROR, "Fail", e.getMessage());
        }
    }

    @FXML
    public void onStart() {
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
                startBinds();
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

    private void startBinds(){
        generationsRunningLabel.textProperty().bind(Bindings.concat("Generation number: ", startTask.getCurrentGenerationsProperty()));
        fitnessRunningLabel.textProperty().bind(Bindings.concat("Fitness number: ", startTask.getCurrentFitnessProperty()));
        generationsProgressBar.progressProperty().bind(startTask.getGenerationsProperty());
        fitnessProgressBar.progressProperty().bind(startTask.getFitnessProperty());
        timeProgressBar.progressProperty().bind(startTask.getTimeProperty());
    }

    @FXML
    public void onStop() {
        try {
            startTask.stop();
            bestItemController.setView();
            bestItem.setVisible(true);
            if(paused) resume(false);
        } catch (Exception e) {
            AlertUtils.displayAlert(Alert.AlertType.ERROR, "Failure", e.getMessage());
        }
    }

    private void resume(boolean run) throws Exception{
        changeSystemInfo.setVisible(false);
        if(run) startTask.resume();
        pauseButton.setText("Pause");
        paused = false;
    }

    private void pause() throws Exception {
        startTask.pause();
        pauseButton.setText("Resume");
        paused = true;
        changeSystemInfoController.setView();
        changeSystemInfo.setVisible(true);
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
            systemInfoController.refreshView();
        }, () ->  {
            AlertUtils.displayAlert(Alert.AlertType.INFORMATION, "Note", "please stop the algorithm first");
            return false;
        });
        themesComboBox.getItems().addAll(Themes.values());
        themesComboBox.setValue(Themes.Default);
        themesComboBox.valueProperty().addListener((obs, oldItem, newItem) -> setThemesChangeListener(newItem));

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
        startImage.visibleProperty().bind(animationsCheckBox.selectedProperty());
        bestItemController.bindAnimationProperty(animationsCheckBox.selectedProperty());
        setSandClockAnimation();
        setTextFieldListeners();
    }

    private void setTextFieldListeners(){
        generationsTextField.textProperty().addListener((item, old, newVal) -> textFieldHelper(generationsTextField, newVal, old));

        fitnessTextField.textProperty().addListener((item, old, newVal) ->{
            if(!fitnessPattern.matcher(newVal).matches()){
                fitnessTextField.setText(old);
            }
        });

        timeTextField.textProperty().addListener((item, old, newVal) -> textFieldHelper(timeTextField, newVal, old));

        jumpsTextField.textProperty().addListener((item, old, newVal) -> textFieldHelper(jumpsTextField, newVal, old));
    }

    private void textFieldHelper(TextField field, String val, String old){
        if(!numberPattern.matcher(val).matches()){
            field.setText(old);
        }
    }

    private void setSandClockAnimation(){
        startImage.setImage(new Image("https://image.flaticon.com/icons/png/512/3448/3448543.png"));
        startImage.setFitHeight(35);
        startImage.setFitWidth(35);
        startImage.setMouseTransparent(true);
        startImage.setTranslateZ(150);
        startImage.setOpacity(0.7);
        final Rotate rotationTransform = new Rotate(0, 0, 20);
        startImage.getTransforms().add(rotationTransform);
        // rotate a square using timeline attached to the rotation transform's angle property.
        final Timeline rotationAnimation = new Timeline();
        rotationAnimation.getKeyFrames()
                .add(
                        new KeyFrame(
                                Duration.seconds(5),
                                new KeyValue(
                                        rotationTransform.angleProperty(),
                                        360
                                )
                        )
                );
        rotationTransform.setAxis(Rotate.X_AXIS);
        rotationAnimation.setCycleCount(Animation.INDEFINITE);
        rotationAnimation.play();
    }

    private void setThemesChangeListener(Themes current){
        setNewTheme(current);
        changeSystemInfoController.setNewTheme(current);
        bestItemController.setNewTheme(current);
        systemInfoController.setNewTheme(current);
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