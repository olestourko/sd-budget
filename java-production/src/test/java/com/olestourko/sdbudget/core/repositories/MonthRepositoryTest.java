/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olestourko.sdbudget.core.repositories;

import com.olestourko.sdbudget.core.dagger.CoreInjector;
import com.olestourko.sdbudget.core.dagger.DaggerCoreInjector;
import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.desktop.dagger.BudgetInjector;
import com.olestourko.sdbudget.desktop.dagger.DaggerBudgetInjector;
import java.util.Calendar;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author oles
 */
public class MonthRepositoryTest {

    public MonthRepositoryTest() {

    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of putMonth method, of class MonthRepository.
     */
    @Test
    public void testPutMonth() {
        CoreInjector injector = DaggerCoreInjector.create();

        Month month = new Month();
        month.setNumber((short) 1);
        month.setYear((short) 2017);
        MonthRepository repository = injector.monthRepository();
        repository.putMonth(month);

        Month result = repository.getMonth(month.getNumber(), month.getYear());
        assertEquals(month, result);
    }

    /**
     * Test of getMonth method, of class MonthRepository.
     */
    @Test
    public void testGetMonth() {
        CoreInjector injector = DaggerCoreInjector.create();

        Month month = new Month();
        month.setNumber((short) 1);
        month.setYear((short) 2017);
        MonthRepository repository = injector.monthRepository();
        repository.putMonth(month);

        Month result = repository.getMonth(month.getNumber(), month.getYear());
        assertEquals(month, result);
    }

    /**
     * Test of getPrevious method, of class MonthRepository.
     */
    @Test
    public void testGetPrevious() {
        Month currentMonth = new Month();
        currentMonth.setNumber((short) 2);
        currentMonth.setYear((short) 2017);

        Month previousMonth = new Month();
        previousMonth.setNumber((short) 1);
        previousMonth.setYear((short) 2017);

        CoreInjector injector = DaggerCoreInjector.create();
        MonthRepository repository = injector.monthRepository();
        repository.putMonth(currentMonth);
        repository.putMonth(previousMonth);

        Month result = repository.getPrevious(currentMonth);
        assertEquals(previousMonth, result);
    }

    /**
     * Test of getNext method, of class MonthRepository.
     */
    @Test
    public void testGetNext() {
        Month currentMonth = new Month();
        currentMonth.setNumber((short) 1);
        currentMonth.setYear((short) 2017);
        
        Month nextMonth = new Month();
        nextMonth.setNumber((short) 2);
        nextMonth.setYear((short) 2017);
        
        CoreInjector injector = DaggerCoreInjector.create();
        MonthRepository repository = injector.monthRepository();
        repository.putMonth(currentMonth);
        repository.putMonth(nextMonth);

        Month result = repository.getNext(currentMonth);
        assertEquals(nextMonth, result);
    }

}
