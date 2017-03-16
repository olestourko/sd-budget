package com.olestourko.sdbudget.core.dagger;

import com.olestourko.sdbudget.desktop.BudgetSceneController;
import com.olestourko.sdbudget.core.services.PeriodServices;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.desktop.ScratchpadSceneController;
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

    Provider<ScratchpadSceneController> scratchpadSceneController();

    PeriodServices periodServices();

    MonthRepository monthRepository();
}
