package com.olestourko.sdbudget.core.commands;

import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;

/**
 *
 * @author oles
 */
public class RemoveBudgetItem implements ICommand {

    public enum Type {
        REVENUE,
        EXPENSE,
        ADJUSTMENT
    };

    private Month month;
    private BudgetItem budgetItem;

    public RemoveBudgetItem(Month month, BudgetItem budgetItem) {
        this.month = month;
        this.budgetItem = budgetItem;
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void undo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
