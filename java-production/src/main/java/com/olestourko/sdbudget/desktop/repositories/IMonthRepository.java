/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olestourko.sdbudget.desktop.repositories;

import com.olestourko.sdbudget.desktop.models.MonthViewModel;
import java.util.Calendar;

/**
 *
 * @author oles
 */
public interface IMonthRepository {

    public void putMonth(MonthViewModel month);
    
    public MonthViewModel getMonth(Calendar calendar);

    public MonthViewModel getPrevious(MonthViewModel month);

    public MonthViewModel getNext(MonthViewModel month);
    
    public void fetchMonths();
    
    public void storeMonths();
}
