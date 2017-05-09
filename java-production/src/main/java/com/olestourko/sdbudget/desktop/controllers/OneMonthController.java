package com.olestourko.sdbudget.desktop.controllers;

import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.desktop.controls.MonthControl;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.core.services.MonthCalculationServices;
import com.olestourko.sdbudget.desktop.models.Budget;
import com.olestourko.sdbudget.core.services.MonthCopyService;
import com.olestourko.sdbudget.core.services.MonthLogicServices;
import com.olestourko.sdbudget.desktop.mappers.MonthMapper;
import javafx.beans.property.SimpleObjectProperty;
import javax.inject.Inject;
import org.mapstruct.factory.Mappers;

public class OneMonthController implements Initializable {

    @FXML
    public MonthControl monthControl;

    private final MonthCalculationServices monthCalculationServices;
    private final MonthLogicServices monthLogicServices;
    private final MonthCopyService monthCopyService;
    private final MonthRepository monthRepository;
    private final Budget budget;
    private final MonthMapper monthMapper;

    @Inject
    OneMonthController(
            MonthCalculationServices monthCalculationServices,
            MonthLogicServices monthLogicServices,
            MonthCopyService monthCopyService,
            MonthRepository monthRepository,
            Budget budget) {
        this.monthCalculationServices = monthCalculationServices;
        this.monthLogicServices = monthLogicServices;
        this.monthCopyService = monthCopyService;
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
            this.monthMapper.updateMonthFromMonthViewModel(monthControl.getMonthViewModel(), month);
            monthCalculationServices.recalculateMonths(month);
            this.monthMapper.updateMonthViewModelFromMonth(month, monthControl.getMonthViewModel());
            return month;
        });

        // Month cloning
        monthControl.getCopyToNextButton().setOnAction(event -> {
            Month nextMonth = monthRepository.getNext(monthControl.getMonth());
            if (nextMonth != null) {
                monthCopyService.cloneMonth(monthControl.getMonth(), nextMonth);
                monthControl.populateTables();
            }
        });
    }

    public void load() {
        this.monthControl.setMonth(budget.getCurrentMonth());
    }
}
