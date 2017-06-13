package com.olestourko.sdbudget.core.commands.mocks;

import com.olestourko.sdbudget.core.commands.ICommand;

/**
 *
 * @author oles
 */
public class MockCommand_HistoryTestImp implements ICommand {

    public StringWrapper stringWrapper;
    public String newValue;
    public String oldValue;
    
    public MockCommand_HistoryTestImp(StringWrapper stringWrapper, String newValue) {
        this.stringWrapper = stringWrapper;
        this.newValue = newValue;
        this.oldValue = stringWrapper.getValue();
    }
    
    @Override
    public void execute() {
        stringWrapper.setValue(newValue);
    }

    @Override
    public void undo() {
        stringWrapper.setValue(oldValue);
    }
    
}
