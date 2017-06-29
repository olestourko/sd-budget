package com.olestourko.sdbudget.core.services;

import java.math.BigDecimal;

/**
 *
 * @author oles
 */
public final class ClosingResult {

    public final BigDecimal closingSurplus;
    public final BigDecimal closingAdjustment;

    public ClosingResult(BigDecimal closingSurplus, BigDecimal closingAdjustment) {
        this.closingSurplus = closingSurplus;
        this.closingAdjustment = closingAdjustment;
    }
}
