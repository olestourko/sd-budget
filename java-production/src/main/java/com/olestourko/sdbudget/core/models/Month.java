package com.olestourko.sdbudget.core.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author oles
 */
public class Month extends Model implements Serializable {

    private short number;
    private short year;
    private boolean isClosed;
    private ArrayList<BudgetItem> revenues = new ArrayList<BudgetItem>();
    private ArrayList<BudgetItem> expenses = new ArrayList<BudgetItem>();
    private ArrayList<BudgetItem> adjustments = new ArrayList<BudgetItem>();
    private BudgetItem netIncomeTarget;
    private BudgetItem openingBalance;
    private BudgetItem closingBalance;

    public short getNumber() {
        return number;
    }

    public void setNumber(short number) {
        this.number = number;
    }

    public short getYear() {
        return year;
    }

    public void setYear(short year) {
        this.year = year;
    }

    public boolean getIsClosed() {
        return this.isClosed;
    }

    public void setIsClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }

    public ArrayList<BudgetItem> getRevenues() {
        return revenues;
    }

    public void setRevenues(ArrayList<BudgetItem> revenues) {
        this.revenues = revenues;
    }

    public ArrayList<BudgetItem> getExpenses() {
        return expenses;
    }

    public void setExpenses(ArrayList<BudgetItem> expenses) {
        this.expenses = expenses;
    }

    public ArrayList<BudgetItem> getAdjustments() {
        return adjustments;
    }

    public void setAdjustments(ArrayList<BudgetItem> adjustments) {
        this.adjustments = adjustments;
    }

    public BudgetItem getNetIncomeTarget() {
        return netIncomeTarget;
    }

    public void setNetIncomeTarget(BudgetItem netIncomeTarget) {
        this.netIncomeTarget = netIncomeTarget;
    }

    public BudgetItem getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(BudgetItem openingBalance) {
        this.openingBalance = openingBalance;
    }

    public BudgetItem getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(BudgetItem closingBalance) {
        this.closingBalance = closingBalance;
    }
}
