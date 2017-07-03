package com.olestourko.sdbudget.web.websocket;

import java.math.BigDecimal;

/**
 *
 * @author oles
 */
public class UpdateBudgetItemMessage {

    private int id;
    private String name;
    private BigDecimal amount;

    public UpdateBudgetItemMessage() {

    }

    public UpdateBudgetItemMessage(int id, String name, BigDecimal amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int month) {
        this.id = month;
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
