package com.olestourko.sdbudget.desktop.controllers;

import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.core.services.MonthCalculationServices;
import com.olestourko.sdbudget.desktop.controls.MonthControl;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.olestourko.sdbudget.desktop.models.MonthViewModel;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.desktop.models.Budget;
import com.olestourko.sdbudget.desktop.mappers.MonthMapper;
import java.util.ArrayList;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javax.inject.Inject;
import org.mapstruct.factory.Mappers;

public class ThreeMonthController implements Initializable {

    @FXML
    public Pane monthControlContainer;

    final private ArrayList<MonthControl> monthControls = new ArrayList<MonthControl>();
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

        this.budget.currentMonthProperty().addListener(month -> {
            SimpleObjectProperty<Month> monthProperty = (SimpleObjectProperty<Month>) month;
            MonthViewModel monthVM = monthMapper.mapMonthToMonthViewModel(monthProperty.getValue());
            this.setMonth(monthVM);
        });

        // This event updates all the months
        Callback<MonthControl, MonthViewModel> monthChangedCallback = new Callback<MonthControl, MonthViewModel>() {
            @Override
            public MonthViewModel call(MonthControl param) {
                MonthViewModel monthVM = getMonth();
                Month month = monthMapper.mapMonthViewModelToMonth(monthVM);
                do {
                    Month previousMonth = monthRepository.getPrevious(month);
                    if (previousMonth != null) {
                        MonthViewModel previousMonthVM = monthMapper.mapMonthToMonthViewModel(previousMonth);
                        monthVM.getOpeningBalance().setAmount(previousMonthVM.getFinalClosingBalance().getAmount());
                        monthVM.getOpeningSurplus().setAmount(previousMonthVM.getClosingSurplus().getAmount());
                    }

                    monthMapper.updateMonthFromMonthViewModel(monthVM, monthVM.getModel());
                    monthServices.calculateMonthTotals(monthVM.getModel());
                    monthMapper.updateMonthViewModelFromMonth(monthVM.getModel(), monthVM);

                    // Get the next month
                    month = monthRepository.getNext(month);
                    if (month != null) {
                        monthVM = monthMapper.mapMonthToMonthViewModel(month);
                    }
                } while (month != null);

                return monthVM;
            }
        };

        // Set event handlers for all the month components
        for (MonthControl monthControl : monthControls) {
            monthControl.setOnMonthChanged(monthChangedCallback);
        }
    }

    public void load() {
        MonthViewModel monthVM = this.monthMapper.mapMonthToMonthViewModel(budget.getCurrentMonth());
        this.setMonth(monthVM);
    }

    private final MonthViewModel getMonth() {
        return this.monthControls.get(0).getMonth();
    }

    private final void setMonth(MonthViewModel month) {
        this.monthControls.get(0).setMonth(month);

        Month monthTwo = monthRepository.getNext(this.monthMapper.mapMonthViewModelToMonth(month));
        Month monthThree = monthRepository.getNext(monthTwo);

        this.monthControls.get(1).setMonth(this.monthMapper.mapMonthToMonthViewModel(monthTwo));
        this.monthControls.get(2).setMonth(this.monthMapper.mapMonthToMonthViewModel(monthThree));
    }
}
