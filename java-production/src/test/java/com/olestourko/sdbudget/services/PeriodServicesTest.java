/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olestourko.sdbudget.services;

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
        double revenue = 2000.0;
        double expenses = 1000.0;
        double adjustments = -250.0;
        double incomeTarget = 500.0;
        double openingBalance = 0.0;
        PeriodServices instance = new PeriodServices();
        EstimateResult result = instance.calculateEstimate(revenue, expenses, adjustments, incomeTarget, openingBalance);
        double delta = 0.01;
        assertEquals(result.netIncome, 750.0, delta);
        assertEquals(result.estimatedBalance, 750.0, delta);
        assertEquals(result.expectedBalance, 500.0, delta);
        assertEquals(result.surplus, 250.0, delta);
    }

    /**
     * Test of calculateClosing method, of class PeriodServices.
     */
    @Test
    public void testCalculateClosing() {
        System.out.println("calculateClosing");
        double incomeTarget = 500.0;
        double openingBalance = 0.0;
        double closingBalance = 600.0;
        PeriodServices instance = new PeriodServices();
        ClosingResult result = instance.calculateClosing(incomeTarget, openingBalance, closingBalance);
        double delta = 0.01;
        assertEquals(result.surplus, 100, delta);
        assertEquals(result.closingAdjustment, 100, delta);
    }

}
