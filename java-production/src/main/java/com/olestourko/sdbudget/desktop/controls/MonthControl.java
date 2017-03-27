package com.olestourko.sdbudget.desktop.controls;

import com.olestourko.sdbudget.desktop.models.BudgetItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import com.olestourko.sdbudget.desktop.models.Month;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.BigDecimalStringConverter;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.util.Callback;

/**
 *
 * @author oles
 */
public class MonthControl extends AnchorPane {

    @FXML
    private Label periodDate;
    @FXML
    private TreeTableView budgetTable;

    private final TreeItem<BudgetItem> budgetTableRoot = new TreeItem<>(new BudgetItem("Budget", BigDecimal.ZERO));
    private final TreeItem<BudgetItem> revenuesRoot = new TreeItem<>(new BudgetItem("Revenues", BigDecimal.ZERO));
    private final TreeItem<BudgetItem> expensesRoot = new TreeItem<>(new BudgetItem("Expenses", BigDecimal.ZERO));
    private final TreeItem<BudgetItem> adjustmentsRoot = new TreeItem<>(new BudgetItem("Adjustments", BigDecimal.ZERO));
    @FXML
    private TableView totalsTable;
    @FXML
    private TableView closingTable;
    @FXML
    public TreeTableColumn nameColumn;
    @FXML
    public TreeTableColumn amountColumn;
    @FXML
    public CheckBox closeMonthCheckBox;

    private final SimpleObjectProperty<Month> month = new SimpleObjectProperty<Month>();

