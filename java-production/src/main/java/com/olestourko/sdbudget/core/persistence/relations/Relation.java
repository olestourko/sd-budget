package com.olestourko.sdbudget.core.persistence.relations;

import com.olestourko.sdbudget.core.models.Model;
import java.util.HashMap;
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
public abstract class Relation<
        FROMMODEL extends Model, TOMODEL extends Model, FROMRECORD extends UpdatableRecord<FROMRECORD>, TORECORD extends UpdatableRecord<TORECORD>> {

    private final DSLContext context;
    protected Table<FROMRECORD> relationTable;
    protected Table<TORECORD> toTable;
    protected TableField toTableId;
    protected TableField relationTableFieldFrom;
    protected TableField relationTableFieldTo;

    public Relation(DSLContext context) {
        this.context = context;
    }

    public void associate(FROMMODEL a, TOMODEL b) {
        Record testRecord = context.select()
                .from(relationTable)
                .where(relationTableFieldFrom.equal(a.getId()))
                .and(relationTableFieldTo.equal(b.getId()))
                .fetchOne();

        if (testRecord == null || testRecord.size() == 0) {
            FROMRECORD record = context.newRecord(relationTable);
            record.set(relationTableFieldFrom, a.getId());
            record.set(relationTableFieldTo, b.getId());
            record.store();
        }
    }

    public void associate(FROMMODEL a, TORECORD b) {
        Record testRecord = context.select()
                .from(relationTable)
                .where(relationTableFieldFrom.equal(a.getId()))
                .and(relationTableFieldTo.equal(getToRecordID(b)))
                .fetchOne();

        if (testRecord == null || testRecord.size() == 0) {
            FROMRECORD record = context.newRecord(relationTable);
            record.set(relationTableFieldFrom, a.getId());
            record.set(relationTableFieldTo, getToRecordID(b));
            record.store();
        }
    }

    public void disassociate(FROMMODEL a, TOMODEL b) {
        Result<FROMRECORD> result = context.select()
                .from(relationTable)
                .where(relationTableFieldFrom.equal(a.getId()))
                .and(relationTableFieldTo.equal(b.getId()))
                .fetch()
                .into(relationTable);

        // There *should* only be a maximum of one result.
        for (FROMRECORD record : result) {
            record.delete();
        }
    }

    public void disassociate(FROMMODEL a, TORECORD b) {
        Result<FROMRECORD> result = context.select()
                .from(relationTable)
                .where(relationTableFieldFrom.equal(a.getId()))
                .and(relationTableFieldTo.equal(getToRecordID(b)))
                .fetch()
                .into(relationTable);

        // There *should* only be a maximum of one result.
        for (FROMRECORD record : result) {
            record.delete();
        }
    }
    
    public Result<TORECORD> load(FROMMODEL a) {
        Result<TORECORD> records = context.select()
                .from(toTable)
                .innerJoin(relationTable)
                .on(relationTableFieldFrom.equal(a.getId()))
                .and(relationTableFieldTo.equal(toTableId))
                .fetch()
                .into(toTable);

        return records;
    }

    public void syncToDB(FROMMODEL fromModel, HashMap<Integer, TOMODEL> toModels) {
        Result<TORECORD> records = this.load(fromModel);
        HashMap<Integer, TORECORD> storedRecords = new HashMap<>();
        for (TORECORD record : records) {
            storedRecords.put(getToRecordID(record), record);
        }

        // Delete stored revenues that aren't supposed to be part of the month anymore
        for (TORECORD toRecord : storedRecords.values()) {
            if (toModels.get(toRecord.get(toTableId)) == null) {
                this.disassociate(fromModel, toRecord);
            }
        }
        // Store revenues that are supposed to be part of the month
        for (TOMODEL toModel : toModels.values()) {
            if (storedRecords.get(toModel.getId()) == null) {
                this.associate(fromModel, toModel);
            }
        }
    }

    protected abstract Integer getFromRecordID(FROMRECORD record);

    protected abstract Integer getToRecordID(TORECORD record);
}
