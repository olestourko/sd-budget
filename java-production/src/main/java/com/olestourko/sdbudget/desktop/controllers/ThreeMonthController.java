package com.olestourko.sdbudget.desktop.controllers;

import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.core.services.MonthCalculationServices;
import com.olestourko.sdbudget.desktop.controls.MonthControl;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.desktop.models.Budget;
import com.olestourko.sdbudget.desktop.mappers.MonthMapper;
import java.util.LinkedList;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javax.inject.Inject;
import org.mapstruct.factory.Mappers;

public class ThreeMonthController implements Initializable {

    @FXML
    public Pane monthControlContainer;

    final private LinkedList<MonthControl> monthControls = new LinkedList<MonthControl>();
    final private MonthCalculationServices monthServices;
    final private MonthRepository monthRepository;
    final private Budget budget;
    private final MonthMapper monthMapper;

    @Inject
    ThreeMonthController(MonthCalculationServices monthServices, MonthRepository monthRepository, Budget budget) {
        this.monthServices = monthServices;
        this.monthRepository = monthRepository;
        this.budget = budget;
        this.monthMapper = Mappers.getMapper(MonthMapper.class);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        monthControls.add((MonthControl) monthControlContainer.getChildren().get(0));
        monthControls.add((MonthControl) monthControlContainer.getChildren().get(1));
        monthControls.add((MonthControl) monthControlContainer.getChildren().get(2));

        // Change the MonthControls' month instance whenever the instance in the Budget changes
        this.budget.currentMonthProperty().addListener(month -> {
            SimpleObjectProperty<Month> monthProperty = (SimpleObjectProperty<Month>) month;
            this.setMonth(monthProperty.getValue());
        });

        // This event updates all the months
        Callback<MonthControl, Month> monthModifiedCallback = new Callback<MonthControl, Month>() {
            @Override
            public Month call(MonthControl monthControl) {
                Month month = monthControl.getMonth();

                for (MonthControl mc : monthControls) {
                    Month mcMonth = mc.getMonth();
                    if (mcMonth != null) {
                        Month previousMonth = monthRepository.getPrevious(mc.getMonth());
                        if (previousMonth != null) {
                            mc.getMonth().getOpeningBalance().setAmount(previousMonth.getClosingBalance().getAmount());
                            mc.getMonth().getOpeningSurplus().setAmount(previousMonth.getClosingSurplus().getAmount());
                        }
                        monthServices.calculateMonthTotals(mc.getMonth());
                        monthMapper.updateMonthViewModelFromMonth(mc.getMonth(), mc.getMonthViewModel());
                        mc.populateTables();
                    }
                }

                return month;
            }
        };

        // Set event handlers for all the month components
        for (MonthControl monthControl : monthControls) {
            monthControl.setOnMonthModified(monthModifiedCallback);
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
}
