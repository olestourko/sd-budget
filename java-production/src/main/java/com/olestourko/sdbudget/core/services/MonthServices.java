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
        if (month.getIsClosed()) {
            ClosingResult result = periodServices.calculateClosing(
                    month.getNetIncomeTarget().getAmount(),
                    month.getOpeningBalance().getAmount(),
                    month.getClosingBalance().getAmount(),
                    month.getOpeningSurplus().getAmount()
            );
            month.getClosingSurplus().setAmount(result.closingSurplus);
        } else {
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
        }

        return month;
    }
}
