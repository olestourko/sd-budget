package com.olestourko.sdbudget.desktop.mappers.persistence;

import com.olestourko.sdbudget.core.models.BudgetItem;
import org.jooq.util.maven.sdbudget.tables.records.BudgetItemRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

/**
 *
 * @author oles
 */
@Mapper
public interface BudgetItemMapper {

    @Mappings({
        @Mapping(source = "id", target = "id"),
        @Mapping(source = "name", target = "name"),
        @Mapping(source = "amount", target = "amount")
    })
    BudgetItem mapBudgetItemRecordToBudgetItem(BudgetItemRecord budgetItemRecord);

    default BudgetItemRecord updateBudgetItemRecordFromBudgetItem(BudgetItem budgetItem, @MappingTarget BudgetItemRecord budgetItemRecord) {
        if (budgetItem.getId() != 0) {
            budgetItemRecord.setId(budgetItem.getId());
        }
        budgetItemRecord.setName(budgetItem.getName());
        budgetItemRecord.setAmount(budgetItem.getAmount());
        return budgetItemRecord;
    }
}
