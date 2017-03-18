package com.olestourko.sdbudget.desktop;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.olestourko.sdbudget.desktop.models.Month;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.core.services.PeriodServices;
import com.olestourko.sdbudget.core.models.Budget;
import javafx.scene.control.Button;
import javax.inject.Inject;

public class BudgetSceneController implements Initializable {

    @FXML
    public MonthComponent monthComponent;
    @FXML
    public Button scratchpadViewButton;
    @FXML
    public Button previousMonthButton;
    @FXML
    public Button nextMonthButton;

    final private PeriodServices periodServices;
    final private MonthRepository monthRepository;
    final private Budget budget;

    @Inject
    BudgetSceneController(PeriodServices periodServices, MonthRepository monthRepository, Budget budget) {
        this.periodServices = periodServices;
        this.monthRepository = monthRepository;
        this.budget = budget;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        previousMonthButton.setOnAction(event -> {
            Month previousMonth = monthRepository.getPrevious(budget.getCurrentMonth());
            if (previousMonth != null) {
                nextMonthButton.disableProperty().set(false);
            } else {
                previousMonthButton.disableProperty().set(true);
            }
        });
        nextMonthButton.setOnAction(event -> {
            Month nextMonth = monthRepository.getNext(budget.getCurrentMonth());
            if (nextMonth != null) {
                previousMonthButton.disableProperty().set(false);
            } else {
                nextMonthButton.disableProperty().set(true);
            }
        });
    }

    public void load() {
        if (monthRepository.getPrevious(budget.getCurrentMonth()) == null) {
            previousMonthButton.disableProperty().set(true);
        }
        if (monthRepository.getNext(budget.getCurrentMonth()) == null) {
            nextMonthButton.disableProperty().set(true);
        }
        this.monthComponent.setMonth(budget.getCurrentMonth());
    }

//    private void calculate() {
//        Month month = budget.getCurrentMonth();
//        if (closingBalance.getAmount().equals(BigDecimal.ZERO)) {
//            // Calculate innter-month estimates
//            EstimateResult result = periodServices.calculateEstimate(
//                    month.revenues.getAmount(),
//                    month.expenses.getAmount(),
//                    month.adjustments.getAmount(),
//                    month.netIncomeTarget.getAmount(),
//                    month.openingBalance.getAmount()
//            );
//
//            closingBalanceTarget.setAmount(month.openingBalance.getAmount()
//                    .add(month.netIncomeTarget.getAmount()));
//            estimatedClosingBalance.setAmount(result.estimatedBalance);
//            surplus.setAmount(result.surplus);
//        } else {
//            // Calculate closing balances
//            ClosingResult result = periodServices.calculateClosing(month.netIncomeTarget.getAmount(), month.openingBalance.getAmount(), closingBalance.getAmount());
//            month.adjustments.setAmount(result.closingAdjustment);
//            estimatedClosingBalance.setAmount(closingBalance.getAmount());
//            surplus.setAmount(result.surplus);
//        }
//    }
}
