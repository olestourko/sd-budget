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
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author oles
 */
public class Month implements IPeriod {

    public final BudgetItem revenues = new BudgetItem("Revenues", new BigDecimal(BigInteger.ZERO));
    public final BudgetItem expenses = new BudgetItem("Expenses", new BigDecimal(BigInteger.ZERO));
    public final BudgetItem adjustments = new BudgetItem("Adjustments", new BigDecimal(BigInteger.ZERO));
    public final BudgetItem netIncomeTarget = new BudgetItem("Net Income Target", new BigDecimal(BigInteger.ZERO));
    
    public final BudgetItem openingBalance = new BudgetItem("Opening Balance", new BigDecimal(BigInteger.ZERO));
    public final BudgetItem openingSurplus = new BudgetItem("Carried Surplus", new BigDecimal(BigInteger.ZERO));
    
    public final BudgetItem closingBalanceTarget = new BudgetItem("Closing Balance Target", new BigDecimal(BigInteger.ZERO));
    public final BudgetItem estimatedClosingBalance = new BudgetItem("Closing Balance (Estimated)", new BigDecimal(BigInteger.ZERO));
    public final BudgetItem totalSurplus = new BudgetItem("Total Surplus", new BigDecimal(BigInteger.ZERO));
    public final BudgetItem closingBalance = new BudgetItem("Closing Balance", BigDecimal.ZERO);
    
    public ObservableList transactions = FXCollections.observableArrayList(); //Adjustment transactions
    final public Calendar calendar;
    
    private final SimpleBooleanProperty isClosed = new SimpleBooleanProperty();
    
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
    
    // Properties
    public void setIsClosed(boolean value) {
        this.isClosed.set(value);
    }
    
    public boolean getIsClosed() {
        return this.isClosed.getValue();
    }
    
    public SimpleBooleanProperty isClosedProperty() {
        return this.isClosed;
    }
}
