package com.olestourko.sdbudget.core.mappers;

import com.olestourko.sdbudget.core.models.Month;
import org.jooq.util.maven.sdbudget.tables.records.MonthRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

/**
 *
 * @author oles
 */
@Mapper
public interface MonthMapper {

    @Mappings({
        @Mapping(source = "id", target = "id"),
        @Mapping(source = "number", target = "number"),
        @Mapping(source = "year", target = "year"),
        @Mapping(source = "isClosed", target = "isClosed")
    })
    Month mapMonthRecordToMonth(MonthRecord monthRecord);

    default MonthRecord updateMonthRecordFromMonth(Month month, @MappingTarget MonthRecord monthRecord) {
        if (month.getId() != 0) {
            monthRecord.setId(month.getId());
        }
        monthRecord.setNumber(month.getNumber());
        monthRecord.setYear(month.getYear());
        monthRecord.setIsClosed(month.getIsClosed());

        return monthRecord;
    }
}
