package com.olestourko.sdbudget.desktop;

import com.olestourko.sdbudget.desktop.controls.MonthControl;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.olestourko.sdbudget.desktop.models.Month;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.core.services.PeriodServices;
import com.olestourko.sdbudget.core.models.Budget;
import com.olestourko.sdbudget.core.services.EstimateResult;
import javafx.scene.control.Button;
import javax.inject.Inject;

public class BudgetSceneController implements Initializable {

    @FXML
    public MonthControl monthControl;
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
            Month previousMonth = monthRepository.getPrevious(this.monthControl.getMonth());
            if (previousMonth != null) {
                nextMonthButton.disableProperty().set(false);
                monthControl.setMonth(previousMonth);
            } else {
                previousMonthButton.disableProperty().set(true);
            }
        });
        nextMonthButton.setOnAction(event -> {
            Month nextMonth = monthRepository.getNext(this.monthControl.getMonth());
            if (nextMonth != null) {
                previousMonthButton.disableProperty().set(false);
                this.monthControl.setMonth(nextMonth);
            } else {
                nextMonthButton.disableProperty().set(true);
            }
        });
        monthControl.setOnMonthChange(event -> {
            // Calculate innter-month estimates
            Month month = monthControl.getMonth();
            EstimateResult result = periodServices.calculateEstimate(
                    month.revenues.getAmount(),
                    month.expenses.getAmount(),
                    month.adjustments.getAmount(),
                    month.netIncomeTarget.getAmount(),
                    month.openingBalance.getAmount()
            );

            month.closingBalanceTarget.setAmount(month.openingBalance.getAmount()
                    .add(month.netIncomeTarget.getAmount()));
            month.estimatedClosingBalance.setAmount(result.estimatedBalance);
            month.surplus.setAmount(result.surplus);
        });
    }

    public void load() {
        if (monthRepository.getPrevious(budget.getCurrentMonth()) == null) {
            previousMonthButton.disableProperty().set(true);
        }
        if (monthRepository.getNext(budget.getCurrentMonth()) == null) {
            nextMonthButton.disableProperty().set(true);
        }
        this.monthControl.setMonth(budget.getCurrentMonth());
    }
}
