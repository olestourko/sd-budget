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
import com.olestourko.sdbudget.core.services.ClosingResult;
import com.olestourko.sdbudget.core.services.EstimateResult;
import com.olestourko.sdbudget.desktop.models.BudgetItem;
import java.math.BigDecimal;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javax.inject.Inject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class BudgetSceneController implements Initializable {

    @FXML
    public MonthControl monthControl;
    @FXML
    public Button scratchpadViewButton;
    @FXML
    public Button previousMonthButton;
    @FXML
    public Button nextMonthButton;
    @FXML
    public MenuBar mainMenu;

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
        mainMenu.getMenus().get(0).setOnAction(event -> {
            throw new NotImplementedException();
        });
        
        previousMonthButton.setOnAction(event -> {
            Month previousMonth = monthRepository.getPrevious(this.monthControl.getMonth());
            if (previousMonth != null) {
                nextMonthButton.disableProperty().set(false);
                monthControl.setMonth(previousMonth);
                budget.setCurrentMonth(previousMonth);

                if (monthRepository.getPrevious(this.monthControl.getMonth()) == null) {
                    previousMonthButton.disableProperty().set(true);
                }
            } else {
                previousMonthButton.disableProperty().set(true);
            }
        });
        nextMonthButton.setOnAction(event -> {
            Month nextMonth = monthRepository.getNext(this.monthControl.getMonth());
            if (nextMonth != null) {
                previousMonthButton.disableProperty().set(false);
                this.monthControl.setMonth(nextMonth);
                budget.setCurrentMonth(nextMonth);

                if (monthRepository.getNext(this.monthControl.getMonth()) == null) {
                    nextMonthButton.disableProperty().set(true);
                }
            } else {
                nextMonthButton.disableProperty().set(true);
            }
        });
        monthControl.setOnMonthChange(event -> {
            Month month = monthControl.getMonth();
            Month previousMonth = monthRepository.getPrevious(month);

            // Calculate innter-month estimates
            if (!month.getIsClosed()) {
                if (previousMonth != null) {
                    month.openingBalance.setAmount(previousMonth.estimatedClosingBalance.getAmount());
                    month.openingSurplus.setAmount(previousMonth.totalSurplus.getAmount());
                }

                EstimateResult result = periodServices.calculateEstimate(
                        month.revenues.getAmount(),
                        month.expenses.getAmount(),
                        month.adjustments.getAmount(),
                        month.netIncomeTarget.getAmount(),
                        month.openingBalance.getAmount(),
                        month.openingSurplus.getAmount()
                );

                month.closingBalanceTarget.setAmount(month.openingBalance.getAmount()
                        .add(month.netIncomeTarget.getAmount()));
                month.estimatedClosingBalance.setAmount(result.estimatedBalance.subtract(month.openingSurplus.getAmount()));
                month.totalSurplus.setAmount(result.surplus);

                BigDecimal sum = BigDecimal.ZERO;
                for (Object o : month.transactions) {
                    BudgetItem item = (BudgetItem) o;
                    sum = sum.add(item.getAmount());
                }
                month.adjustments.setAmount(sum);
            } // Calculate end of month totals
            else {
                ClosingResult result = periodServices.calculateClosing(
                        month.netIncomeTarget.getAmount(),
                        month.openingBalance.getAmount(),
                        month.closingBalance.getAmount(),
                        month.openingSurplus.getAmount()
                );

                month.estimatedClosingBalance.setAmount(month.closingBalance.getAmount());
                month.totalSurplus.setAmount(result.surplus);
                month.adjustments.setAmount(result.closingAdjustment);
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
        this.monthControl.setMonth(budget.getCurrentMonth());
    }
}
