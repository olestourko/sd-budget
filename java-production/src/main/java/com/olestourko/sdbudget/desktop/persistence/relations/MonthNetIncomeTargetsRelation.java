package com.olestourko.sdbudget.desktop.persistence.relations;

import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import javax.inject.Inject;
import org.jooq.DSLContext;
import static org.jooq.util.maven.sdbudget.Tables.*;
import org.jooq.util.maven.sdbudget.tables.records.BudgetItemRecord;
import org.jooq.util.maven.sdbudget.tables.records.MonthNetIncomeTargetsRecord;

public class MonthNetIncomeTargetsRelation extends Relation<Month, BudgetItem, MonthNetIncomeTargetsRecord, BudgetItemRecord> {

    @Inject
    public MonthNetIncomeTargetsRelation(DSLContext context) {
        super(context);
        this.relationTable = MONTH_NET_INCOME_TARGETS;
        this.toTable = BUDGET_ITEM;
        this.relationTableFieldFrom = MONTH_NET_INCOME_TARGETS.MONTH_ID;
        this.relationTableFieldTo = MONTH_NET_INCOME_TARGETS.BUDGET_ITEM_ID;
        this.toTableId = BUDGET_ITEM.ID;
    }

    @Override
    protected Integer getFromRecordID(MonthNetIncomeTargetsRecord record) {
        return record.getId();
    }

    @Override
    protected Integer getToRecordID(BudgetItemRecord record) {
        return record.getId();
    }

}
