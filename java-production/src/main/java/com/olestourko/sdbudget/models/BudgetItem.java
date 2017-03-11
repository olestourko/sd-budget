package com.olestourko.sdbudget.models;

import java.math.BigDecimal;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author oles
 */
public class BudgetItem {

    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleObjectProperty<BigDecimal> amount = new SimpleObjectProperty<BigDecimal>();

    public BudgetItem() {
        this("", new BigDecimal(0));
    }

    public BudgetItem(String name, BigDecimal amount) {
        this.setName(name);
        this.setAmount(amount);
    }

    public String getName() {
        return this.name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public BigDecimal getAmount() {
        return this.amount.get();
    }

    public void setAmount(BigDecimal amount) {
        this.amount.set(amount);
    }

    // This is required to get the observableArrayList to update: https://docs.oracle.com/javafx/2/api/javafx/scene/control/TableView.html
    public SimpleObjectProperty<BigDecimal> amountProperty() {
        return amount;
    }
}
