package com.olestourko.sdbudget.web.websocket;

/**
 *
 * @author oles
 */
public class AddBudgetItemResponse extends Response {

    private int id;

    public AddBudgetItemResponse() {

    }

    public AddBudgetItemResponse(Status status, int id) {
        this.status = status;
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
}
