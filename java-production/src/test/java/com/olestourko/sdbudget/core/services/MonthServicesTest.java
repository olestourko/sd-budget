/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olestourko.sdbudget.core.services;

import com.olestourko.sdbudget.core.dagger.CoreInjector;
import com.olestourko.sdbudget.core.dagger.DaggerCoreInjector;
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
public class MonthServicesTest {

    private final MonthCalculationServices monthServices;

    public MonthServicesTest() {
        CoreInjector injector = DaggerCoreInjector.create();
        this.monthServices = injector.monthServices();
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
     * Test of calculateTotals method, of class MonthServices.
     */
    @Test
    public void testCalculateMonthTotals() {
        System.out.println("calculateTotals: closed month");
        Month month = new Month();
        month.getRevenues().add(new BudgetItem("Total Revenues", new BigDecimal(2000)));
        month.getExpenses().add(new BudgetItem("Total Expenses", new BigDecimal(1000)));
        month.getAdjustments().add(new BudgetItem("Total Adjustments", new BigDecimal(0)));
        month.setNetIncomeTarget(new BudgetItem("Net Income Target", new BigDecimal(500)));
        month.setOpeningBalance(new BudgetItem("Opening Balance", new BigDecimal(1000)));

        month.setOpeningSurplus(new BudgetItem("Opening Surplus", new BigDecimal(0)));
        month.setClosingSurplus(new BudgetItem("Closing Surplus", new BigDecimal(0))); // Don't need to set this
        month.setClosingBalanceTarget(new BudgetItem("Closing Balance Target", new BigDecimal(0))); // Don't need to set this
        month.setEstimatedClosingBalance(new BudgetItem("Estimated Closing Balance", new BigDecimal(0))); // Don't need to set this

        BigDecimal expClosingBalanceTarget = new BigDecimal(1500);
        BigDecimal expEstimatedClosingBalance = new BigDecimal(2000);
        BigDecimal expClosingSurplus = new BigDecimal(500);
        monthServices.calculateMonthTotals(month);
        assertEquals(expClosingBalanceTarget, month.getClosingBalanceTarget().getAmount());
        assertEquals(expEstimatedClosingBalance, month.getEstimatedClosingBalance().getAmount());
        assertEquals(expClosingSurplus, month.getClosingSurplus().getAmount());
    }

    @Test
    public void testCalculateMonthTotals_Closed() {
        System.out.println("calculateTotals: closed month");
        Month month = new Month();
        month.getRevenues().add(new BudgetItem("Total Revenues", new BigDecimal(2000))); // Not Used
        month.getExpenses().add(new BudgetItem("Total Expenses", new BigDecimal(1000))); // Not Used
        month.getAdjustments().add(new BudgetItem("Total Adjustments", new BigDecimal(0))); // Not Used
        month.setNetIncomeTarget(new BudgetItem("Net Income Target", new BigDecimal(500)));
        month.setOpeningBalance(new BudgetItem("Opening Balance", new BigDecimal(1000)));
        month.setClosingBalance(new BudgetItem("Closing Balance", new BigDecimal(1500)));

        month.setOpeningSurplus(new BudgetItem("Opening Surplus", new BigDecimal(0)));
        month.setClosingSurplus(new BudgetItem("Closing Surplus", new BigDecimal(0))); // Don't need to set this
        month.setIsClosed(true);

        BigDecimal expClosingSurplus = new BigDecimal(0);
        monthServices.calculateMonthTotals(month);
        assertEquals(expClosingSurplus, month.getClosingSurplus().getAmount());
    }

}
