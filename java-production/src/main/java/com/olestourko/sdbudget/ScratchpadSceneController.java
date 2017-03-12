package com.olestourko.sdbudget;

import com.olestourko.sdbudget.models.BudgetItem;
import com.olestourko.sdbudget.models.Month;
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

    public Month month;
    final private BudgetItem totalAdjustments = new BudgetItem("Total Adjustments", BigDecimal.ZERO);

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    // TODO: Replace with dependency injection
    public void load() {
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<BudgetItem, String>("name")
        );
        amountColumn.setCellValueFactory(
                new PropertyValueFactory<BudgetItem, Double>("amount")
        );
        //Update the totals whenever the scratchpad table is changed
        scratchPadTable.getItems().addListener(new ListChangeListener<BudgetItem>() {
            @Override
            public void onChanged(Change<? extends BudgetItem> change) {
                calculateTotals();
            }
        });

        //Add the "Adjustment Totals" item
        totalAdjustments.amountProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue observable, Number oldValue, Number newValue) {
                month.adjustments.setAmount((BigDecimal) newValue);
            }
        });

        //Allow editing of adjustments, and update "Total Adjustments" row whenever they change.
        amountColumn.setCellFactory(TextFieldTableCell.<BudgetItem, BigDecimal>forTableColumn(new BigDecimalStringConverter()));
        amountColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<BudgetItem, BigDecimal>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<BudgetItem, BigDecimal> t) {
                BudgetItem budgetItem = (BudgetItem) t.getTableView().getItems().get(t.getTablePosition().getRow());
                budgetItem.setAmount(t.getNewValue());
                calculateTotals();
            }
        });

        //Set up the totals table
        totalsTable.getItems().add(totalAdjustments);

        //Set the date on the label
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
        periodDate.setText(dateFormat.format(month.calendar.getTime()));

        // Remove adjusments when DELETE key is pressed
        scratchPadTable.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.DELETE)) {
                    BudgetItem selectedItem = (BudgetItem) scratchPadTable.getSelectionModel().getSelectedItem();
                    if (selectedItem == totalAdjustments) {
                        return;
                    }

                    scratchPadTable.getItems().remove(selectedItem);
                }
            }
        });

    }

    public void handleAddTransactionButtonAction(ActionEvent event) {
        String name = nameField.getText();
        BigDecimal amount = new BigDecimal(amountField.getText());
        BudgetItem newItem = new BudgetItem(name, amount);
        scratchPadTable.getItems().add(newItem);
        nameField.setText("");
        amountField.setText("");
    }

    private void calculateTotals() {
        BigDecimal sum = BigDecimal.ZERO;
        for (Object o : scratchPadTable.getItems()) {
            BudgetItem item = (BudgetItem) o;
            sum = sum.add(item.getAmount());
        }
        totalAdjustments.setAmount(sum);
    }
}
