package com.olestourko.sdbudget.desktop.models;

import java.math.BigDecimal;
import javafx.collections.ObservableList;

/**
 *
 * @author oles
 */
public interface IPeriod {

    public ObservableList<BudgetItem> getRevenues();

    public ObservableList<BudgetItem> getExpenses();

    public ObservableList<BudgetItem> getAdjustments();

    public void addRevenue(BudgetItem item);
    
    public void addExpense(BudgetItem item);
    
    public void addAdjustment(BudgetItem item);
    
    public void removeRevenue(BudgetItem item);
    
    public void removeExpense(BudgetItem item);
    
    public void removeAdjustment(BudgetItem item);
    
    public BigDecimal getTotalRevenues();

    public BigDecimal getTotalExpenses();

    public BigDecimal getTotalAdjustments();

    public BigDecimal getNetIncomeTarget();

    public BigDecimal getOpeningBalance();
}
