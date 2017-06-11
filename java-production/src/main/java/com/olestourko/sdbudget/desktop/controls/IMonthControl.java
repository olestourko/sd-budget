package com.olestourko.sdbudget.desktop.controls;

import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import javafx.util.Callback;

/**
 *
 * @author oles
 */
public interface IMonthControl {

    public void refresh();
    
    public Month getMonth();

    public void setMonth(Month month);

    public void onItemAdded(Callback<BudgetItem, Month> callback);

    public void onItemRemoved(Callback<BudgetItem, Month> callback);

    public void onItemModified(Callback<BudgetItem, Month> callback);

    public void onMonthCloseModified(Callback<Month, Month> callback);

    public void onMonthCopy(Callback<Month, Month> callback);
}
