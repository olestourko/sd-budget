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
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.desktop.models.Budget;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.ShortStringConverter;
import javax.inject.Inject;

/**
 *
 * @author oles
 */
public class ChartController implements Initializable {

    Budget budget;
    MonthRepository monthRepository;
    CommandInvoker commandInvoker;

    @FXML
    public AreaChart chart;
    @FXML
    CategoryAxis xAxis;
    @FXML
    NumberAxis yAxis;

    private ShortStringConverter shortConverter = new ShortStringConverter();
    private IntegerStringConverter intConverter = new IntegerStringConverter();

    @Inject
    public ChartController(
            Budget budget,
            MonthRepository monthRepository,
            CommandInvoker commandInvoker) {
        this.budget = budget;
        this.monthRepository = monthRepository;
        this.commandInvoker = commandInvoker;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        xAxis.gapStartAndEndProperty().set(false);
        refreshData();
        budget.currentMonthProperty().addListener(property -> {
            refreshData();
        });

        ICommandCallback commandHandler = new ICommandCallback() {
            @Override
            public void handle(ICommand command) {
                refreshData();
            }
        };
        commandInvoker.addListener(UpdateBudgetItem.class, commandHandler);
        commandInvoker.addListener(AddBudgetItem.class, commandHandler);
        commandInvoker.addListener(RemoveBudgetItem.class, commandHandler);
        commandInvoker.addListener(SetMonthClosed.class, commandHandler);
        commandInvoker.addListener(CopyMonth.class, commandHandler);
    }

    private void refreshData() {
        chart.getData().clear();

        // Get past values
        XYChart.Series pastSeries = new XYChart.Series();
        Month previousMonth = budget.getCurrentMonth();
        List<XYChart.Data> pastData = new ArrayList<XYChart.Data>();
        for (int i = 0; i < 6; i++) {
            if (previousMonth == null) {
                break;
            } else {
                String label = intConverter.toString(previousMonth.getNumber() + 1) + " / " + shortConverter.toString(previousMonth.getYear());
                pastData.add(new XYChart.Data(label, previousMonth.getClosingSurplus().getAmount()));
                previousMonth = monthRepository.getPrevious(previousMonth);
            }
        }
        Collections.reverse(pastData);
        pastSeries.getData().addAll(pastData);

        // Get future values
        XYChart.Series futureSeries = new XYChart.Series();
        Month nextMonth = budget.getCurrentMonth();
        List<XYChart.Data> futureData = new ArrayList<XYChart.Data>();
        for (int i = 0; i < 6; i++) {
            if (nextMonth == null) {
                break;
            } else {
                String label = intConverter.toString(nextMonth.getNumber() + 1) + " / " + shortConverter.toString(nextMonth.getYear());
                futureData.add(new XYChart.Data(label, nextMonth.getClosingSurplus().getAmount()));
                nextMonth = monthRepository.getNext(nextMonth);
            }
        }

        futureSeries.getData().addAll(futureData);

        chart.getData().addAll(pastSeries, futureSeries);
    }

}
