package com.olestourko.sdbudget.core.dagger;

import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.core.services.MonthCalculationServices;
import com.olestourko.sdbudget.core.services.MonthCopyService;
import com.olestourko.sdbudget.core.services.MonthLogicServices;
import com.olestourko.sdbudget.core.services.PeriodCalculationServices;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 *
 * @author oles
 */
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
    MonthCalculationServices monthCalculationServices(
            PeriodCalculationServices periodCalculationServices,
            MonthRepository monthRepository
    ) {
        return new MonthCalculationServices(periodCalculationServices, monthRepository);
    }

    @Provides
    @Singleton
    MonthCopyService monthCopyService() {
        return new MonthCopyService();
    }

    @Provides
    @Singleton
    MonthLogicServices monthLogicServices(MonthRepository monthRepository) {
        return new MonthLogicServices(monthRepository);
    }
}
