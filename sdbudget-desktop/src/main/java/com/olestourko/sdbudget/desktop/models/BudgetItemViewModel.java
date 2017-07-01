package com.olestourko.sdbudget.desktop.models;

import com.olestourko.sdbudget.core.models.BudgetItem;
import java.math.BigDecimal;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author oles
 */
public class BudgetItemViewModel {

    private BudgetItem model = new BudgetItem();
    
    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleObjectProperty<BigDecimal> amount = new SimpleObjectProperty<BigDecimal>();
    
    public BudgetItemViewModel() {
        this("", new BigDecimal(0));
    }

    public BudgetItemViewModel(String name, BigDecimal amount) {
        this.setName(name);
        this.setAmount(amount);
    }

    public BudgetItem getModel() {
        return model;
    }
    
    public void setModel(BudgetItem model) {
        this.model = model;
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

    public SimpleStringProperty nameProperty() {
        return name;
    }

    // This is required to get the observableArrayList to update: https://docs.oracle.com/javafx/2/api/javafx/scene/control/TableView.html
    public SimpleObjectProperty<BigDecimal> amountProperty() {
        return amount;
    }
}
