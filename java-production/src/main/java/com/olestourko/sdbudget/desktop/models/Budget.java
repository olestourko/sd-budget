package com.olestourko.sdbudget.desktop.models;

import com.olestourko.sdbudget.desktop.models.MonthViewModel;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author oles
 */
public class Budget {

    final private SimpleObjectProperty<MonthViewModel> currentMonth = new SimpleObjectProperty<MonthViewModel>();

    public MonthViewModel getCurrentMonth() {
        return this.currentMonth.get();
    }

    public void setCurrentMonth(MonthViewModel month) {
        this.currentMonth.set(month);
    }

    public SimpleObjectProperty<MonthViewModel> currentMonthProperty() {
        return this.currentMonth;
    }
}
