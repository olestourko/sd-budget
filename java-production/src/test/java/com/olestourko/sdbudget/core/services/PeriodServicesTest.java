/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olestourko.sdbudget.core.services;

import com.olestourko.sdbudget.core.services.PeriodCalculationServices;
import com.olestourko.sdbudget.core.services.ClosingResult;
import com.olestourko.sdbudget.core.services.EstimateResult;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.math.BigDecimal;

/**
 *
 * @author oles
 */
public class PeriodServicesTest {

    public PeriodServicesTest() {
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
     * Test of calculateEstimate method, of class PeriodServices.
     */
    @Test
    public void testCalculateEstimate() {
        System.out.println("calculateEstimate");
        BigDecimal revenue = new BigDecimal("2000.0");
        BigDecimal expenses = new BigDecimal("1000.0");
        BigDecimal adjustments = new BigDecimal("-250.0");
        BigDecimal incomeTarget = new BigDecimal("500.0");
        BigDecimal openingBalance = new BigDecimal("0.0");
        BigDecimal carriedSurplus = new BigDecimal("0.0");
        PeriodCalculationServices instance = new PeriodCalculationServices();
        EstimateResult result = instance.calculateEstimate(revenue, expenses, adjustments, incomeTarget, openingBalance, carriedSurplus);
        assertEquals(result.netIncome, new BigDecimal("750.0"));
        assertEquals(result.estimatedBalance, new BigDecimal("750.0"));
        assertEquals(result.expectedBalance, new BigDecimal("500.0"));
        assertEquals(result.surplus, new BigDecimal("250.0"));
    }

    /**
     * Test of calculateClosing method, of class PeriodServices.
     */
    @Test
    public void testCalculateClosing() {
        System.out.println("calculateClosing");
        BigDecimal incomeTarget = new BigDecimal("500.0");
        BigDecimal openingBalance = new BigDecimal("0.0");
        BigDecimal closingBalance = new BigDecimal("600.0");
        BigDecimal carriedSurplus = new BigDecimal("0.0");
        PeriodCalculationServices instance = new PeriodCalculationServices();
        ClosingResult result = instance.calculateClosing(incomeTarget, openingBalance, closingBalance, carriedSurplus);
        assertEquals(result.closingSurplus, new BigDecimal("100.0"));
        assertEquals(result.closingAdjustment, new BigDecimal("100.0"));
    }

    /**
     * Test of carried surplus on estimate calculation
     */
    @Test
    public void testEstimate_CarriedSurplus() {
        System.out.println("calculateClosing");
        BigDecimal revenue = new BigDecimal("2000.0");
        BigDecimal expenses = new BigDecimal("1000.0");
        BigDecimal adjustments = new BigDecimal("-250.0");
        BigDecimal incomeTarget = new BigDecimal("500.0");
        BigDecimal openingBalance = new BigDecimal("0.0");
        BigDecimal carriedSurplus = new BigDecimal("100.0");
        PeriodCalculationServices instance = new PeriodCalculationServices();
        EstimateResult result = instance.calculateEstimate(revenue, expenses, adjustments, incomeTarget, openingBalance, carriedSurplus);
        assertEquals(result.netIncome, new BigDecimal("750.0"));
        assertEquals(result.estimatedBalance, new BigDecimal("850.0"));
        assertEquals(result.expectedBalance, new BigDecimal("500.0"));
        assertEquals(result.surplus, new BigDecimal("350.0"));
    }

    /**
     * Test of carried surplus on closing calculation
     */
    @Test
    public void testClosing_CarriedSurplus() {
        System.out.println("calculateClosing");
        BigDecimal incomeTarget = new BigDecimal("500.0");
        BigDecimal openingBalance = new BigDecimal("0.0");
        BigDecimal closingBalance = new BigDecimal("600.0");
        BigDecimal carriedSurplus = new BigDecimal("100.0");
        PeriodCalculationServices instance = new PeriodCalculationServices();
        ClosingResult result = instance.calculateClosing(incomeTarget, openingBalance, closingBalance, carriedSurplus);
        assertEquals(result.closingSurplus, new BigDecimal("200.0"));
        assertEquals(result.closingAdjustment, new BigDecimal("100.0"));
    }

}
