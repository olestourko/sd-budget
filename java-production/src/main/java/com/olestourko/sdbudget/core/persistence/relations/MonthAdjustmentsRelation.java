package com.olestourko.sdbudget.core.persistence.relations;

import org.jooq.DSLContext;
import static org.jooq.util.maven.sdbudget.Tables.*;
import org.jooq.util.maven.sdbudget.tables.records.BudgetItemRecord;
import org.jooq.util.maven.sdbudget.tables.records.MonthAdjustmentsRecord;

public class MonthAdjustmentsRelation extends Relation<MonthAdjustmentsRecord, BudgetItemRecord> {

    public MonthAdjustmentsRelation(DSLContext context) {
        super(context);
        this.relationTable = MONTH_ADJUSTMENTS;
        this.toTable = BUDGET_ITEM;
        this.relationTableFieldFrom = MONTH_ADJUSTMENTS.MONTH_ID;
        this.relationTableFieldTo = MONTH_ADJUSTMENTS.BUDGET_ITEM_ID;
        this.toTableId = BUDGET_ITEM.ID;
    }

}
