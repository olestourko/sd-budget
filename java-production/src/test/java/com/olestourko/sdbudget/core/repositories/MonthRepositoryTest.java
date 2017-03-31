/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olestourko.sdbudget.core.repositories;

import com.olestourko.sdbudget.desktop.dagger.BudgetInjector;
import com.olestourko.sdbudget.desktop.dagger.DaggerBudgetInjector;
import com.olestourko.sdbudget.desktop.repositories.MonthRepository;
import com.olestourko.sdbudget.desktop.models.MonthViewModel;
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
        BudgetInjector injector = DaggerBudgetInjector.create();

        MonthViewModel month = new MonthViewModel(Calendar.getInstance());
        MonthRepository repository = injector.monthRepository().get();
        repository.putMonth(month);

        MonthViewModel result = repository.getMonth(month.calendar);
        assertEquals(month, result);
    }

    /**
     * Test of getMonth method, of class MonthRepository.
     */
    @Test
    public void testGetMonth() {
        BudgetInjector injector = DaggerBudgetInjector.create();

        MonthViewModel month = new MonthViewModel(Calendar.getInstance());
        MonthRepository repository = injector.monthRepository().get();
        repository.putMonth(month);

        MonthViewModel result = repository.getMonth(month.calendar);
        assertEquals(month, result);
    }

    /**
     * Test of getPrevious method, of class MonthRepository.
     */
    @Test
    public void testGetPrevious() {
        MonthViewModel currentMonth = new MonthViewModel(Calendar.getInstance());
        Calendar previousCalendar = Calendar.getInstance();
        previousCalendar.add(Calendar.MONTH, -1);
        MonthViewModel previousMonth = new MonthViewModel(previousCalendar);

        BudgetInjector injector = DaggerBudgetInjector.create();
        MonthRepository repository = injector.monthRepository().get();
        repository.putMonth(currentMonth);
        repository.putMonth(previousMonth);

        MonthViewModel result = repository.getPrevious(currentMonth);
        assertEquals(previousMonth, result);
    }

    /**
     * Test of getNext method, of class MonthRepository.
     */
    @Test
    public void testGetNext() {
        MonthViewModel currentMonth = new MonthViewModel(Calendar.getInstance());
        Calendar nextCalendar = Calendar.getInstance();
        nextCalendar.add(Calendar.MONTH, 1);
        MonthViewModel nextMonth = new MonthViewModel(nextCalendar);

        BudgetInjector injector = DaggerBudgetInjector.create();
        MonthRepository repository = injector.monthRepository().get();
        repository.putMonth(currentMonth);
        repository.putMonth(nextMonth);

        MonthViewModel result = repository.getNext(currentMonth);
        assertEquals(nextMonth, result);
    }

}
