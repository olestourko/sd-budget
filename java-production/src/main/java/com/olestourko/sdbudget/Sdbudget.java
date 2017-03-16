package com.olestourko.sdbudget;

import com.olestourko.sdbudget.desktop.BudgetSceneController;
import com.olestourko.sdbudget.desktop.ScratchpadSceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.core.services.EstimateResult;
import java.math.BigDecimal;
import java.math.BigInteger;
import javafx.scene.layout.AnchorPane;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import com.olestourko.sdbudget.core.dagger.Budget;
import com.olestourko.sdbudget.core.dagger.DaggerBudget;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import java.util.Calendar;

public class Sdbudget extends Application {

    private BudgetItem closingBalanceTarget = new BudgetItem("Closing Balance Target", new BigDecimal(BigInteger.ZERO));
    private BudgetItem estimatedClosingBalance = new BudgetItem("Closing Balance (Estimated)", new BigDecimal(BigInteger.ZERO));
    private BudgetItem surplus = new BudgetItem("Surplus or Defecit (Estimated)", new BigDecimal(BigInteger.ZERO));

    @Override
    public void start(Stage stage) throws Exception {
        final Budget budget = DaggerBudget.create();

        //Populate the month repository
        MonthRepository monthRepository = budget.monthRepository();
        for (int i = 0; i < 12; i++) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, i);
            monthRepository.putMonth(new Month(cal));
        }

        //Set the current month
        Month month = monthRepository.getMonth(Calendar.getInstance());
        ObservableList<BudgetItem> budgetTableItems = FXCollections.observableArrayList(
                month.revenues,
                month.expenses,
                month.adjustments,
                month.netIncomeTarget,
                month.openingBalance
        );

        BudgetSceneController budgetSceneController = budget.budgetSceneController().get();
        FXMLLoader budgetSceneLoader = new FXMLLoader(getClass().getResource("/desktop/fxml/BudgetScene.fxml"));
        budgetSceneLoader.setController(budgetSceneController);
        AnchorPane root = budgetSceneLoader.load();
        budgetSceneController.items = budgetTableItems;
        budgetSceneController.closingBalanceTarget = closingBalanceTarget;
        budgetSceneController.estimatedClosingBalance = estimatedClosingBalance;
        budgetSceneController.surplus = surplus;
        budgetSceneController.load();
        Scene budgetScene = new Scene(root);
        budgetScene.getStylesheets().add("/desktop/styles/Styles.css");

        ScratchpadSceneController scratchpadSceneController = budget.scratchpadSceneController().get();
        FXMLLoader scratchpadSceneLoader = new FXMLLoader(getClass().getResource("/desktop/fxml/ScratchpadScene.fxml"));
        scratchpadSceneLoader.setController(scratchpadSceneController);
        root = scratchpadSceneLoader.load();
        ScratchpadSceneController scratchPadSceneController = scratchpadSceneLoader.getController();
        scratchPadSceneController.load();
        Scene scratchpadScene = new Scene(root);
        scratchpadScene.getStylesheets().add("/desktop/styles/Styles.css");

        stage.setTitle("S/D Budget");
        stage.setWidth(380);
        stage.setHeight(580);
        stage.setScene(budgetScene);
        stage.show();

        budgetSceneController.scratchpadViewButton.setOnAction(event -> {
            stage.setScene(scratchpadScene);
        });

        scratchPadSceneController.budgetViewButton.setOnAction(event -> {
            stage.setScene(budgetScene);
        });

        //Listen to changes for the month's adjustments
        month.adjustments.amountProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue observable, Number oldValue, Number newValue) {
                EstimateResult result = budget.periodServices().calculateEstimate(
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
