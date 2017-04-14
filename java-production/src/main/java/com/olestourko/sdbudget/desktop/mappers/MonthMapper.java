package com.olestourko.sdbudget.desktop.mappers;

import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.desktop.models.BudgetItemViewModel;
import com.olestourko.sdbudget.desktop.models.MonthViewModel;
import java.util.Calendar;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author oles
 */
@Mapper
public abstract class MonthMapper {

    public MonthViewModel mapMonthToMonthViewModel(Month month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, month.getNumber());
        cal.set(Calendar.YEAR, month.getYear());
        MonthViewModel viewModel = new MonthViewModel(cal);
        viewModel.setIsClosed(month.getIsClosed());

        BudgetItemMapper budgetItemMapper = Mappers.getMapper(BudgetItemMapper.class);
        // Map Revenues
        for (BudgetItem budgetItem : month.getRevenues()) {
            viewModel.getRevenues().add(budgetItemMapper.mapBudgetItemToBudgetItemViewModel(budgetItem));
        }
        // Map Expenses
        for (BudgetItem budgetItem : month.getExpenses()) {
            viewModel.getExpenses().add(budgetItemMapper.mapBudgetItemToBudgetItemViewModel(budgetItem));
        }
        // Map Adjustments
        for (BudgetItem budgetItem : month.getAdjustments()) {
            viewModel.getAdjustments().add(budgetItemMapper.mapBudgetItemToBudgetItemViewModel(budgetItem));
        }
        // Map Net Income Target
        if (month.getNetIncomeTarget() != null) {
            budgetItemMapper.updateBudgetItemViewModelFromBudgetItem(viewModel.netIncomeTarget, month.getNetIncomeTarget());
        }

        // Map Opening Balance
        if (month.getOpeningBalance() != null) {
            budgetItemMapper.updateBudgetItemViewModelFromBudgetItem(viewModel.openingBalance, month.getOpeningBalance());
        }

        viewModel.setModel(month);
        return viewModel;
    }

    public Month updateMonthFromMonthViewModel(MonthViewModel monthViewModel, @MappingTarget Month month) {
        month.setNumber((short) monthViewModel.calendar.get(Calendar.MONTH));
        month.setYear((short) monthViewModel.calendar.get(Calendar.YEAR));
        month.setIsClosed(monthViewModel.getIsClosed());
        // Map Revenues
        month.getRevenues().clear();
        for (BudgetItemViewModel revenue : monthViewModel.getRevenues()) {
            month.getRevenues().add(revenue.getModel());
        }
        // Map Expenses
        month.getExpenses().clear();
        for (BudgetItemViewModel expense : monthViewModel.getExpenses()) {
            month.getExpenses().add(expense.getModel());
        }
        // Map Adjustments
        month.getAdjustments().clear();
        for (BudgetItemViewModel adjustment : monthViewModel.getAdjustments()) {
            month.getAdjustments().add(adjustment.getModel());
        }

        return month;
    }
}
