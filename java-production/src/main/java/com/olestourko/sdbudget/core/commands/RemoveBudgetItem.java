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
    private Type type;

    public RemoveBudgetItem(Month month, BudgetItem budgetItem, Type type) {
        this.month = month;
        this.budgetItem = budgetItem;
        this.type = type;
    }

    @Override
    public void execute() {
        switch (type) {
            case REVENUE:
                month.getRevenues().remove(budgetItem);
                break;

            case EXPENSE:
                month.getExpenses().remove(budgetItem);
                break;

            case ADJUSTMENT:
                month.getAdjustments().remove(budgetItem);
                break;
        }
    }

    @Override
    public void undo() {
        switch (type) {
            case REVENUE:
                month.getRevenues().add(budgetItem);
                break;

            case EXPENSE:
                month.getExpenses().add(budgetItem);
                break;

            case ADJUSTMENT:
                month.getAdjustments().add(budgetItem);
                break;
        }
    }

}
