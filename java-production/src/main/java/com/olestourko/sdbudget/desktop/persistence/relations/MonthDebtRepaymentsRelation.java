package com.olestourko.sdbudget.desktop.persistence.relations;

import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import javax.inject.Inject;
import org.jooq.DSLContext;
import static org.jooq.util.maven.sdbudget.Tables.*;
import org.jooq.util.maven.sdbudget.tables.records.BudgetItemRecord;
import org.jooq.util.maven.sdbudget.tables.records.MonthDebtRepaymentsRecord;

public class MonthDebtRepaymentsRelation extends Relation<Month, BudgetItem, MonthDebtRepaymentsRecord, BudgetItemRecord> {

    @Inject
    public MonthDebtRepaymentsRelation(DSLContext context) {
        super(context);
        this.relationTable = MONTH_DEBT_REPAYMENTS;
        this.toTable = BUDGET_ITEM;
        this.relationTableFieldFrom = MONTH_DEBT_REPAYMENTS.MONTH_ID;
        this.relationTableFieldTo = MONTH_DEBT_REPAYMENTS.BUDGET_ITEM_ID;
        this.toTableId = BUDGET_ITEM.ID;
    }

    @Override
    protected Integer getFromRecordID(MonthDebtRepaymentsRecord record) {
        return record.getId();
    }

    @Override
    protected Integer getToRecordID(BudgetItemRecord record) {
        return record.getId();
    }

}
