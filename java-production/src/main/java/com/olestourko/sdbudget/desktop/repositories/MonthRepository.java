package com.olestourko.sdbudget.desktop.repositories;

import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.core.persistence.BudgetItemPersistence;
import com.olestourko.sdbudget.core.persistence.MonthPersistence;
import com.olestourko.sdbudget.desktop.models.BudgetItemViewModel;
import com.olestourko.sdbudget.desktop.models.MonthViewModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 *
 * @author oles
 */
@Singleton
public class MonthRepository implements IMonthRepository {

    private final HashMap<String, MonthViewModel> months = new HashMap<String, MonthViewModel>();
    private final MonthPersistence monthPersistence;
    private final BudgetItemPersistence budgetItemPersistence;

    @Inject
    public MonthRepository(MonthPersistence monthPersistence, BudgetItemPersistence budgetItemPersistence) {
        this.monthPersistence = monthPersistence;
        this.budgetItemPersistence = budgetItemPersistence;
    }

    @Override
    public void putMonth(MonthViewModel month) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("M:yyyy");
        String stringDate = dateFormat.format(month.calendar.getTime());
        if (month.getModel() == null) {
            Month coreModel = new Month();
            coreModel.setNumber((short) month.calendar.get(Calendar.MONTH));
            coreModel.setYear((short) month.calendar.get(Calendar.YEAR));
            month.setMonthCoreModel(coreModel);
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
        for (Month coreModel : months) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, 0);
            cal.set(Calendar.MONTH, coreModel.getNumber());
            cal.set(Calendar.YEAR, coreModel.getYear());
            MonthViewModel viewModel = new MonthViewModel(cal);
            // Map Revenues
            for (BudgetItem budgetItem : coreModel.getRevenues()) {
                BudgetItemViewModel budgetItemViewModel = new BudgetItemViewModel();
                budgetItemViewModel.setModel(budgetItem);
                budgetItemViewModel.setName(budgetItem.getName());
                budgetItemViewModel.setAmount(budgetItem.getAmount());
                viewModel.getRevenues().add(budgetItemViewModel);
            }
            // Map Expenses
            for (BudgetItem budgetItem : coreModel.getExpenses()) {
                BudgetItemViewModel budgetItemViewModel = new BudgetItemViewModel();
                budgetItemViewModel.setModel(budgetItem);
                budgetItemViewModel.setName(budgetItem.getName());
                budgetItemViewModel.setAmount(budgetItem.getAmount());
                viewModel.getExpenses().add(budgetItemViewModel);
            }
            // Map Adjustments
            for (BudgetItem budgetItem : coreModel.getAdjustments()) {
                BudgetItemViewModel budgetItemViewModel = new BudgetItemViewModel();
                budgetItemViewModel.setModel(budgetItem);
                budgetItemViewModel.setName(budgetItem.getName());
                budgetItemViewModel.setAmount(budgetItem.getAmount());
                viewModel.getAdjustments().add(budgetItemViewModel);
            }

            viewModel.setMonthCoreModel(coreModel);
            putMonth(viewModel);
        }
    }

    @Override
    public void storeMonths() {
        for (MonthViewModel month : months.values()) {
            monthPersistence.store(month.getModel());

            // Store the associated Revenues
            for (BudgetItemViewModel budgetItemViewModel : month.getRevenues()) {
                if (budgetItemViewModel.getModel() == null) {
                    budgetItemViewModel.setModel(new BudgetItem());
                }

                budgetItemViewModel.getModel().setName(budgetItemViewModel.getName());
                budgetItemViewModel.getModel().setAmount(budgetItemViewModel.getAmount());

                budgetItemPersistence.store(budgetItemViewModel.getModel());
                monthPersistence.associateRevenue(month.getModel(), budgetItemViewModel.getModel());
            }

            // Store the associated Expenses
            for (BudgetItemViewModel budgetItemViewModel : month.getExpenses()) {
                if (budgetItemViewModel.getModel() == null) {
                    budgetItemViewModel.setModel(new BudgetItem());
                }

                budgetItemViewModel.getModel().setName(budgetItemViewModel.getName());
                budgetItemViewModel.getModel().setAmount(budgetItemViewModel.getAmount());

                budgetItemPersistence.store(budgetItemViewModel.getModel());
                monthPersistence.associateExpense(month.getModel(), budgetItemViewModel.getModel());
            }

            // Store the associated Adjustments
            for (BudgetItemViewModel budgetItemViewModel : month.getAdjustments()) {
                if (budgetItemViewModel.getModel() == null) {
                    budgetItemViewModel.setModel(new BudgetItem());
                }

                budgetItemViewModel.getModel().setName(budgetItemViewModel.getName());
                budgetItemViewModel.getModel().setAmount(budgetItemViewModel.getAmount());

                budgetItemPersistence.store(budgetItemViewModel.getModel());
                monthPersistence.associateAdjustment(month.getModel(), budgetItemViewModel.getModel());
            }
        }
    }

}
