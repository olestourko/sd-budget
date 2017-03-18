package com.olestourko.sdbudget.desktop;

import com.olestourko.sdbudget.desktop.models.BudgetItem;
import com.olestourko.sdbudget.desktop.models.Month;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.core.models.Budget;
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

public class ScratchpadSceneController implements Initializable {

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
    @FXML
    public Button budgetViewButton;

    final private MonthRepository monthRepository;
    final private BudgetItem totalAdjustments = new BudgetItem("Total Adjustments", BigDecimal.ZERO);
    final private Budget budget;

    final private ListChangeListener<BudgetItem> listChangeListener = new ListChangeListener<BudgetItem>() {
        @Override
        public void onChanged(Change<? extends BudgetItem> change) {
            calculate();
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @Inject
    public ScratchpadSceneController(MonthRepository monthRepository, Budget budget) {
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

        nameColumn.setCellValueFactory(
                new PropertyValueFactory<BudgetItem, String>("name")
        );
        amountColumn.setCellValueFactory(
                new PropertyValueFactory<BudgetItem, Double>("amount")
        );

        //Add the "Adjustment Totals" item
        totalAdjustments.amountProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue observable, Number oldValue, Number newValue) {
                budget.getCurrentMonth().adjustments.setAmount((BigDecimal) newValue);
            }
        });

        //Allow editing of adjustments, and update "Total Adjustments" row whenever they change.
        amountColumn.setCellFactory(TextFieldTableCell.<BudgetItem, BigDecimal>forTableColumn(new BigDecimalStringConverter()));
        amountColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<BudgetItem, BigDecimal>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<BudgetItem, BigDecimal> t) {
                BudgetItem budgetItem = (BudgetItem) t.getTableView().getItems().get(t.getTablePosition().getRow());
                budgetItem.setAmount(t.getNewValue());
                calculate();
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
                    BudgetItem selectedItem = (BudgetItem) scratchPadTable.getSelectionModel().getSelectedItem();
                    if (selectedItem == totalAdjustments) {
                        return;
                    }
                    budget.getCurrentMonth().transactions.remove(selectedItem);
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
        budget.getCurrentMonth().transactions.add(newItem);
    }

    private void calculate() {
        BigDecimal sum = BigDecimal.ZERO;
        for (Object o : scratchPadTable.getItems()) {
            BudgetItem item = (BudgetItem) o;
            sum = sum.add(item.getAmount());
        }
        totalAdjustments.setAmount(sum);
    }

    private void setMonth(Month month) {
        budget.setCurrentMonth(month);
        scratchPadTable.getItems().removeListener(listChangeListener);
        scratchPadTable.setItems(month.transactions);
        scratchPadTable.getItems().addListener(listChangeListener);
        calculate();        
    }
}
