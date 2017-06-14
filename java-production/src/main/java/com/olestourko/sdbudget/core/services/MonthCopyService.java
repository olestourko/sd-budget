package com.olestourko.sdbudget.core.services;

import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import javax.inject.Inject;

/**
 *
 * @author oles
 */
public class MonthCopyService {

    @Inject
    public MonthCopyService() {

    }

    public BudgetItem cloneBudgetItem(BudgetItem from) {
        return cloneBudgetItem(from, new BudgetItem());
    }

    public BudgetItem cloneBudgetItem(BudgetItem from, BudgetItem to) {
        to.setAmount(from.getAmount());
        to.setName(from.getName());
        return to;
    }

    public Month cloneMonth(Month from, Month to) {
        to.getRevenues().clear();
        for (BudgetItem revenue : from.getRevenues()) {
            to.getRevenues().add(cloneBudgetItem(revenue));
        }

        to.getExpenses().clear();
        for (BudgetItem expense : from.getExpenses()) {
            to.getExpenses().add(cloneBudgetItem(expense));
        }

        cloneBudgetItem(from.getNetIncomeTarget(), to.getNetIncomeTarget());
        
        return to;
    }
}
