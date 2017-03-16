package com.olestourko.sdbudget.core.models;

import java.math.BigDecimal;

/**
 *
 * @author oles
 */
public interface IPeriod {
    public BigDecimal getTotalRevenues();
    public BigDecimal getTotalExpenses();
    public BigDecimal getTotalAdjustments();
    public BigDecimal getNetIncomeTarget();
    public BigDecimal getOpeningBalance();
}