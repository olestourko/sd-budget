package com.olestourko.sdbudget.desktop.persistence.relations;

import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import javax.inject.Inject;
import org.jooq.DSLContext;
import static org.jooq.util.maven.sdbudget.Tables.*;
import org.jooq.util.maven.sdbudget.tables.records.BudgetItemRecord;
import org.jooq.util.maven.sdbudget.tables.records.MonthInvestmentOutflowsRecord;

public class MonthInvestmentOutflowsRelation extends Relation<Month, BudgetItem, MonthInvestmentOutflowsRecord, BudgetItemRecord> {

    @Inject
    public MonthInvestmentOutflowsRelation(DSLContext context) {
        super(context);
        this.relationTable = MONTH_INVESTMENT_OUTFLOWS;
        this.toTable = BUDGET_ITEM;
        this.relationTableFieldFrom = MONTH_INVESTMENT_OUTFLOWS.MONTH_ID;
        this.relationTableFieldTo = MONTH_INVESTMENT_OUTFLOWS.BUDGET_ITEM_ID;
        this.toTableId = BUDGET_ITEM.ID;
    }

    @Override
    protected Integer getFromRecordID(MonthInvestmentOutflowsRecord record) {
        return record.getId();
    }

    @Override
    protected Integer getToRecordID(BudgetItemRecord record) {
        return record.getId();
    }

}
