package com.olestourko.sdbudget.core.persistence.relations;

import com.olestourko.sdbudget.core.models.Model;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UpdatableRecord;

/**
 *
 * @author oles
 */
public abstract class Relation<R extends UpdatableRecord<R>, S extends UpdatableRecord<S>> {
    
    private final DSLContext context;
    protected Table<R> relationTable;
    protected Table<S> toTable;
    protected TableField toTableId;
    protected TableField relationTableFieldFrom;
    protected TableField relationTableFieldTo;
    
    public Relation(DSLContext context) {
        this.context = context;
    }
    
    public void associate(Model a, Model b) {
        Record testRecord = context.select()
                .from(relationTable)
                .where(relationTableFieldFrom.equal(a.getId()))
                .and(relationTableFieldTo.equal(b.getId()))
                .fetchOne();
        
        if(testRecord == null || testRecord.size() == 0) {
            R record = context.newRecord(relationTable);
            record.set(relationTableFieldFrom, a.getId());
            record.set(relationTableFieldTo, b.getId());
            record.store();
        }
    }
    
    public Result<S> load(Model a) {
        Result<S> records = context.select()
                .from(toTable)
                .innerJoin(relationTable)
                .on(relationTableFieldFrom.equal(a.getId()))
                .and(relationTableFieldTo.equal(toTableId))
                .fetch()
                .into(toTable);
        
        return records;
    }
}
