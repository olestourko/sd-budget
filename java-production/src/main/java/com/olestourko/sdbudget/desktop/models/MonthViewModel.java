/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olestourko.sdbudget.desktop.models;

import java.math.BigDecimal;
import com.olestourko.sdbudget.desktop.models.BudgetItemViewModel;
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
    private final Callback<BudgetItemViewModel, Observable[]> extractor = new Callback<BudgetItemViewModel, Observable[]>() {
        @Override
        public Observable[] call(BudgetItemViewModel item) {
            return new Observable[]{
                item.nameProperty(), item.amountProperty()
            };
        }
    };
    
    private Month model;
    
    protected final ObservableList<BudgetItemViewModel> revenues = FXCollections.observableArrayList(extractor);
    protected final ObservableList<BudgetItemViewModel> expenses = FXCollections.observableArrayList(extractor);
    protected final ObservableList<BudgetItemViewModel> adjustments = FXCollections.observableArrayList(extractor);

    public final BudgetItemViewModel netIncomeTarget = new BudgetItemViewModel("Net Income Target", new BigDecimal(BigInteger.ZERO));
    public final BudgetItemViewModel openingBalance = new BudgetItemViewModel("Opening Balance", new BigDecimal(BigInteger.ZERO));
    public final BudgetItemViewModel openingSurplus = new BudgetItemViewModel("Carried Surplus", new BigDecimal(BigInteger.ZERO));
    public final BudgetItemViewModel closingBalanceTarget = new BudgetItemViewModel("Closing Balance Target", new BigDecimal(BigInteger.ZERO));
    public final BudgetItemViewModel estimatedClosingBalance = new BudgetItemViewModel("Closing Balance (Estimated)", new BigDecimal(BigInteger.ZERO));
    public final BudgetItemViewModel totalSurplus = new BudgetItemViewModel("Total Surplus", new BigDecimal(BigInteger.ZERO));
    public final BudgetItemViewModel closingBalance = new BudgetItemViewModel("Closing Balance", BigDecimal.ZERO);

    final public Calendar calendar;

    private final SimpleBooleanProperty isClosed = new SimpleBooleanProperty();
   
    public MonthViewModel(Calendar calendar) {
        this.calendar = calendar;
    }
    
    public Month getModel() {
        return model;
    }
    
    public void setMonthCoreModel(Month model) {
        this.model = model;
    }
    
    @Override
    public BigDecimal getTotalRevenues() {
        return revenues.stream().map(BudgetItemViewModel::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalExpenses() {
        return expenses.stream().map(BudgetItemViewModel::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalAdjustments() {
        return adjustments.stream().map(BudgetItemViewModel::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
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
    public ObservableList<BudgetItemViewModel> getRevenues() {
        return revenues;
    }

    @Override
    public ObservableList<BudgetItemViewModel> getExpenses() {
        return expenses;
    }

    @Override
    public ObservableList<BudgetItemViewModel> getAdjustments() {
        return adjustments;
    }

    @Override
    public void removeRevenue(BudgetItemViewModel item) {
        revenues.remove(item);
    }

    @Override
    public void removeExpense(BudgetItemViewModel item) {
        expenses.remove(item);
    }

    @Override
    public void removeAdjustment(BudgetItemViewModel item) {
        adjustments.remove(item);
    }

    @Override
    public void addRevenue(BudgetItemViewModel item) {
        revenues.add(item);
    }

    @Override
    public void addExpense(BudgetItemViewModel item) {
        expenses.add(item);
    }

    @Override
    public void addAdjustment(BudgetItemViewModel item) {
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
