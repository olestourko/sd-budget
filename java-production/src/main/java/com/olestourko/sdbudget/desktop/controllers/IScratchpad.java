package com.olestourko.sdbudget.desktop.controllers;

import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.desktop.models.BudgetItemViewModel;
import javafx.util.Callback;

/**
 *
 * @author oles
 */
public interface IScratchpad {

    public Month getMonth();
    
    public void setMonth(Month month);
    
    public void onAdjustmentAdded(Callback<BudgetItemViewModel, Month> callback);

    public void onAdjustmentRemoved(Callback<BudgetItemViewModel, Month> callback);

    public void onAdjustmentModified(Callback<BudgetItemViewModel, Month> callback);
}
