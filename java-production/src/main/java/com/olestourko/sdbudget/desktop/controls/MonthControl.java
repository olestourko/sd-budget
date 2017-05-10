package com.olestourko.sdbudget.desktop.controls;

import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.core.services.MonthLogicServices;
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
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TextFieldTreeTableCell;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author oles
 */
public class MonthControl extends AnchorPane implements IMonthControl {

    @FXML
    private Label dateLabel;
    @FXML
    private TreeTableView budgetTable;
    @FXML
    private TableView totalsTable;
    @FXML
    private TableView closingTable;
    @FXML
    public CheckBox closeMonthCheckBox;
    @FXML
    private Button copyToNext;

    private final TreeItem<BudgetItemViewModel> budgetTableRoot = new TreeItem<>(new BudgetItemViewModel("Budget", BigDecimal.ZERO));
    private final TreeItem<BudgetItemViewModel> revenuesRoot = new TreeItem<>(new BudgetItemViewModel("Revenues", BigDecimal.ZERO));
    private final TreeItem<BudgetItemViewModel> expensesRoot = new TreeItem<>(new BudgetItemViewModel("Expenses", BigDecimal.ZERO));
    private final TreeItem<BudgetItemViewModel> adjustmentsRoot = new TreeItem<>(new BudgetItemViewModel("Adjustments", BigDecimal.ZERO));

    private final TreeItem<BudgetItemViewModel> debtRepaymentsTreeItem = new TreeItem<>();
    private final TreeItem<BudgetItemViewModel> investmentOutflowsTreeItem = new TreeItem<>();
    private final TreeItem<BudgetItemViewModel> netIncomeTargetTreeItem = new TreeItem<>();
    private final TreeItem<BudgetItemViewModel> openingBalanceTreeItem = new TreeItem<>();

    private final SimpleObjectProperty<Month> month = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<MonthViewModel> monthViewModel = new SimpleObjectProperty<MonthViewModel>();
    private final MonthMapper monthMapper;

    private MonthLogicServices monthLogicServices;

    public void setMonthLogicServices(MonthLogicServices monthLogicServices) {
        this.monthLogicServices = monthLogicServices;
    }

    // <editor-fold defaultstate="collapsed" desc="Month modified callback">
    // This callback should be called (passing the Month) when and only when the month's items are changed or updated through the Control UI ONLY
    private Callback<MonthControl, Month> monthModifiedCallback;

    public void setOnMonthModified(Callback<MonthControl, Month> callback) {
        this.monthModifiedCallback = callback;
    }

    protected void callMonthModifiedCallback() {
        monthMapper.updateMonthFromMonthViewModel(monthViewModel.getValue(), month.getValue()); // Update the Month instance with the monthViewModel
        if (monthModifiedCallback != null) {
            monthModifiedCallback.call(this);
        }
    }
    // </editor-fold>

    public Button getCopyToNextButton() {
        return this.copyToNext;
    }

    // <editor-fold defaultstate="collapsed" desc="Month Revenues/Expenses/Adjustments change listeners">
    // These update the table tree items whenever monthViewModels's revenues/expenses/adjustments lists change
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
    // </editor-fold>

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

        setupBudgetTable();
        setupTotalsTable();
        setupClosingTable();
        preventMultipleRowSelection();

        // Set the handler for the "Close Month" checkbox
        this.closeMonthCheckBox.selectedProperty().addListener(checkbox -> {
            this.monthViewModel.get().setIsClosed(this.closeMonthCheckBox.isSelected());
            callMonthModifiedCallback();
            updateTableStyles();
        });

        updateTableStyles();

