package com.olestourko.sdbudget.core.persistence;

import com.olestourko.sdbudget.core.mappers.BudgetItemMapper;
import com.olestourko.sdbudget.core.mappers.MonthMapper;
import com.olestourko.sdbudget.core.persistence.relations.MonthRevenuesRelation;
import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.core.persistence.relations.MonthAdjustmentsRelation;
import com.olestourko.sdbudget.core.persistence.relations.MonthClosingBalancesRelation;
import com.olestourko.sdbudget.core.persistence.relations.MonthDebtRepaymentsRelation;
import com.olestourko.sdbudget.core.persistence.relations.MonthExpensesRelation;
import com.olestourko.sdbudget.core.persistence.relations.MonthInvestmentOutflowsRelation;
import com.olestourko.sdbudget.core.persistence.relations.MonthNetIncomeTargetsRelation;
import com.olestourko.sdbudget.core.persistence.relations.MonthOpeningBalancesRelation;
import java.util.ArrayList;
import java.util.HashMap;
import javax.inject.Inject;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import static org.jooq.util.maven.sdbudget.Tables.*;
import org.jooq.util.maven.sdbudget.tables.records.BudgetItemRecord;
import org.jooq.util.maven.sdbudget.tables.records.MonthRecord;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author oles
 */
public class MonthPersistence implements IPersistance<Month> {

    private final DSLContext createDSLContext;
    private final MonthMapper monthMapper;
    private final BudgetItemMapper budgetItemMapper;

    /* Relations */
    private final MonthRevenuesRelation monthRevenuesRelation;
    private final MonthExpensesRelation monthExpensesRelation;
    private final MonthAdjustmentsRelation monthAdjustmentsRelation;
    private final MonthDebtRepaymentsRelation monthDebtRepaymentsRelation;
    private final MonthInvestmentOutflowsRelation monthInvestmentOuflowsRelation;
    private final MonthNetIncomeTargetsRelation monthNetIncomeTargetsRelation;
    private final MonthOpeningBalancesRelation monthOpeningBalanceRelation;
    private final MonthClosingBalancesRelation monthClosingBalanceRelation;

    @Inject
    public MonthPersistence(
            DSLContext createDSLContext,
            MonthRevenuesRelation monthRevenuesRelation,
            MonthExpensesRelation monthExpensesRelation,
            MonthAdjustmentsRelation monthAdjustmentsRelation,
            MonthDebtRepaymentsRelation monthDebtRepaymentsRelation,
            MonthInvestmentOutflowsRelation monthInvestmentOutflowsRelation,
            MonthNetIncomeTargetsRelation monthNetIncomeTargetsRelation,
            MonthOpeningBalancesRelation monthOpeningBalanceRelation,
            MonthClosingBalancesRelation monthClosingBalanceRelation
    ) {
        this.createDSLContext = createDSLContext;
        this.monthMapper = Mappers.getMapper(MonthMapper.class);
        this.budgetItemMapper = Mappers.getMapper(BudgetItemMapper.class);

        this.monthRevenuesRelation = monthRevenuesRelation;
        this.monthExpensesRelation = monthExpensesRelation;
        this.monthAdjustmentsRelation = monthAdjustmentsRelation;
        this.monthDebtRepaymentsRelation = monthDebtRepaymentsRelation;
        this.monthInvestmentOuflowsRelation = monthInvestmentOutflowsRelation;
        this.monthNetIncomeTargetsRelation = monthNetIncomeTargetsRelation;
        this.monthOpeningBalanceRelation = monthOpeningBalanceRelation;
        this.monthClosingBalanceRelation = monthClosingBalanceRelation;
    }

    @Override
    public Month create() {
        MonthRecord record = createDSLContext.newRecord(MONTH);
        record.store();
        return monthMapper.mapMonthRecordToMonth(record);
    }

    @Override
    public Month find(int id) {
        MonthRecord record = createDSLContext.fetchOne(MONTH, MONTH.ID.eq(id));
        if (record != null) {
            return this.monthMapper.mapMonthRecordToMonth(record);
        }

        return null;
    }

    @Override
    public Month store(Month model) {
        MonthRecord record = createDSLContext.fetchOne(MONTH, MONTH.ID.eq(model.getId()));
        if (record == null) {
            record = createDSLContext.newRecord(MONTH);
        }

        monthMapper.updateMonthRecordFromMonth(model, record);
        record.store();

        if (model.getId() == 0) {
            model.setId(record.getId());
        }

        return model;
    }

