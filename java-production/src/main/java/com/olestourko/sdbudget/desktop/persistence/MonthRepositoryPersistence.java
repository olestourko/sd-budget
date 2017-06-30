package com.olestourko.sdbudget.desktop.persistence;

import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import java.util.ArrayList;
import javax.inject.Inject;

/**
 *
 * @author oles
 */
public class MonthRepositoryPersistence {

    final MonthPersistence monthPersistence;
    final BudgetItemPersistence budgetItemPersistence;
    
    @Inject
    public MonthRepositoryPersistence(MonthPersistence monthPersistence, BudgetItemPersistence budgetItemPersistence) {
        this.monthPersistence = monthPersistence;
        this.budgetItemPersistence = budgetItemPersistence;
    }
    
    public void fetchMonths(MonthRepository monthRepository) {
        ArrayList<Month> months = monthPersistence.getAllMonths();
        for (Month month : months) {
            monthRepository.putMonth(month);
        }
    }

    public void storeMonths(MonthRepository monthRepository) {
        for (Month month : monthRepository.getMonths()) {
            monthPersistence.store(month);

            // Store the associated Revenues
            for (BudgetItem budgetItem : month.getRevenues()) {
                budgetItem = budgetItemPersistence.store(budgetItem);
            }
            monthPersistence.syncRevenuesToDB(month);

            // Store the associated Expenses
            for (BudgetItem budgetItem : month.getExpenses()) {
                budgetItem = budgetItemPersistence.store(budgetItem);
            }
            monthPersistence.syncExpensesToDB(month);

            // Store the associated Adjustments
            for (BudgetItem budgetItem : month.getAdjustments()) {
                budgetItem = budgetItemPersistence.store(budgetItem);
            }
            monthPersistence.syncAdjustmentsToDB(month);

            // Store the associated Debt Repayments
            BudgetItem debtRepayments = month.getDebtRepayments();
            budgetItemPersistence.store(debtRepayments);
            monthPersistence.associateDebtRepayments(month, debtRepayments);

            // Store the associated Investment Outflows
            BudgetItem investmentOutflows = month.getInvestmentOutflows();
            budgetItemPersistence.store(investmentOutflows);
            monthPersistence.associateInvestmentOuflows(month, investmentOutflows);

            // Store the associated Net Income Target
            BudgetItem netIncomeTarget = month.getNetIncomeTarget();
            budgetItemPersistence.store(netIncomeTarget);
            monthPersistence.associateNetIncomeTarget(month, netIncomeTarget);

            // Store the associated Opening Balance
            BudgetItem openingBalance = month.getOpeningBalance();
            budgetItemPersistence.store(openingBalance);
            monthPersistence.associateOpeningBalance(month, openingBalance);

            // Store the associated Closing Balance
            BudgetItem closingBalance = month.getClosingBalance();
            budgetItemPersistence.store(closingBalance);
            monthPersistence.associateClosingBalance(month, closingBalance);
        }

        // Relationships don't clean up orphans on dissociation, so run a query to find orphans and delete them
        monthPersistence.removeUnusedBudgetItems();
    }
}