    private final ListChangeListener<BudgetItem> monthListChangeListener = new ListChangeListener<BudgetItem>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends BudgetItem> change) {
            revenuesRoot.getValue().setAmount(month.getValue().getTotalRevenues());
            expensesRoot.getValue().setAmount(month.getValue().getTotalExpenses());
            adjustmentsRoot.getValue().setAmount(month.getValue().getTotalAdjustments());
        }
    };

    public MonthControl() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/desktop/fxml/MonthControl.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        nameColumn.setCellFactory(new Callback<TreeTableColumn<BudgetItem, String>, TreeTableCell<BudgetItem, String>>() {
            @Override
            public TreeTableCell<BudgetItem, String> call(TreeTableColumn<BudgetItem, String> p) {
                ButtonTreeTableCell cell = new ButtonTreeTableCell("+");
                cell.setShowButtonCondition(new Callback<ButtonTreeTableCell, Boolean>() {
                    @Override
                    public Boolean call(ButtonTreeTableCell cell) {
                        TreeItem treeItem = cell.getTreeTableRow().getTreeItem();
                        try {
                            return (treeItem == revenuesRoot || treeItem == expensesRoot);
                        } catch (NullPointerException exception) {
                            // Do nothing
                        }
                        return false;
                    }
                });

                cell.button.setOnAction(event -> {
                    BudgetItem newBudgetItem = new BudgetItem("New Item", BigDecimal.ZERO);
                    TreeItem<BudgetItem> treeItem = cell.getTreeTableRow().getTreeItem();
                    if (treeItem.getValue() == revenuesRoot.getValue()) {
                        month.getValue().addRevenue(newBudgetItem);
                    } else if (treeItem.getValue() == expensesRoot.getValue()) {
                        month.getValue().addExpense(newBudgetItem);
                    }
                    TreeItem<BudgetItem> newTreeItem = new TreeItem<BudgetItem>(newBudgetItem);
                    treeItem.getChildren().add(newTreeItem);
                    treeItem.setExpanded(true);
                });
                return cell;
            }
        });

        nameColumn.setCellValueFactory(new Callback<CellDataFeatures<BudgetItem, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<BudgetItem, String> p) {
                return p.getValue().getValue().nameProperty();
            }
        });

        // This draws the textfield when editing a table cell
        amountColumn.setCellValueFactory(new Callback<CellDataFeatures<BudgetItem, BigDecimal>, ObservableValue<BigDecimal>>() {
            public ObservableValue<BigDecimal> call(CellDataFeatures<BudgetItem, BigDecimal> p) {
                // p.getValue() returns the TreeItem<Person> instance for a particular TreeTableView row,
                // p.getValue().getValue() returns the Person instance inside the TreeItem<Person>
                return p.getValue().getValue().amountProperty();
            }
        });

        // This draws the textfield when editing a table cell
        amountColumn.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn(new BigDecimalStringConverter()));
        // This is a callback for edits
        amountColumn.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<BudgetItem, BigDecimal>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<BudgetItem, BigDecimal> t) {
                TreeItem treeItem = t.getTreeTablePosition().getTreeItem();
                BudgetItem budgetItem = (BudgetItem) treeItem.getValue();
//                BudgetItem budgetItem = (BudgetItem) t.getTableView().getItems().get(t.getTablePosition().getRow());
                budgetItem.setAmount(t.getNewValue());
                MonthControl.this.fireEvent(new ActionEvent());
            }
        });

        budgetTableRoot.setExpanded(true);
        budgetTable.setEditable(true);
        budgetTable.setRoot(budgetTableRoot);

        // Update the tables when the month is changed
        this.monthProperty().addListener(property -> {
            refreshTables();
        });

        // Set up the closing table
        TableColumn closingTableAmountColumn = (TableColumn) closingTable.getColumns().get(1);
        closingTableAmountColumn.setCellFactory(TextFieldTableCell.<BudgetItem, BigDecimal>forTableColumn(new BigDecimalStringConverter()));
        closingTableAmountColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<BudgetItem, BigDecimal>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<BudgetItem, BigDecimal> t) {
                BudgetItem budgetItem = (BudgetItem) t.getTableView().getItems().get(t.getTablePosition().getRow());
                budgetItem.setAmount(t.getNewValue());
                MonthControl.this.fireEvent(new ActionEvent());
            }
        });

        // Set the handler for the "Close Month" checkbox
        this.closeMonthCheckBox.selectedProperty().addListener(checkbox -> {
            this.month.get().setIsClosed(this.closeMonthCheckBox.isSelected());
            updateTableStyles();
        });

        updateTableStyles();
    }

    public void refreshTables() {
        Month month = this.getMonth();
        budgetTableRoot.getChildren().clear();
        revenuesRoot.getChildren().clear();
        revenuesRoot.getValue().setAmount(month.getTotalRevenues());
        expensesRoot.getChildren().clear();
        expensesRoot.getValue().setAmount(month.getTotalExpenses());
        adjustmentsRoot.getChildren().clear();
        adjustmentsRoot.getValue().setAmount(month.getTotalAdjustments());

        budgetTableRoot.getChildren().add(revenuesRoot);
        budgetTableRoot.getChildren().add(expensesRoot);
        budgetTableRoot.getChildren().add(adjustmentsRoot);

        for (BudgetItem revenue : month.getRevenues()) {
            revenuesRoot.getChildren().add(new TreeItem<BudgetItem>(revenue));
        }

        for (BudgetItem expense : month.getExpenses()) {
            expensesRoot.getChildren().add(new TreeItem<BudgetItem>(expense));
        }

        budgetTableRoot.getChildren().add(new TreeItem<>(month.netIncomeTarget));
        budgetTableRoot.getChildren().add(new TreeItem<>(month.openingBalance));

        totalsTable.getItems().clear();
        totalsTable.getItems().addAll(month.closingBalanceTarget,
                month.estimatedClosingBalance,
                month.openingSurplus,
                month.totalSurplus);
        closingTable.getItems().clear();
        closingTable.getItems().addAll(month.closingBalance);

        // Set the date on the label
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
        periodDate.setText(dateFormat.format(month.calendar.getTime()));
        MonthControl.this.fireEvent(new ActionEvent());

        // Set the closing checkbox value
        closeMonthCheckBox.setSelected(month.getIsClosed());
    }

    // Enable / disable tables based on the "Month Closed" checkbox
    private void updateTableStyles() {
        if (this.closeMonthCheckBox.isSelected()) {
            budgetTable.getStyleClass().add("disabled");
            budgetTable.setEditable(false);
            closingTable.getStyleClass().removeAll("disabled");
            closingTable.setEditable(true);
        } else {
            budgetTable.getStyleClass().removeAll("disabled");
            budgetTable.setEditable(true);
            closingTable.getStyleClass().addAll("disabled");
            closingTable.setEditable(false);
        }
    }

    // Month property
    public Month getMonth() {
        return this.month.get();
    }

    public void setMonth(Month month) {
        if (this.month.getValue() != null) {
            this.month.getValue().getRevenues().removeListener(monthListChangeListener);
            this.month.getValue().getExpenses().removeListener(monthListChangeListener);
            this.month.getValue().getAdjustments().removeListener(monthListChangeListener);
        }
        this.month.set(month);
        this.month.getValue().getRevenues().addListener(monthListChangeListener);
        this.month.getValue().getExpenses().addListener(monthListChangeListener);
        this.month.getValue().getAdjustments().addListener(monthListChangeListener);
    }

    public SimpleObjectProperty<Month> monthProperty() {
        return this.month;
    }

    /**
     * Month change event
     * http://book2s.com/java/src/package/javafx/scene/control/buttonbase.html
     * http://book2s.com/java/src/package/javafx/scene/control/button.html
     */
    public final ObjectProperty<EventHandler<ActionEvent>> onMonthChangeProperty() {
        return onMonthChange;
    }

    public final void setOnMonthChange(EventHandler<ActionEvent> value) {
        onMonthChangeProperty().set(value);
    }

    public final EventHandler<ActionEvent> getOnMonthChange() {
        return onMonthChange.get();
    }

    private ObjectProperty<EventHandler<ActionEvent>> onMonthChange = new ObjectPropertyBase<EventHandler<ActionEvent>>() {
        @Override
        protected void invalidated() {
            setEventHandler(ActionEvent.ACTION, get());
        }

        @Override
        public Object getBean() {
            return MonthControl.this;
        }

        @Override
        public String getName() {
            return "onMonthChange";
        }
    };
}
