/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olestourko.sdbudget.core.persistence;

import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import java.util.ArrayList;
import javax.inject.Inject;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import static org.jooq.util.maven.sdbudget.Tables.*;
import org.jooq.util.maven.sdbudget.tables.records.BudgetItemRecord;
import org.jooq.util.maven.sdbudget.tables.records.MonthRecord;
import org.jooq.util.maven.sdbudget.tables.records.MonthRevenuesRecord;

/**
 *
 * @author oles
 */
public class MonthPersistence implements IPersistance<Month> {

    private final DSLContext createDSLContext;

    @Inject
    public MonthPersistence(DSLContext createDSLContext) {
        this.createDSLContext = createDSLContext;
    }

    @Override
    public Month create() {
        MonthRecord record = createDSLContext.newRecord(MONTH);
        record.store();
        Month month = new Month();
        month.setId(record.getId());
        return month;
    }

    @Override
    public Month find(int id) {
        MonthRecord record = createDSLContext.fetchOne(MONTH, MONTH.ID.eq(id));
        if (record != null) {
            Month month = new Month();
            month.setId(record.getId());
            month.setNumber(record.getNumber());
            month.setYear(record.getYear());
            return month;
        }

        return null;
    }

    @Override
    public void store(Month model) {
        MonthRecord record = createDSLContext.fetchOne(MONTH, MONTH.ID.eq(model.getId()));
        if (record == null) {
            record = createDSLContext.newRecord(MONTH);
        }

        if (model.getId() != 0) {
            record.setId(model.getId());
        }

        record.setNumber(model.getNumber());
        record.setYear(model.getYear());
        record.store();

        if (model.getId() == 0) {
            model.setId(record.getId());
        }
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
            Month month = new Month();
            month.setId(monthRecord.getId());
            month.setNumber(monthRecord.getNumber());
            month.setYear(monthRecord.getYear());
            months.add(month);
        }

        // Fetch associated revenues
        for(Month month : months) {
            fetchRevenues(month);
        }
        
        return months;
    }

    // Associations
    public void associateRevenue(Month month, BudgetItem budgetItem) {
        Record testRecord = createDSLContext.select()
                .from(MONTH_REVENUES)
                .where(MONTH_REVENUES.MONTH_ID.equal(month.getId()))
                .and(MONTH_REVENUES.BUDGET_ITEM_ID.equal(budgetItem.getId()))
                .fetchOne();
        
        if(testRecord == null || testRecord.size() == 0) {
            MonthRevenuesRecord record = createDSLContext.newRecord(MONTH_REVENUES);
            record.set(MONTH_REVENUES.MONTH_ID, month.getId());
            record.set(MONTH_REVENUES.BUDGET_ITEM_ID, budgetItem.getId());
            record.store();
        }
    }

    public void fetchRevenues(Month month) {
        Result<BudgetItemRecord> records = createDSLContext.select()
                .from(BUDGET_ITEM)
                .innerJoin(MONTH_REVENUES)
                .on(MONTH_REVENUES.MONTH_ID.equal(month.getId()))
                .and(MONTH_REVENUES.BUDGET_ITEM_ID.equal(BUDGET_ITEM.ID))
                .fetch()
                .into(BUDGET_ITEM);

        for(BudgetItemRecord record : records) {
            BudgetItem budgetItem = new BudgetItem();
            budgetItem.setId(record.getId());
            budgetItem.setName(record.getName());
            budgetItem.setAmount(record.getAmount());
            month.getRevenues().add(budgetItem);
        }
    }
}
