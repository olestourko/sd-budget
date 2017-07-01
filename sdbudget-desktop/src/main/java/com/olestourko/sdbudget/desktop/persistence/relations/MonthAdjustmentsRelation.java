package com.olestourko.sdbudget.desktop.persistence.relations;

import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import javax.inject.Inject;
import org.jooq.DSLContext;
import static org.jooq.util.maven.sdbudget.Tables.*;
import org.jooq.util.maven.sdbudget.tables.records.BudgetItemRecord;
import org.jooq.util.maven.sdbudget.tables.records.MonthAdjustmentsRecord;

public class MonthAdjustmentsRelation extends Relation<Month, BudgetItem, MonthAdjustmentsRecord, BudgetItemRecord> {

    @Inject
    public MonthAdjustmentsRelation(DSLContext context) {
        super(context);
        this.relationTable = MONTH_ADJUSTMENTS;
        this.toTable = BUDGET_ITEM;
        this.relationTableFieldFrom = MONTH_ADJUSTMENTS.MONTH_ID;
        this.relationTableFieldTo = MONTH_ADJUSTMENTS.BUDGET_ITEM_ID;
        this.toTableId = BUDGET_ITEM.ID;
    }

    @Override
    protected Integer getFromRecordID(MonthAdjustmentsRecord record) {
        return record.getId();
    }

    @Override
    protected Integer getToRecordID(BudgetItemRecord record) {
        return record.getId();
    }

}
