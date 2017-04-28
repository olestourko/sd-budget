package com.olestourko.sdbudget.core.dagger;

import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.core.services.MonthLogicServices;
import com.olestourko.sdbudget.core.services.PeriodCalculationServices;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 *
 * @author oles
 */
//@CoreApplicationScope
@Singleton
@Module
public class ServicesModule {

    @Provides
    @Singleton
    PeriodCalculationServices periodServices() {
        return new PeriodCalculationServices();
    }

    @Provides
    @Singleton
    MonthLogicServices monthLogicServices(MonthRepository monthRepository) {
        return new MonthLogicServices(monthRepository);
    }
}
