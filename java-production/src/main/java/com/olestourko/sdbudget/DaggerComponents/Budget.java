package com.olestourko.sdbudget.DaggerComponents;

import com.olestourko.sdbudget.BudgetSceneController;
import com.olestourko.sdbudget.services.PeriodServices;
import dagger.Component;
import javax.inject.Singleton;
import javax.inject.Provider;

/**
 *
 * @author oles
 */
@Singleton
@Component(modules = BudgetModule.class)
public interface Budget {
    Provider<BudgetSceneController> budgetSceneController();
    PeriodServices periodServices();
}
