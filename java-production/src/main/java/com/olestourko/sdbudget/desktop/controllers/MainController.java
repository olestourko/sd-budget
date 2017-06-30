package com.olestourko.sdbudget.desktop.controllers;

import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.desktop.models.factories.MonthFactory;
import com.olestourko.sdbudget.desktop.models.Budget;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.core.services.MonthCalculationServices;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.layout.Pane;
import javax.inject.Inject;

/**
 *
 * @author oles
 */
public class MainController implements Initializable {

    @FXML
    public MenuBar mainMenu;
    @FXML
    public Button previousMonthButton;
    @FXML
    public Button nextMonthButton;
    @FXML
    public Button scratchpadViewButton;
    @FXML
    public Pane contentContainer;
    @FXML
    public MenuItem saveMenuItem;
    @FXML
    public MenuItem undoMenuItem;
    @FXML
    public MenuItem redoMenuItem;
    @FXML
    public RadioMenuItem oneMonthViewMenuItem;
    @FXML
    public RadioMenuItem nMonthViewMenuItem;
    @FXML
    public RadioMenuItem scratchpadViewMenuItem;
    @FXML
    public RadioMenuItem chartViewMenuItem;

    private final MonthCalculationServices monthCalculationServices;
    private final MonthRepository monthRepository;
    private final MonthFactory monthFactory;
    private final Budget budget;

    @Inject
    MainController(
            MonthCalculationServices monthCalculationServices,
            MonthRepository monthRepository,
            MonthFactory monthFactory,
            Budget budget
    ) {
        this.monthCalculationServices = monthCalculationServices;
        this.monthRepository = monthRepository;
        this.monthFactory = monthFactory;
        this.budget = budget;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        previousMonthButton.setDisable(monthRepository.getPrevious(budget.getCurrentMonth()) == null);
        nextMonthButton.setDisable(monthRepository.getNext(budget.getCurrentMonth()) == null);
        
        previousMonthButton.setOnAction(event -> {
            Month previousMonth = monthRepository.getPrevious(budget.getCurrentMonth());
            if (previousMonth != null) {
                nextMonthButton.disableProperty().set(false);
                budget.setCurrentMonth(previousMonth);

                if (monthRepository.getPrevious(budget.getCurrentMonth()) == null) {
                    previousMonthButton.disableProperty().set(true);
                }
            } else {
                previousMonthButton.disableProperty().set(true);
            }
        });
        nextMonthButton.setOnAction(event -> {
            Month nextMonth = monthRepository.getNext(budget.getCurrentMonth());

            if (nextMonth == null) {
                nextMonth = monthFactory.createNextMonth(budget.getCurrentMonth());
                monthRepository.putMonth(nextMonth);
            }

            monthCalculationServices.recalculateMonths(budget.getCurrentMonth());
            budget.setCurrentMonth(nextMonth);

            previousMonthButton.disableProperty().set(false);
        });
    }
}
