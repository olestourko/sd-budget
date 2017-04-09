package com.olestourko.sdbudget.core.persistence;

import com.olestourko.sdbudget.core.mappers.BudgetItemMapper;
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
        BudgetItem budgetItem = new BudgetItem();
        budgetItem.setId(record.getId());
        return budgetItem;
    }

    @Override
    public BudgetItem find(int id) {
        BudgetItemRecord record = createDSLContext.fetchOne(BUDGET_ITEM, BUDGET_ITEM.ID.eq(id));
        if (record != null) {
            BudgetItem budgetItem = this.budgetItemMapper.mapBudgetItemRecordToBudgetItem(record);
            return budgetItem;
        }

        return null;
    }

    @Override
    public void store(BudgetItem model) {
        BudgetItemRecord record = createDSLContext.fetchOne(BUDGET_ITEM, BUDGET_ITEM.ID.eq(model.getId()));
        if (record == null) {
            record = createDSLContext.newRecord(BUDGET_ITEM);
        }

        if (model.getId() != 0) {
            record.setId(model.getId());
        }

        record.setName(model.getName());
        record.setAmount(model.getAmount());
        record.store();

        if (model.getId() == 0) {
            model.setId(record.getId());
        }
    }

    @Override
    public void delete(BudgetItem model) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
