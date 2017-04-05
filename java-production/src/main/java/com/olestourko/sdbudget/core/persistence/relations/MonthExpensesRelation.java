package com.olestourko.sdbudget.core.persistence.relations;

import org.jooq.DSLContext;
import static org.jooq.util.maven.sdbudget.Tables.*;
import org.jooq.util.maven.sdbudget.tables.records.BudgetItemRecord;
import org.jooq.util.maven.sdbudget.tables.records.MonthExpensesRecord;

public class MonthExpensesRelation extends Relation<MonthExpensesRecord, BudgetItemRecord> {

    public MonthExpensesRelation(DSLContext context) {
        super(context);
        this.relationTable = MONTH_EXPENSES;
        this.toTable = BUDGET_ITEM;
        this.relationTableFieldFrom = MONTH_EXPENSES.MONTH_ID;
        this.relationTableFieldTo = MONTH_EXPENSES.BUDGET_ITEM_ID;
        this.toTableId = BUDGET_ITEM.ID;
    }

}
