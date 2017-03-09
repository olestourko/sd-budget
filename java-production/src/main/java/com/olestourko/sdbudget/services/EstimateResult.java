package com.olestourko.sdbudget.services;

/**
 *
 * @author oles
 */
public final class EstimateResult {

    public double netIncome;
    public double estimatedBalance;
    public double expectedBalance;
    public double surplus;

    public EstimateResult(double netIncome, double estimatedBalance, double expectedBalance, double surplus) {
        this.netIncome = netIncome;
        this.estimatedBalance = estimatedBalance;
        this.expectedBalance = expectedBalance;
        this.surplus = surplus;
    }
}
