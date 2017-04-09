package com.olestourko.sdbudget.core.mappers;

import com.olestourko.sdbudget.core.models.BudgetItem;
import org.jooq.util.maven.sdbudget.tables.records.BudgetItemRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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

    @Mappings({
        @Mapping(source = "id", target = "id"),
        @Mapping(source = "name", target = "name"),
        @Mapping(source = "amount", target = "amount")
    })
    BudgetItemRecord mapBudgetItemToBudgetItemRecord(BudgetItem budgetItem);
}
