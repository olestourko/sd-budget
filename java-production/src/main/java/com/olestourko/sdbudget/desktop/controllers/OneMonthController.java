package com.olestourko.sdbudget.desktop.controllers;

import com.olestourko.sdbudget.desktop.controls.MonthControl;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.olestourko.sdbudget.desktop.models.MonthViewModel;
import com.olestourko.sdbudget.desktop.repositories.MonthRepository;
import com.olestourko.sdbudget.desktop.models.Budget;
import com.olestourko.sdbudget.core.services.MonthServices;
import com.olestourko.sdbudget.desktop.mappers.MonthMapper;
import javafx.beans.property.SimpleObjectProperty;
import javax.inject.Inject;
import org.mapstruct.factory.Mappers;

public class OneMonthController implements Initializable {

    @FXML
    public MonthControl monthControl;

    final private MonthServices monthServices;
    final private MonthRepository monthRepository;
    final private Budget budget;

    @Inject
    OneMonthController(MonthServices monthServices, MonthRepository monthRepository, Budget budget) {
        this.monthServices = monthServices;
        this.monthRepository = monthRepository;
        this.budget = budget;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.budget.currentMonthProperty().addListener(month -> {
            SimpleObjectProperty<MonthViewModel> monthProperty = (SimpleObjectProperty<MonthViewModel>) month;
            this.monthControl.setMonth(monthProperty.getValue());
        });

        monthControl.setOnMonthChanged(event -> {
            MonthViewModel month = monthControl.getMonth();
            MonthViewModel previousMonth = monthRepository.getPrevious(month);
            if (previousMonth != null) {
                month.getOpeningBalance().setAmount(previousMonth.getFinalClosingBalance().getAmount());                
                month.getOpeningSurplus().setAmount(previousMonth.getClosingSurplus().getAmount());
            }

            MonthMapper mapper = Mappers.getMapper(MonthMapper.class);
            mapper.updateMonthFromMonthViewModel(month, month.getModel());
            monthServices.calculateMonthTotals(month.getModel());
            mapper.updateMonthViewModelFromMonth(month.getModel(), month);
            return month;
        });
    }

    public void load() {
        this.monthControl.setMonth(budget.getCurrentMonth());
    }
}
