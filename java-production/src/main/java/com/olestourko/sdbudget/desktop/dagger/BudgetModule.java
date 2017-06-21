package com.olestourko.sdbudget.desktop.dagger;

import com.olestourko.sdbudget.Configuration;
import com.olestourko.sdbudget.core.commands.CommandInvoker;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.core.services.MonthCalculationServices;
import com.olestourko.sdbudget.core.services.MonthCopyService;
import com.olestourko.sdbudget.core.services.MonthLogicServices;
import com.olestourko.sdbudget.desktop.Frontend;
import com.olestourko.sdbudget.desktop.controllers.ChartController;
import com.olestourko.sdbudget.desktop.controllers.MainController;
import com.olestourko.sdbudget.desktop.controllers.OneMonthController;
import com.olestourko.sdbudget.desktop.controllers.ScratchpadController;
import com.olestourko.sdbudget.desktop.controllers.ThreeMonthController;
import com.olestourko.sdbudget.desktop.models.Budget;
import dagger.Module;
import dagger.Provides;

/**
 *
 * @author oles
 */
@DesktopApplicationScope
@Module
public class BudgetModule {

    @Provides
    public Frontend frontend(
            Budget budget,
            MonthCalculationServices monthCalculationServices,
            MonthRepository monthRepository,
            MainController mainController,
            OneMonthController oneMonthController,
            ThreeMonthController threeMonthController,
            ScratchpadController scratchpadController,
            ChartController chartController,
            CommandInvoker commandInvoker,
            Configuration configuration) {

        return new Frontend(
                budget,
                monthCalculationServices,
                monthRepository,
                mainController,
                oneMonthController,
                threeMonthController,
                scratchpadController,
                chartController,
                commandInvoker,
                configuration.getVersion()
        );
    }

    @Provides
    public ScratchpadController scratchpadController(
            Budget budget,
            Configuration configuration,
            CommandInvoker commandInvoker
    ) {
        return new ScratchpadController(budget, configuration.getCurrency(), commandInvoker);
    }

    @Provides
    public OneMonthController oneMonthController(
            MonthCalculationServices monthCalculationServices,
            MonthLogicServices monthLogicServices,
            MonthCopyService monthCopyService,
            MonthRepository monthRepository,
            Budget budget,
            Configuration configuration,
            CommandInvoker commandInvoker
    ) {
        return new OneMonthController(
                monthLogicServices,
                monthRepository,
                budget,
                configuration.getCurrency(),
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
            Configuration configuration,
            CommandInvoker commandInvoker
    ) {
        return new ThreeMonthController(
                monthLogicServices,
                monthRepository,
                budget,
                configuration.getCurrency(),
                commandInvoker
        );
    }

    @Provides
    public ChartController chartController(
            Budget budget,
            MonthRepository monthRepository,
            CommandInvoker commandInvoker
    ) {
        return new ChartController(
                budget,
                monthRepository,
                commandInvoker
        );
    }
}