    @Override
    public void delete(Month model) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ArrayList<Month> getAllMonths() {
        Result<Record> records = createDSLContext.select().from(MONTH).fetch();
        ArrayList<Month> months = new ArrayList<Month>();
        for (Record record : records) {
            MonthRecord monthRecord = (MonthRecord) record;
            Month month = this.monthMapper.mapMonthRecordToMonth(monthRecord);
            months.add(month);
        }

        // Fetch associated revenues
        for (Month month : months) {
            syncRevenuesFromDB(month);
            syncExpensesFromDB(month);
            syncAdjustmentsFromDB(month);
            syncDebtRepaymentsFromDB(month);
            syncInvestmentOutflowsFromDB(month);
            syncNetIncomeTargetFromDB(month);
            syncOpeningBalanceFromDB(month);
            syncClosingBalanceFromDB(month);
        }

        return months;
    }

    public void associateRevenue(Month month, BudgetItem budgetItem) {
        monthRevenuesRelation.associate(month, budgetItem);
    }

    public void syncRevenuesFromDB(Month month) {
        Result<BudgetItemRecord> records = monthRevenuesRelation.load(month);
        for (BudgetItemRecord record : records) {
            BudgetItem budgetItem = budgetItemMapper.mapBudgetItemRecordToBudgetItem(record);
            month.getRevenues().add(budgetItem);
        }
    }

    public void syncRevenuesToDB(Month month) {
        HashMap<Integer, BudgetItem> newRevenues = new HashMap<Integer, BudgetItem>();
        for (BudgetItem revenue : month.getRevenues()) {
            newRevenues.put(revenue.getId(), revenue);
        }
        monthRevenuesRelation.syncToDB(month, newRevenues);
    }

    public void associateExpense(Month month, BudgetItem budgetItem) {
        monthExpensesRelation.associate(month, budgetItem);
    }

    public void syncExpensesFromDB(Month month) {
        Result<BudgetItemRecord> records = monthExpensesRelation.load(month);

        for (BudgetItemRecord record : records) {
            BudgetItem budgetItem = budgetItemMapper.mapBudgetItemRecordToBudgetItem(record);
            month.getExpenses().add(budgetItem);
        }
    }

    public void syncExpensesToDB(Month month) {
        HashMap<Integer, BudgetItem> newExpenses = new HashMap<Integer, BudgetItem>();
        for (BudgetItem expense : month.getExpenses()) {
            newExpenses.put(expense.getId(), expense);
        }
        monthExpensesRelation.syncToDB(month, newExpenses);
    }

    public void associateAdjustment(Month month, BudgetItem budgetItem) {
        monthAdjustmentsRelation.associate(month, budgetItem);
    }

    public void syncAdjustmentsFromDB(Month month) {
        Result<BudgetItemRecord> records = monthAdjustmentsRelation.load(month);

        for (BudgetItemRecord record : records) {
            BudgetItem budgetItem = budgetItemMapper.mapBudgetItemRecordToBudgetItem(record);
            month.getAdjustments().add(budgetItem);
        }
    }

    public void syncAdjustmentsToDB(Month month) {
        HashMap<Integer, BudgetItem> newAdjustments = new HashMap<Integer, BudgetItem>();
        for (BudgetItem adjustment : month.getAdjustments()) {
            newAdjustments.put(adjustment.getId(), adjustment);
        }
        monthAdjustmentsRelation.syncToDB(month, newAdjustments);
    }

    public void associateDebtRepayments(Month month, BudgetItem budgetItem) {
        monthDebtRepaymentsRelation.associate(month, budgetItem);
    }

    public void syncDebtRepaymentsFromDB(Month month) {
        Result<BudgetItemRecord> records = monthDebtRepaymentsRelation.load(month);

        if (records.size() > 0) {
            BudgetItemRecord record = records.get(0);
            BudgetItem budgetItem = budgetItemMapper.mapBudgetItemRecordToBudgetItem(record);
            month.setDebtRepayments(budgetItem);
        }
    }

    public void associateInvestmentOuflows(Month month, BudgetItem budgetItem) {
        monthInvestmentOuflowsRelation.associate(month, budgetItem);
    }

    public void syncInvestmentOutflowsFromDB(Month month) {
        Result<BudgetItemRecord> records = monthInvestmentOuflowsRelation.load(month);

        if (records.size() > 0) {
            BudgetItemRecord record = records.get(0);
            BudgetItem budgetItem = budgetItemMapper.mapBudgetItemRecordToBudgetItem(record);
            month.setInvestmentOutflows(budgetItem);
        }
    }

