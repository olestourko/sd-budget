package com.olestourko.sdbudget.desktop.controllers;

import com.olestourko.sdbudget.desktop.controls.MonthControl;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.olestourko.sdbudget.desktop.models.MonthViewModel;
import com.olestourko.sdbudget.desktop.repositories.MonthRepository;
import com.olestourko.sdbudget.core.services.PeriodServices;
import com.olestourko.sdbudget.desktop.models.Budget;
import com.olestourko.sdbudget.core.services.ClosingResult;
import com.olestourko.sdbudget.core.services.EstimateResult;
import com.olestourko.sdbudget.desktop.models.BudgetItemViewModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javax.inject.Inject;

public class ThreeMonthController implements Initializable {

    @FXML
    public Pane monthControlContainer;

    final private ArrayList<MonthControl> monthControls = new ArrayList<MonthControl>();
    final private PeriodServices periodServices;
    final private MonthRepository monthRepository;
    final private Budget budget;

    @Inject
    ThreeMonthController(PeriodServices periodServices, MonthRepository monthRepository, Budget budget) {
        this.periodServices = periodServices;
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
        EventHandler<ActionEvent> handleMonthChange = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MonthViewModel month = getMonth();
                do {
                    MonthViewModel previousMonth = monthRepository.getPrevious(month);
                    // Calculate innter-month estimates
                    if (!month.getIsClosed()) {
                        if (previousMonth != null) {
                            month.openingBalance.setAmount(previousMonth.estimatedClosingBalance.getAmount());
                            month.openingSurplus.setAmount(previousMonth.totalSurplus.getAmount());
                        }

                        EstimateResult result = periodServices.calculateEstimate(
                                month.getTotalRevenues(),
                                month.getTotalExpenses(),
                                month.getTotalAdjustments(),
                                month.netIncomeTarget.getAmount(),
                                month.openingBalance.getAmount(),
                                month.openingSurplus.getAmount()
                        );

                        month.closingBalanceTarget.setAmount(month.openingBalance.getAmount()
                                .add(month.netIncomeTarget.getAmount()));
                        month.estimatedClosingBalance.setAmount(result.estimatedBalance.subtract(month.openingSurplus.getAmount()));
                        month.totalSurplus.setAmount(result.surplus);

                        BigDecimal sum = BigDecimal.ZERO;
                        for (Object o : month.getAdjustments()) {
                            BudgetItemViewModel item = (BudgetItemViewModel) o;
                            sum = sum.add(item.getAmount());
                        }
                    } // Calculate end of month totals
                    else {
                        ClosingResult result = periodServices.calculateClosing(
                                month.netIncomeTarget.getAmount(),
                                month.openingBalance.getAmount(),
                                month.closingBalance.getAmount(),
                                month.openingSurplus.getAmount()
                        );

                        month.estimatedClosingBalance.setAmount(month.closingBalance.getAmount());
                        month.totalSurplus.setAmount(result.surplus);
                    }
                    // Get the next month
                    month = monthRepository.getNext(month);
                } while (month != null);
            }
        };

        // Set event handlers for all the month components
        for (MonthControl monthControl : monthControls) {
            monthControl.setOnMonthChange(handleMonthChange);
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
