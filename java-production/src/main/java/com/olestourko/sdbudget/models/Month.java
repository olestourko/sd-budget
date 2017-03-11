/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olestourko.sdbudget.models;

import java.math.BigDecimal;
import com.olestourko.sdbudget.models.BudgetItem;
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
    public Calendar calendar;

    public Month() {
        revenues = new BudgetItem("Revenues", 0);
        expenses = new BudgetItem("Expenses", 0);
        adjustments = new BudgetItem("Adjustments", 0);
        netIncomeTarget = new BudgetItem("Net Income Target", 0);
        openingBalance = new BudgetItem("Opening Balance", 0);
        calendar = Calendar.getInstance();
    }

    // Implementions for IPeriod methods
    public BigDecimal getTotalRevenues() {
        return new BigDecimal(revenues.getAmount());
    }

    public BigDecimal getTotalExpenses() {
        return new BigDecimal(expenses.getAmount());
    }

    public BigDecimal getTotalAdjustments() {
        return new BigDecimal(adjustments.getAmount());
    }

    public BigDecimal getNetIncomeTarget() {
        return new BigDecimal(netIncomeTarget.getAmount());
    }

    public BigDecimal getOpeningBalance() {
        return new BigDecimal(openingBalance.getAmount());
    }
}
