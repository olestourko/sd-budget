package com.olestourko.sdbudget.core.dagger;

import com.olestourko.sdbudget.core.persistence.BudgetItemPersistence;
import com.olestourko.sdbudget.core.persistence.MonthPersistence;
import com.olestourko.sdbudget.core.persistence.relations.MonthAdjustmentsRelation;
import com.olestourko.sdbudget.core.persistence.relations.MonthClosingBalancesRelation;
import com.olestourko.sdbudget.core.persistence.relations.MonthExpensesRelation;
import com.olestourko.sdbudget.core.persistence.relations.MonthNetIncomeTargetsRelation;
import com.olestourko.sdbudget.core.persistence.relations.MonthOpeningBalancesRelation;
import com.olestourko.sdbudget.core.persistence.relations.MonthRevenuesRelation;
import com.olestourko.sdbudget.core.services.MonthServices;
import com.olestourko.sdbudget.core.services.PeriodServices;
import dagger.Component;
import javax.inject.Provider;
import javax.inject.Singleton;
import org.jooq.DSLContext;

/**
 *
 * @author oles
 */
@Singleton
@Component(modules = {PersistenceModule.class, ServicesModule.class})
public interface CoreInjector {

    DSLContext createDSLContext();

    Provider<MonthPersistence> monthPersistenceProvider();

    Provider<BudgetItemPersistence> budgetItemProvider();

    /* Relations */
    MonthRevenuesRelation monthRevenuesRelation();

    MonthExpensesRelation monthExpensesRelation();

    MonthAdjustmentsRelation monthAdjustmentsRelation();

    MonthNetIncomeTargetsRelation monthNetIncomeTargetsRelation();

    MonthOpeningBalancesRelation monthOpeningBalancesRelation();

    MonthClosingBalancesRelation monthClosingBalancesRelation();

    /* Services */
    PeriodServices periodServices();
    
    MonthServices monthServices();
}
