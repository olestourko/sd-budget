package com.olestourko.sdbudget;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.olestourko.sdbudget.models.BudgetItem;
import com.olestourko.sdbudget.models.Month;
import com.olestourko.sdbudget.services.PeriodServices;
import com.olestourko.sdbudget.services.EstimateResult;
import java.math.BigDecimal;
import java.math.BigInteger;
import javafx.scene.layout.AnchorPane;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import static javafx.application.Application.launch;

public class Sdbudget extends Application {

    private PeriodServices periodServices = new PeriodServices();
    private Month month = new Month();
    private TableView scratchPadTable;
    private Button budgetButton = new Button("Budget");
    private BudgetItem closingBalanceTarget = new BudgetItem("Closing Balance Target", new BigDecimal(BigInteger.ZERO));
    private BudgetItem estimatedClosingBalance = new BudgetItem("Closing Balance (Estimated)", new BigDecimal(BigInteger.ZERO));
    private BudgetItem surplus = new BudgetItem("Surplus or Defecit (Estimated)", new BigDecimal(BigInteger.ZERO));

    private ObservableList<BudgetItem> budgetTableItems = FXCollections.observableArrayList(
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
        FXMLLoader budgetSceneLoader = new FXMLLoader(getClass().getResource("/fxml/BudgetScene.fxml"));
        AnchorPane root = budgetSceneLoader.load();
        BudgetSceneController budgetSceneController = budgetSceneLoader.getController();
        budgetSceneController.month = month;
        budgetSceneController.items = budgetTableItems;
        budgetSceneController.closingBalanceTarget = closingBalanceTarget;
        budgetSceneController.estimatedClosingBalance = estimatedClosingBalance;
        budgetSceneController.surplus = surplus;
        budgetSceneController.periodServices = periodServices;
        budgetSceneController.load();
        Scene budgetScene = new Scene(root);
        budgetScene.getStylesheets().add("/styles/Styles.css");

        FXMLLoader scratchpadSceneLoader = new FXMLLoader(getClass().getResource("/fxml/ScratchpadScene.fxml"));
        root = scratchpadSceneLoader.load();
        ScratchpadSceneController scratchPadSceneController = scratchpadSceneLoader.getController();
        scratchPadSceneController.month = month;
        scratchPadSceneController.load();
        Scene scratchpadScene = new Scene(root);
        scratchpadScene.getStylesheets().add("/styles/Styles.css");

        stage.setTitle("S/D Budget");
        stage.setWidth(380);
        stage.setHeight(480);
        stage.setScene(budgetScene);
        stage.show();

        budgetSceneController.scratchpadViewButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.setScene(scratchpadScene);
            }
        });

        scratchPadSceneController.budgetViewButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                stage.setScene(budgetScene);
            }
        });

        //Listen to changes for the month's adjustments
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

                closingBalanceTarget.setAmount(month.openingBalance.getAmount()
                        .add(month.netIncomeTarget.getAmount()));
                estimatedClosingBalance.setAmount(result.estimatedBalance);
                surplus.setAmount(result.surplus);
            }
        });
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
