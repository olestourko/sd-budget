package com.olestourko.sdbudget.core.models;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author oles
 */
public class BudgetItem extends Model implements Serializable {

    private String name;
    private BigDecimal amount;

    public BudgetItem() {

    }

    public BudgetItem(String name, BigDecimal amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