    public void associateNetIncomeTarget(Month month, BudgetItem budgetItem) {
        monthNetIncomeTargetsRelation.associate(month, budgetItem);
    }

    public void syncNetIncomeTargetFromDB(Month month) {
        Result<BudgetItemRecord> records = monthNetIncomeTargetsRelation.load(month);

        if (records.size() > 0) {
            BudgetItemRecord record = records.get(0);
            BudgetItem budgetItem = budgetItemMapper.mapBudgetItemRecordToBudgetItem(record);
            month.setNetIncomeTarget(budgetItem);
        }
    }

    public void associateOpeningBalance(Month month, BudgetItem budgetItem) {
        monthOpeningBalanceRelation.associate(month, budgetItem);
    }

    public void syncOpeningBalanceFromDB(Month month) {
        Result<BudgetItemRecord> records = monthOpeningBalanceRelation.load(month);

        if (records.size() > 0) {
            BudgetItemRecord record = records.get(0);
            BudgetItem budgetItem = budgetItemMapper.mapBudgetItemRecordToBudgetItem(record);
            month.setOpeningBalance(budgetItem);
        }
    }

    public void associateClosingBalance(Month month, BudgetItem budgetItem) {
        monthClosingBalanceRelation.associate(month, budgetItem);
    }

    public void syncClosingBalanceFromDB(Month month) {
        Result<BudgetItemRecord> records = monthClosingBalanceRelation.load(month);

        if (records.size() > 0) {
            BudgetItemRecord record = records.get(0);
            BudgetItem budgetItem = budgetItemMapper.mapBudgetItemRecordToBudgetItem(record);
            month.setClosingBalance(budgetItem);
        }
    }

    public void removeUnusedBudgetItems() {
        Result<BudgetItemRecord> budgetItemRecords = createDSLContext.select(BUDGET_ITEM.ID, BUDGET_ITEM.NAME, BUDGET_ITEM.AMOUNT).from(BUDGET_ITEM)
                .leftJoin(MONTH_REVENUES).on(BUDGET_ITEM.ID.equal(MONTH_REVENUES.BUDGET_ITEM_ID))
                .leftJoin(MONTH_EXPENSES).on(BUDGET_ITEM.ID.equal(MONTH_EXPENSES.BUDGET_ITEM_ID))
                .leftJoin(MONTH_ADJUSTMENTS).on(BUDGET_ITEM.ID.equal(MONTH_ADJUSTMENTS.BUDGET_ITEM_ID))
                .leftJoin(MONTH_DEBT_REPAYMENTS).on(BUDGET_ITEM.ID.equal(MONTH_DEBT_REPAYMENTS.BUDGET_ITEM_ID))
                .leftJoin(MONTH_INVESTMENT_OUTFLOWS).on(BUDGET_ITEM.ID.equal(MONTH_INVESTMENT_OUTFLOWS.BUDGET_ITEM_ID))
                .leftJoin(MONTH_NET_INCOME_TARGETS).on(BUDGET_ITEM.ID.equal(MONTH_NET_INCOME_TARGETS.BUDGET_ITEM_ID))
                .leftJoin(MONTH_OPENING_BALANCES).on(BUDGET_ITEM.ID.equal(MONTH_OPENING_BALANCES.BUDGET_ITEM_ID))
                .leftJoin(MONTH_CLOSING_BALANCES).on(BUDGET_ITEM.ID.equal(MONTH_CLOSING_BALANCES.BUDGET_ITEM_ID))
                .where(MONTH_REVENUES.BUDGET_ITEM_ID.isNull())
                .and(MONTH_EXPENSES.BUDGET_ITEM_ID.isNull())
                .and(MONTH_ADJUSTMENTS.BUDGET_ITEM_ID.isNull())
                .and(MONTH_DEBT_REPAYMENTS.BUDGET_ITEM_ID.isNull())
                .and(MONTH_INVESTMENT_OUTFLOWS.BUDGET_ITEM_ID.isNull())
                .and(MONTH_NET_INCOME_TARGETS.BUDGET_ITEM_ID.isNull())
                .and(MONTH_OPENING_BALANCES.BUDGET_ITEM_ID.isNull())
                .and(MONTH_CLOSING_BALANCES.BUDGET_ITEM_ID.isNull())
                .fetch().into(BUDGET_ITEM);

        for (BudgetItemRecord budgetItemRecord : budgetItemRecords) {
            budgetItemRecord.delete();
        }

    }
}
