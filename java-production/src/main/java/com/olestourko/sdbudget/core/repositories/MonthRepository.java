package com.olestourko.sdbudget.core.repositories;

import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.core.persistence.BudgetItemPersistence;
import com.olestourko.sdbudget.core.persistence.MonthPersistence;
import com.olestourko.sdbudget.desktop.mappers.BudgetItemMapper;
import com.olestourko.sdbudget.desktop.mappers.MonthMapper;
import com.olestourko.sdbudget.desktop.models.BudgetItemViewModel;
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

    private final HashMap<String, Month> months = new HashMap<>();
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
    public void putMonth(Month month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, month.getNumber());
        calendar.set(Calendar.YEAR, month.getYear());
        SimpleDateFormat dateFormat = new SimpleDateFormat("M:yyyy");
        String stringDate = dateFormat.format(calendar.getTime());
        months.put(stringDate, month);
    }

    @Override
    public Month getMonth(short number, short year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, number);
        calendar.set(Calendar.YEAR, year);
        SimpleDateFormat dateFormat = new SimpleDateFormat("M:yyyy");
        String stringDate = dateFormat.format(calendar.getTime());
        Month month = months.get(stringDate);
        return month;
    }

    @Override
    public Month getPrevious(Month month) {
        Calendar previousCalendar = Calendar.getInstance();
        previousCalendar.set(Calendar.MONTH, month.getNumber());
        previousCalendar.set(Calendar.YEAR, month.getYear());
        previousCalendar.add(Calendar.MONTH, -1);
        return this.getMonth((short) previousCalendar.get(Calendar.MONTH), (short) previousCalendar.get(Calendar.YEAR));
    }

    @Override
    public Month getNext(Month month) {
        Calendar nextCalendar = Calendar.getInstance();
        nextCalendar.set(Calendar.MONTH, month.getNumber());
        nextCalendar.set(Calendar.YEAR, month.getYear());
        nextCalendar.add(Calendar.MONTH, 1);
        return this.getMonth((short) nextCalendar.get(Calendar.MONTH), (short) nextCalendar.get(Calendar.YEAR));
    }

    @Override
    public void fetchMonths() {
        ArrayList<Month> months = monthPersistence.getAllMonths();
        for (Month month : months) {
            putMonth(month);
        }
    }

    @Override
    public void storeMonths() {
        for (Month month : months.values()) {
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

            //Store the associated Net Income Target
            BudgetItem netIncomeTarget = month.getNetIncomeTarget();
            budgetItemPersistence.store(netIncomeTarget);
            monthPersistence.associateNetIncomeTarget(month, netIncomeTarget);

            //Store the associated Opening Balance
            BudgetItem openingBalance = month.getOpeningBalance();
            budgetItemPersistence.store(openingBalance);
            monthPersistence.associateOpeningBalance(month, openingBalance);

            //Store the associated Closing Balance
            BudgetItem closingBalance = month.getClosingBalance();
            budgetItemPersistence.store(closingBalance);
            monthPersistence.associateClosingBalance(month, closingBalance);
        }
    }

}
