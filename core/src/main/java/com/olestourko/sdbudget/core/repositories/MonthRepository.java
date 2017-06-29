package com.olestourko.sdbudget.core.repositories;

import com.olestourko.sdbudget.core.models.Month;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author oles
 */
public class MonthRepository implements IMonthRepository {

    public final Map<String, Month> months = new TreeMap<>();

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
        previousCalendar.set(Calendar.DAY_OF_MONTH, 1);
        previousCalendar.set(Calendar.MONTH, month.getNumber());
        previousCalendar.set(Calendar.YEAR, month.getYear());
        previousCalendar.add(Calendar.MONTH, -1);
        return this.getMonth((short) previousCalendar.get(Calendar.MONTH), (short) previousCalendar.get(Calendar.YEAR));
    }

    @Override
    public Month getNext(Month month) {
        Calendar nextCalendar = Calendar.getInstance();
        nextCalendar.set(Calendar.DAY_OF_MONTH, 1);
        nextCalendar.set(Calendar.MONTH, month.getNumber());
        nextCalendar.set(Calendar.YEAR, month.getYear());
        nextCalendar.add(Calendar.MONTH, 1);
        return this.getMonth((short) nextCalendar.get(Calendar.MONTH), (short) nextCalendar.get(Calendar.YEAR));
    }

    @Override
    public Month getFirst() {
        // http://www.baeldung.com/java-8-sort-lambda
        List<Month> monthsList = new ArrayList<>(months.values());
        Collections.sort(monthsList, new Comparator<Month>() {
            @Override
            public int compare(Month month1, Month month2) {
                if (month1.getYear() == month2.getYear()) {
                    if (month1.getNumber() == month2.getNumber()) {
                        return 0;
                    } else if (month1.getNumber() < month2.getNumber()) {
                        return -1;
                    } else {
                        return 1;
                    }
                } else if (month1.getYear() < month2.getYear()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });

        return (Month) monthsList.get(0);
    }

}
