package com.olestourko.sdbudget.core.persistence.relations;

import org.jooq.DSLContext;
import org.jooq.util.maven.sdbudget.tables.records.MonthRevenuesRecord;
import static org.jooq.util.maven.sdbudget.Tables.*;

public class MonthRevenuesRelation extends Relation<MonthRevenuesRecord> {

    public MonthRevenuesRelation(DSLContext context) {
        super(context);
        this.relationTable = MONTH_REVENUES;
        this.relationTableFieldFrom = MONTH_REVENUES.MONTH_ID;
        this.relationTableFieldTo = MONTH_REVENUES.BUDGET_ITEM_ID;
    }

}
