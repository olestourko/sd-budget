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
import com.olestourko.sdbudget.services.PeriodServices;
import com.olestourko.sdbudget.services.EstimateResult;
import javafx.scene.layout.AnchorPane;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;

public class Sdbudget extends Application {

    private PeriodServices periodServices = new PeriodServices();
    private TableView budgetTable = new TableView();
    private Button scratchPadButton = new Button("Scratchpad");
    private Button budgetButton = new Button("Budget");
    private TableView scratchPadTable = new TableView();
    private BudgetItem revenues = new BudgetItem("Revenues", 2000);
    private BudgetItem expenses = new BudgetItem("Expenses", 1000);
    private BudgetItem adjustments = new BudgetItem("Adjustments", 0);
    private BudgetItem netIncomeTarget = new BudgetItem("Net Income Target", 500);
    private BudgetItem openingBalance = new BudgetItem("Opening Balance", 0);
    private BudgetItem closingBalanceTarget = new BudgetItem("Closing Balance Target", 0);
    private BudgetItem estimatedClosingBalance = new BudgetItem("Closing Balance (Estimated)", 0);
    private BudgetItem surplus = new BudgetItem("Surplus or Defecit (Estimated)", 0);

    private ObservableList<BudgetItem> data = FXCollections.observableArrayList(
            revenues,
            expenses,
            adjustments,
            netIncomeTarget,
            openingBalance,
            closingBalanceTarget,
            estimatedClosingBalance,
            surplus
    );

    @Override
    public void start(Stage stage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));

        // The Budget Scene
        AnchorPane anchorPane = new AnchorPane(); //http://o7planning.org/en/10645/javafx-anchorpane-layout-tutorial
        AnchorPane.setTopAnchor(budgetTable, 5.0);
        AnchorPane.setBottomAnchor(budgetTable, 35.0);
        AnchorPane.setLeftAnchor(budgetTable, 5.0);
        AnchorPane.setRightAnchor(budgetTable, 5.0);
        AnchorPane.setBottomAnchor(scratchPadButton, 5.0);
        AnchorPane.setRightAnchor(scratchPadButton, 5.0);
        anchorPane.getChildren().addAll(budgetTable, scratchPadButton);
        Scene budgetScene = new Scene(anchorPane);
        budgetScene.getStylesheets().add("/styles/Styles.css");
        stage.setScene(budgetScene);

        // The Scratchpad Scene
        anchorPane = new AnchorPane(); //http://o7planning.org/en/10645/javafx-anchorpane-layout-tutorial
        AnchorPane.setTopAnchor(scratchPadTable, 5.0);
        AnchorPane.setBottomAnchor(scratchPadTable, 35.0);
        AnchorPane.setLeftAnchor(scratchPadTable, 5.0);
        AnchorPane.setRightAnchor(scratchPadTable, 5.0);
        AnchorPane.setBottomAnchor(budgetButton, 5.0);
        AnchorPane.setRightAnchor(budgetButton, 5.0);
        anchorPane.getChildren().addAll(scratchPadTable, budgetButton);
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
                        revenues.getAmount(),
                        expenses.getAmount(),
                        adjustments.getAmount(),
                        netIncomeTarget.getAmount(),
                        openingBalance.getAmount()
                );

                estimatedClosingBalance.setAmount(result.estimatedBalance);
                surplus.setAmount(result.surplus);
            }
        });

        budgetTable.setItems(data);
        budgetTable.getColumns().addAll(name, amount);

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

}
