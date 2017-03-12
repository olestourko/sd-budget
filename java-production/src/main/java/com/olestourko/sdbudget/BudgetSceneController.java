package com.olestourko.sdbudget;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.collections.ObservableList;
import com.olestourko.sdbudget.models.Month;
import com.olestourko.sdbudget.models.BudgetItem;
import com.olestourko.sdbudget.services.EstimateResult;
import com.olestourko.sdbudget.services.PeriodServices;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.BigDecimalStringConverter;
import javax.inject.Inject;

public class BudgetSceneController implements Initializable {

    @FXML
    private TableView budgetTable;
    @FXML
    private TableView totalsTable;
    @FXML
    private Label periodDate;
    @FXML
    public TableColumn nameColumn;
    @FXML
    public TableColumn amountColumn;
    @FXML
    public Button scratchpadViewButton;

    public Month month;
    public ObservableList<BudgetItem> items;
    public BudgetItem closingBalanceTarget;
    public BudgetItem estimatedClosingBalance;
    public BudgetItem surplus;
    final private PeriodServices periodServices;

    @Inject
    BudgetSceneController(PeriodServices periodServices) {
        this.periodServices = periodServices;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    // TODO: Replace with dependency injection
    public void load() {
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<BudgetItem, String>("name")
        );
        amountColumn.setCellValueFactory(
                new PropertyValueFactory<BudgetItem, BigDecimal>("amount")
        );
        budgetTable.setItems(items);
        //This draws the textfield when editing a table cell
        amountColumn.setCellFactory(TextFieldTableCell.<BudgetItem, BigDecimal>forTableColumn(new BigDecimalStringConverter()));
        //This is a callback for edits
        amountColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<BudgetItem, BigDecimal>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<BudgetItem, BigDecimal> t) {
                BudgetItem budgetItem = (BudgetItem) t.getTableView().getItems().get(t.getTablePosition().getRow());
                budgetItem.setAmount(t.getNewValue());
                EstimateResult result = periodServices.calculateEstimate(
                        month.revenues.getAmount(),
                        month.expenses.getAmount(),
                        month.adjustments.getAmount(),
                        month.netIncomeTarget.getAmount(),
                        month.openingBalance.getAmount()
                );

                closingBalanceTarget.setAmount(month.openingBalance.getAmount()
                        .add(month.netIncomeTarget.getAmount()));
                estimatedClosingBalance.setAmount(result.estimatedBalance);
                surplus.setAmount(result.surplus);
            }
        });

        //Set up the totals table
        totalsTable.getItems().addAll(closingBalanceTarget, estimatedClosingBalance, surplus);
        
        //Set the date on the label
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
        periodDate.setText(dateFormat.format(month.calendar.getTime()));
    }
}
