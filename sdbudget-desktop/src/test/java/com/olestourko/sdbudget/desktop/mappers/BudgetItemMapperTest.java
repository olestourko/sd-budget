package com.olestourko.sdbudget.desktop.mappers;

import com.olestourko.sdbudget.desktop.mappers.frontend.BudgetItemMapper;
import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.desktop.models.BudgetItemViewModel;
import java.math.BigDecimal;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mapstruct.factory.Mappers;
import static org.junit.Assert.*;

/**
 *
 * @author oles
 */
public class BudgetItemMapperTest {

    protected BudgetItemMapper mapper;
    
    public BudgetItemMapperTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        mapper = Mappers.getMapper(BudgetItemMapper.class);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of mapBudgetItemToBudgetItemViewModel method, of class BudgetItemMapper.
     */
    @Test
    public void testMapBudgetItemToBudgetItemViewModel() {
        BudgetItem budgetItem = new BudgetItem("Test Item", BigDecimal.ONE);
        BudgetItemViewModel result = mapper.mapBudgetItemToBudgetItemViewModel(budgetItem);
        assertEquals("Test Item", result.getName());
        assertEquals(0, result.getAmount().compareTo(BigDecimal.ONE));
    }

    /**
     * Test of mapBudgetItemViewModelToBudgetItem method, of class BudgetItemMapper.
     */
    @Test
    public void testMapBudgetItemViewModelToBudgetItem() {
        BudgetItemViewModel budgetItemViewModel = new BudgetItemViewModel("Test Item", BigDecimal.ONE);
        BudgetItem result = mapper.mapBudgetItemViewModelToBudgetItem(budgetItemViewModel);
        assertEquals("Test Item", result.getName());
        assertEquals(0, result.getAmount().compareTo(BigDecimal.ONE));
    }

    /**
     * Test of updateBudgetItemFromBudgetItemViewModel method, of class BudgetItemMapper.
     */
    @Test
    public void testUpdateBudgetItemFromBudgetItemViewModel() {
        BudgetItem budgetItem = new BudgetItem("Before", BigDecimal.ZERO);
        BudgetItemViewModel budgetItemViewModel = new BudgetItemViewModel("After", BigDecimal.ONE);
        BudgetItem result = mapper.updateBudgetItemFromBudgetItemViewModel(budgetItem, budgetItemViewModel);
        assertEquals("After", result.getName());
        assertEquals(0, result.getAmount().compareTo(BigDecimal.ONE));
    }

    /**
     * Test of updateBudgetItemViewModelFromBudgetItem method, of class BudgetItemMapper.
     */
    @Test
    public void testUpdateBudgetItemViewModelFromBudgetItem() {
        BudgetItemViewModel budgetItemViewModel = new BudgetItemViewModel("Before", BigDecimal.ZERO);
        BudgetItem budgetItem = new BudgetItem("After", BigDecimal.ONE);
        BudgetItemViewModel result = mapper.updateBudgetItemViewModelFromBudgetItem(budgetItemViewModel, budgetItem);
        assertEquals("After", result.getName());
        assertEquals(0, result.getAmount().compareTo(BigDecimal.ONE));
    }
}
