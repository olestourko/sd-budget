package com.olestourko.sdbudget.core.persistence;

import com.olestourko.sdbudget.core.mappers.BudgetItemMapper;
import com.olestourko.sdbudget.core.mappers.MonthMapper;
import com.olestourko.sdbudget.core.persistence.relations.MonthRevenuesRelation;
import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.core.persistence.relations.MonthAdjustmentsRelation;
import com.olestourko.sdbudget.core.persistence.relations.MonthExpensesRelation;
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

    @Inject
    public MonthPersistence(DSLContext createDSLContext) {
        this.createDSLContext = createDSLContext;
        this.monthMapper = Mappers.getMapper(MonthMapper.class);
        this.budgetItemMapper = Mappers.getMapper(BudgetItemMapper.class);
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
            syncNetIncomeTargetFromDB(month);
            syncOpeningBalanceFromDB(month);
        }

        return months;
    }

    public void associateRevenue(Month month, BudgetItem budgetItem) {
        MonthRevenuesRelation relation = new MonthRevenuesRelation(createDSLContext);
        relation.associate(month, budgetItem);
    }

    public void syncRevenuesFromDB(Month month) {
        MonthRevenuesRelation relation = new MonthRevenuesRelation(createDSLContext);
        Result<BudgetItemRecord> records = relation.load(month);
        for (BudgetItemRecord record : records) {
            BudgetItem budgetItem = budgetItemMapper.mapBudgetItemRecordToBudgetItem(record);
            month.getRevenues().add(budgetItem);
        }
    }

    public void syncRevenuesToDB(Month month) {
        MonthRevenuesRelation relation = new MonthRevenuesRelation(createDSLContext);
        HashMap<Integer, BudgetItem> newRevenues = new HashMap<Integer, BudgetItem>();
        for (BudgetItem revenue : month.getRevenues()) {
            newRevenues.put(revenue.getId(), revenue);
        }
        relation.syncToDB(month, newRevenues);
    }

    public void associateExpense(Month month, BudgetItem budgetItem) {
        MonthExpensesRelation relation = new MonthExpensesRelation(createDSLContext);
        relation.associate(month, budgetItem);
    }

    public void syncExpensesFromDB(Month month) {
        MonthExpensesRelation relation = new MonthExpensesRelation(createDSLContext);
        Result<BudgetItemRecord> records = relation.load(month);

        for (BudgetItemRecord record : records) {
            BudgetItem budgetItem = budgetItemMapper.mapBudgetItemRecordToBudgetItem(record);
            month.getExpenses().add(budgetItem);
        }
    }

    public void syncExpensesToDB(Month month) {
        MonthExpensesRelation relation = new MonthExpensesRelation(createDSLContext);
        HashMap<Integer, BudgetItem> newExpenses = new HashMap<Integer, BudgetItem>();
        for (BudgetItem expense : month.getExpenses()) {
            newExpenses.put(expense.getId(), expense);
        }
        relation.syncToDB(month, newExpenses);
    }

    public void associateAdjustment(Month month, BudgetItem budgetItem) {
        MonthAdjustmentsRelation relation = new MonthAdjustmentsRelation(createDSLContext);
        relation.associate(month, budgetItem);
    }

    public void syncAdjustmentsFromDB(Month month) {
        MonthAdjustmentsRelation relation = new MonthAdjustmentsRelation(createDSLContext);
        Result<BudgetItemRecord> records = relation.load(month);

        for (BudgetItemRecord record : records) {
            BudgetItem budgetItem = budgetItemMapper.mapBudgetItemRecordToBudgetItem(record);
            month.getAdjustments().add(budgetItem);
        }
    }

    public void syncAdjustmentsToDB(Month month) {
        MonthAdjustmentsRelation relation = new MonthAdjustmentsRelation(createDSLContext);
        HashMap<Integer, BudgetItem> newAdjustments = new HashMap<Integer, BudgetItem>();
        for (BudgetItem adjustment : month.getAdjustments()) {
            newAdjustments.put(adjustment.getId(), adjustment);
        }
        relation.syncToDB(month, newAdjustments);
    }
    
    public void associateNetIncomeTarget(Month month, BudgetItem budgetItem) {
        MonthNetIncomeTargetsRelation relation = new MonthNetIncomeTargetsRelation(createDSLContext);
        relation.associate(month, budgetItem);
    }

    public void syncNetIncomeTargetFromDB(Month month) {
        MonthNetIncomeTargetsRelation relation = new MonthNetIncomeTargetsRelation(createDSLContext);
        Result<BudgetItemRecord> records = relation.load(month);

        if (records.size() > 0) {
            BudgetItemRecord record = records.get(0);
            BudgetItem budgetItem = budgetItemMapper.mapBudgetItemRecordToBudgetItem(record);
            month.setNetIncomeTarget(budgetItem);
        }
    }

    public void associateOpeningBalance(Month month, BudgetItem budgetItem) {
        MonthOpeningBalancesRelation relation = new MonthOpeningBalancesRelation(createDSLContext);
        relation.associate(month, budgetItem);
    }

    public void syncOpeningBalanceFromDB(Month month) {
        MonthOpeningBalancesRelation relation = new MonthOpeningBalancesRelation(createDSLContext);
        Result<BudgetItemRecord> records = relation.load(month);

        if (records.size() > 0) {
            BudgetItemRecord record = records.get(0);
            BudgetItem budgetItem = budgetItemMapper.mapBudgetItemRecordToBudgetItem(record);
            month.setOpeningBalance(budgetItem);
        }
    }
}
