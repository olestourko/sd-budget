/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olestourko.sdbudget.desktop.models;

import java.math.BigDecimal;
import com.olestourko.sdbudget.desktop.models.BudgetItem;
import com.olestourko.sdbudget.core.models.IPeriod;
import java.math.BigInteger;
import java.util.Calendar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author oles
 */
public class Month implements IPeriod {

    final public BudgetItem revenues = new BudgetItem("Revenues", new BigDecimal(BigInteger.ZERO));
    final public BudgetItem expenses = new BudgetItem("Expenses", new BigDecimal(BigInteger.ZERO));
    final public BudgetItem adjustments = new BudgetItem("Adjustments", new BigDecimal(BigInteger.ZERO));
    final public BudgetItem netIncomeTarget = new BudgetItem("Net Income Target", new BigDecimal(BigInteger.ZERO));
    final public BudgetItem openingBalance = new BudgetItem("Opening Balance", new BigDecimal(BigInteger.ZERO));
    
    final public BudgetItem closingBalanceTarget = new BudgetItem("Closing Balance Target", new BigDecimal(BigInteger.ZERO));
    final public BudgetItem estimatedClosingBalance = new BudgetItem("Closing Balance (Estimated)", new BigDecimal(BigInteger.ZERO));
    final public BudgetItem surplus = new BudgetItem("Surplus or Defecit (Estimated)", new BigDecimal(BigInteger.ZERO));
    final public BudgetItem closingBalance = new BudgetItem("Closing Balance", BigDecimal.ZERO);
    
    public ObservableList transactions = FXCollections.observableArrayList(); //Adjustment transactions
    final public Calendar calendar;
    
    public Month(Calendar calendar) {
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
