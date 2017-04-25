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
import com.olestourko.sdbudget.desktop.mappers.MonthMapper;
import javafx.beans.property.SimpleObjectProperty;
import javax.inject.Inject;
import org.mapstruct.factory.Mappers;

public class OneMonthController implements Initializable {

    @FXML
    public MonthControl monthControl;

    private final MonthCalculationServices monthServices;
    private final MonthRepository monthRepository;
    private final Budget budget;
    private final MonthMapper monthMapper;

    @Inject
    OneMonthController(MonthCalculationServices monthServices, MonthRepository monthRepository, Budget budget) {
        this.monthServices = monthServices;
        this.monthRepository = monthRepository;
        this.budget = budget;
        this.monthMapper = Mappers.getMapper(MonthMapper.class);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.budget.currentMonthProperty().addListener(month -> {
            SimpleObjectProperty<Month> monthProperty = (SimpleObjectProperty<Month>) month;
            MonthViewModel monthVM = monthMapper.mapMonthToMonthViewModel(monthProperty.getValue());
            this.monthControl.setMonth(monthVM);
        });

        monthControl.setOnMonthChanged(event -> {
            MonthViewModel monthViewModel = monthControl.getMonth();
            Month month = this.monthMapper.mapMonthViewModelToMonth(monthViewModel);
            Month previousMonth = monthRepository.getPrevious(month);
            if (previousMonth != null) {
                MonthViewModel previousMonthVM = this.monthMapper.mapMonthToMonthViewModel(previousMonth);
                month.getOpeningBalance().setAmount(previousMonthVM.getFinalClosingBalance().getAmount());                
                month.getOpeningSurplus().setAmount(previousMonthVM.getClosingSurplus().getAmount());
            }

            this.monthMapper.updateMonthFromMonthViewModel(monthViewModel, month);
            monthServices.calculateMonthTotals(month);
            this.monthMapper.updateMonthViewModelFromMonth(month, monthViewModel);
            return monthViewModel;
        });
    }

    public void load() {
        MonthViewModel monthViewModel = this.monthMapper.mapMonthToMonthViewModel(budget.getCurrentMonth());
        this.monthControl.setMonth(monthViewModel);
    }
}
