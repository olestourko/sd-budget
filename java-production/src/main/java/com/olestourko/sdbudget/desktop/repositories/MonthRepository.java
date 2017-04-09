package com.olestourko.sdbudget.desktop.repositories;

import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.core.persistence.BudgetItemPersistence;
import com.olestourko.sdbudget.core.persistence.MonthPersistence;
import com.olestourko.sdbudget.desktop.mappers.BudgetItemMapper;
import com.olestourko.sdbudget.desktop.mappers.MonthMapper;
import com.olestourko.sdbudget.desktop.models.BudgetItemViewModel;
import com.olestourko.sdbudget.desktop.models.MonthViewModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author oles
 */
@Singleton
public class MonthRepository implements IMonthRepository {

    private final HashMap<String, MonthViewModel> months = new HashMap<String, MonthViewModel>();
    private final MonthPersistence monthPersistence;
    private final BudgetItemPersistence budgetItemPersistence;
    private final MonthMapper monthMapper;
    private final BudgetItemMapper budgetItemMapper;

    @Inject
    public MonthRepository(MonthPersistence monthPersistence, BudgetItemPersistence budgetItemPersistence) {
        this.monthPersistence = monthPersistence;
        this.budgetItemPersistence = budgetItemPersistence;
        this.monthMapper = Mappers.getMapper(MonthMapper.class);
        this.budgetItemMapper = Mappers.getMapper(BudgetItemMapper.class);
    }

    @Override
    public void putMonth(MonthViewModel month) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("M:yyyy");
        String stringDate = dateFormat.format(month.calendar.getTime());
        if (month.getModel() == null) {
            Month coreModel = new Month();
            coreModel.setNumber((short) month.calendar.get(Calendar.MONTH));
            coreModel.setYear((short) month.calendar.get(Calendar.YEAR));
            month.setModel(coreModel);
        }
        months.put(stringDate, month);
    }

    @Override
    public MonthViewModel getMonth(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("M:yyyy");
        String stringDate = dateFormat.format(calendar.getTime());
        MonthViewModel month = months.get(stringDate);
        return month;
    }

    @Override
    public MonthViewModel getPrevious(MonthViewModel month) {
        Calendar previousCalendar = Calendar.getInstance();
        previousCalendar.setTime(month.calendar.getTime());
        previousCalendar.add(Calendar.MONTH, -1);
        return this.getMonth(previousCalendar);
    }

    @Override
    public MonthViewModel getNext(MonthViewModel month) {
        Calendar nextCalendar = Calendar.getInstance();
        nextCalendar.setTime(month.calendar.getTime());
        nextCalendar.add(Calendar.MONTH, 1);
        return this.getMonth(nextCalendar);
    }

    @Override
    public void fetchMonths() {
        ArrayList<Month> months = monthPersistence.getAllMonths();
        for (Month month : months) {
            MonthViewModel viewModel = monthMapper.mapMonthToMonthViewModel(month);

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
            putMonth(viewModel);
        }
    }

    @Override
    public void storeMonths() {
        for (MonthViewModel month : months.values()) {
            monthPersistence.store(month.getModel());

            // Store the associated Revenues
            for (BudgetItemViewModel budgetItemViewModel : month.getRevenues()) {
                budgetItemMapper.updateBudgetItemFromBudgetItemViewModel(budgetItemViewModel.getModel(), budgetItemViewModel);
                budgetItemPersistence.store(budgetItemViewModel.getModel());
                monthPersistence.associateRevenue(month.getModel(), budgetItemViewModel.getModel());
            }

            // Store the associated Expenses
            for (BudgetItemViewModel budgetItemViewModel : month.getExpenses()) {
                budgetItemMapper.updateBudgetItemFromBudgetItemViewModel(budgetItemViewModel.getModel(), budgetItemViewModel);
                budgetItemPersistence.store(budgetItemViewModel.getModel());
                monthPersistence.associateExpense(month.getModel(), budgetItemViewModel.getModel());
            }

            // Store the associated Adjustments
            for (BudgetItemViewModel budgetItemViewModel : month.getAdjustments()) {
                budgetItemMapper.updateBudgetItemFromBudgetItemViewModel(budgetItemViewModel.getModel(), budgetItemViewModel);
                budgetItemPersistence.store(budgetItemViewModel.getModel());
                monthPersistence.associateAdjustment(month.getModel(), budgetItemViewModel.getModel());
            }

            //Store the associated Net Income Target
            BudgetItemViewModel netIncomeTargetViewModel = month.netIncomeTarget;
            budgetItemMapper.updateBudgetItemFromBudgetItemViewModel(netIncomeTargetViewModel.getModel(), netIncomeTargetViewModel);
            budgetItemPersistence.store(netIncomeTargetViewModel.getModel());
            monthPersistence.associateNetIncomeTarget(month.getModel(), netIncomeTargetViewModel.getModel());

            //Store the associated Opening Balance
            BudgetItemViewModel openingBalanceViewModel = month.openingBalance;
            budgetItemMapper.updateBudgetItemFromBudgetItemViewModel(openingBalanceViewModel.getModel(), openingBalanceViewModel);
            budgetItemPersistence.store(openingBalanceViewModel.getModel());
            monthPersistence.associateOpeningBalance(month.getModel(), openingBalanceViewModel.getModel());
        }
    }

}
