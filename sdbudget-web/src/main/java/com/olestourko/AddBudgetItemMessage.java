package com.olestourko;

import java.math.BigDecimal;

/**
 *
 * @author oles
 */
public class AddBudgetItemMessage {

    private int month;
    private String name;
    private BigDecimal amount;
    
    public AddBudgetItemMessage() {
        
    }
    
    public AddBudgetItemMessage(int month, String name, BigDecimal amount) {
        this.month = month;
        this.name = name;
        this.amount = this.amount;
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
}
