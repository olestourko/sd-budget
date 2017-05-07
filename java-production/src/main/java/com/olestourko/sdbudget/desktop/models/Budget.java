package com.olestourko.sdbudget.desktop.models;

import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.core.services.MonthCalculationServices;
import com.olestourko.sdbudget.desktop.dagger.DesktopApplicationScope;
import javafx.beans.property.SimpleObjectProperty;
import javax.inject.Inject;

/**
 *
 * @author oles
 */
@DesktopApplicationScope
public class Budget {

    private final SimpleObjectProperty<Month> currentMonth = new SimpleObjectProperty<Month>();
    private final MonthRepository monthRepository;
    private final MonthCalculationServices monthCalculationServices;

    @Inject
    public Budget(MonthRepository monthRepository, MonthCalculationServices monthCalculationServices) {
        this.monthRepository = monthRepository;
        this.monthCalculationServices = monthCalculationServices;
    }

    public Month getCurrentMonth() {
        return this.currentMonth.get();
    }

    public void setCurrentMonth(Month month) {
        this.currentMonth.set(month);
    }

    public SimpleObjectProperty<Month> currentMonthProperty() {
        return this.currentMonth;
    }

    public void recalculateMonths(Month startingMonth) {
        Month currentMonth = startingMonth;
        while (currentMonth != null) {
            Month previousMonth = monthRepository.getPrevious(startingMonth);
            if (previousMonth != null) {
                if (previousMonth.getIsClosed()) {
                    currentMonth.getOpeningBalance().setAmount(previousMonth.getClosingBalance().getAmount());
                } else {
                    currentMonth.getOpeningBalance().setAmount(previousMonth.getEstimatedClosingBalance().getAmount());
                }
                currentMonth.getOpeningSurplus().setAmount(previousMonth.getClosingSurplus().getAmount());
            }
            monthCalculationServices.calculateMonthTotals(currentMonth);
            currentMonth = monthRepository.getNext(currentMonth);
        }
    }
}
