package bestItemComponent;

import DTO.*;
import commands.BestSolutionCommand;
import commands.CommandResult;
import commands.EngineWrapper;
import commands.ProcessCommand;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import models.SerialItem;
import models.TimeTable;
import models.TimeTableSystemDataSupplier;
import utils.AlertUtils;

import java.util.*;
import java.util.stream.IntStream;

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
    private final IntegerProperty currentDisplayItemIndex;
    private final IntegerProperty jumpProperty;
    private EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper;
    private enum Aspect {CLASS, TEACHER, RAW}
    private GridPane tablePos;
    private VBox rawVBox;
    private TableView<RawTableItem> rawTable;

    @FXML
    private ScrollPane tableSplitPane;
    @FXML
    private ComboBox<Aspect> aspectComboBox;

    @FXML
    private ComboBox<ComboItem> itemComboBox;

    @FXML
    private Label itemLabel;

    @FXML
    private Button prevButton;

    @FXML
    private Button nextButton;
    @FXML
    private TextField jumpTF;

    @FXML
    private LineChart<NumberAxis, NumberAxis> fitnessChart;
    @FXML
    private VBox rulesVBox;
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
        currentDisplayItemIndex = new SimpleIntegerProperty(-1);
        jumpProperty = new SimpleIntegerProperty(1);
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
        mainTitleLabel.getStyleClass().add("title");
        rulesTitleLabel = new Label("Rules Score:");
        rulesTitleLabel.getStyleClass().add("subTitle");
        generationLabel = new Label();
        fitnessLabel = new Label();
        softRulesLabel = new Label();
        hardRulesLabel = new Label();
        itemComboBox.setVisible(false);
        itemLabel.setVisible(false);
        jumpTF.textProperty().setValue(String.valueOf(jumpProperty.get()));
        jumpTF.textProperty().addListener((item, old, newVal) -> {
            try{
                if(newVal.trim().isEmpty())
                {
                    return;
                }

                int num = Integer.parseInt(newVal);
                if(num < 0 || num >= history.size()){
                    jumpTF.setText(old);
                }
                else{
                    jumpProperty.setValue(num);
                }
            }catch (NumberFormatException e){
                jumpTF.setText(old);
            }
        });
        aspectComboBox.getItems().addAll(Aspect.values());
        aspectComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldVal, newVal) -> {
            if(newVal.equals(Aspect.RAW)){
                displayRaw();
                itemComboBox.setVisible(false);
                itemComboBox.setValue(null);
                itemLabel.setVisible(false);
                return;
            }

            itemComboBox.setVisible(true);
            itemLabel.setVisible(true);
            itemComboBox.getItems().clear();
            switch (newVal){
                case CLASS: setItemsComboBox(wrapper.getEngine().getSystemInfo().getClasses()); break;
                case TEACHER: setItemsComboBox(wrapper.getEngine().getSystemInfo().getTeachers()); break;
            }
        });

        itemComboBox.getSelectionModel().selectedItemProperty().addListener((item, old, newVal) -> displayTable(history.get(currentDisplayItemIndex.get()).getSolution(), newVal));

        currentDisplayItemIndex.addListener((item, old, newVal) -> {
            nextButton.disableProperty().set(currentDisplayItemIndex.get() + jumpProperty.get() > history.size() - 1);
            prevButton.disableProperty().set(currentDisplayItemIndex.get() - jumpProperty.get() < 0);
        });

        jumpProperty.addListener((item, old, newVal) -> {
            nextButton.disableProperty().set(currentDisplayItemIndex.get() + jumpProperty.get() > history.size() - 1);
            prevButton.disableProperty().set(currentDisplayItemIndex.get() - jumpProperty.get() < 0);
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
        currentDisplayItemIndex.setValue(history.size() - 1);
        fillChart(result.getResult());
    }

    private void display(CommandResult<BestSolutionDTO> result) throws Exception {
        if(result.isFailed()){
            throw new Exception(result.getErrorMessage());
        }

        BestSolutionDTO solution = result.getResult();
        days = solution.getSupplier().getDays();
        hours = solution.getSupplier().getHours();
        displayRules(solution.getGenerationCreated(), solution.getFitness(), solution.getSolution().getSoftRulesAvg(), solution.getSolution().getHardRulesAvg(), solution.getSolution().getRulesScore());
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
        final int[] i = {1};
        rules.forEach((rule, ruleScore) -> {
            Label title = new Label("Rule " + "#" + i[0] + ": ");
            title.getStyleClass().add("subTitle");
            Label name = new Label("Rule name: " + rule.getType());
            Label strength = new Label("Rule strength: " + rule.getStrength());
            Label scoreLabel = new Label("Rule score: " + ruleScore);
            rulesVBox.getChildren().addAll(title, name, strength, scoreLabel);
            i[0]++;
        });
    }

    private void createInitialGrid() {
        tablePos = new GridPane();
        tablePos.setGridLinesVisible(true);
        Label daysHours = new Label("days\\hours");
        daysHours.setId("daysHours");
        tablePos.add(daysHours, 0, 0);
        for(int i = 1; i <= hours; i++){
            Label curr = new Label(String.valueOf(i));
            curr.getStyleClass().add("tableHead");
            tablePos.add(curr, i, 0);
        }

        for(int j = 1; j <= days; j++){
            Label curr = new Label(String.valueOf(j));
            curr.getStyleClass().add("tableHead");
            tablePos.add(curr, 0, j);
        }

        setRowColConstraints();
    }

    private void setRowColConstraints(){
        tablePos.getRowConstraints().clear();
        tablePos.getColumnConstraints().clear();
        RowConstraints firstR = new RowConstraints(75);
        firstR.setFillHeight(true);
        firstR.setValignment(VPos.CENTER);
        tablePos.getRowConstraints().add(firstR);
        ColumnConstraints firstC = new ColumnConstraints(75);
        firstC.setFillWidth(true);
        firstC.setHalignment(HPos.CENTER);
        tablePos.getColumnConstraints().add(firstC);
        double width = hours > days ? 700d : 500d;
        double height = days > hours ? 700d : 500d;
        for(int i = 1; i <= hours; i++){
            ColumnConstraints col = new ColumnConstraints(width / hours);
            col.setFillWidth(true);
            col.setHalignment(HPos.CENTER);
            tablePos.getColumnConstraints().add(col);

        }

        for(int j = 1; j <= days; j++){
            RowConstraints row = new RowConstraints(height / days);
            row.setFillHeight(true);
            row.setValignment(VPos.CENTER);
            tablePos.getRowConstraints().add(row);
        }
    }

    private void displayTable(TimeTableDTO table, ComboItem currentChoice){
        Map<Integer, Map<Integer, List<TimeTableItemDTO>>> map = new HashMap<>();
        switch (aspectComboBox.getValue()){
            case CLASS: map = getClassMap(table, currentChoice.id); break;
            case TEACHER: map = getTeacherMap(table, currentChoice.id); break;
        }

        if(tablePos == null){
            createInitialGrid();
        }

        for(int row=1; row<=days; row++){
            for(int col=1;col<=hours; col++){
                List<CellItem> cellLabels = getCellItems(map.get(row).get(col), aspectComboBox.getValue());
                ScrollPane cell = createCell(cellLabels, aspectComboBox.getValue());
                cell.setStyle("-fx-background-color:black;");
                tablePos.add(cell, col, row);
                GridPane.setHgrow(cell, Priority.ALWAYS);
                GridPane.setVgrow(cell, Priority.ALWAYS);
            }
        }

        tableSplitPane.setContent(tablePos);
    }

    private ScrollPane createCell(List<CellItem> items, Aspect aspect){
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToHeight(true);
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(5));
        if(items != null && items.size() > 0){
            TableView<CellItem> table = new TableView<>();
            TableColumn<CellItem,String> itemCol = new TableColumn<>(aspect.equals(Aspect.CLASS) ? "Teacher" : "Class");
            TableColumn<CellItem,String> subjectCol = new TableColumn<>("Subject");
            itemCol.setCellValueFactory(
                    new PropertyValueFactory<>("serialItem")
            );
            subjectCol.setCellValueFactory(
                    new PropertyValueFactory<>("subject")
            );

            table.setItems(FXCollections.observableArrayList(items));
            table.getColumns().addAll(itemCol, subjectCol);
            vbox.getChildren().add(table);
        }

        scroll.setContent(vbox);
        return scroll;
    }

    private List<CellItem> getCellItems(List<TimeTableItemDTO> items, Aspect aspect){
        List<CellItem> cellItems = new ArrayList<>();
        items.forEach(item -> {
            switch (aspect){
                case TEACHER: CellItem currentT = new CellItem(item.getSchoolClass().getName() + ", " + item.getSchoolClass().getId(), item.getSubject().toString());
                              cellItems.add(currentT);
                              break;
                case CLASS: CellItem currentC = new CellItem(item.getTeacher().getName() + ", " + item.getTeacher().getId(), item.getSubject().toString());
                            cellItems.add(currentC);
                            break;
            }
        });

        return cellItems;
    }

    private Map<Integer, Map<Integer, List<TimeTableItemDTO>>> getClassMap(TimeTableDTO table, int classId){
        TimeTableSystemDataSupplier info = wrapper.getEngine().getSystemInfo();
        Map<Integer, Map<Integer, List<TimeTableItemDTO>>> dayHourTable = initializeTableView(info);
        table.getItems().forEach(item -> {
            if(item.getSchoolClass().getId() == classId){
                dayHourTable.get(item.getDay()).get(item.getHour()).add(item);
            }
        });

        return dayHourTable;
    }

    private Map<Integer, Map<Integer, List<TimeTableItemDTO>>> getTeacherMap(TimeTableDTO table, int teacherId){
        TimeTableSystemDataSupplier info = wrapper.getEngine().getSystemInfo();
        Map<Integer, Map<Integer, List<TimeTableItemDTO>>> dayHourTable = initializeTableView(info);
        table.getItems().forEach(item -> {
            if(item.getTeacher().getId() == teacherId){
                dayHourTable.get(item.getDay()).get(item.getHour()).add(item);
            }
        });

        return dayHourTable;
    }

    private Map<Integer, Map<Integer, List<TimeTableItemDTO>>> initializeTableView(TimeTableSystemDataSupplier supplier){
        Map<Integer, Map<Integer, List<TimeTableItemDTO>>> dayHourTable = new HashMap<>();
        IntStream.range(1, supplier.getDays() + 1).forEach(i -> {
            dayHourTable.put(i, new HashMap<>());
            IntStream.range(1, supplier.getHours() + 1).forEach(j -> dayHourTable.get(i).put(j, new ArrayList<>()));
        });

        return dayHourTable;
    }

    private void displayRaw(){
        if(rawVBox == null){
            rawVBox = new VBox();
            rawVBox.setSpacing(5);
            rawVBox.setPadding(new Insets(10));
            rawVBox.setAlignment(Pos.TOP_LEFT);
            rawTable = new TableView<>();
            TableColumn<RawTableItem,String> dayCol = new TableColumn<>("Day");
            TableColumn<RawTableItem,String> hourCol = new TableColumn<>("Hour");
            TableColumn<RawTableItem,Integer> classCol = new TableColumn<>("Class");
            TableColumn<RawTableItem,String> teacherCol = new TableColumn<>("Teacher");
            TableColumn<RawTableItem,String> subjectCol = new TableColumn<>("Subject");
            dayCol.setCellValueFactory(
                    new PropertyValueFactory<>("day")
            );
            hourCol.setCellValueFactory(
                    new PropertyValueFactory<>("hour")
            );
            classCol.setCellValueFactory(
                    new PropertyValueFactory<>("klass")
            );
            teacherCol.setCellValueFactory(
                    new PropertyValueFactory<>("teacher")
            );
            subjectCol.setCellValueFactory(
                    new PropertyValueFactory<>("subject")
            );

            rawTable.getColumns().addAll(dayCol, hourCol, classCol, teacherCol, subjectCol);
            rawVBox.getChildren().add(rawTable);
        }

        List<RawTableItem> items = new ArrayList<>();
        history.get(currentDisplayItemIndex.get()).getSolution().getItems().forEach(item -> items.add(new RawTableItem(item.getDay(),item.getHour(),
                item.getTeacher().getName(),
                item.getSchoolClass().getName(),
                item.getSubject().getName())));
        rawTable.setItems(FXCollections.observableArrayList(items));
        tableSplitPane.setContent(rawTable);
    }

    @FXML
    void onNext(ActionEvent event) {
        if(currentDisplayItemIndex.get() + jumpProperty.get() <= history.size() - 1){
            currentDisplayItemIndex.set(currentDisplayItemIndex.get() + jumpProperty.get());
            tripHelper();
        }
    }

    @FXML
    void onPrev(ActionEvent event) {
        if(currentDisplayItemIndex.get() - jumpProperty.get() >= 0){
            currentDisplayItemIndex.set(currentDisplayItemIndex.get() - jumpProperty.get());
            tripHelper();
        }
    }

    private void tripHelper(){
        FitnessHistoryItemDTO<TimeTableDTO> current = history.get(currentDisplayItemIndex.get());
        displayRules(current.getGenerationNumber(), current.getCurrentGenerationFitness(), current.getSolution().getSoftRulesAvg(), current.getSolution().getHardRulesAvg(), current.getSolution().getRulesScore());
        if(aspectComboBox.getValue().equals(Aspect.RAW)){
            displayRaw();
        }
        else{
            displayTable(current.getSolution(), itemComboBox.getValue());
        }
    }
}