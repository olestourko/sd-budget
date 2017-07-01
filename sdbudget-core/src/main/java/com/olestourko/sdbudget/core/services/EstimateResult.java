package com.olestourko.sdbudget.core.services;

import java.math.BigDecimal;

/**
 *
 * @author oles
 */
public final class EstimateResult {

    public final BigDecimal netIncome;
    public final BigDecimal estimatedBalance;
    public final BigDecimal expectedBalance;
    public final BigDecimal surplus;

    public EstimateResult(BigDecimal netIncome, BigDecimal estimatedBalance, BigDecimal expectedBalance, BigDecimal surplus) {
        this.netIncome = netIncome;
        this.estimatedBalance = estimatedBalance;
        this.expectedBalance = expectedBalance;
        this.surplus = surplus;
    }
}
