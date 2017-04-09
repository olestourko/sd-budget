package com.olestourko.sdbudget.desktop.mappers;

import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.desktop.models.MonthViewModel;
import java.util.Calendar;
import org.mapstruct.Mapper;

/**
 *
 * @author oles
 */
@Mapper
public abstract class MonthMapper {

    public MonthViewModel mapMonthToMonthViewModel(Month month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 0);
        cal.set(Calendar.MONTH, month.getNumber());
        cal.set(Calendar.YEAR, month.getYear());
        MonthViewModel viewModel = new MonthViewModel(cal);
        viewModel.setModel(month);
        return viewModel;
    }
}
