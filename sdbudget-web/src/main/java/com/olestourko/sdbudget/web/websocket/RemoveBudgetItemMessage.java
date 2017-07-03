package com.olestourko.sdbudget.web.websocket;

import java.math.BigDecimal;

/**
 *
 * @author oles
 */
public class RemoveBudgetItemMessage {

    private int id;

    public RemoveBudgetItemMessage() {

    }

    public RemoveBudgetItemMessage(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
