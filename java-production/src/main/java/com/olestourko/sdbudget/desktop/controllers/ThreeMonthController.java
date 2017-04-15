package com.olestourko.sdbudget.desktop.controllers;

import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.core.services.MonthServices;
import com.olestourko.sdbudget.desktop.controls.MonthControl;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.olestourko.sdbudget.desktop.models.MonthViewModel;
import com.olestourko.sdbudget.desktop.repositories.MonthRepository;
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
    final private MonthServices monthServices;
    final private MonthRepository monthRepository;
    final private Budget budget;

    @Inject
    ThreeMonthController(MonthServices monthServices, MonthRepository monthRepository, Budget budget) {
        this.monthServices = monthServices;
        this.monthRepository = monthRepository;
        this.budget = budget;
    }

    @Override

    public void initialize(URL url, ResourceBundle rb) {
        monthControls.add((MonthControl) monthControlContainer.getChildren().get(0));
        monthControls.add((MonthControl) monthControlContainer.getChildren().get(1));
        monthControls.add((MonthControl) monthControlContainer.getChildren().get(2));

        this.budget.currentMonthProperty().addListener(month -> {
            SimpleObjectProperty<MonthViewModel> monthProperty = (SimpleObjectProperty<MonthViewModel>) month;
            this.setMonth(monthProperty.getValue());
        });

        // This event updates all the months
        Callback<MonthControl, MonthViewModel> monthChangedCallback = new Callback<MonthControl, MonthViewModel>() {
            @Override
            public MonthViewModel call(MonthControl param) {
                MonthViewModel month = getMonth();
                do {
                    MonthViewModel previousMonth = monthRepository.getPrevious(month);
                    if (previousMonth != null) {
                        month.getOpeningBalance().setAmount(previousMonth.getFinalClosingBalance().getAmount());
                        month.getOpeningSurplus().setAmount(previousMonth.getClosingSurplus().getAmount());
                    }

                    MonthMapper mapper = Mappers.getMapper(MonthMapper.class);
                    mapper.updateMonthFromMonthViewModel(month, month.getModel());
                    monthServices.calculateMonthTotals(month.getModel());
                    mapper.updateMonthViewModelFromMonth(month.getModel(), month);

                    // Get the next month
                    month = monthRepository.getNext(month);
                } while (month != null);

                return month;
            }
        };

        // Set event handlers for all the month components
        for (MonthControl monthControl : monthControls) {
            monthControl.setOnMonthChanged(monthChangedCallback);
        }
    }

    public void load() {
        this.setMonth(budget.getCurrentMonth());
    }

    private final MonthViewModel getMonth() {
        return this.monthControls.get(0).getMonth();
    }

    private final void setMonth(MonthViewModel month) {
        this.monthControls.get(0).setMonth(month);
        this.monthControls.get(1).setMonth(monthRepository.getNext(this.monthControls.get(0).getMonth()));
        this.monthControls.get(2).setMonth(monthRepository.getNext(this.monthControls.get(1).getMonth()));
    }
}
