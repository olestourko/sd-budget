/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olestourko.sdbudget.repositories;

import com.olestourko.sdbudget.models.Month;
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
        Month month = new Month();

        MonthRepository repository = new MonthRepository();
        repository.putMonth(month);

        Month result = repository.getMonth(month.calendar);
        assertEquals(month, result);
    }

    /**
     * Test of getMonth method, of class MonthRepository.
     */
    @Test
    public void testGetMonth() {
        Month month = new Month();

        MonthRepository repository = new MonthRepository();
        repository.putMonth(month);

        Month result = repository.getMonth(month.calendar);
        assertEquals(month, result);
    }

    /**
     * Test of getPrevious method, of class MonthRepository.
     */
    @Test
    public void testGetPrevious() {
        Month currentMonth = new Month();
        Month previousMonth = new Month();
        previousMonth.calendar.add(Calendar.MONTH, -1);

        MonthRepository repository = new MonthRepository();
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
        Month nextMonth = new Month();
        nextMonth.calendar.add(Calendar.MONTH, 1);

        MonthRepository repository = new MonthRepository();
        repository.putMonth(currentMonth);
        repository.putMonth(nextMonth);

        Month result = repository.getNext(currentMonth);
        assertEquals(nextMonth, result);
    }

}
