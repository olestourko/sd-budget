package com.olestourko.sdbudget.core.persistence.relations;

import org.jooq.DSLContext;
import static org.jooq.util.maven.sdbudget.Tables.*;
import org.jooq.util.maven.sdbudget.tables.records.BudgetItemRecord;
import org.jooq.util.maven.sdbudget.tables.records.MonthOpeningBalancesRecord;

public class MonthOpeningBalancesRelation extends Relation<MonthOpeningBalancesRecord, BudgetItemRecord> {

    public MonthOpeningBalancesRelation(DSLContext context) {
        super(context);
        this.relationTable = MONTH_OPENING_BALANCES;
        this.toTable = BUDGET_ITEM;
        this.relationTableFieldFrom = MONTH_OPENING_BALANCES.MONTH_ID;
        this.relationTableFieldTo = MONTH_OPENING_BALANCES.BUDGET_ITEM_ID;
        this.toTableId = BUDGET_ITEM.ID;
    }

}
