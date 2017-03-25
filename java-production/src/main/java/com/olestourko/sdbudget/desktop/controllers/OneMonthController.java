package com.olestourko.sdbudget.desktop.controllers;

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
import javafx.beans.property.SimpleObjectProperty;
import javax.inject.Inject;

public class OneMonthController implements Initializable {

    @FXML
    public MonthControl monthControl;

    final private PeriodServices periodServices;
    final private MonthRepository monthRepository;
    final private Budget budget;

    @Inject
    OneMonthController(PeriodServices periodServices, MonthRepository monthRepository, Budget budget) {
        this.periodServices = periodServices;
        this.monthRepository = monthRepository;
        this.budget = budget;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.budget.currentMonthProperty().addListener(month -> {
            SimpleObjectProperty<Month> monthProperty = (SimpleObjectProperty<Month>) month;
            this.monthControl.setMonth(monthProperty.getValue());
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
                        month.getTotalRevenues(),
                        month.getTotalExpenses(),
                        month.getTotalAdjustments(),
                        month.netIncomeTarget.getAmount(),
                        month.openingBalance.getAmount(),
                        month.openingSurplus.getAmount()
                );

                month.closingBalanceTarget.setAmount(month.openingBalance.getAmount()
                        .add(month.netIncomeTarget.getAmount()));
                month.estimatedClosingBalance.setAmount(result.estimatedBalance.subtract(month.openingSurplus.getAmount()));
                month.totalSurplus.setAmount(result.surplus);

                BigDecimal sum = BigDecimal.ZERO;
                for (Object o : month.getAdjustments()) {
                    BudgetItem item = (BudgetItem) o;
                    sum = sum.add(item.getAmount());
                }
//                month.adjustments.setAmount(sum);
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
//                month.adjustments.setAmount(result.closingAdjustment);
            }
        });
    }

    public void load() {
        this.monthControl.setMonth(budget.getCurrentMonth());
    }
}
