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
    private ArrayList<BudgetItem> revenues = new ArrayList<BudgetItem>();
    private ArrayList<BudgetItem> expenses = new ArrayList<BudgetItem>();
    private ArrayList<BudgetItem> adjustments = new ArrayList<BudgetItem>();

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
}
