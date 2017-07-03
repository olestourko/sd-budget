package com.olestourko.sdbudget.web.websocket;

/**
 *
 * @author oles
 */
public abstract class Response {

    public enum Status {
        SUCCESS, ERROR
    }
    
    protected Status status;
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
}
