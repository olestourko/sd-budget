/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olestourko.sdbudget.services;

/**
 *
 * @author oles
 */
public class PeriodServices {

    public Estimate calculateEstimate(double revenue, double expenses, double adjustments, double incomeTarget, double openingBalance) {
        double netIncome = revenue - expenses + adjustments;
        double estimatedBalance = openingBalance + netIncome;
        double expectedBalance = openingBalance + incomeTarget;
        double surplus = estimatedBalance - expectedBalance;
        
        return new Estimate(netIncome, estimatedBalance, expectedBalance, surplus);
    }
    
    public Closing calculateClosing(double incomeTarget, double openingBalance, double closingBalance) {
        double closingBalanceTarget = openingBalance + incomeTarget;
        double surplus = closingBalance - closingBalanceTarget;
        double closingAdjustment = surplus;
        
        return new Closing(surplus, closingAdjustment);
    }
    
}

final class Estimate {
    public double netIncome;
    public double estimatedBalance;
    public double expectedBalance;
    public double surplus;
    
    public Estimate(double netIncome, double estimatedBalance, double expectedBalance, double surplus) {
        this.netIncome = netIncome;
        this.estimatedBalance = estimatedBalance;
        this.expectedBalance = expectedBalance;
        this.surplus = surplus;
    }
}

final class Closing {
    public double surplus;
    public double closingAdjustment;
    
    public Closing(double surplus, double closingAdjustment) {
        this.surplus = surplus;
        this.closingAdjustment = closingAdjustment;
    }
}