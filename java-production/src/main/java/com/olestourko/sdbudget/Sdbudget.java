package com.olestourko.sdbudget;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.converter.DoubleStringConverter;
import com.olestourko.sdbudget.models.BudgetItem;
import com.olestourko.sdbudget.models.Month;
import com.olestourko.sdbudget.services.PeriodServices;
import com.olestourko.sdbudget.services.EstimateResult;
import java.text.SimpleDateFormat;
import javafx.scene.layout.AnchorPane;
import static javafx.application.Application.launch;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import java.util.Calendar;

public class Sdbudget extends Application {

    private PeriodServices periodServices = new PeriodServices();
    private Month month = new Month();
    private TableView budgetTable;
    private TableView scratchPadTable;
    private Button scratchPadButton = new Button("Scratchpad");
    private Button budgetButton = new Button("Budget");
    private BudgetItem closingBalanceTarget = new BudgetItem("Closing Balance Target", 0);
    private BudgetItem estimatedClosingBalance = new BudgetItem("Closing Balance (Estimated)", 0);
    private BudgetItem surplus = new BudgetItem("Surplus or Defecit (Estimated)", 0);

    private ObservableList<BudgetItem> data = FXCollections.observableArrayList(
            month.revenues,
            month.expenses,
            month.adjustments,
            month.netIncomeTarget,
            month.openingBalance,
            closingBalanceTarget,
            estimatedClosingBalance,
            surplus
    );

    @Override
    public void start(Stage stage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
        buildBudgetTable();
        buildScratchpadTable();

        // The Budget Scene
        Label label = new Label();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
        label.setText(dateFormat.format(month.calendar.getTime()));

        AnchorPane anchorPane = new AnchorPane(); //http://o7planning.org/en/10645/javafx-anchorpane-layout-tutorial
        AnchorPane.setTopAnchor(label, 5.0);
        AnchorPane.setLeftAnchor(label, 5.0);
        AnchorPane.setTopAnchor(budgetTable, 25.0);
        AnchorPane.setBottomAnchor(budgetTable, 35.0);
        AnchorPane.setLeftAnchor(budgetTable, 5.0);
        AnchorPane.setRightAnchor(budgetTable, 5.0);
        AnchorPane.setBottomAnchor(scratchPadButton, 5.0);
        AnchorPane.setRightAnchor(scratchPadButton, 5.0);
        anchorPane.getChildren().addAll(label, budgetTable, scratchPadButton);
        Scene budgetScene = new Scene(anchorPane);
        budgetScene.getStylesheets().add("/styles/Styles.css");
        stage.setScene(budgetScene);

        // The Scratchpad Scene
        label = new Label();
        label.setText(dateFormat.format(month.calendar.getTime()));

        anchorPane = new AnchorPane(); //http://o7planning.org/en/10645/javafx-anchorpane-layout-tutorial
        AnchorPane.setTopAnchor(label, 5.0);
        AnchorPane.setLeftAnchor(label, 5.0);
        AnchorPane.setTopAnchor(scratchPadTable, 25.0);
        AnchorPane.setBottomAnchor(scratchPadTable, 35.0);
        AnchorPane.setLeftAnchor(scratchPadTable, 5.0);
        AnchorPane.setRightAnchor(scratchPadTable, 5.0);
        AnchorPane.setBottomAnchor(budgetButton, 5.0);
        AnchorPane.setRightAnchor(budgetButton, 5.0);
        anchorPane.getChildren().addAll(label, scratchPadTable, budgetButton);
        Scene scratchPadScene = new Scene(anchorPane);
        scratchPadScene.getStylesheets().add("/styles/Styles.css");

        stage.setTitle("S/D Budget");
        stage.setWidth(380);
        stage.setHeight(480);

        scratchPadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.setScene(scratchPadScene);
            }
        });

        budgetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                stage.setScene(budgetScene);
            }
        });

        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private final TableView buildBudgetTable() {
        budgetTable = new TableView();
        budgetTable.setEditable(true);
        TableColumn name = new TableColumn("name");
        name.setCellValueFactory(
                new PropertyValueFactory<BudgetItem, String>("name")
        );
        TableColumn amount = new TableColumn("amount");
        amount.setCellValueFactory(
                new PropertyValueFactory<BudgetItem, Double>("amount")
        );
        //This draws the textfield when editing a table cell
        amount.setCellFactory(TextFieldTableCell.<BudgetItem, Double>forTableColumn(new DoubleStringConverter()));
        //This is a callback for edits
        amount.setOnEditCommit(new EventHandler<CellEditEvent<BudgetItem, Double>>() {
            @Override
            public void handle(CellEditEvent<BudgetItem, Double> t) {
                BudgetItem budgetItem = (BudgetItem) t.getTableView().getItems().get(t.getTablePosition().getRow());
                budgetItem.setAmount(t.getNewValue());
                EstimateResult result = periodServices.calculateEstimate(
                        month.revenues.getAmount(),
                        month.expenses.getAmount(),
                        month.adjustments.getAmount(),
                        month.netIncomeTarget.getAmount(),
                        month.openingBalance.getAmount()
                );

                closingBalanceTarget.setAmount(month.openingBalance.getAmount() + month.netIncomeTarget.getAmount());
                estimatedClosingBalance.setAmount(result.estimatedBalance);
                surplus.setAmount(result.surplus);
            }
        });

        // Update the estimates whenever the Adjustment field is updated (from Scratchpad)
        month.adjustments.amountProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue observable, Number oldValue, Number newValue) {
                EstimateResult result = periodServices.calculateEstimate(
                        month.revenues.getAmount(),
                        month.expenses.getAmount(),
                        month.adjustments.getAmount(),
                        month.netIncomeTarget.getAmount(),
                        month.openingBalance.getAmount()
                );

                closingBalanceTarget.setAmount(month.openingBalance.getAmount() + month.netIncomeTarget.getAmount());
                estimatedClosingBalance.setAmount(result.estimatedBalance);
                surplus.setAmount(result.surplus);
            }
        });

        budgetTable.setItems(data);
        budgetTable.getColumns().addAll(name, amount);

        return budgetTable;
    }

    private final TableView buildScratchpadTable() {
        scratchPadTable = new TableView();
        scratchPadTable.setEditable(true);
        TableColumn name = new TableColumn("name");
        name.setCellValueFactory(
                new PropertyValueFactory<BudgetItem, String>("name")
        );
        TableColumn amount = new TableColumn("amount");
        amount.setCellValueFactory(
                new PropertyValueFactory<BudgetItem, Double>("amount")
        );
        scratchPadTable.getColumns().addAll(name, amount);
        BudgetItem totalAdjustments = new BudgetItem("Total Adjustments", 0);
        totalAdjustments.amountProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue observable, Number oldValue, Number newValue) {
                month.adjustments.setAmount((double) newValue);
            }
        });
        scratchPadTable.getItems().addAll(new BudgetItem("Adjustment 1", 0), new BudgetItem("Adjustment 2", 0), totalAdjustments);
