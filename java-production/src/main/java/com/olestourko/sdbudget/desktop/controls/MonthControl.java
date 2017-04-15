package com.olestourko.sdbudget.desktop.controls;

import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.desktop.models.BudgetItemViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import com.olestourko.sdbudget.desktop.models.MonthViewModel;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 *
 * @author oles
 */
public class MonthControl extends AnchorPane {

    @FXML
    private Label periodDate;
    @FXML
    private TreeTableView budgetTable;

    private final TreeItem<BudgetItemViewModel> budgetTableRoot = new TreeItem<>(new BudgetItemViewModel("Budget", BigDecimal.ZERO));
    private final TreeItem<BudgetItemViewModel> revenuesRoot = new TreeItem<>(new BudgetItemViewModel("Revenues", BigDecimal.ZERO));
    private final TreeItem<BudgetItemViewModel> expensesRoot = new TreeItem<>(new BudgetItemViewModel("Expenses", BigDecimal.ZERO));
    private final TreeItem<BudgetItemViewModel> adjustmentsRoot = new TreeItem<>(new BudgetItemViewModel("Adjustments", BigDecimal.ZERO));
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

    private final SimpleObjectProperty<MonthViewModel> month = new SimpleObjectProperty<MonthViewModel>();

    private Callback<MonthControl, MonthViewModel> monthChangedCallback;

    public void setOnMonthChanged(Callback<MonthControl, MonthViewModel> callback) {
        this.monthChangedCallback = callback;
    }

