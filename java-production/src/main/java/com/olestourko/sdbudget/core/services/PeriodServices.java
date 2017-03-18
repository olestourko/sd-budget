package com.olestourko.sdbudget.core.services;

import java.math.BigDecimal;
import com.olestourko.sdbudget.core.services.EstimateResult;

/**
 *
 * @author oles
 */
public class PeriodServices {

    public EstimateResult calculateEstimate(BigDecimal revenue, BigDecimal expenses, BigDecimal adjustments, BigDecimal incomeTarget, BigDecimal openingBalance, BigDecimal openingSurplus) {
        BigDecimal netIncome = revenue.subtract(expenses)
                .add(adjustments);
        BigDecimal estimatedBalance = openingBalance.add(netIncome)
                .add(openingSurplus);
        BigDecimal expectedBalance = openingBalance.add(incomeTarget);
        BigDecimal surplus = estimatedBalance.subtract(expectedBalance);

        return new EstimateResult(netIncome, estimatedBalance, expectedBalance, surplus);
    }

    public ClosingResult calculateClosing(BigDecimal incomeTarget, BigDecimal openingBalance, BigDecimal closingBalance, BigDecimal carriedSurplus) {
        BigDecimal closingBalanceTarget = openingBalance.add(incomeTarget);
        BigDecimal periodSurplus = closingBalance.subtract(closingBalanceTarget);
        BigDecimal totalSurplus = periodSurplus.add(carriedSurplus);

        return new ClosingResult(totalSurplus, periodSurplus);
    }

}
