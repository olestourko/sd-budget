package com.olestourko.sdbudget.core.models.factories;

import com.olestourko.sdbudget.core.models.Month;
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
public class MonthFactoryTest {
    
    public MonthFactoryTest() {
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
     * Test of createNextMonth method, of class MonthFactory.
     */
    @Test
    public void testCreateNextMonth() {
        Month month = new Month();
        month.setNumber((short) 0);
        month.setYear((short) 2017);
        
        MonthFactory monthFactory = new MonthFactory();
        Month nextMonth = monthFactory.createNextMonth(month);
        assertEquals(1, nextMonth.getNumber());
        assertEquals(2017, nextMonth.getYear());
    }
    
}
