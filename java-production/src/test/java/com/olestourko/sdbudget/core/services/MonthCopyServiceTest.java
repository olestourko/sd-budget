package com.olestourko.sdbudget.core.services;

import com.olestourko.sdbudget.core.dagger.CoreComponent;
import com.olestourko.sdbudget.core.dagger.DaggerCoreComponent;
import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.core.services.MonthCopyServiceTest;
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
public class MonthCopyServiceTest {

    CoreComponent coreComponent;

    public MonthCopyServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        this.coreComponent = DaggerCoreComponent.builder().build();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of cloneBudgetItem method, of class MonthCopyService.
     */
    @Test
    public void testCloneBudgetItem() {
        BudgetItem from = new BudgetItem();
        from.setId(10);
        from.setName("Revenue");
        from.setAmount(new BigDecimal(100));

        MonthCopyService monthCopyService = coreComponent.monthCopyService();
        BudgetItem result = monthCopyService.cloneBudgetItem(from);

        assertNotEquals(from.getId(), result.getId());
        assertEquals(from.getName(), result.getName());
        assertEquals(0, from.getAmount().compareTo(result.getAmount())); // 0 means that the two BigDecimals are equal
    }

    /**
     * Test of cloneMonth method, of class MonthCopyService.
     */
    @Test
    public void testCloneMonth() {
        Month from = new Month();
        from.getRevenues().add(new BudgetItem("Revenue 1", new BigDecimal(100)));
        from.getExpenses().add(new BudgetItem("Expense 1", new BigDecimal(50)));
        from.getAdjustments().add(new BudgetItem("Adjustment 1", new BigDecimal(50)));
        
        Month to = new Month();
        to.getRevenues().add(new BudgetItem("Revenue 2", new BigDecimal(100)));
        
        MonthCopyService monthCopyService = coreComponent.monthCopyService();
        monthCopyService.cloneMonth(from, to);
        assertEquals(to.getRevenues().size(), from.getRevenues().size());
        assertEquals(to.getExpenses().size(), from.getExpenses().size());
        assertEquals(to.getAdjustments().size(), from.getAdjustments().size());
    }
}
