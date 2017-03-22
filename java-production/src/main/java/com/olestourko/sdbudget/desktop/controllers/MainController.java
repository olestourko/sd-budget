package com.olestourko.sdbudget.desktop.controllers;

import com.olestourko.sdbudget.core.models.Budget;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.core.services.PeriodServices;
import com.olestourko.sdbudget.desktop.models.Month;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
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
    
    final private MonthRepository monthRepository;
    final private Budget budget;

    @Inject
    MainController(MonthRepository monthRepository, Budget budget) {
        this.monthRepository = monthRepository;
        this.budget = budget;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
            if (nextMonth != null) {
                previousMonthButton.disableProperty().set(false);
                budget.setCurrentMonth(nextMonth);

                if (monthRepository.getNext(budget.getCurrentMonth()) == null) {
                    nextMonthButton.disableProperty().set(true);
                }
            } else {
                nextMonthButton.disableProperty().set(true);
            }
        });
    }

}
