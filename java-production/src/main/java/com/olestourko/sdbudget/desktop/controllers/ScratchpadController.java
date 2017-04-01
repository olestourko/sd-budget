package com.olestourko.sdbudget.desktop.controllers;

import com.olestourko.sdbudget.desktop.models.BudgetItemViewModel;
import com.olestourko.sdbudget.desktop.models.MonthViewModel;
import com.olestourko.sdbudget.desktop.repositories.MonthRepository;
import com.olestourko.sdbudget.desktop.models.Budget;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.converter.BigDecimalStringConverter;
import javax.inject.Inject;

public class ScratchpadController implements Initializable {

    @FXML
    private TableView scratchPadTable;
    @FXML
    private TableView totalsTable;
    @FXML
    private Label periodDate;
    @FXML
    private TableColumn nameColumn;
    @FXML
    private TableColumn amountColumn;
    @FXML
    private TextField nameField;
    @FXML
    private TextField amountField;

    final private MonthRepository monthRepository;
    final private BudgetItemViewModel totalAdjustments = new BudgetItemViewModel("Total Adjustments", BigDecimal.ZERO);
    final private Budget budget;

    final private ListChangeListener<BudgetItemViewModel> listChangeListener = new ListChangeListener<BudgetItemViewModel>() {
        @Override
        public void onChanged(Change<? extends BudgetItemViewModel> change) {
            totalAdjustments.setAmount(budget.getCurrentMonth().getTotalAdjustments());
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @Inject
    public ScratchpadController(MonthRepository monthRepository, Budget budget) {
        this.monthRepository = monthRepository;
        this.budget = budget;
    }

    // TODO: Replace with dependency injection
    public void load() {
        //Set the month and add callback for when the month property in the Budget model changes
        this.setMonth(budget.getCurrentMonth());
        budget.currentMonthProperty().addListener(event -> {
            this.setMonth(budget.getCurrentMonth());
        });

        nameColumn.setCellValueFactory(new PropertyValueFactory<BudgetItemViewModel, String>("name")
        );
        amountColumn.setCellValueFactory(new PropertyValueFactory<BudgetItemViewModel, Double>("amount")
        );

        //Add the "Adjustment Totals" item
        totalAdjustments.amountProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue observable, Number oldValue, Number newValue) {
//                budget.getCurrentMonth().adjustments.setAmount((BigDecimal) newValue);
            }
        });

        //Allow editing of adjustments, and update "Total Adjustments" row whenever they change.
        amountColumn.setCellFactory(TextFieldTableCell.<BudgetItemViewModel, BigDecimal>forTableColumn(new BigDecimalStringConverter()));
        amountColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<BudgetItemViewModel, BigDecimal>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<BudgetItemViewModel, BigDecimal> t) {
                BudgetItemViewModel budgetItem = (BudgetItemViewModel) t.getTableView().getItems().get(t.getTablePosition().getRow());
                budgetItem.setAmount(t.getNewValue());
                totalAdjustments.setAmount(budget.getCurrentMonth().getTotalAdjustments());
            }
        });

        //Set up the totals table
        totalsTable.getItems().add(totalAdjustments);

        //Set the date on the label
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
        periodDate.setText(dateFormat.format(budget.getCurrentMonth().calendar.getTime()));

        // Remove adjusments when DELETE key is pressed
        scratchPadTable.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.DELETE)) {
                    BudgetItemViewModel selectedItem = (BudgetItemViewModel) scratchPadTable.getSelectionModel().getSelectedItem();
                    if (selectedItem == totalAdjustments) {
                        return;
                    }
                    budget.getCurrentMonth().removeAdjustment(selectedItem);
                }
            }
        });

    }

    public void handleAddTransactionButtonAction(ActionEvent event) {
        String name = nameField.getText();
        BigDecimal amount = new BigDecimal(amountField.getText());
        BudgetItemViewModel newItem = new BudgetItemViewModel(name, amount);
        nameField.setText("");
        amountField.setText("");
        budget.getCurrentMonth().addAdjustment(newItem);
    }

    private void setMonth(MonthViewModel month) {
        budget.setCurrentMonth(month);
        scratchPadTable.getItems().removeListener(listChangeListener);
        scratchPadTable.setItems(month.getAdjustments());
        scratchPadTable.getItems().addListener(listChangeListener);
        totalAdjustments.setAmount(month.getTotalAdjustments());
    }
}
