package com.olestourko.sdbudget.desktop.controls;

import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.desktop.models.BudgetItemViewModel;
import com.olestourko.sdbudget.desktop.models.MonthViewModel;
import javafx.util.Callback;

/**
 *
 * @author oles
 */
public interface IMonthControl {

    public Month getMonth();

    public void setMonth(Month month);

    public void onItemAdded(Callback<BudgetItemViewModel, Month> callback);

    public void onItemRemoved(Callback<BudgetItemViewModel, Month> callback);

    public void onItemModified(Callback<BudgetItemViewModel, Month> callback);

    public void onMonthCloseModified(Callback<MonthViewModel, Month> callback);

    public void onMonthCopy(Callback<MonthViewModel, Month> callback);
}
