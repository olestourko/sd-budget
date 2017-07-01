package com.olestourko.sdbudget.desktop.controllers;

import com.olestourko.sdbudget.core.commands.AddBudgetItem;
import com.olestourko.sdbudget.core.commands.CommandInvoker;
import com.olestourko.sdbudget.core.commands.CopyMonth;
import com.olestourko.sdbudget.core.commands.ICommand;
import com.olestourko.sdbudget.core.commands.ICommandCallback;
import com.olestourko.sdbudget.core.commands.RemoveBudgetItem;
import com.olestourko.sdbudget.core.commands.SetMonthClosed;
import com.olestourko.sdbudget.core.commands.UpdateBudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.desktop.controls.MonthControl;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.desktop.models.Budget;
import com.olestourko.sdbudget.core.services.MonthLogicServices;
import javafx.beans.property.SimpleObjectProperty;
import javax.inject.Inject;

public class OneMonthController implements Initializable, INMonthController {

    @FXML
    public MonthControl monthControl;

    private final MonthLogicServices monthLogicServices;
    private final MonthRepository monthRepository;
    private final Budget budget;
    private final String currency;
    private final CommandInvoker commandInvoker;

    @Inject
    public OneMonthController(
            MonthLogicServices monthLogicServices,
            MonthRepository monthRepository,
            Budget budget,
            String currency,
            CommandInvoker commandInvoker
    ) {
        this.monthLogicServices = monthLogicServices;
        this.monthRepository = monthRepository;
        this.budget = budget;
        this.currency = currency;
        this.commandInvoker = commandInvoker;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Change the MonthControl's month instance whenever the instance in the Budget changes
        this.budget.currentMonthProperty().addListener(month -> {
            SimpleObjectProperty<Month> monthProperty = (SimpleObjectProperty<Month>) month;
            this.monthControl.setMonth(monthProperty.getValue());
        });

        monthControl.setCurrency(currency);
        monthControl.setMonthLogicServices(monthLogicServices);
        monthControl.setCommandInvoker(commandInvoker);
        ICommandCallback commandHandler = new ICommandCallback() {
            @Override
            public void handle(ICommand command) {
                monthControl.refresh();
            }
        };
        commandInvoker.addListener(UpdateBudgetItem.class, commandHandler);
        commandInvoker.addListener(AddBudgetItem.class, commandHandler);
        commandInvoker.addListener(RemoveBudgetItem.class, commandHandler);
        commandInvoker.addListener(SetMonthClosed.class, commandHandler);
        commandInvoker.addListener(CopyMonth.class, commandHandler);

        // Month cloning
        monthControl.getCopyToNextButton().setOnAction(event -> {
            Month nextMonth = monthRepository.getNext(monthControl.getMonth());
            if (nextMonth != null) {
                commandInvoker.invoke(new CopyMonth(monthControl.getMonth(), nextMonth));
            }
        });
    }

    public void load() {
        monthControl.setMonth(budget.getCurrentMonth());
    }

    @Override
    public void refresh() {
        monthControl.refresh();
    }
}
