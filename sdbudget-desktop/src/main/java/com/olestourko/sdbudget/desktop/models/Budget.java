package com.olestourko.sdbudget.desktop.models;

import com.olestourko.sdbudget.core.models.Month;
import javafx.beans.property.SimpleObjectProperty;
import javax.inject.Inject;

/**
 *
 * @author oles
 */
public class Budget {

    private final SimpleObjectProperty<Month> currentMonth = new SimpleObjectProperty<Month>();

    @Inject
    public Budget() {

    }

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
