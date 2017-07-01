/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olestourko.sdbudget.core.commands;

import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import org.hamcrest.CoreMatchers;
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
public class RemoveBudgetItemTest {

    private Month month;
    private BudgetItem budgetItem;

    public RemoveBudgetItemTest() {
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
        this.budgetItem = new BudgetItem();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of execute method, of class RemoveBudgetItem.
     */
    @Test
    public void testExecute() {
        RemoveBudgetItem command = new RemoveBudgetItem(month, budgetItem, RemoveBudgetItem.Type.REVENUE);
        command.execute();
        assertThat(month.getRevenues(), CoreMatchers.not(CoreMatchers.hasItem(budgetItem)));
    }

    /**
     * Test of undo method, of class RemoveBudgetItem.
     */
    @Test
    public void testUndo() {
        RemoveBudgetItem command = new RemoveBudgetItem(month, budgetItem, RemoveBudgetItem.Type.REVENUE);
        command.execute();
        command.undo();
        assertThat(month.getRevenues(), CoreMatchers.hasItem(budgetItem));
    }

}
