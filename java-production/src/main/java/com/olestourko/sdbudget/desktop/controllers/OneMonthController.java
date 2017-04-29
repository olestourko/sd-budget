package com.olestourko.sdbudget.desktop.controllers;

import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.desktop.controls.MonthControl;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.olestourko.sdbudget.desktop.models.MonthViewModel;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.desktop.models.Budget;
import com.olestourko.sdbudget.core.services.MonthCalculationServices;
import com.olestourko.sdbudget.core.services.MonthLogicServices;
import com.olestourko.sdbudget.desktop.mappers.MonthMapper;
import javafx.beans.property.SimpleObjectProperty;
import javax.inject.Inject;
import org.mapstruct.factory.Mappers;

public class OneMonthController implements Initializable {

    @FXML
    public MonthControl monthControl;

    private final MonthCalculationServices monthCalculationervices;
    final private MonthLogicServices monthLogicServices;
    private final MonthRepository monthRepository;
    private final Budget budget;
    private final MonthMapper monthMapper;

    @Inject
    OneMonthController(MonthCalculationServices monthServices, MonthLogicServices monthLogicServices, MonthRepository monthRepository, Budget budget) {
        this.monthCalculationervices = monthServices;
        this.monthLogicServices = monthLogicServices;
        this.monthRepository = monthRepository;
        this.budget = budget;
        this.monthMapper = Mappers.getMapper(MonthMapper.class);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Change the MonthControl's month instance whenever the instance in the Budget changes
        this.budget.currentMonthProperty().addListener(month -> {
            SimpleObjectProperty<Month> monthProperty = (SimpleObjectProperty<Month>) month;
            this.monthControl.setMonth(monthProperty.getValue());
        });

        monthControl.setMonthLogicServices(monthLogicServices);
        monthControl.setOnMonthModified(event -> {
            Month month = monthControl.getMonth();
            Month previousMonth = monthRepository.getPrevious(month);
            if (previousMonth != null) {
                MonthViewModel previousMonthVM = this.monthMapper.mapMonthToMonthViewModel(previousMonth);
                month.getOpeningBalance().setAmount(previousMonthVM.getFinalClosingBalance().getAmount());                
                month.getOpeningSurplus().setAmount(previousMonthVM.getClosingSurplus().getAmount());
            }

            this.monthMapper.updateMonthFromMonthViewModel(monthControl.getMonthViewModel(), month);
            monthCalculationervices.calculateMonthTotals(month);
            this.monthMapper.updateMonthViewModelFromMonth(month, monthControl.getMonthViewModel());
            return month;
        });
    }

    public void load() {
        this.monthControl.setMonth(budget.getCurrentMonth());
    }
}
