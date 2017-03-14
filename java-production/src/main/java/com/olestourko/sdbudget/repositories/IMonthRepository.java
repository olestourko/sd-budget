/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olestourko.sdbudget.repositories;

import com.olestourko.sdbudget.models.Month;
import java.util.Calendar;

/**
 *
 * @author oles
 */
public interface IMonthRepository {

    public void putMonth(Month month);
    
    public Month getMonth(Calendar calendar);

    public Month getPrevious(Month month);

    public Month getNext(Month month);
}
