package com.olestourko.sdbudget.services;

import java.math.BigDecimal;
import com.olestourko.sdbudget.services.EstimateResult;

/**
 *
 * @author oles
 */
public class PeriodServices {

    public EstimateResult calculateEstimate(BigDecimal revenue, BigDecimal expenses, BigDecimal adjustments, BigDecimal incomeTarget, BigDecimal openingBalance) {
        BigDecimal netIncome = revenue.subtract(expenses)
                .add(adjustments);
        BigDecimal estimatedBalance = openingBalance.add(netIncome);
        BigDecimal expectedBalance = openingBalance.add(incomeTarget);
        BigDecimal surplus = estimatedBalance.subtract(expectedBalance);

        return new EstimateResult(netIncome, estimatedBalance, expectedBalance, surplus);
    }

    public ClosingResult calculateClosing(BigDecimal incomeTarget, BigDecimal openingBalance, BigDecimal closingBalance) {
        BigDecimal closingBalanceTarget = openingBalance.add(incomeTarget);
        BigDecimal surplus = closingBalance.subtract(closingBalanceTarget);
        BigDecimal closingAdjustment = surplus;

        return new ClosingResult(surplus, closingAdjustment);
    }

}
