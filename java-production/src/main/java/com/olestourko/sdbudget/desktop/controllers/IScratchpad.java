package com.olestourko.sdbudget.desktop.controllers;

import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import javafx.util.Callback;

/**
 *
 * @author oles
 */
public interface IScratchpad {

    public Month getMonth();
    
    public void setMonth(Month month);
    
    public void onAdjustmentAdded(Callback<BudgetItem, Month> callback);

    public void onAdjustmentRemoved(Callback<BudgetItem, Month> callback);

    public void onAdjustmentModified(Callback<BudgetItem, Month> callback);
}
