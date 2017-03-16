package com.olestourko.sdbudget.core.models;

import com.olestourko.sdbudget.desktop.models.Month;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author oles
 */
public class Budget {

    final private SimpleObjectProperty<Month> currentMonth = new SimpleObjectProperty<Month>();

    public Month getCurrentMonth() {
        return this.currentMonth.get();
    }

    public void setCurrentMonth(Month month) {
        this.currentMonth.set(month);
    }

    public SimpleObjectProperty<Month> currentMonthProperty() {
        return this.currentMonth;
    }
}
