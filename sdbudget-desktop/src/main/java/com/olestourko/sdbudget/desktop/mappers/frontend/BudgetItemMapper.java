package com.olestourko.sdbudget.desktop.mappers.frontend;

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
    
    public BudgetItem mapBudgetItemViewModelToBudgetItem(BudgetItemViewModel budgetItemViewModel) {
        BudgetItem budgetItem = new BudgetItem();
        budgetItem.setName(budgetItemViewModel.getName());
        budgetItem.setAmount(budgetItemViewModel.getAmount());
        return budgetItem;
    }

    public BudgetItem updateBudgetItemFromBudgetItemViewModel(@MappingTarget BudgetItem budgetItem, BudgetItemViewModel budgetItemViewModel) {
        budgetItem.setName(budgetItemViewModel.getName());
        budgetItem.setAmount(budgetItemViewModel.getAmount());
        return budgetItem;
    }
    
    public BudgetItemViewModel updateBudgetItemViewModelFromBudgetItem(@MappingTarget BudgetItemViewModel budgetItemViewModel, BudgetItem budgetItem) {
        budgetItemViewModel.setModel(budgetItem);
        budgetItemViewModel.setName(budgetItem.getName());
        budgetItemViewModel.setAmount(budgetItem.getAmount());
        return budgetItemViewModel;
    }
}
