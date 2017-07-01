package com.olestourko.sdbudget.core.services;

import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.core.repositories.MonthRepository;

public class MonthLogicServices {

    final MonthRepository monthRepository;

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

    public boolean isMonthOpenable(Month month) {
        Month nextMonth = monthRepository.getNext(month);
        if (nextMonth == null) {
            return true;
        }

        return !nextMonth.getIsClosed();
    }

    public boolean isMonthFirst(Month month) {
        return monthRepository.getFirst() == month;
    }

    public boolean canEditOpeningBalance(Month month) {
        Month previousMonth = monthRepository.getPrevious(month);
        return previousMonth == null;
    }

    public boolean isMonthCloneable(Month month) {
        return (monthRepository.getNext(month) != null);
    }
}
