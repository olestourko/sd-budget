package com.olestourko.sdbudget.desktop.dagger;

import com.olestourko.sdbudget.core.models.Budget;
import com.olestourko.sdbudget.desktop.controllers.OneMonthController;
import com.olestourko.sdbudget.core.services.PeriodServices;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.desktop.controllers.MainController;
import com.olestourko.sdbudget.desktop.controllers.ScratchpadController;
import com.olestourko.sdbudget.desktop.controllers.ThreeMonthController;
import dagger.Component;
import javax.inject.Singleton;
import javax.inject.Provider;

/**
 *
 * @author oles
 */
@Singleton
@Component(modules = BudgetModule.class)
public interface BudgetInjector {

    Budget budget();
    
    Provider<MainController> mainController();
    
    Provider<OneMonthController> oneMonthController();
    
    Provider<ThreeMonthController> threeMonthController();

    Provider<ScratchpadController> scratchpadController();

    PeriodServices periodServices();

    MonthRepository monthRepository();
}
