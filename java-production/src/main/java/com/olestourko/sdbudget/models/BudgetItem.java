package com.olestourko.sdbudget.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 *
 * @author oles
 */
public class BudgetItem {

    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleDoubleProperty amount = new SimpleDoubleProperty();

    public BudgetItem() {
        this("", 0);
    }

    public BudgetItem(String name, double amount) {
        this.setName(name);
        this.setAmount(amount);
    }

    public String getName() {
        return this.name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public double getAmount() {
        return this.amount.get();
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }

    // This is required to get the observableArrayList to update: https://docs.oracle.com/javafx/2/api/javafx/scene/control/TableView.html
    public SimpleDoubleProperty amountProperty() {
        return amount;
    }
}
