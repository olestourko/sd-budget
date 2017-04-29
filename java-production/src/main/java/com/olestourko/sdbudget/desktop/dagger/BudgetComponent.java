package com.olestourko.sdbudget.desktop.dagger;

import com.olestourko.sdbudget.desktop.models.Budget;
import com.olestourko.sdbudget.desktop.controllers.OneMonthController;
import com.olestourko.sdbudget.desktop.controllers.MainController;
import com.olestourko.sdbudget.desktop.controllers.ScratchpadController;
import com.olestourko.sdbudget.desktop.controllers.ThreeMonthController;
import com.olestourko.sdbudget.core.services.PeriodCalculationServices;
import com.olestourko.sdbudget.desktop.controls.MonthControl;
import dagger.Component;
import javax.inject.Provider;
import com.olestourko.sdbudget.core.dagger.CoreComponent;

/**
 *
 * @author oles
 */
@DesktopApplicationScope
@Component(modules = {BudgetModule.class}, dependencies = {CoreComponent.class})
public interface BudgetComponent {

    Budget budget();
    
    Provider<MainController> mainController();
    
    Provider<OneMonthController> oneMonthController();
    
    Provider<ThreeMonthController> threeMonthController();

    Provider<ScratchpadController> scratchpadController();

    PeriodCalculationServices periodServices();
    
    void inject(MonthControl monthControl);
}
