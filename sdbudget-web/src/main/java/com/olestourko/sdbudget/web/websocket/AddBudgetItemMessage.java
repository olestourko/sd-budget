package com.olestourko.sdbudget.web.websocket;

import java.math.BigDecimal;

/**
 *
 * @author oles
 */
public class AddBudgetItemMessage {

    private int month;
    private String name;
    private BigDecimal amount;
    private String type;
    
    public AddBudgetItemMessage() {
        
    }
    
    public AddBudgetItemMessage(int month, String name, BigDecimal amount, String type) {
        this.month = month;
        this.name = name;
        this.amount = amount;
        this.type = type;
    }
    
    public int getMonth() {
        return month;
    }
    
    public void setMonth(int month) {
        this.month = month;
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
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
}
