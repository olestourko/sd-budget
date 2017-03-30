/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olestourko.sdbudget.desktop.repositories;

import com.olestourko.sdbudget.desktop.models.Month;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 *
 * @author oles
 */
public class MonthRepository implements IMonthRepository {

    private final HashMap<String, Month> months = new HashMap<String, Month>();
    
    @Override
    public void putMonth(Month month) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("M:yyyy");
        String stringDate = dateFormat.format(month.calendar.getTime());
        months.put(stringDate, month);
    }
    
    @Override
    public Month getMonth(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("M:yyyy");
        String stringDate = dateFormat.format(calendar.getTime());
        Month month = months.get(stringDate);
        return month;
    }

    @Override
    public Month getPrevious(Month month) {
        Calendar previousCalendar = Calendar.getInstance();
        previousCalendar.setTime(month.calendar.getTime());
        previousCalendar.add(Calendar.MONTH, -1);
        return this.getMonth(previousCalendar);
    }

    @Override
    public Month getNext(Month month) {
        Calendar nextCalendar = Calendar.getInstance();
        nextCalendar.setTime(month.calendar.getTime());
        nextCalendar.add(Calendar.MONTH, 1);
        return this.getMonth(nextCalendar);
    }
    
}
