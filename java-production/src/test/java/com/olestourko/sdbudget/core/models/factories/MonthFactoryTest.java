package com.olestourko.sdbudget.core.models.factories;

import com.olestourko.sdbudget.core.dagger.CoreComponent;
import com.olestourko.sdbudget.core.dagger.DaggerCoreComponent;
import com.olestourko.sdbudget.core.models.Month;
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
public class MonthFactoryTest {

    MonthFactory monthFactory;

    public MonthFactoryTest() {
        CoreComponent coreComponent = DaggerCoreComponent.create();
        this.monthFactory = coreComponent.monthFactory();
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
     * Test of createCurrentMonth method, of class MonthFactory.
     */
    @Test
    public void testCreateCurrentMonth() {
        Month currentMonth = monthFactory.createCurrentMonth();
        Calendar calendar = Calendar.getInstance();

        assertEquals(calendar.get(Calendar.MONTH), currentMonth.getNumber());
        assertEquals(calendar.get(Calendar.YEAR), currentMonth.getYear());
    }

    /**
     * Test of createNextMonth method, of class MonthFactory.
     */
    @Test
    public void testCreateNextMonth() {
        Month month = new Month();
        month.setNumber((short) 0);
        month.setYear((short) 2017);

        Month nextMonth = monthFactory.createNextMonth(month);
        assertEquals(1, nextMonth.getNumber());
        assertEquals(2017, nextMonth.getYear());
    }

}
