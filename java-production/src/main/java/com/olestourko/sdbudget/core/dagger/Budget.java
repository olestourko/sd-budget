package com.olestourko.sdbudget.core.dagger;

import com.olestourko.sdbudget.desktop.BudgetSceneController;
import com.olestourko.sdbudget.core.services.PeriodServices;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
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
    MonthRepository monthRepository();
}
