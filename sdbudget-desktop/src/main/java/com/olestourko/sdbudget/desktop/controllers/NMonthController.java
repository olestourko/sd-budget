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
import com.olestourko.sdbudget.desktop.models.factories.MonthFactory;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.core.services.MonthLogicServices;
import com.olestourko.sdbudget.desktop.controls.MonthControl;
import com.olestourko.sdbudget.desktop.models.Budget;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javax.inject.Inject;

/**
 *
 * @author oles
 */
public class NMonthController implements Initializable {

    @FXML
    HBox container;

    private final MonthLogicServices monthLogicServices;
    private final MonthRepository monthRepository;
    private final MonthFactory monthFactory;
    private final Budget budget;
    private final String currency;
    private final CommandInvoker commandInvoker;

    Deque<MonthControl> monthControlPool = new ArrayDeque<>();

    @Inject
    public NMonthController(
            MonthLogicServices monthLogicServices,
            MonthRepository monthRepository,
            MonthFactory monthFactory,
            Budget budget,
            String currency,
            CommandInvoker commandInvoker
    ) {
        this.monthLogicServices = monthLogicServices;
        this.monthRepository = monthRepository;
        this.monthFactory = monthFactory;
        this.budget = budget;
        this.currency = currency;
        this.commandInvoker = commandInvoker;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Change the MonthControl's month instance whenever the instance in the Budget changes
        this.budget.currentMonthProperty().addListener(month -> {
            SimpleObjectProperty<Month> monthProperty = (SimpleObjectProperty<Month>) month;

            MonthControl previousMonthControl = monthControlPool.peekLast(); // Last element is the first that was added to the Deque
            Iterator iterator = monthControlPool.descendingIterator();
            while (iterator.hasNext()) {
                MonthControl monthControl = (MonthControl) iterator.next();

                if (previousMonthControl == monthControl) {
                    monthControl.setMonth(budget.getCurrentMonth());
                    previousMonthControl = monthControl;
                    continue;
                }

                Month newMonth = monthRepository.getNext(previousMonthControl.getMonth());
                if (newMonth == null) {
                    newMonth = monthFactory.createNextMonth(previousMonthControl.getMonth());
                    monthRepository.putMonth(newMonth);
                }
                monthControl.setMonth(newMonth);
                previousMonthControl = monthControl;
            }
        });

        container.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                int monthControlCount = (newValue.intValue() / 420) + 1;

                // Added missing controls (expanding window size)
                while (monthControlPool.size() < monthControlCount) {
                    MonthControl monthControl = createMonthControl();
                    monthControl.setMonth(budget.getCurrentMonth());

                    // Set the month for the new control
                    Month month = monthRepository.getNext(monthControlPool.peek().getMonth());
                    if (month == null) {
                        month = monthFactory.createNextMonth(monthControlPool.peek().getMonth());
                        monthRepository.putMonth(month);
                    }

                    monthControl.setMonth(month);
                    monthControlPool.push(monthControl);
                    container.getChildren().add(monthControl);
                }

                // Remove existing controls (contracting window size)
                while (monthControlPool.size() > monthControlCount) {
                    MonthControl monthControl = monthControlPool.pop();
                    container.getChildren().remove(monthControl);
                }
            }

        });
    }

    public void load() {
        // Create a single MonthControl
        MonthControl monthControl = createMonthControl();
        monthControl.setMonth(budget.getCurrentMonth());
        monthControlPool.add(monthControl);
        container.getChildren().add(monthControl);
    }

    MonthControl createMonthControl() {
        MonthControl monthControl = new MonthControl();
        monthControl.setCurrency(currency);
        monthControl.setMonthLogicServices(monthLogicServices);
        monthControl.setCommandInvoker(commandInvoker);
        ICommandCallback commandHandler = new ICommandCallback() {
            @Override
            public void handle(ICommand command) {
                monthControl.refresh();
            }
        };
        commandInvoker.addListener(UpdateBudgetItem.class, commandHandler);
        commandInvoker.addListener(AddBudgetItem.class, commandHandler);
        commandInvoker.addListener(RemoveBudgetItem.class, commandHandler);
        commandInvoker.addListener(SetMonthClosed.class, commandHandler);
        commandInvoker.addListener(CopyMonth.class, commandHandler);
        monthControl.prefWidthProperty().bind(container.widthProperty());
        monthControl.prefHeightProperty().bind(container.heightProperty());

        // Month cloning
        monthControl.getCopyToNextButton().setOnAction(event -> {
            Month nextMonth = monthRepository.getNext(monthControl.getMonth());
            if (nextMonth != null) {
                commandInvoker.invoke(new CopyMonth(monthControl.getMonth(), nextMonth));
            }
        });

        return monthControl;
    }
}
