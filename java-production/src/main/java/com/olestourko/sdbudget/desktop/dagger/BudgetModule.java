package com.olestourko.sdbudget.desktop.dagger;

import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.core.services.PeriodCalculationServices;
import com.olestourko.sdbudget.desktop.models.Budget;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 *
 * @author oles
 */
@Module
public class BudgetModule {

    @Provides
    @Singleton
    PeriodCalculationServices providePeriodServices() {
        return new PeriodCalculationServices();
    }

    @Provides
    @Singleton
    Budget provideBudget() {
        return new Budget();
    }

}
