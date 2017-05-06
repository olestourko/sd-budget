package com.olestourko.sdbudget.desktop.controllers;

import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.desktop.controls.CurrencyTableCell;
import com.olestourko.sdbudget.desktop.models.BudgetItemViewModel;
import com.olestourko.sdbudget.desktop.models.MonthViewModel;
import com.olestourko.sdbudget.desktop.mappers.MonthMapper;
import com.olestourko.sdbudget.desktop.models.Budget;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import javax.inject.Inject;
import org.mapstruct.factory.Mappers;

public class ScratchpadController implements Initializable {

    @FXML
    private TableView scratchpadTable;
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

    private final BudgetItemViewModel totalAdjustments = new BudgetItemViewModel("Total Adjustments", BigDecimal.ZERO);
    private final MonthMapper monthMapper;
    private final Budget budget;
    private final SimpleObjectProperty<Month> month = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<MonthViewModel> monthViewModel = new SimpleObjectProperty<>();

    private final ListChangeListener<BudgetItemViewModel> listChangeListener = new ListChangeListener<BudgetItemViewModel>() {
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

    public void load() {
        //Set the month and add callback for when the month property in the Budget model changes       
        this.setMonth(budget.getCurrentMonth());
        budget.currentMonthProperty().addListener(event -> {
            this.setMonth(budget.getCurrentMonth());
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
        nameColumn.setCellFactory(new Callback<TableColumn<BudgetItemViewModel, String>, TableCell<BudgetItemViewModel, String>>() {
            @Override
            public TableCell<BudgetItemViewModel, String> call(TableColumn<BudgetItemViewModel, String> param) {
                TextFieldTableCell cell = new TextFieldTableCell(new DefaultStringConverter());
                return cell;
            }
        });
        nameColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<BudgetItemViewModel, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<BudgetItemViewModel, String> t) {
                BudgetItemViewModel budgetItem = (BudgetItemViewModel) t.getTableView().getItems().get(t.getTablePosition().getRow());
                budgetItem.setName(t.getNewValue());
            }
        });

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

        // Disable selection on the totals table
        totalsTable.setSelectionModel(null);

        //Set the date on the label
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
        periodDate.setText(dateFormat.format(this.monthViewModel.get().calendar.getTime()));

        // Remove adjusments when DELETE key is pressed
        scratchpadTable.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.DELETE)) {
                    BudgetItemViewModel selectedItem = (BudgetItemViewModel) scratchpadTable.getSelectionModel().getSelectedItem();
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
        BudgetItem newItem = new BudgetItem(name, amount);
        nameField.setText("");
        amountField.setText("");
        this.month.getValue().getAdjustments().add(newItem);
        this.monthMapper.updateMonthViewModelFromMonth(this.month.getValue(), this.monthViewModel.getValue());
    }

    protected void setMonth(Month month) {
        MonthViewModel monthViewModel = monthMapper.mapMonthToMonthViewModel(month);
        this.month.set(month);
        this.monthViewModel.set(monthViewModel);

        scratchpadTable.getItems().removeListener(listChangeListener);
        scratchpadTable.setItems(monthViewModel.getAdjustments());
        scratchpadTable.getItems().addListener(listChangeListener);
        totalAdjustments.setAmount(month.getTotalAdjustments());
    }
}
