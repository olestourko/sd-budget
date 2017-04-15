package com.olestourko.sdbudget.core.services;

import com.olestourko.sdbudget.core.models.Month;
import javax.inject.Inject;

/**
 *
 * @author oles
 */
public class MonthServices {

    private final PeriodServices periodServices;

    @Inject
    public MonthServices(PeriodServices periodServices) {
        this.periodServices = periodServices;
    }

    public Month calculateMonthTotals(Month month) {
        EstimateResult result = periodServices.calculateEstimate(
                month.getTotalRevenues(),
                month.getTotalExpenses(),
                month.getTotalAdjustments(),
                month.getNetIncomeTarget().getAmount(),
                month.getOpeningBalance().getAmount(),
                month.getOpeningSurplus().getAmount()
        );

        month.getClosingBalanceTarget().setAmount(result.expectedBalance);
        month.getEstimatedClosingBalance().setAmount(result.estimatedBalance);
        month.getClosingSurplus().setAmount(result.surplus);

        if (month.getIsClosed()) {
            ClosingResult closingResult = periodServices.calculateClosing(
                    month.getNetIncomeTarget().getAmount(),
                    month.getOpeningBalance().getAmount(),
                    month.getClosingBalance().getAmount(),
                    month.getOpeningSurplus().getAmount()
            );
            month.getClosingSurplus().setAmount(closingResult.closingSurplus);
        }

        return month;
    }
}
