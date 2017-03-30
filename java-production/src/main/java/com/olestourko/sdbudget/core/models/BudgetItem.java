package com.olestourko.sdbudget.core.models;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author oles
 */
public class BudgetItem implements Serializable {

    private int id;
    private String name;
    private BigDecimal amount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
