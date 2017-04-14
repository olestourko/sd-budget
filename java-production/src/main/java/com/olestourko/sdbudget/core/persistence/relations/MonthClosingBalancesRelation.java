package com.olestourko.sdbudget.core.persistence.relations;

import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import javax.inject.Inject;
import org.jooq.DSLContext;
import static org.jooq.util.maven.sdbudget.Tables.*;
import org.jooq.util.maven.sdbudget.tables.records.BudgetItemRecord;
import org.jooq.util.maven.sdbudget.tables.records.MonthClosingBalancesRecord;

public class MonthClosingBalancesRelation extends Relation<Month, BudgetItem, MonthClosingBalancesRecord, BudgetItemRecord> {

    @Inject
    public MonthClosingBalancesRelation(DSLContext context) {
        super(context);
        this.relationTable = MONTH_CLOSING_BALANCES;
        this.toTable = BUDGET_ITEM;
        this.relationTableFieldFrom = MONTH_CLOSING_BALANCES.MONTH_ID;
        this.relationTableFieldTo = MONTH_CLOSING_BALANCES.BUDGET_ITEM_ID;
        this.toTableId = BUDGET_ITEM.ID;
    }

    @Override
    protected Integer getFromRecordID(MonthClosingBalancesRecord record) {
        return record.getId();
    }

    @Override
    protected Integer getToRecordID(BudgetItemRecord record) {
        return record.getId();
    }

}
