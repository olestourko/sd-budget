package com.olestourko;

/**
 *
 * @author oles
 */
public class ExampleWebSocketMessage {
    private String content;
    
    public ExampleWebSocketMessage() {
        
    }
    
    public ExampleWebSocketMessage(String content) {
        this.content = content;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
}