    private final ListChangeListener<BudgetItemViewModel> revenuesListChangeListener = new ListChangeListener<BudgetItemViewModel>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends BudgetItemViewModel> change) {
            revenuesRoot.getValue().setAmount(month.getValue().getTotalRevenues());
            revenuesRoot.getChildren().clear();
            for (BudgetItemViewModel revenue : month.getValue().getRevenues()) {
                revenuesRoot.getChildren().add(new TreeItem<BudgetItemViewModel>(revenue));
            }
        }
    };

    private final ListChangeListener<BudgetItemViewModel> expensesListChangeListener = new ListChangeListener<BudgetItemViewModel>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends BudgetItemViewModel> change) {
            expensesRoot.getValue().setAmount(month.getValue().getTotalExpenses());
            expensesRoot.getChildren().clear();
            for (BudgetItemViewModel expense : month.getValue().getExpenses()) {
                expensesRoot.getChildren().add(new TreeItem<BudgetItemViewModel>(expense));
            }
        }
    };

    private final ListChangeListener<BudgetItemViewModel> adjustmentsListChangeListener = new ListChangeListener<BudgetItemViewModel>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends BudgetItemViewModel> change) {
            adjustmentsRoot.getValue().setAmount(month.getValue().getTotalAdjustments());
            adjustmentsRoot.getChildren().clear();
            for (BudgetItemViewModel adjustment : month.getValue().getAdjustments()) {
                adjustmentsRoot.getChildren().add(new TreeItem<BudgetItemViewModel>(adjustment));
            }
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

        nameColumn.setCellFactory(new Callback<TreeTableColumn<BudgetItemViewModel, String>, TreeTableCell<BudgetItemViewModel, String>>() {
            @Override
            public TreeTableCell<BudgetItemViewModel, String> call(TreeTableColumn<BudgetItemViewModel, String> p) {
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
                    BudgetItemViewModel newBudgetItem = new BudgetItemViewModel("New Item", BigDecimal.ZERO);
                    newBudgetItem.setModel(new BudgetItem());
                    TreeItem<BudgetItemViewModel> treeItem = cell.getTreeTableRow().getTreeItem();
                    if (treeItem.getValue() == revenuesRoot.getValue()) {
                        month.getValue().getRevenues().add(newBudgetItem);
                    } else if (treeItem.getValue() == expensesRoot.getValue()) {
                        month.getValue().getExpenses().add(newBudgetItem);
                    }
                    treeItem.setExpanded(true);
                });
//                cell.setEditable(treeItem == revenuesRoot || treeItem == expensesRoot);

                return cell;
            }
        });

        nameColumn.setCellValueFactory(new Callback<CellDataFeatures<BudgetItemViewModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<BudgetItemViewModel, String> p) {
                return p.getValue().getValue().nameProperty();
            }
        });

        nameColumn.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<BudgetItemViewModel, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<BudgetItemViewModel, String> t) {
                TreeItem treeItem = t.getTreeTablePosition().getTreeItem();
                if (treeItem.getChildren().size() == 0) {
                    BudgetItemViewModel budgetItem = (BudgetItemViewModel) treeItem.getValue();
                    budgetItem.setName(t.getNewValue());
                    callMonthChangeCallback();
                }
            }

        });

        // This draws the textfield when editing a table cell
        amountColumn.setCellValueFactory(new Callback<CellDataFeatures<BudgetItemViewModel, BigDecimal>, ObservableValue<BigDecimal>>() {
            public ObservableValue<BigDecimal> call(CellDataFeatures<BudgetItemViewModel, BigDecimal> p) {
                // p.getValue() returns the TreeItem<Person> instance for a particular TreeTableView row,
                // p.getValue().getValue() returns the Person instance inside the TreeItem<Person>
                return p.getValue().getValue().amountProperty();
            }
        });

        amountColumn.setCellFactory(new Callback<TreeTableColumn<BudgetItemViewModel, BigDecimal>, TreeTableCell<BudgetItemViewModel, BigDecimal>>() {
            StringConverter<BigDecimal> converter;

            @Override
            public TreeTableCell<BudgetItemViewModel, BigDecimal> call(TreeTableColumn<BudgetItemViewModel, BigDecimal> param) {
                CurrencyTreeTableCell cell = new CurrencyTreeTableCell("$");
                return cell;
            }
        });

        amountColumn.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<BudgetItemViewModel, BigDecimal>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<BudgetItemViewModel, BigDecimal> t) {
                TreeItem treeItem = t.getTreeTablePosition().getTreeItem();
                if (treeItem.getChildren().size() == 0) {
                    BudgetItemViewModel budgetItem = (BudgetItemViewModel) treeItem.getValue();
                    budgetItem.setAmount(t.getNewValue());
                    callMonthChangeCallback();
                }
            }
        });

        budgetTableRoot.setExpanded(true);
        budgetTable.setEditable(true);
        budgetTable.setRoot(budgetTableRoot);

        // Update the tables when the month is changed
        this.monthProperty().addListener(property -> {
            callMonthChangeCallback();
            MonthViewModel month = this.getMonth();
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

            for (BudgetItemViewModel revenue : month.getRevenues()) {
                revenuesRoot.getChildren().add(new TreeItem<BudgetItemViewModel>(revenue));
            }

            for (BudgetItemViewModel expense : month.getExpenses()) {
                expensesRoot.getChildren().add(new TreeItem<BudgetItemViewModel>(expense));
            }

            budgetTableRoot.getChildren().add(new TreeItem<>(month.getNetIncomeTarget()));
            budgetTableRoot.getChildren().add(new TreeItem<>(month.getOpeningBalance()));

            totalsTable.getItems().clear();
            totalsTable.getItems().addAll(
                    month.getClosingBalanceTarget(),
                    month.getEstimatedClosingBalance(),
                    month.getOpeningSurplus(),
                    month.getClosingSurplus()
            );
            closingTable.getItems().clear();
            closingTable.getItems().addAll(month.getClosingBalance());

            // Set the date on the label
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
            periodDate.setText(dateFormat.format(month.calendar.getTime()));

            // Set the closing checkbox value
            closeMonthCheckBox.setSelected(month.getIsClosed());
        });

        // Totals table
        TableColumn totalsTableAmountColumn = (TableColumn) totalsTable.getColumns().get(1);
        totalsTableAmountColumn.setCellFactory(new Callback<TableColumn<BudgetItemViewModel, BigDecimal>, TableCell<BudgetItemViewModel, BigDecimal>>() {
            StringConverter<BigDecimal> converter;

            @Override
            public TableCell<BudgetItemViewModel, BigDecimal> call(TableColumn<BudgetItemViewModel, BigDecimal> param) {
                CurrencyTableCell cell = new CurrencyTableCell("$");
                return cell;
            }
        });

        // Set up the closing table
        TableColumn closingTableAmountColumn = (TableColumn) closingTable.getColumns().get(1);
        closingTableAmountColumn.setCellFactory(new Callback<TableColumn<BudgetItemViewModel, BigDecimal>, TableCell<BudgetItemViewModel, BigDecimal>>() {
            StringConverter<BigDecimal> converter;

            @Override
            public TableCell<BudgetItemViewModel, BigDecimal> call(TableColumn<BudgetItemViewModel, BigDecimal> param) {
                CurrencyTableCell cell = new CurrencyTableCell("$");
                return cell;
            }
        });

        closingTableAmountColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<BudgetItemViewModel, BigDecimal>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<BudgetItemViewModel, BigDecimal> t) {
                BudgetItemViewModel budgetItem = (BudgetItemViewModel) t.getTableView().getItems().get(t.getTablePosition().getRow());
                budgetItem.setAmount(t.getNewValue());
                callMonthChangeCallback();
            }
        });

        // Set the handler for the "Close Month" checkbox
        this.closeMonthCheckBox.selectedProperty().addListener(checkbox -> {
            this.month.get().setIsClosed(this.closeMonthCheckBox.isSelected());
            updateTableStyles();
        });

        updateTableStyles();

        // Remove item when DELETE key is pressed
        budgetTable.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.DELETE)) {
                    TreeItem treeItem = (TreeItem) budgetTable.getSelectionModel().getSelectedItem();
                    BudgetItemViewModel selectedItem = (BudgetItemViewModel) treeItem.getValue();
                    MonthViewModel month = MonthControl.this.month.get();
                    if (month.getRevenues().contains(selectedItem)) {
                        month.getRevenues().remove(selectedItem);
                    } else if (month.getExpenses().contains(selectedItem)) {
                        month.getExpenses().remove(selectedItem);
                    }
                    callMonthChangeCallback();
                }
            }
        });
    }

    protected void callMonthChangeCallback() {
        if (monthChangedCallback != null) {
            monthChangedCallback.call(this);
        }
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
    public MonthViewModel getMonth() {
        return this.month.get();
    }

    public void setMonth(MonthViewModel month) {
        if (this.month.getValue() != null) {
            this.month.getValue().getRevenues().removeListener(revenuesListChangeListener);
            this.month.getValue().getExpenses().removeListener(expensesListChangeListener);
            this.month.getValue().getAdjustments().removeListener(adjustmentsListChangeListener);
        }
        this.month.set(month);
        month.getRevenues().addListener(revenuesListChangeListener);
        month.getExpenses().addListener(expensesListChangeListener);
        month.getAdjustments().addListener(adjustmentsListChangeListener);
    }

    public SimpleObjectProperty<MonthViewModel> monthProperty() {
        return this.month;
    }
}