        // Repopulates the tables when the reference to the month instance changes
        this.monthProperty().addListener(property -> {
            this.populateTables();
        });

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
                    callMonthModifiedCallback();
                }
            }
        });
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

    public MonthViewModel getMonthViewModel() {
        return this.monthViewModel.get();
    }

    public void setMonthViewModel(MonthViewModel monthViewModel) {
        this.monthViewModel.set(monthViewModel);
    }

    public SimpleObjectProperty<Month> monthProperty() {
        return this.month;
    }

    // <editor-fold defaultstate="collapsed" desc="Table Column / Cell setup functions">
    protected void setupBudgetTable() {
        TreeTableColumn nameColumn = (TreeTableColumn) budgetTable.getColumns().get(0);
        TreeTableColumn amountColumn = (TreeTableColumn) budgetTable.getColumns().get(1);
        TreeTableColumn actionColumn = (TreeTableColumn) budgetTable.getColumns().get(2);

        nameColumn.prefWidthProperty().bind(budgetTable.widthProperty().multiply(0.6).subtract(28));
        amountColumn.prefWidthProperty().bind(budgetTable.widthProperty().multiply(0.4));
        actionColumn.prefWidthProperty().set(24);
        nameColumn.setResizable(false);
        amountColumn.setResizable(false);
        actionColumn.setResizable(false);

        nameColumn.setCellValueFactory(new Callback<CellDataFeatures<BudgetItemViewModel, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<BudgetItemViewModel, String> p) {
                return p.getValue().getValue().nameProperty();
            }
        });

        nameColumn.setCellFactory(new Callback<TreeTableColumn<BudgetItemViewModel, BigDecimal>, TreeTableCell<BudgetItemViewModel, BigDecimal>>() {
            @Override
            public TreeTableCell<BudgetItemViewModel, BigDecimal> call(TreeTableColumn<BudgetItemViewModel, BigDecimal> param) {
                TextFieldTreeTableCell cell = new TextFieldTreeTableCell(new DefaultStringConverter());
                return cell;
            }
        });

        nameColumn.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<BudgetItemViewModel, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<BudgetItemViewModel, String> t) {
                TreeItem treeItem = t.getTreeTablePosition().getTreeItem();
                if (treeItem.getChildren().size() == 0) {
                    BudgetItemViewModel budgetItem = (BudgetItemViewModel) treeItem.getValue();
                    budgetItem.setName(t.getNewValue());
                    // Update the month model
                    monthMapper.updateMonthFromMonthViewModel(monthViewModel.getValue(), month.getValue());
                    callMonthModifiedCallback();
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

        // Initializes the currency-formated cells for the amount column
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

                    // Don't allow negative numbers for revenues, expenses, debt repayments, investment outflows
                    List<TreeItem> allowNegative = new ArrayList<>();
                    allowNegative.add(openingBalanceTreeItem);
                    allowNegative.add(netIncomeTargetTreeItem);
                    if (!allowNegative.contains(treeItem) && t.getNewValue().compareTo(BigDecimal.ZERO) == -1) {
                        budgetTable.refresh();
                    } else {
                        budgetItem.setAmount(t.getNewValue());
                        callMonthModifiedCallback();
                    }
                }
            }
        });

        actionColumn.setCellFactory(new Callback<TreeTableColumn<BudgetItemViewModel, String>, TreeTableCell<BudgetItemViewModel, String>>() {
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
                    // Update the month model
                    monthMapper.updateMonthFromMonthViewModel(monthViewModel.getValue(), month.getValue());
                    treeItem.setExpanded(true);
                    callMonthModifiedCallback();
                });

                return cell;
            }
        });

        budgetTableRoot.getChildren().addAll(revenuesRoot, expensesRoot, adjustmentsRoot, debtRepaymentsTreeItem, investmentOutflowsTreeItem, netIncomeTargetTreeItem, openingBalanceTreeItem);
        budgetTableRoot.setExpanded(true);
        budgetTable.setEditable(true);
        budgetTable.setRoot(budgetTableRoot);
    }

    protected void setupTotalsTable() {
        TableColumn nameColumn = (TableColumn) totalsTable.getColumns().get(0);
        TableColumn amountColumn = (TableColumn) totalsTable.getColumns().get(1);

        nameColumn.prefWidthProperty().bind(totalsTable.widthProperty().multiply(0.6).subtract(28));
        amountColumn.prefWidthProperty().bind(totalsTable.widthProperty().multiply(0.4));
        nameColumn.setResizable(false);
        amountColumn.setResizable(false);

        amountColumn.setCellFactory(new Callback<TableColumn<BudgetItemViewModel, BigDecimal>, TableCell<BudgetItemViewModel, BigDecimal>>() {
            StringConverter<BigDecimal> converter;

            @Override
            public TableCell<BudgetItemViewModel, BigDecimal> call(TableColumn<BudgetItemViewModel, BigDecimal> param) {
                CurrencyTableCell cell = new CurrencyTableCell("$");
                return cell;
            }
        });

        totalsTable.setSelectionModel(null);
    }

    protected void setupClosingTable() {
        TableColumn nameColumn = (TableColumn) closingTable.getColumns().get(0);
        TableColumn amountColumn = (TableColumn) closingTable.getColumns().get(1);

        nameColumn.prefWidthProperty().bind(closingTable.widthProperty().multiply(0.6).subtract(28));
        amountColumn.prefWidthProperty().bind(closingTable.widthProperty().multiply(0.4));
        nameColumn.setResizable(false);
        amountColumn.setResizable(false);

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
                callMonthModifiedCallback();
            }
        });
    }

    protected void preventMultipleRowSelection() {
        budgetTable.getSelectionModel().selectedItemProperty().addListener(objectProperty -> {
            closingTable.getSelectionModel().clearSelection();
        });

        closingTable.getSelectionModel().selectedItemProperty().addListener(objectProperty -> {
            budgetTable.getSelectionModel().clearSelection();
        });
    }

    public void populateTables() {
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

        revenuesRoot.getChildren().clear();
        revenuesRoot.getValue().setAmount(monthViewModel.getTotalRevenues());
        expensesRoot.getChildren().clear();
        expensesRoot.getValue().setAmount(monthViewModel.getTotalExpenses());
        adjustmentsRoot.getChildren().clear();
        adjustmentsRoot.getValue().setAmount(monthViewModel.getTotalAdjustments());

        for (BudgetItemViewModel revenue : monthViewModel.getRevenues()) {
            revenuesRoot.getChildren().add(new TreeItem<BudgetItemViewModel>(revenue));
        }

        for (BudgetItemViewModel expense : monthViewModel.getExpenses()) {
            expensesRoot.getChildren().add(new TreeItem<BudgetItemViewModel>(expense));
        }

        debtRepaymentsTreeItem.setValue(monthViewModel.getDebtRepayments());
        investmentOutflowsTreeItem.setValue(monthViewModel.getInvestmentOutflows());
        netIncomeTargetTreeItem.setValue(monthViewModel.getNetIncomeTarget());
        openingBalanceTreeItem.setValue(monthViewModel.getOpeningBalance());

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
        closeMonthCheckBox.setDisable(!monthLogicServices.isMonthClosable(month.getValue()));

        // Enable/Disable month closing button
        copyToNext.setDisable(!monthLogicServices.isMonthCloneable(month.getValue()));
    }

    @Override
    public Month getMonth() {
        return this.month.get();
    }

    @Override
    public void setMonth(Month month) {
        this.month.set(month);
    }

    // <editor-fold defaultstate="collapsed" desc="Callbacks">
    private Callback<BudgetItemViewModel, Month> onItemAddedCallback;

    @Override
    public void onItemAdded(Callback<BudgetItemViewModel, Month> callback) {
        this.onItemAddedCallback = callback;
    }

    private void callOnItemAdded(BudgetItemViewModel item) {
        if (onItemAddedCallback != null) {
            onItemAddedCallback.call(item);
        }
    }

    private Callback<BudgetItemViewModel, Month> onItemRemovedCallback;

    @Override
    public void onItemRemoved(Callback<BudgetItemViewModel, Month> callback) {
        this.onItemRemovedCallback = callback;
    }

    private void callOnItemRemoved(BudgetItemViewModel item) {
        if (onItemRemovedCallback != null) {
            onItemRemovedCallback.call(item);
        }
    }

    private Callback<BudgetItemViewModel, Month> onItemModifiedCallback;

    @Override
    public void onItemModified(Callback<BudgetItemViewModel, Month> callback) {
        this.onItemModifiedCallback = callback;
    }

    private void callOnItemModified(BudgetItemViewModel item) {
        if (onItemModifiedCallback != null) {
            onItemModifiedCallback.call(item);
        }
    }

    private Callback<MonthViewModel, Month> onMonthCloseModifiedCallback;

    @Override
    public void onMonthCloseModified(Callback<MonthViewModel, Month> callback) {
        this.onMonthCloseModifiedCallback = callback;
    }

    private void callOnMonthCloseModified(MonthViewModel monthViewModel) {
        if (onMonthCloseModifiedCallback != null) {
            onMonthCloseModifiedCallback.call(monthViewModel);
        }
    }

    private Callback<MonthViewModel, Month> onMonthCopyCallback;

    @Override
    public void onMonthCopy(Callback<MonthViewModel, Month> callback) {
        this.onMonthCopyCallback = callback;
    }

    private void callOnMonthCopyCallback(MonthViewModel monthViewModel) {
        if (onMonthCopyCallback != null) {
            onMonthCopyCallback.call(monthViewModel);
        }
    }
    // </editor-fold>
}
