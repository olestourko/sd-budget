package com.olestourko.sdbudget.desktop.persistence.relations;

import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import javax.inject.Inject;
import org.jooq.DSLContext;
import static org.jooq.util.maven.sdbudget.Tables.*;
import org.jooq.util.maven.sdbudget.tables.records.BudgetItemRecord;
import org.jooq.util.maven.sdbudget.tables.records.MonthExpensesRecord;

public class MonthExpensesRelation extends Relation<Month, BudgetItem, MonthExpensesRecord, BudgetItemRecord> {

    @Inject
    public MonthExpensesRelation(DSLContext context) {
        super(context);
        this.relationTable = MONTH_EXPENSES;
        this.toTable = BUDGET_ITEM;
        this.relationTableFieldFrom = MONTH_EXPENSES.MONTH_ID;
        this.relationTableFieldTo = MONTH_EXPENSES.BUDGET_ITEM_ID;
        this.toTableId = BUDGET_ITEM.ID;
    }
    
    @Override
    protected Integer getFromRecordID(MonthExpensesRecord record) {
        return record.getId();
    }

    @Override
    protected Integer getToRecordID(BudgetItemRecord record) {
        return record.getId();
    }
}
