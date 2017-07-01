package com.olestourko.sdbudget.core.commands;

import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;

/**
 *
 * @author oles
 */
public class UpdateBudgetItem implements ICommand {

    private BudgetItem item;
    // The state that the item should be like
    private BudgetItem targetState;
    private BudgetItem previousState;

    public UpdateBudgetItem(BudgetItem item, BudgetItem targetState) {
        this.item = item;
        this.targetState = new BudgetItem(targetState.getName(), targetState.getAmount());
        this.previousState = new BudgetItem(item.getName(), item.getAmount());
    }

    @Override
    public void execute() {
        this.item.setName(targetState.getName());
        this.item.setAmount(targetState.getAmount());
    }

    @Override
    public void undo() {
        this.item.setName(previousState.getName());
        this.item.setAmount(previousState.getAmount());
    }

    public BudgetItem getBudgetItem() {
        return this.item;
    }

}
