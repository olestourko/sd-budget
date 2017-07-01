/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olestourko.sdbudget.core.commands;

import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import java.math.BigDecimal;
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
public class CopyMonthTest {

    Month from;
    Month to;

    public CopyMonthTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        from = new Month();
        from.getRevenues().add(new BudgetItem("Revenue 1", new BigDecimal(100)));
        from.getExpenses().add(new BudgetItem("Expense 1", new BigDecimal(50)));
        from.getAdjustments().add(new BudgetItem("Adjustment 1", new BigDecimal(50)));
        to = new Month();
        to.getRevenues().add(new BudgetItem("Revenue 2", new BigDecimal(100)));
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of execute method, of class CopyMonth.
     */
    @Test
    public void testExecute() {
        CopyMonth command = new CopyMonth(from, to);
        command.execute();
        assertEquals(to.getRevenues().size(), from.getRevenues().size());
        assertEquals(to.getExpenses().size(), from.getExpenses().size());
        assertNotEquals(to.getAdjustments().size(), from.getAdjustments().size()); // Adjustments should not be cloned
    }

    /**
     * Test of undo method, of class CopyMonth.
     */
    @Test
    public void testUndo() {
        int originalRevenuesSize = to.getRevenues().size();
        int originalExpensesSize = to.getExpenses().size();
        int originalAdjustmentsSize = to.getAdjustments().size();
        
        CopyMonth command = new CopyMonth(from, to);
        command.execute();
        command.undo();
        
        assertEquals(originalRevenuesSize, to.getRevenues().size());
        assertEquals(originalExpensesSize, to.getExpenses().size());
        assertEquals(originalAdjustmentsSize, to.getAdjustments().size()); // Adjustments should not be cloned
    }
}
