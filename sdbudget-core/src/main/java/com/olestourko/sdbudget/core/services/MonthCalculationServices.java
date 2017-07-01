package com.olestourko.sdbudget.core.services;

import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.core.services.ClosingResult;
import com.olestourko.sdbudget.core.services.EstimateResult;
import com.olestourko.sdbudget.core.services.PeriodCalculationServices;

/**
 *
 * @author oles
 */
public class MonthCalculationServices {

    final PeriodCalculationServices periodCalculationServices;
    final MonthRepository monthRepository;

    public MonthCalculationServices(PeriodCalculationServices periodServices, MonthRepository monthRepository) {
        this.periodCalculationServices = periodServices;
        this.monthRepository = monthRepository;
    }

    public Month calculateMonthTotals(Month month) {
        EstimateResult result = periodCalculationServices.calculateEstimate(
                month.getTotalRevenues(),
                month.getTotalExpenses(),
                month.getTotalAdjustments(),
                month.getDebtRepayments().getAmount(),
                month.getInvestmentOutflows().getAmount(),
                month.getNetIncomeTarget().getAmount(),
                month.getOpeningBalance().getAmount(),
                month.getOpeningSurplus().getAmount()
        );

        month.getClosingBalanceTarget().setAmount(result.expectedBalance);
        month.getEstimatedClosingBalance().setAmount(result.estimatedBalance.subtract(month.getOpeningSurplus().getAmount())); // Adding the opening surplus doesn't make sense in the context of multiple months
        month.getClosingSurplus().setAmount(result.surplus);

        if (month.getIsClosed()) {
            ClosingResult closingResult = periodCalculationServices.calculateClosing(
                    month.getNetIncomeTarget().getAmount(),
                    month.getDebtRepayments().getAmount(),
                    month.getInvestmentOutflows().getAmount(),
                    month.getOpeningBalance().getAmount(),
                    month.getClosingBalance().getAmount(),
                    month.getOpeningSurplus().getAmount()
            );
            month.getClosingSurplus().setAmount(closingResult.closingSurplus);
        }

        return month;
    }

    public void recalculateMonths(Month startingMonth) {
        Month currentMonth = startingMonth;
        while (currentMonth != null) {
            Month previousMonth = monthRepository.getPrevious(currentMonth);
            if (previousMonth != null) {
                if (previousMonth.getIsClosed()) {
                    currentMonth.getOpeningBalance().setAmount(previousMonth.getClosingBalance().getAmount());
                } else {
                    currentMonth.getOpeningBalance().setAmount(previousMonth.getEstimatedClosingBalance().getAmount());
                }
                currentMonth.getOpeningSurplus().setAmount(previousMonth.getClosingSurplus().getAmount());
            }
            calculateMonthTotals(currentMonth);
            currentMonth = monthRepository.getNext(currentMonth);
        }
    }
}
