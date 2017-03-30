package com.olestourko.sdbudget.desktop.dagger;

import com.olestourko.sdbudget.desktop.repositories.MonthRepository;
import com.olestourko.sdbudget.core.services.PeriodServices;
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
    PeriodServices providePeriodServices() {
        return new PeriodServices();
    }

    @Provides
    @Singleton
    MonthRepository provideMonthRepository() {
        return new MonthRepository();
    }

    @Provides
    @Singleton
    Budget provideBudget() {
        return new Budget();
    }

}
