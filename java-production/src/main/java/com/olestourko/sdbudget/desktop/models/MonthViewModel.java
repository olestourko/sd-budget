/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olestourko.sdbudget.desktop.models;

import java.math.BigDecimal;
import com.olestourko.sdbudget.desktop.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import java.math.BigInteger;
import java.util.Calendar;
import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;

/**
 *
 * @author oles
 */
public class MonthViewModel implements IPeriod {

    /*
    An extractor is used to detect changes within list items (instead of just detecting added/removed items from the observable list)
    https://gist.github.com/andytill/3116203
    http://docs.oracle.com/javase/8/javafx/api/javafx/collections/FXCollections.html#observableArrayList-javafx.util.Callback-
     */
    private final Callback<BudgetItem, Observable[]> extractor = new Callback<BudgetItem, Observable[]>() {
        @Override
        public Observable[] call(BudgetItem item) {
            return new Observable[]{
                item.nameProperty(), item.amountProperty()
            };
        }
    };
    
    private Month monthCoreModel;
    
    protected final ObservableList<BudgetItem> revenues = FXCollections.observableArrayList(extractor);
    protected final ObservableList<BudgetItem> expenses = FXCollections.observableArrayList(extractor);
    protected final ObservableList<BudgetItem> adjustments = FXCollections.observableArrayList(extractor);

    public final BudgetItem netIncomeTarget = new BudgetItem("Net Income Target", new BigDecimal(BigInteger.ZERO));
    public final BudgetItem openingBalance = new BudgetItem("Opening Balance", new BigDecimal(BigInteger.ZERO));
    public final BudgetItem openingSurplus = new BudgetItem("Carried Surplus", new BigDecimal(BigInteger.ZERO));
    public final BudgetItem closingBalanceTarget = new BudgetItem("Closing Balance Target", new BigDecimal(BigInteger.ZERO));
    public final BudgetItem estimatedClosingBalance = new BudgetItem("Closing Balance (Estimated)", new BigDecimal(BigInteger.ZERO));
    public final BudgetItem totalSurplus = new BudgetItem("Total Surplus", new BigDecimal(BigInteger.ZERO));
    public final BudgetItem closingBalance = new BudgetItem("Closing Balance", BigDecimal.ZERO);

    final public Calendar calendar;

    private final SimpleBooleanProperty isClosed = new SimpleBooleanProperty();
   
    public MonthViewModel(Calendar calendar) {
        this.calendar = calendar;
    }
    
    public Month getMonthCoreModel() {
        return monthCoreModel;
    }
    
    public void setMonthCoreModel(Month model) {
        this.monthCoreModel = model;
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
    public void removeRevenue(BudgetItem item) {
        revenues.remove(item);
    }

    @Override
    public void removeExpense(BudgetItem item) {
        expenses.remove(item);
    }

    @Override
    public void removeAdjustment(BudgetItem item) {
        adjustments.remove(item);
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
