package com.olestourko.sdbudget.desktop.dagger;

import com.olestourko.sdbudget.core.dagger.CoreInjector;
import com.olestourko.sdbudget.desktop.models.Budget;
import com.olestourko.sdbudget.desktop.controllers.OneMonthController;
import com.olestourko.sdbudget.desktop.controllers.MainController;
import com.olestourko.sdbudget.desktop.controllers.ScratchpadController;
import com.olestourko.sdbudget.desktop.controllers.ThreeMonthController;
import com.olestourko.sdbudget.core.services.PeriodCalculationServices;
import dagger.Component;
import javax.inject.Provider;

/**
 *
 * @author oles
 */
@DesktopApplicationScope
@Component(modules = {BudgetModule.class}, dependencies = {CoreInjector.class})
public interface BudgetInjector {

    Budget budget();
    
    Provider<MainController> mainController();
    
    Provider<OneMonthController> oneMonthController();
    
    Provider<ThreeMonthController> threeMonthController();

    Provider<ScratchpadController> scratchpadController();

    PeriodCalculationServices periodServices();
}
