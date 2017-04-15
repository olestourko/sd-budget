package com.olestourko.sdbudget.desktop.models;

import java.math.BigDecimal;
import javafx.collections.ObservableList;

/**
 *
 * @author oles
 */
public interface IPeriod {

    public ObservableList<BudgetItemViewModel> getRevenues();

    public ObservableList<BudgetItemViewModel> getExpenses();

    public ObservableList<BudgetItemViewModel> getAdjustments();

    public void addRevenue(BudgetItemViewModel item);
    
    public void addExpense(BudgetItemViewModel item);
    
    public void addAdjustment(BudgetItemViewModel item);
    
    public void removeRevenue(BudgetItemViewModel item);
    
    public void removeExpense(BudgetItemViewModel item);
    
    public void removeAdjustment(BudgetItemViewModel item);
    
    public BigDecimal getTotalRevenues();

    public BigDecimal getTotalExpenses();

    public BigDecimal getTotalAdjustments();

    public BudgetItemViewModel getNetIncomeTarget();

    public BudgetItemViewModel getOpeningBalance();
    
    public BudgetItemViewModel getClosingBalance();
    
    public BudgetItemViewModel getOpeningSurplus();
    
    public BudgetItemViewModel getClosingSurplus();
    
    public BudgetItemViewModel getClosingBalanceTarget();
    
    public BudgetItemViewModel getEstimatedClosingBalance();
}
