package com.olestourko.sdbudget.core.commands.mocks;

/**
 *
 * @author oles
 */
public class StringWrapper {
    private String value;
    
    public StringWrapper(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
}
