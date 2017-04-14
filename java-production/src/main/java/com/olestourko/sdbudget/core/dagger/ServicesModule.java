package com.olestourko.sdbudget.core.dagger;

import com.olestourko.sdbudget.core.services.PeriodServices;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 *
 * @author oles
 */
@Module
public class ServicesModule {

    @Provides
    @Singleton
    PeriodServices periodServices() {
        return new PeriodServices();
    }
}
