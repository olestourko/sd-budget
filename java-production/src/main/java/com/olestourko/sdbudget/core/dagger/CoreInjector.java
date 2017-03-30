package com.olestourko.sdbudget.core.dagger;

import com.olestourko.sdbudget.core.persistence.BudgetItemPersistence;
import com.olestourko.sdbudget.core.persistence.MonthPersistence;
import dagger.Component;
import javax.inject.Provider;
import javax.inject.Singleton;
import org.jooq.DSLContext;

/**
 *
 * @author oles
 */
@Singleton
@Component(modules = PersistenceModule.class)
public interface CoreInjector {

    DSLContext createDSLContext();

    Provider<MonthPersistence> monthPersistenceProvider();

    Provider<BudgetItemPersistence> budgetItemProvider();
}
