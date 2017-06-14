package com.olestourko.sdbudget.desktop.controls;

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

    public void onMonthCopy(Callback<Month, Month> callback);
}
