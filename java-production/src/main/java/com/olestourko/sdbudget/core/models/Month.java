/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olestourko.sdbudget.core.models;

import java.math.BigDecimal;
import com.olestourko.sdbudget.core.models.BudgetItem;
import java.math.BigInteger;
import java.util.Calendar;

/**
 *
 * @author oles
 */
public class Month implements IPeriod {

    public BudgetItem revenues;
    public BudgetItem expenses;
    public BudgetItem adjustments;
    public BudgetItem netIncomeTarget;
    public BudgetItem openingBalance;
    final public Calendar calendar;

    public Month(Calendar calendar) {
        revenues = new BudgetItem("Revenues", new BigDecimal(BigInteger.ZERO));
        expenses = new BudgetItem("Expenses", new BigDecimal(BigInteger.ZERO));
        adjustments = new BudgetItem("Adjustments", new BigDecimal(BigInteger.ZERO));
        netIncomeTarget = new BudgetItem("Net Income Target", new BigDecimal(BigInteger.ZERO));
        openingBalance = new BudgetItem("Opening Balance", new BigDecimal(BigInteger.ZERO));
        this.calendar = calendar;
    }

    // Implementions for IPeriod methods
    public BigDecimal getTotalRevenues() {
        return revenues.getAmount();
    }

    public BigDecimal getTotalExpenses() {
        return expenses.getAmount();
    }

    public BigDecimal getTotalAdjustments() {
        return adjustments.getAmount();
    }

    public BigDecimal getNetIncomeTarget() {
        return netIncomeTarget.getAmount();
    }

    public BigDecimal getOpeningBalance() {
        return openingBalance.getAmount();
    }
}
