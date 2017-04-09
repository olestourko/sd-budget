package com.olestourko.sdbudget.desktop.mappers;

import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.desktop.models.BudgetItemViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 *
 * @author oles
 */
@Mapper
public abstract class BudgetItemMapper {

    public BudgetItemViewModel mapBudgetItemToBudgetItemViewModel(BudgetItem budgetItem) {
        BudgetItemViewModel budgetItemViewModel = new BudgetItemViewModel();
        budgetItemViewModel.setModel(budgetItem);
        budgetItemViewModel.setName(budgetItem.getName());
        budgetItemViewModel.setAmount(budgetItem.getAmount());
        return budgetItemViewModel;
    }

    public BudgetItem updateBudgetItemFromBudgetItemViewModel(BudgetItem budgetItem, @MappingTarget BudgetItemViewModel budgetItemViewModel) {
        budgetItem.setName(budgetItemViewModel.getName());
        budgetItem.setAmount(budgetItemViewModel.getAmount());
        return budgetItem;
    }
    
    public BudgetItemViewModel updateBudgetItemViewModelFromBudgetItem(BudgetItemViewModel budgetItemViewModel, @MappingTarget BudgetItem budgetItem) {
        budgetItemViewModel.setModel(budgetItem);
        budgetItemViewModel.setName(budgetItem.getName());
        budgetItemViewModel.setAmount(budgetItem.getAmount());
        return budgetItemViewModel;
    }
}
