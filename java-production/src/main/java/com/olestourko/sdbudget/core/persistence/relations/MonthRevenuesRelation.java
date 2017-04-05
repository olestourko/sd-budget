package com.olestourko.sdbudget.core.persistence.relations;

import org.jooq.DSLContext;
import org.jooq.util.maven.sdbudget.tables.records.MonthRevenuesRecord;
import static org.jooq.util.maven.sdbudget.Tables.*;
import org.jooq.util.maven.sdbudget.tables.records.BudgetItemRecord;

public class MonthRevenuesRelation extends Relation<MonthRevenuesRecord, BudgetItemRecord> {

    public MonthRevenuesRelation(DSLContext context) {
        super(context);
        this.relationTable = MONTH_REVENUES;
        this.toTable = BUDGET_ITEM;
        this.relationTableFieldFrom = MONTH_REVENUES.MONTH_ID;
        this.relationTableFieldTo = MONTH_REVENUES.BUDGET_ITEM_ID;
        this.toTableId = BUDGET_ITEM.ID;
    }

}
