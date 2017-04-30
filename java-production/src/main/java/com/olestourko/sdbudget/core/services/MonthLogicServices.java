package com.olestourko.sdbudget.core.services;

import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import javax.inject.Inject;

public class MonthLogicServices {

    private final MonthRepository monthRepository;

    @Inject
    public MonthLogicServices(MonthRepository monthRepository) {
        this.monthRepository = monthRepository;
    }

    public boolean isMonthClosable(Month month) {
        Month previousMonth = monthRepository.getPrevious(month);
        if (previousMonth == null) {
            return true;
        }

        return previousMonth.getIsClosed();
    }

    public boolean canEditOpeningBalance(Month month) {
        Month previousMonth = monthRepository.getPrevious(month);
        return previousMonth == null;
    }

    public boolean isMonthCloneable(Month month) {
        return (monthRepository.getNext(month) != null);
    }
}
