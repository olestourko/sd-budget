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
import java.math.BigInteger;

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
        BigDecimal debtRepayments = new BigDecimal(BigInteger.ZERO);
        BigDecimal investmentOutflows = new BigDecimal(BigInteger.ZERO);
        BigDecimal incomeTarget = new BigDecimal("500.0");
        BigDecimal openingBalance = new BigDecimal(BigInteger.ZERO);
        BigDecimal carriedSurplus = new BigDecimal(BigInteger.ZERO);
        PeriodCalculationServices instance = new PeriodCalculationServices();
        EstimateResult result = instance.calculateEstimate(
                revenue,
                expenses,
                adjustments,
                debtRepayments,
                investmentOutflows,
                incomeTarget,
                openingBalance,
                carriedSurplus
        );
        assertEquals(new BigDecimal("750.0").compareTo(result.netIncome), 0);
        assertEquals(new BigDecimal("750.0").compareTo(result.estimatedBalance), 0);
        assertEquals(new BigDecimal("500.0").compareTo(result.expectedBalance), 0);
        assertEquals(new BigDecimal("250.0").compareTo(result.surplus), 0);
    }

    /**
     * Test of calculateClosing method, of class PeriodServices.
     */
    @Test
    public void testCalculateClosing() {
        System.out.println("calculateClosing");
        BigDecimal incomeTarget = new BigDecimal("500.0");
        BigDecimal openingBalance = new BigDecimal(BigInteger.ZERO);
        BigDecimal debtRepayments = new BigDecimal(BigInteger.ZERO);
        BigDecimal investmentOutflows = new BigDecimal(BigInteger.ZERO);
        BigDecimal closingBalance = new BigDecimal("600.0");
        BigDecimal carriedSurplus = new BigDecimal(BigInteger.ZERO);
        PeriodCalculationServices instance = new PeriodCalculationServices();
        ClosingResult result = instance.calculateClosing(
                incomeTarget,
                debtRepayments,
                investmentOutflows,
                openingBalance,
                closingBalance,
                carriedSurplus
        );
        assertEquals(new BigDecimal("100.0").compareTo(result.closingSurplus), 0);
        assertEquals(new BigDecimal("100.0").compareTo(result.closingAdjustment), 0);
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
        BigDecimal debtRepayments = new BigDecimal(BigInteger.ZERO);
        BigDecimal investmentOutflows = new BigDecimal(BigInteger.ZERO);
        BigDecimal incomeTarget = new BigDecimal("500.0");
        BigDecimal openingBalance = new BigDecimal(BigInteger.ZERO);
        BigDecimal carriedSurplus = new BigDecimal("100.0");
        PeriodCalculationServices instance = new PeriodCalculationServices();
        EstimateResult result = instance.calculateEstimate(
                revenue,
                expenses,
                adjustments,
                debtRepayments,
                investmentOutflows,
                incomeTarget,
                openingBalance,
                carriedSurplus
        );
        assertEquals(new BigDecimal("750.0").compareTo(result.netIncome), 0);
        assertEquals(new BigDecimal("850.0").compareTo(result.estimatedBalance), 0);
        assertEquals(new BigDecimal("500.0").compareTo(result.expectedBalance), 0);
        assertEquals(new BigDecimal("350.0").compareTo(result.surplus), 0);
    }

    /**
     * Test of carried surplus on closing calculation
     */
    @Test
    public void testClosing_CarriedSurplus() {
        System.out.println("calculateClosing");
        BigDecimal incomeTarget = new BigDecimal("500.0");
        BigDecimal debtRepayments = new BigDecimal(BigInteger.ZERO);
        BigDecimal investmentOutflows = new BigDecimal(BigInteger.ZERO);
        BigDecimal openingBalance = new BigDecimal(BigInteger.ZERO);
        BigDecimal closingBalance = new BigDecimal("600.0");
        BigDecimal carriedSurplus = new BigDecimal("100.0");
        PeriodCalculationServices instance = new PeriodCalculationServices();
        ClosingResult result = instance.calculateClosing(
                incomeTarget,
                debtRepayments,
                investmentOutflows,
                openingBalance,
                closingBalance,
                carriedSurplus
        );
        assertEquals(new BigDecimal("200.0").compareTo(result.closingSurplus), 0);
        assertEquals(new BigDecimal("100.0").compareTo(result.closingAdjustment), 0);
    }

    /**
     * Test of investment outflows on estimate calculation
     */
    @Test
    public void testEstimate_InvestmentOutflows() {
        BigDecimal revenues = new BigDecimal(BigInteger.ZERO);
        BigDecimal expenses = new BigDecimal(BigInteger.ZERO);
        BigDecimal adjustments = new BigDecimal(BigInteger.ZERO);
        BigDecimal incomeTarget = new BigDecimal(BigInteger.ZERO);
        BigDecimal openingBalance = new BigDecimal(BigInteger.ZERO);
        BigDecimal investmentOutflows = new BigDecimal("100.0");
        BigDecimal debtRepayments = new BigDecimal(BigInteger.ZERO);
        BigDecimal carriedSurplus = new BigDecimal(BigInteger.ZERO);
        PeriodCalculationServices instance = new PeriodCalculationServices();
        EstimateResult result = instance.calculateEstimate(
                revenues,
                expenses,
                adjustments,
                debtRepayments,
                investmentOutflows,
                incomeTarget,
                openingBalance,
                carriedSurplus
        );
        assertEquals(new BigDecimal("-100.0").compareTo(result.expectedBalance), 0);
        assertEquals(new BigDecimal("-100.0").compareTo(result.estimatedBalance), 0);
        assertEquals(new BigDecimal("0.0").compareTo(result.surplus), 0);
    }

    /**
     * Test of investment outflows on estimate calculation
     */
    @Test
    public void testEstimate_DebtRepayments() {
        BigDecimal revenues = new BigDecimal(BigInteger.ZERO);
        BigDecimal expenses = new BigDecimal(BigInteger.ZERO);
        BigDecimal adjustments = new BigDecimal(BigInteger.ZERO);
        BigDecimal incomeTarget = new BigDecimal(BigInteger.ZERO);
        BigDecimal openingBalance = new BigDecimal(BigInteger.ZERO);
        BigDecimal investmentOutflows = new BigDecimal(BigInteger.ZERO);
        BigDecimal debtRepayments = new BigDecimal("100.00");
        BigDecimal carriedSurplus = new BigDecimal(BigInteger.ZERO);
        PeriodCalculationServices instance = new PeriodCalculationServices();
        EstimateResult result = instance.calculateEstimate(
                revenues,
                expenses,
                adjustments,
                debtRepayments,
                investmentOutflows,
                incomeTarget,
                openingBalance,
                carriedSurplus
        );
        assertEquals(new BigDecimal("-100.0").compareTo(result.expectedBalance), 0);
        assertEquals(new BigDecimal("-100.00").compareTo(result.estimatedBalance), 0);
        assertEquals(new BigDecimal("0.0").compareTo(result.surplus), 0);
    }
}
