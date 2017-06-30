package com.olestourko.sdbudget.core.dagger;

import com.olestourko.sdbudget.Configuration;
import com.olestourko.sdbudget.core.commands.CommandInvoker;
import com.olestourko.sdbudget.core.models.factories.MonthFactory;
import com.olestourko.sdbudget.core.persistence.BudgetItemPersistence;
import com.olestourko.sdbudget.core.persistence.MonthPersistence;
import com.olestourko.sdbudget.core.persistence.relations.MonthAdjustmentsRelation;
import com.olestourko.sdbudget.core.persistence.relations.MonthClosingBalancesRelation;
import com.olestourko.sdbudget.core.persistence.relations.MonthDebtRepaymentsRelation;
import com.olestourko.sdbudget.core.persistence.relations.MonthExpensesRelation;
import com.olestourko.sdbudget.core.persistence.relations.MonthInvestmentOutflowsRelation;
import com.olestourko.sdbudget.core.persistence.relations.MonthNetIncomeTargetsRelation;
import com.olestourko.sdbudget.core.persistence.relations.MonthOpeningBalancesRelation;
import com.olestourko.sdbudget.core.persistence.relations.MonthRevenuesRelation;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.core.services.MonthCalculationServices;
import com.olestourko.sdbudget.core.services.MonthCopyService;
import com.olestourko.sdbudget.core.services.MonthLogicServices;
import com.olestourko.sdbudget.core.services.PeriodCalculationServices;
import com.olestourko.sdbudget.desktop.persistence.MonthRepositoryPersistence;
import dagger.Component;
import javax.inject.Provider;
import javax.inject.Singleton;
import org.jooq.DSLContext;

/**
 *
 * @author oles
 */
//@CoreApplicationScope
@Singleton
@Component(modules = {PersistenceModule.class, ServicesModule.class, CoreModule.class})
public interface CoreComponent {

    Configuration configuration();
    
    /* Commands */
    CommandInvoker commandInvoker();

    /* Persistence */
    Provider<DSLContext> DSLContextProvider();
    
    Provider<MonthPersistence> monthPersistenceProvider();

    MonthRepository monthRepository();

    Provider<BudgetItemPersistence> budgetItemProvider();
    
    MonthRepositoryPersistence monthRepositoryPersistence();

    /* Relations */
    MonthRevenuesRelation monthRevenuesRelation();

    MonthExpensesRelation monthExpensesRelation();

    MonthAdjustmentsRelation monthAdjustmentsRelation();

    MonthDebtRepaymentsRelation monthDebtRepaymentsRelation();

    MonthInvestmentOutflowsRelation monthInvestmentOutflowsRelation();

    MonthNetIncomeTargetsRelation monthNetIncomeTargetsRelation();

    MonthOpeningBalancesRelation monthOpeningBalancesRelation();

    MonthClosingBalancesRelation monthClosingBalancesRelation();

    /* Services */
    PeriodCalculationServices periodServices();

    MonthCalculationServices monthServices();

    MonthLogicServices monthLogicServices();

    MonthCopyService monthCopyService();

    /* Factories */
    MonthFactory monthFactory();
}
