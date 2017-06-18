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
import com.olestourko.sdbudget.core.services.MonthLogicServices;
import com.olestourko.sdbudget.desktop.models.Budget;
import java.util.LinkedList;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;
import javax.inject.Inject;

public class ThreeMonthController implements Initializable, INMonthController {

    @FXML
    public Pane monthControlContainer;

    private final LinkedList<MonthControl> monthControls = new LinkedList<MonthControl>();
    private final MonthLogicServices monthLogicServices;
    private final MonthRepository monthRepository;
    private final Budget budget;
    private final String currency;
    private final CommandInvoker commandInvoker;

    @Inject
    public ThreeMonthController(
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
        monthControls.add((MonthControl) monthControlContainer.getChildren().get(0));
        monthControls.add((MonthControl) monthControlContainer.getChildren().get(1));
        monthControls.add((MonthControl) monthControlContainer.getChildren().get(2));
        monthControls.get(0).setMonthLogicServices(monthLogicServices);
        monthControls.get(0).setCommandInvoker(commandInvoker);
        monthControls.get(1).setMonthLogicServices(monthLogicServices);
        monthControls.get(1).setCommandInvoker(commandInvoker);
        monthControls.get(2).setMonthLogicServices(monthLogicServices);
        monthControls.get(2).setCommandInvoker(commandInvoker);

        // Change the MonthControls' month instance whenever the instance in the Budget changes
        this.budget.currentMonthProperty().addListener(month -> {
            SimpleObjectProperty<Month> monthProperty = (SimpleObjectProperty<Month>) month;
            this.setMonth(monthProperty.getValue());
        });

        // This callback updates all the months
        ICommandCallback commandHandler = new ICommandCallback() {
            @Override
            public void handle(ICommand command) {
                for (MonthControl monthControl : monthControls) {
                    Month mcMonth = monthControl.getMonth();
                    if (mcMonth != null) {
                        monthControl.refresh();
                    }
                }
            }
        };

        // Register command listeners
        commandInvoker.addListener(UpdateBudgetItem.class, commandHandler);
        commandInvoker.addListener(AddBudgetItem.class, commandHandler);
        commandInvoker.addListener(RemoveBudgetItem.class, commandHandler);
        commandInvoker.addListener(SetMonthClosed.class, commandHandler);
        commandInvoker.addListener(CopyMonth.class, commandHandler);

        for (MonthControl monthControl : monthControls) {
            // Set currency for month control
            monthControl.setCurrency(currency);

            // Set event handlers for all the month components
            // Month cloning    
            monthControl.getCopyToNextButton().setOnAction(event -> {
                Month nextMonth = monthRepository.getNext(monthControl.getMonth());
                if (nextMonth != null) {
                    commandInvoker.invoke(new CopyMonth(monthControl.getMonth(), nextMonth));
                }
            });
        }
    }

    public void load() {        
        this.setMonth(budget.getCurrentMonth());
    }

    private final void setMonth(Month month) {
        this.monthControls.get(0).setMonth(month);
        this.monthControls.get(1).setMonth(monthRepository.getNext(this.monthControls.get(0).getMonth()));
        this.monthControls.get(2).setMonth(monthRepository.getNext(this.monthControls.get(1).getMonth()));
    }

    @Override
    public void refresh() {
        for (MonthControl monthControl : monthControls) {
            monthControl.refresh();
        }
    }
}
