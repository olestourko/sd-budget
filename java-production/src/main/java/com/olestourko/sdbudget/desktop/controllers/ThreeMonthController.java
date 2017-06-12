package com.olestourko.sdbudget.desktop.controllers;

import com.olestourko.sdbudget.core.commands.CommandInvoker;
import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.core.services.MonthCalculationServices;
import com.olestourko.sdbudget.desktop.controls.MonthControl;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.core.services.MonthCopyService;
import com.olestourko.sdbudget.core.services.MonthLogicServices;
import com.olestourko.sdbudget.desktop.models.Budget;
import com.olestourko.sdbudget.desktop.mappers.MonthMapper;
import java.util.LinkedList;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javax.inject.Inject;
import org.mapstruct.factory.Mappers;

public class ThreeMonthController implements Initializable, INMonthController {

    @FXML
    public Pane monthControlContainer;

    private final LinkedList<MonthControl> monthControls = new LinkedList<MonthControl>();
    private final MonthCalculationServices monthCalculationServices;
    private final MonthLogicServices monthLogicServices;
    private final MonthCopyService monthCopyService;
    private final MonthRepository monthRepository;
    private final Budget budget;
    private final MonthMapper monthMapper;
    private final String currency;
    private final CommandInvoker commandInvoker;

    @Inject
    public ThreeMonthController(
            MonthCalculationServices monthCalculationServices,
            MonthLogicServices monthLogicServices,
            MonthCopyService monthCopyService,
            MonthRepository monthRepository,
            Budget budget,
            String currency,
            CommandInvoker commandInvoker
    ) {
        this.monthCalculationServices = monthCalculationServices;
        this.monthLogicServices = monthLogicServices;
        this.monthCopyService = monthCopyService;
        this.monthRepository = monthRepository;
        this.budget = budget;
        this.monthMapper = Mappers.getMapper(MonthMapper.class);
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
        Callback<MonthControl, Month> monthModifiedCallback = new Callback<MonthControl, Month>() {
            @Override
            public Month call(MonthControl monthControl) {
                monthMapper.updateMonthFromMonthViewModel(monthControl.getMonthViewModel(), monthControl.getMonth());
                monthCalculationServices.recalculateMonths(monthControl.getMonth());

                for (MonthControl mc : monthControls) {
                    Month mcMonth = mc.getMonth();
                    if (mcMonth != null) {
                        monthMapper.updateMonthViewModelFromMonth(mc.getMonth(), mc.getMonthViewModel());
                        mc.refresh();
                    }
                }

                return monthControl.getMonth();
            }
        };

        for (MonthControl monthControl : monthControls) {
            // Set currency for month control
            monthControl.setCurrency(currency);
            
            // Set event handlers for all the month components
            monthControl.setOnMonthModified(monthModifiedCallback);

            // Month cloning    
            monthControl.getCopyToNextButton().setOnAction(event -> {
                Month nextMonth = monthRepository.getNext(monthControl.getMonth());
                if (nextMonth != null) {
                    monthCopyService.cloneMonth(monthControl.getMonth(), nextMonth);
                    monthCalculationServices.recalculateMonths(nextMonth);
                    populateMonthControls();
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

    private void populateMonthControls() {
        for (MonthControl monthControl : monthControls) {
            monthControl.refresh();
        }
    }

    @Override
    public void refresh() {
        for (MonthControl monthControl : monthControls) {
            monthControl.refresh();
        }
    }
}
