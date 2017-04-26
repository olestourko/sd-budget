package com.olestourko.sdbudget.desktop.controls;

import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.desktop.mappers.MonthMapper;
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
import org.mapstruct.factory.Mappers;

/**
 *
 * @author oles
 */
public class MonthControl extends AnchorPane {

    @FXML
    private Label dateLabel;
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

    private final SimpleObjectProperty<Month> month = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<MonthViewModel> monthViewModel = new SimpleObjectProperty<MonthViewModel>();
    private final MonthMapper monthMapper;

    private Callback<MonthControl, Month> monthChangedCallback;

    public void setOnMonthChanged(Callback<MonthControl, Month> callback) {
        this.monthChangedCallback = callback;
    }

    private final ListChangeListener<BudgetItemViewModel> revenuesListChangeListener = new ListChangeListener<BudgetItemViewModel>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends BudgetItemViewModel> change) {
            revenuesRoot.getValue().setAmount(monthViewModel.getValue().getTotalRevenues());
            revenuesRoot.getChildren().clear();
            for (BudgetItemViewModel revenue : monthViewModel.getValue().getRevenues()) {
                revenuesRoot.getChildren().add(new TreeItem<BudgetItemViewModel>(revenue));
            }
        }
    };

    private final ListChangeListener<BudgetItemViewModel> expensesListChangeListener = new ListChangeListener<BudgetItemViewModel>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends BudgetItemViewModel> change) {
            expensesRoot.getValue().setAmount(monthViewModel.getValue().getTotalExpenses());
            expensesRoot.getChildren().clear();
            for (BudgetItemViewModel expense : monthViewModel.getValue().getExpenses()) {
                expensesRoot.getChildren().add(new TreeItem<BudgetItemViewModel>(expense));
            }
        }
    };

    private final ListChangeListener<BudgetItemViewModel> adjustmentsListChangeListener = new ListChangeListener<BudgetItemViewModel>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends BudgetItemViewModel> change) {
            adjustmentsRoot.getValue().setAmount(monthViewModel.getValue().getTotalAdjustments());
            adjustmentsRoot.getChildren().clear();
            for (BudgetItemViewModel adjustment : monthViewModel.getValue().getAdjustments()) {
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

        this.monthMapper = Mappers.getMapper(MonthMapper.class);

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
                        monthViewModel.getValue().getRevenues().add(newBudgetItem);
                    } else if (treeItem.getValue() == expensesRoot.getValue()) {
                        monthViewModel.getValue().getExpenses().add(newBudgetItem);
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

        // Prevent the tables from having rows selected at the same time
        budgetTable.getSelectionModel().selectedItemProperty().addListener(objectProperty -> {
            totalsTable.getSelectionModel().clearSelection();
            closingTable.getSelectionModel().clearSelection();
        });

        totalsTable.getSelectionModel().selectedItemProperty().addListener(objectProperty -> {
            budgetTable.getSelectionModel().clearSelection();
            closingTable.getSelectionModel().clearSelection();
        });

        closingTable.getSelectionModel().selectedItemProperty().addListener(objectProperty -> {
            budgetTable.getSelectionModel().clearSelection();
            totalsTable.getSelectionModel().clearSelection();
        });

        // Update the tables when the month is changed
        this.monthProperty().addListener(property -> {
            MonthViewModel monthViewModel = monthMapper.mapMonthToMonthViewModel(getMonth());
            if (this.monthViewModel.getValue() != null) {
                this.monthViewModel.getValue().getRevenues().removeListener(revenuesListChangeListener);
                this.monthViewModel.getValue().getExpenses().removeListener(expensesListChangeListener);
                this.monthViewModel.getValue().getAdjustments().removeListener(adjustmentsListChangeListener);
            }

            this.monthViewModel.set(monthViewModel);
            monthViewModel.getRevenues().addListener(revenuesListChangeListener);
            monthViewModel.getExpenses().addListener(expensesListChangeListener);
            monthViewModel.getAdjustments().addListener(adjustmentsListChangeListener);

            // This does the calculations
            callMonthChangeCallback();
            
            budgetTableRoot.getChildren().clear();
            revenuesRoot.getChildren().clear();
            revenuesRoot.getValue().setAmount(monthViewModel.getTotalRevenues());
            expensesRoot.getChildren().clear();
            expensesRoot.getValue().setAmount(monthViewModel.getTotalExpenses());
            adjustmentsRoot.getChildren().clear();
            adjustmentsRoot.getValue().setAmount(monthViewModel.getTotalAdjustments());

            budgetTableRoot.getChildren().add(revenuesRoot);
            budgetTableRoot.getChildren().add(expensesRoot);
            budgetTableRoot.getChildren().add(adjustmentsRoot);

            for (BudgetItemViewModel revenue : monthViewModel.getRevenues()) {
                revenuesRoot.getChildren().add(new TreeItem<BudgetItemViewModel>(revenue));
            }

            for (BudgetItemViewModel expense : monthViewModel.getExpenses()) {
                expensesRoot.getChildren().add(new TreeItem<BudgetItemViewModel>(expense));
            }

            budgetTableRoot.getChildren().add(new TreeItem<>(monthViewModel.getNetIncomeTarget()));
            budgetTableRoot.getChildren().add(new TreeItem<>(monthViewModel.getOpeningBalance()));

            totalsTable.getItems().clear();
            totalsTable.getItems().addAll(
                    monthViewModel.getClosingBalanceTarget(),
                    monthViewModel.getEstimatedClosingBalance(),
                    monthViewModel.getOpeningSurplus(),
                    monthViewModel.getClosingSurplus()
            );
            closingTable.getItems().clear();
            closingTable.getItems().addAll(monthViewModel.getClosingBalance());

            // Set the date on the label
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
            dateLabel.setText(dateFormat.format(monthViewModel.calendar.getTime()));

            // Set the closing checkbox value
            closeMonthCheckBox.setSelected(monthViewModel.getIsClosed());
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
            this.monthViewModel.get().setIsClosed(this.closeMonthCheckBox.isSelected());
            callMonthChangeCallback();
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
                    MonthViewModel month = MonthControl.this.monthViewModel.get();
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
    public Month getMonth() {
        return this.month.get();
    }

    public void setMonth(Month month) {
        this.month.set(month);
    }

    public MonthViewModel getMonthViewModel() {
        return this.monthViewModel.get();
    }

    public void setMonthViewModel(MonthViewModel monthViewModel) {
        this.monthViewModel.set(monthViewModel);
    }

    public SimpleObjectProperty<Month> monthProperty() {
        return this.month;
    }
}