//        scratchPadTable.setRowFactory(new Callback<TableView<BudgetItem>, TableRow<BudgetItem>>() {
//            @Override
//            public TableRow<BudgetItem> call(TableView<BudgetItem> param) {
//                TableRow<BudgetItem> row = new TableRow<BudgetItem>();
//                row.setOnMouseClicked(new EventHandler<MouseEvent>() {
//                    @Override
//                    public void handle(MouseEvent event) {
//                        BudgetItem budgetItem = new BudgetItem();
//                        scratchPadTable.getItems().add(budgetItem);
//                    }
//                });
//
//                return row;
//            }
//
//        });
        //This draws the textfield when editing a table cell
        amount.setCellFactory(TextFieldTableCell.<BudgetItem, Double>forTableColumn(new DoubleStringConverter()));
        //This is a callback for edits
        amount.setOnEditCommit(new EventHandler<CellEditEvent<BudgetItem, Double>>() {
            @Override
            public void handle(CellEditEvent<BudgetItem, Double> t) {
                BudgetItem budgetItem = (BudgetItem) t.getTableView().getItems().get(t.getTablePosition().getRow());
                budgetItem.setAmount(t.getNewValue());
                double sum = 0;
                for (Object o : scratchPadTable.getItems()) {
                    BudgetItem item = (BudgetItem) o;
                    if (item != totalAdjustments) {
                        sum += item.getAmount();
                    }
                }
                totalAdjustments.setAmount(sum);
            }
        });
        return scratchPadTable;
    }
}
