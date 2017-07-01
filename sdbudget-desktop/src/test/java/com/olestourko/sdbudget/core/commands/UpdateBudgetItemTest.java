package com.olestourko.sdbudget.core.commands;

import com.olestourko.sdbudget.core.models.BudgetItem;
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
public class UpdateBudgetItemTest {
    
    private BudgetItem budgetItem;
    private BudgetItem targetModel;
    
    public UpdateBudgetItemTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        budgetItem = new BudgetItem("Before", BigDecimal.ZERO);
        targetModel = new BudgetItem("After", BigDecimal.ONE);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of execute method, of class UpdateBudgetItem.
     */
    @Test
    public void testExecute() {
        UpdateBudgetItem command = new UpdateBudgetItem(budgetItem, targetModel);
        command.execute();
        assertEquals("After", budgetItem.getName());
        assertEquals(BigDecimal.ONE, budgetItem.getAmount());
    }

    /**
     * Test of undo method, of class UpdateBudgetItem.
     */
    @Test
    public void testUndo() {
        UpdateBudgetItem command = new UpdateBudgetItem(budgetItem, targetModel);
        command.execute();
        command.undo();
        assertEquals("Before", budgetItem.getName());
        assertEquals(BigDecimal.ZERO, budgetItem.getAmount());        
    }
    
}
