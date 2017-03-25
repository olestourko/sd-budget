/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olestourko.sdbudget.desktop.models;

import java.math.BigDecimal;
import com.olestourko.sdbudget.desktop.models.BudgetItem;
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

    protected final ObservableList<BudgetItem> revenues = FXCollections.observableArrayList();
    protected final ObservableList<BudgetItem> expenses = FXCollections.observableArrayList();
    protected final ObservableList<BudgetItem> adjustments = FXCollections.observableArrayList();

    public final BudgetItem netIncomeTarget = new BudgetItem("Net Income Target", new BigDecimal(BigInteger.ZERO));

    public final BudgetItem openingBalance = new BudgetItem("Opening Balance", new BigDecimal(BigInteger.ZERO));
    public final BudgetItem openingSurplus = new BudgetItem("Carried Surplus", new BigDecimal(BigInteger.ZERO));

    public final BudgetItem closingBalanceTarget = new BudgetItem("Closing Balance Target", new BigDecimal(BigInteger.ZERO));
    public final BudgetItem estimatedClosingBalance = new BudgetItem("Closing Balance (Estimated)", new BigDecimal(BigInteger.ZERO));
    public final BudgetItem totalSurplus = new BudgetItem("Total Surplus", new BigDecimal(BigInteger.ZERO));
    public final BudgetItem closingBalance = new BudgetItem("Closing Balance", BigDecimal.ZERO);

    final public Calendar calendar;

    private final SimpleBooleanProperty isClosed = new SimpleBooleanProperty();

    public Month(Calendar calendar) {
        this.calendar = calendar;
    }

    @Override
    public BigDecimal getTotalRevenues() {
        return revenues.stream().map(BudgetItem::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalExpenses() {
        return expenses.stream().map(BudgetItem::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalAdjustments() {
        return adjustments.stream().map(BudgetItem::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getNetIncomeTarget() {
        return netIncomeTarget.getAmount();
    }

    @Override
    public BigDecimal getOpeningBalance() {
        return openingBalance.getAmount();
    }

    @Override
    public ObservableList<BudgetItem> getRevenues() {
        return revenues;
    }

    @Override
    public ObservableList<BudgetItem> getExpenses() {
        return expenses;
    }

    @Override
    public ObservableList<BudgetItem> getAdjustments() {
        return adjustments;
    }

    @Override
    public void addRevenue(BudgetItem item) {
        revenues.add(item);
    }

    @Override
    public void addExpense(BudgetItem item) {
        expenses.add(item);
    }

    @Override
    public void addAdjustment(BudgetItem item) {
        adjustments.add(item);
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
