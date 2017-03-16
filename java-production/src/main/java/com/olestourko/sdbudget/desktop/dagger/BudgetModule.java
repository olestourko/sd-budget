package com.olestourko.sdbudget.desktop.dagger;

import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.core.services.PeriodServices;
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
}
