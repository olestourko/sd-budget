package com.olestourko.sdbudget.desktop.controllers;

import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.desktop.controls.CurrencyTableCell;
import com.olestourko.sdbudget.desktop.models.BudgetItemViewModel;
import com.olestourko.sdbudget.desktop.models.MonthViewModel;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.desktop.mappers.MonthMapper;
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
import javafx.scene.control.TableView;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javax.inject.Inject;
import org.mapstruct.factory.Mappers;

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

    final private BudgetItemViewModel totalAdjustments = new BudgetItemViewModel("Total Adjustments", BigDecimal.ZERO);
    final private MonthMapper monthMapper;
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
    public ScratchpadController(Budget budget) {
        this.monthMapper = Mappers.getMapper(MonthMapper.class);
        this.budget = budget;
    }

    // TODO: Replace with dependency injection
    public void load() {
        //Set the month and add callback for when the month property in the Budget model changes
        MonthViewModel monthViewModel = monthMapper.mapMonthToMonthViewModel(budget.getCurrentMonth());
        
        this.setMonth(monthViewModel);
        budget.currentMonthProperty().addListener(event -> {
            this.setMonth(monthMapper.mapMonthToMonthViewModel(budget.getCurrentMonth()));
        });

        nameColumn.setCellValueFactory(new PropertyValueFactory<BudgetItemViewModel, String>("name"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<BudgetItemViewModel, Double>("amount"));

        //Add the "Adjustment Totals" item
        totalAdjustments.amountProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue observable, Number oldValue, Number newValue) {
//                budget.getCurrentMonth().adjustments.setAmount((BigDecimal) newValue);
            }
        });

        //Allow editing of adjustments, and update "Total Adjustments" row whenever they change.
        amountColumn.setCellFactory(new Callback<TableColumn<BudgetItemViewModel, BigDecimal>, TableCell<BudgetItemViewModel, BigDecimal>>() {
            StringConverter<BigDecimal> converter;

            @Override
            public TableCell<BudgetItemViewModel, BigDecimal> call(TableColumn<BudgetItemViewModel, BigDecimal> param) {
                CurrencyTableCell cell = new CurrencyTableCell("$");
                return cell;
            }
        });
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

        ((TableColumn) totalsTable.getColumns().get(1)).setCellFactory(new Callback<TableColumn<BudgetItemViewModel, BigDecimal>, TableCell<BudgetItemViewModel, BigDecimal>>() {
            StringConverter<BigDecimal> converter;

            @Override
            public TableCell<BudgetItemViewModel, BigDecimal> call(TableColumn<BudgetItemViewModel, BigDecimal> param) {
                CurrencyTableCell cell = new CurrencyTableCell("$");
                return cell;
            }
        });
        
        //Set the date on the label
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
        periodDate.setText(dateFormat.format(monthViewModel.calendar.getTime()));

        // Remove adjusments when DELETE key is pressed
        scratchPadTable.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.DELETE)) {
                    BudgetItemViewModel selectedItem = (BudgetItemViewModel) scratchPadTable.getSelectionModel().getSelectedItem();
                    if (selectedItem == totalAdjustments) {
                        return;
                    }
                    MonthViewModel monthViewModel = monthMapper.mapMonthToMonthViewModel(budget.getCurrentMonth());
                    monthViewModel.removeAdjustment(selectedItem);
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
        MonthViewModel monthViewModel = monthMapper.mapMonthToMonthViewModel(budget.getCurrentMonth());
        monthViewModel.addAdjustment(newItem);
    }

    private void setMonth(MonthViewModel monthViewModel) {
        Month month = monthMapper.mapMonthViewModelToMonth(monthViewModel);
//        budget.setCurrentMonth(month);
        scratchPadTable.getItems().removeListener(listChangeListener);
        scratchPadTable.setItems(monthViewModel.getAdjustments());
        scratchPadTable.getItems().addListener(listChangeListener);
        totalAdjustments.setAmount(month.getTotalAdjustments());
    }
}
