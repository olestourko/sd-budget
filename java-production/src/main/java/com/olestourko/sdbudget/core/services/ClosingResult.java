package com.olestourko.sdbudget.core.services;

import java.math.BigDecimal;

/**
 *
 * @author oles
 */
public final class ClosingResult {

    public final BigDecimal surplus;
    public final BigDecimal closingAdjustment;

    public ClosingResult(BigDecimal surplus, BigDecimal closingAdjustment) {
        this.surplus = surplus;
        this.closingAdjustment = closingAdjustment;
    }
}
