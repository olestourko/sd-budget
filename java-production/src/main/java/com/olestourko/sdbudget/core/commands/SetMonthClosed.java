package com.olestourko.sdbudget.core.commands;

import com.olestourko.sdbudget.core.models.Month;

/**
 *
 * @author oles
 */
public class SetMonthClosed implements ICommand {

    private Month month;
    private boolean targetIsClosed;
    private boolean previousIsClosed;
    
    public SetMonthClosed(Month month, boolean isClosed) {
        this.month = month;
        this.targetIsClosed = isClosed;
        this.previousIsClosed = month.getIsClosed();
    }
    
    @Override
    public void execute() {
        this.month.setIsClosed(targetIsClosed);
    }

    @Override
    public void undo() {
        this.month.setIsClosed(previousIsClosed);
    }
    
}
