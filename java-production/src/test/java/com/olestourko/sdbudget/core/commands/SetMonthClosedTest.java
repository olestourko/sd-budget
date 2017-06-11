package com.olestourko.sdbudget.core.commands;

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
public class SetMonthClosedTest {
    
    private Month month;
    
    public SetMonthClosedTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        this.month = new Month();
        this.month.setIsClosed(false);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of execute method, of class SetMonthClosed.
     */
    @Test
    public void testExecute() {
        SetMonthClosed command = new SetMonthClosed(month, true);
        command.execute();
        assertEquals(true, month.getIsClosed());
    }

    /**
     * Test of undo method, of class SetMonthClosed.
     */
    @Test
    public void testUndo() {
        SetMonthClosed command = new SetMonthClosed(month, true);
        command.execute();
        command.undo();
        assertEquals(false, month.getIsClosed());
    }
    
}
