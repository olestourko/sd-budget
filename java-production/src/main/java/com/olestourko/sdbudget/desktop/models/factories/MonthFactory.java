package com.olestourko.sdbudget.desktop.models.factories;

import com.olestourko.sdbudget.core.models.Month;
import java.util.Calendar;
import javax.inject.Inject;

/**
 *
 * @author oles
 */
public class MonthFactory {

    @Inject
    public MonthFactory() {

    }

    public Month createCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Month month = new Month();
        month.setNumber((short) calendar.get(Calendar.MONTH));
        month.setYear((short) calendar.get(Calendar.YEAR));
        return month;
    }
    
    public Month createNextMonth(Month month) {
        Calendar nextCalendar = Calendar.getInstance();
        nextCalendar.set(Calendar.DAY_OF_MONTH, 1);
        nextCalendar.set(Calendar.MONTH, month.getNumber());
        nextCalendar.set(Calendar.YEAR, month.getYear());
        nextCalendar.add(Calendar.MONTH, 1);
        Month nextMonth = new Month();
        nextMonth.setNumber((short) nextCalendar.get(Calendar.MONTH));
        nextMonth.setYear((short) nextCalendar.get(Calendar.YEAR));
        return nextMonth;
    }
}
