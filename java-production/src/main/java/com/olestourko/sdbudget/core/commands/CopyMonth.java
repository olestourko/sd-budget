package com.olestourko.sdbudget.core.commands;

import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.core.services.MonthCopyService;

/**
 *
 * @author oles 
 * Clones the revenues and expenses from one month to another.
 * Basically a wrapper for MonthCopyService.
 */
public class CopyMonth implements ICommand {

    private Month from;
    private Month to;
    private Month originalCopy;

    private MonthCopyService monthCopyService = new MonthCopyService();

    public CopyMonth(Month from, Month to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public void execute() {
        originalCopy = monthCopyService.cloneMonth(from, new Month());
        to = monthCopyService.cloneMonth(from, to);
    }

    @Override
    public void undo() {
        from = monthCopyService.cloneMonth(originalCopy, from);
    }

    public Month getFrom() {
        return this.from;
    }

    public Month getTo() {
        return this.to;
    }

}
