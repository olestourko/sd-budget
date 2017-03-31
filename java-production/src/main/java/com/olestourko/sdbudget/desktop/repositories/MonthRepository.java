package com.olestourko.sdbudget.desktop.repositories;

import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.core.persistence.MonthPersistence;
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

    @Inject
    public MonthRepository(MonthPersistence monthPersistence) {
        this.monthPersistence = monthPersistence;
    }

    @Override
    public void putMonth(MonthViewModel month) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("M:yyyy");
        String stringDate = dateFormat.format(month.calendar.getTime());
        if (month.getMonthCoreModel() == null) {
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
        ArrayList<com.olestourko.sdbudget.core.models.Month> months = monthPersistence.getAllMonths();
        for (Month coreModel : months) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, coreModel.getNumber());
            cal.set(Calendar.YEAR, coreModel.getYear());
            MonthViewModel viewModel = new MonthViewModel(cal);
            viewModel.setMonthCoreModel(coreModel);
            putMonth(viewModel);
        }
    }

    @Override
    public void storeMonths() {
        for (MonthViewModel month : months.values()) {
            monthPersistence.store(month.getMonthCoreModel());
        }
    }

}
