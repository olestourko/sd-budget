package com.olestourko.sdbudget.services;

/**
 *
 * @author oles
 */
public final class ClosingResult {

    public double surplus;
    public double closingAdjustment;

    public ClosingResult(double surplus, double closingAdjustment) {
        this.surplus = surplus;
        this.closingAdjustment = closingAdjustment;
    }
}
