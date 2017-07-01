package com.olestourko.sdbudget.desktop.persistence;

import com.olestourko.sdbudget.desktop.mappers.persistence.BudgetItemMapper;
import com.olestourko.sdbudget.core.models.BudgetItem;
import javax.inject.Inject;
import org.jooq.DSLContext;
import static org.jooq.util.maven.sdbudget.Tables.*;
import org.jooq.util.maven.sdbudget.tables.records.BudgetItemRecord;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author oles
 */
public class BudgetItemPersistence implements IPersistance<BudgetItem> {

    private final DSLContext createDSLContext;
    private final BudgetItemMapper budgetItemMapper;

    @Inject
    public BudgetItemPersistence(DSLContext createDSLContext) {
        this.createDSLContext = createDSLContext;
        this.budgetItemMapper = Mappers.getMapper(BudgetItemMapper.class);
    }

    @Override
    public BudgetItem create() {
        BudgetItemRecord record = createDSLContext.newRecord(BUDGET_ITEM);
        record.store();
        return budgetItemMapper.mapBudgetItemRecordToBudgetItem(record);
    }

    @Override
    public BudgetItem find(int id) {
        BudgetItemRecord record = createDSLContext.fetchOne(BUDGET_ITEM, BUDGET_ITEM.ID.eq(id));
        if (record != null) {
            return this.budgetItemMapper.mapBudgetItemRecordToBudgetItem(record);
        }

        return null;
    }

    @Override
    public BudgetItem store(BudgetItem model) {
        BudgetItemRecord record = createDSLContext.fetchOne(BUDGET_ITEM, BUDGET_ITEM.ID.eq(model.getId()));
        if (record == null) {
            record = createDSLContext.newRecord(BUDGET_ITEM);
        }

        budgetItemMapper.updateBudgetItemRecordFromBudgetItem(model, record);
        record.store();

        if (model.getId() == 0) {
            model.setId(record.getId());
        }

        return model;
    }

    @Override
    public void delete(BudgetItem model) {
        BudgetItemRecord record = createDSLContext.fetchOne(BUDGET_ITEM, BUDGET_ITEM.ID.eq(model.getId()));
        if (record != null) {
            record.delete();
        }
    }
}
