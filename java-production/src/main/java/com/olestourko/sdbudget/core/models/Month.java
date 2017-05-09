package com.olestourko.sdbudget.core.models;

import java.io.Serializable;
import java.math.BigDecimal;
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
    private BudgetItem debtRepayments = new BudgetItem("Debt Repayments", BigDecimal.ZERO);
    private BudgetItem investmentOutflows = new BudgetItem("Investment Outflows", BigDecimal.ZERO);
    private BudgetItem netIncomeTarget = new BudgetItem("Net Income Target", BigDecimal.ZERO);
    private BudgetItem openingBalance = new BudgetItem("Opening Balance", BigDecimal.ZERO);
    private BudgetItem closingBalance = new BudgetItem("Closing Balance", BigDecimal.ZERO);

    // These aren't meant to be stored in DB
    private BudgetItem openingSurplus = new BudgetItem("Opening Surplus", BigDecimal.ZERO);
    private BudgetItem closingSurplus = new BudgetItem("Closing Surplus", BigDecimal.ZERO);
    private BudgetItem closingBalanceTarget = new BudgetItem("Closing Balance Target", BigDecimal.ZERO);
    private BudgetItem estimatedClosingBalance = new BudgetItem("Estimated Closing Balance", BigDecimal.ZERO);

    // Static factory method
    // https://dzone.com/articles/consider-static-factory-methods-instead-of-constru
    public static Month createMonth(int number, int year) {
        Month month = new Month();
        month.setNumber((short) number);
        month.setYear((short) year);
        return month;
    }

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

// <editor-fold defaultstate="collapsed" desc="Revenue/Expense/Totals aggregators">
    public BigDecimal getTotalRevenues() {
        return revenues.stream().map(BudgetItem::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalExpenses() {
        return expenses.stream().map(BudgetItem::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalAdjustments() {
        return adjustments.stream().map(BudgetItem::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
// </editor-fold>

    public BudgetItem getDebtRepayments() {
        return debtRepayments;
    }

    public void setDebtRepayments(BudgetItem debtRepayments) {
        this.debtRepayments = debtRepayments;
    }

    public BudgetItem getInvestmentOutflows() {
        return this.investmentOutflows;
    }

    public void setInvestmentOutflows(BudgetItem investmentOutflows) {
        this.investmentOutflows = investmentOutflows;
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

// <editor-fold defaultstate="collapsed" desc="Properties not to be saved to DB">    
    public BudgetItem getOpeningSurplus() {
        return openingSurplus;
    }

    public void setOpeningSurplus(BudgetItem openingSurplus) {
        this.openingSurplus = openingSurplus;
    }

    public BudgetItem getClosingSurplus() {
        return closingSurplus;
    }

    public void setClosingSurplus(BudgetItem closingSurplus) {
        this.closingSurplus = closingSurplus;
    }

    public BudgetItem getClosingBalanceTarget() {
        return closingBalanceTarget;
    }

    public void setClosingBalanceTarget(BudgetItem closingBalanceTarget) {
        this.closingBalanceTarget = closingBalanceTarget;
    }

    public BudgetItem getEstimatedClosingBalance() {
        return estimatedClosingBalance;
    }

    public void setEstimatedClosingBalance(BudgetItem estimatedClosingBalance) {
        this.estimatedClosingBalance = estimatedClosingBalance;
    }
    // </editor-fold>
}
