package com.olestourko.sdbudget.services;

import com.olestourko.sdbudget.services.EstimateResult;

/**
 *
 * @author oles
 */
public class PeriodServices {

    public EstimateResult calculateEstimate(double revenue, double expenses, double adjustments, double incomeTarget, double openingBalance) {
        double netIncome = revenue - expenses + adjustments;
        double estimatedBalance = openingBalance + netIncome;
        double expectedBalance = openingBalance + incomeTarget;
        double surplus = estimatedBalance - expectedBalance;

        return new EstimateResult(netIncome, estimatedBalance, expectedBalance, surplus);
    }

    public ClosingResult calculateClosing(double incomeTarget, double openingBalance, double closingBalance) {
        double closingBalanceTarget = openingBalance + incomeTarget;
        double surplus = closingBalance - closingBalanceTarget;
        double closingAdjustment = surplus;

        return new ClosingResult(surplus, closingAdjustment);
    }

}
