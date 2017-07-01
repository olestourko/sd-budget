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
public class AddBudgetItemTest {
    
    private Month month;
    private BudgetItem budgetItem;
    
    public AddBudgetItemTest() {
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
     * Test of execute method, of class AddBudgetItem.
     */
    @Test
    public void testExecute() {
        AddBudgetItem command = new AddBudgetItem(month, budgetItem, AddBudgetItem.Type.REVENUE);
        command.execute();
        assertThat(month.getRevenues(), CoreMatchers.hasItem(budgetItem));
    }

    /**
     * Test of undo method, of class AddBudgetItem.
     */
    @Test
    public void testUndo() {
        AddBudgetItem command = new AddBudgetItem(month, budgetItem, AddBudgetItem.Type.REVENUE);
        command.execute();
        command.undo();
        assertThat(month.getRevenues(), CoreMatchers.not(CoreMatchers.hasItem(budgetItem)));
    }
    
}
