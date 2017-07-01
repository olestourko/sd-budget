package com.olestourko.sdbudget.desktop.mappers.frontend;

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

        budgetItemMapper.updateBudgetItemViewModelFromBudgetItem(viewModel.getDebtRepayments(), month.getDebtRepayments());
        budgetItemMapper.updateBudgetItemViewModelFromBudgetItem(viewModel.getInvestmentOutflows(), month.getInvestmentOutflows());
        budgetItemMapper.updateBudgetItemViewModelFromBudgetItem(viewModel.getNetIncomeTarget(), month.getNetIncomeTarget());
        budgetItemMapper.updateBudgetItemViewModelFromBudgetItem(viewModel.getOpeningBalance(), month.getOpeningBalance());
        budgetItemMapper.updateBudgetItemViewModelFromBudgetItem(viewModel.getClosingBalance(), month.getClosingBalance());
        budgetItemMapper.updateBudgetItemViewModelFromBudgetItem(viewModel.getOpeningSurplus(), month.getOpeningSurplus());
        budgetItemMapper.updateBudgetItemViewModelFromBudgetItem(viewModel.getClosingSurplus(), month.getClosingSurplus());
        budgetItemMapper.updateBudgetItemViewModelFromBudgetItem(viewModel.getClosingBalanceTarget(), month.getClosingBalanceTarget());
        budgetItemMapper.updateBudgetItemViewModelFromBudgetItem(viewModel.getEstimatedClosingBalance(), month.getEstimatedClosingBalance());
        viewModel.setModel(month);
        return viewModel;
    }

    public Month mapMonthViewModelToMonth(MonthViewModel monthViewModel) {
        Month month = new Month();
        month.setNumber((short) monthViewModel.calendar.get(Calendar.MONTH));
        month.setYear((short) monthViewModel.calendar.get(Calendar.YEAR));
        month.setIsClosed(monthViewModel.getIsClosed());

        BudgetItemMapper budgetItemMapper = Mappers.getMapper(BudgetItemMapper.class);

        // Map Revenues
        month.getRevenues().clear();
        for (BudgetItemViewModel revenue : monthViewModel.getRevenues()) {
            budgetItemMapper.updateBudgetItemFromBudgetItemViewModel(revenue.getModel(), revenue);
            month.getRevenues().add(revenue.getModel());
        }
        // Map Expenses
        month.getExpenses().clear();
        for (BudgetItemViewModel expense : monthViewModel.getExpenses()) {
            budgetItemMapper.updateBudgetItemFromBudgetItemViewModel(expense.getModel(), expense);
            month.getExpenses().add(expense.getModel());
        }
        // Map Adjustments
        month.getAdjustments().clear();
        for (BudgetItemViewModel adjustment : monthViewModel.getAdjustments()) {
            budgetItemMapper.updateBudgetItemFromBudgetItemViewModel(adjustment.getModel(), adjustment);
            month.getAdjustments().add(adjustment.getModel());
        }

        month.setDebtRepayments(budgetItemMapper.mapBudgetItemViewModelToBudgetItem(monthViewModel.getDebtRepayments()));
        month.setInvestmentOutflows(budgetItemMapper.mapBudgetItemViewModelToBudgetItem(monthViewModel.getInvestmentOutflows()));
        month.setNetIncomeTarget(budgetItemMapper.mapBudgetItemViewModelToBudgetItem(monthViewModel.getNetIncomeTarget()));
        month.setOpeningBalance(budgetItemMapper.mapBudgetItemViewModelToBudgetItem(monthViewModel.getOpeningBalance()));
        month.setClosingBalance(budgetItemMapper.mapBudgetItemViewModelToBudgetItem(monthViewModel.getClosingBalance()));
        month.setOpeningSurplus(budgetItemMapper.mapBudgetItemViewModelToBudgetItem(monthViewModel.getOpeningSurplus()));
        month.setClosingSurplus(budgetItemMapper.mapBudgetItemViewModelToBudgetItem(monthViewModel.getClosingSurplus()));
        month.setClosingBalance(budgetItemMapper.mapBudgetItemViewModelToBudgetItem(monthViewModel.getClosingBalance()));

        return month;
    }

    public Month updateMonthFromMonthViewModel(MonthViewModel monthViewModel, @MappingTarget Month month) {
        month.setNumber((short) monthViewModel.calendar.get(Calendar.MONTH));
        month.setYear((short) monthViewModel.calendar.get(Calendar.YEAR));
        month.setIsClosed(monthViewModel.getIsClosed());

        BudgetItemMapper budgetItemMapper = Mappers.getMapper(BudgetItemMapper.class);

        // Map Revenues
        month.getRevenues().clear();
        for (BudgetItemViewModel revenue : monthViewModel.getRevenues()) {
            budgetItemMapper.updateBudgetItemFromBudgetItemViewModel(revenue.getModel(), revenue);
            month.getRevenues().add(revenue.getModel());
        }
        // Map Expenses
        month.getExpenses().clear();
        for (BudgetItemViewModel expense : monthViewModel.getExpenses()) {
            budgetItemMapper.updateBudgetItemFromBudgetItemViewModel(expense.getModel(), expense);
            month.getExpenses().add(expense.getModel());
        }
        // Map Adjustments
        month.getAdjustments().clear();
        for (BudgetItemViewModel adjustment : monthViewModel.getAdjustments()) {
            budgetItemMapper.updateBudgetItemFromBudgetItemViewModel(adjustment.getModel(), adjustment);
            month.getAdjustments().add(adjustment.getModel());
        }

        budgetItemMapper.updateBudgetItemFromBudgetItemViewModel(monthViewModel.getDebtRepayments().getModel(), monthViewModel.getDebtRepayments());
        budgetItemMapper.updateBudgetItemFromBudgetItemViewModel(monthViewModel.getInvestmentOutflows().getModel(), monthViewModel.getInvestmentOutflows());
        budgetItemMapper.updateBudgetItemFromBudgetItemViewModel(monthViewModel.getNetIncomeTarget().getModel(), monthViewModel.getNetIncomeTarget());
        budgetItemMapper.updateBudgetItemFromBudgetItemViewModel(monthViewModel.getOpeningBalance().getModel(), monthViewModel.getOpeningBalance());
        budgetItemMapper.updateBudgetItemFromBudgetItemViewModel(monthViewModel.getClosingBalance().getModel(), monthViewModel.getClosingBalance());
        budgetItemMapper.updateBudgetItemFromBudgetItemViewModel(monthViewModel.getOpeningSurplus().getModel(), monthViewModel.getOpeningSurplus());
        budgetItemMapper.updateBudgetItemFromBudgetItemViewModel(monthViewModel.getClosingSurplus().getModel(), monthViewModel.getClosingSurplus());
        budgetItemMapper.updateBudgetItemFromBudgetItemViewModel(monthViewModel.getClosingBalanceTarget().getModel(), monthViewModel.getClosingBalanceTarget());
        budgetItemMapper.updateBudgetItemFromBudgetItemViewModel(monthViewModel.getEstimatedClosingBalance().getModel(), monthViewModel.getEstimatedClosingBalance());
        return month;
    }

    public MonthViewModel updateMonthViewModelFromMonth(Month month, @MappingTarget MonthViewModel monthViewModel) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, month.getNumber());
        cal.set(Calendar.YEAR, month.getYear());
        monthViewModel.setIsClosed(month.getIsClosed());

        BudgetItemMapper budgetItemMapper = Mappers.getMapper(BudgetItemMapper.class);
        // Map Revenues
        monthViewModel.getRevenues().clear();
        for (BudgetItem budgetItem : month.getRevenues()) {
            monthViewModel.getRevenues().add(budgetItemMapper.mapBudgetItemToBudgetItemViewModel(budgetItem));
        }
        // Map Expenses
        monthViewModel.getExpenses().clear();
        for (BudgetItem budgetItem : month.getExpenses()) {
            monthViewModel.getExpenses().add(budgetItemMapper.mapBudgetItemToBudgetItemViewModel(budgetItem));
        }
        // Map Adjustments
        monthViewModel.getAdjustments().clear();
        for (BudgetItem budgetItem : month.getAdjustments()) {
            monthViewModel.getAdjustments().add(budgetItemMapper.mapBudgetItemToBudgetItemViewModel(budgetItem));
        }

        budgetItemMapper.updateBudgetItemViewModelFromBudgetItem(monthViewModel.getDebtRepayments(), month.getDebtRepayments());
        budgetItemMapper.updateBudgetItemViewModelFromBudgetItem(monthViewModel.getInvestmentOutflows(), month.getInvestmentOutflows());
        budgetItemMapper.updateBudgetItemViewModelFromBudgetItem(monthViewModel.getNetIncomeTarget(), month.getNetIncomeTarget());
        budgetItemMapper.updateBudgetItemViewModelFromBudgetItem(monthViewModel.getOpeningBalance(), month.getOpeningBalance());
        budgetItemMapper.updateBudgetItemViewModelFromBudgetItem(monthViewModel.getClosingBalance(), month.getClosingBalance());
        budgetItemMapper.updateBudgetItemViewModelFromBudgetItem(monthViewModel.getOpeningSurplus(), month.getOpeningSurplus());
        budgetItemMapper.updateBudgetItemViewModelFromBudgetItem(monthViewModel.getClosingSurplus(), month.getClosingSurplus());
        budgetItemMapper.updateBudgetItemViewModelFromBudgetItem(monthViewModel.getClosingBalanceTarget(), month.getClosingBalanceTarget());
        budgetItemMapper.updateBudgetItemViewModelFromBudgetItem(monthViewModel.getEstimatedClosingBalance(), month.getEstimatedClosingBalance());
        monthViewModel.setModel(month);
        return monthViewModel;
    }
}
