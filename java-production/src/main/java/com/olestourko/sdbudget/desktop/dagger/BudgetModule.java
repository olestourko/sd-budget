package com.olestourko.sdbudget.desktop.dagger;

import com.olestourko.sdbudget.core.commands.CommandInvoker;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.core.services.MonthCalculationServices;
import com.olestourko.sdbudget.core.services.MonthCopyService;
import com.olestourko.sdbudget.core.services.MonthLogicServices;
import com.olestourko.sdbudget.desktop.controllers.OneMonthController;
import com.olestourko.sdbudget.desktop.controllers.ScratchpadController;
import com.olestourko.sdbudget.desktop.controllers.ThreeMonthController;
import com.olestourko.sdbudget.desktop.models.Budget;
import dagger.Module;
import dagger.Provides;
import org.cfg4j.provider.ConfigurationProvider;

/**
 *
 * @author oles
 */
@DesktopApplicationScope
@Module
public class BudgetModule {

    @Provides
    public ScratchpadController scratchpadController(Budget budget, ConfigurationProvider configurationProvider, CommandInvoker commandInvoker) {
        String currency = "$";
        try {
            currency = configurationProvider.getProperty("currency", String.class);
        } catch (Exception exception) {
            // The currency probably isn't set in the config file
        }

        return new ScratchpadController(budget, currency, commandInvoker);
    }

    @Provides
    public OneMonthController oneMonthController(
            MonthCalculationServices monthCalculationServices,
            MonthLogicServices monthLogicServices,
            MonthCopyService monthCopyService,
            MonthRepository monthRepository,
            Budget budget,
            ConfigurationProvider configurationProvider,
            CommandInvoker commandInvoker
    ) {

        String currency = "$";
        try {
            currency = configurationProvider.getProperty("currency", String.class);
        } catch (Exception exception) {
            // The currency probably isn't set in the config file
        }

        return new OneMonthController(
                monthCalculationServices,
                monthLogicServices,
                monthCopyService,
                monthRepository,
                budget,
                currency,
                commandInvoker
        );
    }

    @Provides
    public ThreeMonthController threeMonthController(
            MonthCalculationServices monthCalculationServices,
            MonthLogicServices monthLogicServices,
            MonthCopyService monthCopyService,
            MonthRepository monthRepository,
            Budget budget,
            ConfigurationProvider configurationProvider,
            CommandInvoker commandInvoker) {

        String currency = "$";
        try {
            currency = configurationProvider.getProperty("currency", String.class);
        } catch (Exception exception) {
            // The currency probably isn't set in the config file
        }

        return new ThreeMonthController(
                monthCalculationServices,
                monthLogicServices,
                monthCopyService,
                monthRepository,
                budget,
                currency,
                commandInvoker
        );
    }
}
