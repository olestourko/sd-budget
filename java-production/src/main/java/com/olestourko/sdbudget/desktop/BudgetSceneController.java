package com.olestourko.sdbudget.desktop;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.collections.ObservableList;
import com.olestourko.sdbudget.desktop.models.Month;
import com.olestourko.sdbudget.desktop.models.BudgetItem;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.core.services.ClosingResult;
import com.olestourko.sdbudget.core.services.EstimateResult;
import com.olestourko.sdbudget.core.services.PeriodServices;
import com.olestourko.sdbudget.core.models.Budget;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javafx.collections.FXCollections;
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
    private TableView closingTable;
    @FXML
    private Label periodDate;
    @FXML
    public TableColumn nameColumn;
    @FXML
    public TableColumn amountColumn;
    @FXML
    public Button scratchpadViewButton;
    @FXML
    public Button previousMonthButton;
    @FXML
    public Button nextMonthButton;

    public ObservableList<BudgetItem> items;
    public BudgetItem closingBalanceTarget;
    public BudgetItem estimatedClosingBalance;
    public BudgetItem surplus;
    final private PeriodServices periodServices;
    final private MonthRepository monthRepository;
    final private Budget budget;

    final private BudgetItem closingBalance = new BudgetItem("Closing Balance", BigDecimal.ZERO);

    @Inject
    BudgetSceneController(PeriodServices periodServices, MonthRepository monthRepository, Budget budget) {
        this.periodServices = periodServices;
        this.monthRepository = monthRepository;
        this.budget = budget;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        previousMonthButton.setOnAction(event -> {
            Month previousMonth = monthRepository.getPrevious(budget.getCurrentMonth());
            if (previousMonth != null) {
                setMonth(previousMonth);
                nextMonthButton.disableProperty().set(false);
            } else {
                previousMonthButton.disableProperty().set(true);
            }
        });
        nextMonthButton.setOnAction(event -> {
            Month nextMonth = monthRepository.getNext(budget.getCurrentMonth());
            if (nextMonth != null) {
                setMonth(nextMonth);
                previousMonthButton.disableProperty().set(false);
            } else {
                nextMonthButton.disableProperty().set(true);
            }
        });
    }

    // TODO: Replace with dependency injection
    public void load() {
        //Set the month and add callback for when the month property in the Budget model changes
        this.setMonth(budget.getCurrentMonth());
        budget.currentMonthProperty().addListener(event -> {
            this.setMonth(budget.getCurrentMonth());
        });
        
        if(monthRepository.getPrevious(budget.getCurrentMonth()) == null) {
            previousMonthButton.disableProperty().set(true);
        }
        if(monthRepository.getNext(budget.getCurrentMonth()) == null) {
            nextMonthButton.disableProperty().set(true);
        }
        
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<BudgetItem, String>("name")
        );
        amountColumn.setCellValueFactory(
                new PropertyValueFactory<BudgetItem, BigDecimal>("amount")
        );
        //This draws the textfield when editing a table cell
        amountColumn.setCellFactory(TextFieldTableCell.<BudgetItem, BigDecimal>forTableColumn(new BigDecimalStringConverter()));
        //This is a callback for edits
        amountColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<BudgetItem, BigDecimal>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<BudgetItem, BigDecimal> t) {
                BudgetItem budgetItem = (BudgetItem) t.getTableView().getItems().get(t.getTablePosition().getRow());
                budgetItem.setAmount(t.getNewValue());
                calculate();
            }
        });

        //Set up the totals table
        totalsTable.getItems().addAll(closingBalanceTarget, estimatedClosingBalance, surplus);

        //Set up the closing table
        closingTable.getItems().addAll(closingBalance);
        TableColumn closingTableAmountColumn = (TableColumn) closingTable.getColumns().get(1);
        closingTableAmountColumn.setCellFactory(TextFieldTableCell.<BudgetItem, BigDecimal>forTableColumn(new BigDecimalStringConverter()));
        closingTableAmountColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<BudgetItem, BigDecimal>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<BudgetItem, BigDecimal> t) {
                BudgetItem budgetItem = (BudgetItem) t.getTableView().getItems().get(t.getTablePosition().getRow());
                budgetItem.setAmount(t.getNewValue());
                calculate();
            }
        });
    }

    private void setMonth(Month month) {
        budget.setCurrentMonth(month);
        budgetTable.setItems(FXCollections.observableArrayList(
                month.revenues,
                month.expenses,
                month.adjustments,
                month.netIncomeTarget,
                month.openingBalance
        ));
        
        //Set the date on the label
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
        periodDate.setText(dateFormat.format(month.calendar.getTime()));
        
        calculate();
    }

    private void calculate() {
        Month month = budget.getCurrentMonth();
        if (closingBalance.getAmount().equals(BigDecimal.ZERO)) {
            // Calculate innter-month estimates
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
        } else {
            // Calculate closing balances
            ClosingResult result = periodServices.calculateClosing(month.netIncomeTarget.getAmount(), month.openingBalance.getAmount(), closingBalance.getAmount());
            month.adjustments.setAmount(result.closingAdjustment);
            estimatedClosingBalance.setAmount(closingBalance.getAmount());
            surplus.setAmount(result.surplus);
        }
    }
}
