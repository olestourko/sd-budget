package com.olestourko.sdbudget.desktop.controls;

import com.olestourko.sdbudget.core.commands.AddBudgetItem;
import com.olestourko.sdbudget.core.commands.CommandInvoker;
import com.olestourko.sdbudget.core.commands.RemoveBudgetItem;
import com.olestourko.sdbudget.core.commands.SetMonthClosed;
import com.olestourko.sdbudget.core.commands.UpdateBudgetItem;
import com.olestourko.sdbudget.desktop.controls.handlers.TreeTableViewEnterPressedHandler;
import com.olestourko.sdbudget.desktop.controls.handlers.TableViewEnterPressedHandler;
import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.core.services.MonthLogicServices;
import com.olestourko.sdbudget.desktop.controls.ButtonTreeTableCell;
import com.olestourko.sdbudget.desktop.controls.CurrencyTableCell;
import com.olestourko.sdbudget.desktop.controls.CurrencyTreeTableCell;
import com.olestourko.sdbudget.desktop.controls.IMonthControl;
import com.olestourko.sdbudget.desktop.mappers.MonthMapper;
import com.olestourko.sdbudget.desktop.models.BudgetItemViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import com.olestourko.sdbudget.desktop.models.MonthViewModel;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollBar;
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
    private final TreeItem<BudgetItemViewModel> openingSurplusTreeItem = new TreeItem<>();

    private final SimpleObjectProperty<Month> month = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<MonthViewModel> monthViewModel = new SimpleObjectProperty<MonthViewModel>();
    private final MonthMapper monthMapper;

    private final SimpleObjectProperty<String> currency = new SimpleObjectProperty<>("$");

    private MonthLogicServices monthLogicServices;
    private CommandInvoker commandInvoker;

    public void setMonthLogicServices(MonthLogicServices monthLogicServices) {
        this.monthLogicServices = monthLogicServices;
    }

    public void setCommandInvoker(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
    }

    public Button getCopyToNextButton() {
        return this.copyToNext;
    }

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
        this.closeMonthCheckBox.setOnAction(property -> {;
            commandInvoker.invoke(new SetMonthClosed(month.getValue(), closeMonthCheckBox.selectedProperty().getValue()));
        });

        // Repopulates the tables when the reference to the month instance changes
        this.monthProperty().addListener(property -> {
            this.refresh();
        });

        budgetTable.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                // Remove item when DELETE key is pressed
                if (event.getCode().equals(KeyCode.DELETE)) {
                    TreeItem<BudgetItemViewModel> treeItem = (TreeItem) budgetTable.getSelectionModel().getSelectedItem();
                    TreeItem<BudgetItemViewModel> parentTreeItem = treeItem.getParent();
                    BudgetItem selectedItem = ((BudgetItemViewModel) treeItem.getValue()).getModel();
                    if (month.getValue().getRevenues().contains(selectedItem)) {
                        commandInvoker.invoke(new RemoveBudgetItem(month.getValue(), selectedItem, RemoveBudgetItem.Type.REVENUE));
                        budgetTable.getSelectionModel().select(parentTreeItem);
                    } else if (month.getValue().getExpenses().contains(selectedItem)) {
                        commandInvoker.invoke(new RemoveBudgetItem(month.getValue(), selectedItem, RemoveBudgetItem.Type.EXPENSE));
                        budgetTable.getSelectionModel().select(parentTreeItem);
                    }
                }
            }
        });
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

        nameColumn.prefWidthProperty().bind(budgetTable.widthProperty().multiply(0.7).subtract(28));
        amountColumn.prefWidthProperty().bind(budgetTable.widthProperty().multiply(0.3));
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
                TreeItem<BudgetItemViewModel> treeItem = t.getTreeTablePosition().getTreeItem();
                TreeItem<BudgetItemViewModel> parentTreeItem = treeItem.getParent();
                BudgetItemViewModel budgetItemVM = (BudgetItemViewModel) treeItem.getValue();
                BudgetItem budgetItem = budgetItemVM.getModel();
                if (treeItem.getChildren().size() == 0) {
                    commandInvoker.invoke(new UpdateBudgetItem(budgetItem, new BudgetItem(t.getNewValue(), budgetItem.getAmount())));
                    // Select the edited item
                    // https://www.mkyong.com/java8/java-8-streams-filter-examples/
                    if (parentTreeItem != null) {
                        treeItem.setExpanded(true);
                        TreeItem<BudgetItemViewModel> newTreeItem = parentTreeItem.getChildren().stream().filter(item -> {
                            return ((TreeItem<BudgetItemViewModel>) item).getValue().getModel() == budgetItemVM.getModel();
                        }).findFirst().orElse(null);
                        int row = budgetTable.getRow(newTreeItem);
                        budgetTable.getSelectionModel().select(row);
                    }
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
                CurrencyTreeTableCell cell = new CurrencyTreeTableCell(currency.getValue());
                cell.label.textProperty().bind(currency);
                return cell;
            }
        });

        amountColumn.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<BudgetItemViewModel, BigDecimal>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<BudgetItemViewModel, BigDecimal> t) {
                TreeItem treeItem = t.getTreeTablePosition().getTreeItem();
                TreeItem<BudgetItemViewModel> parentTreeItem = treeItem.getParent();

                if (treeItem.getChildren().size() == 0) {
                    BudgetItemViewModel budgetItemVM = (BudgetItemViewModel) treeItem.getValue();
                    BudgetItem budgetItem = budgetItemVM.getModel();

                    // Don't allow negative numbers for revenues, expenses, debt repayments, investment outflows
                    List<TreeItem> allowNegative = new ArrayList<>();
                    allowNegative.add(openingBalanceTreeItem);
                    allowNegative.add(netIncomeTargetTreeItem);
                    allowNegative.add(investmentOutflowsTreeItem);
                    if (!allowNegative.contains(treeItem) && t.getNewValue().compareTo(BigDecimal.ZERO) == -1) {
                        budgetTable.refresh();
                    } else {
                        commandInvoker.invoke(new UpdateBudgetItem(budgetItem, new BudgetItem(budgetItem.getName(), t.getNewValue())));
                    }

                    // Select the edited item
                    // https://www.mkyong.com/java8/java-8-streams-filter-examples/
                    if (parentTreeItem != null) {
                        treeItem.setExpanded(true);
                        TreeItem<BudgetItemViewModel> newTreeItem = parentTreeItem.getChildren().stream().filter(item -> {
                            return ((TreeItem<BudgetItemViewModel>) item).getValue().getModel() == budgetItemVM.getModel();
                        }).findFirst().orElse(null);
                        int row = budgetTable.getRow(newTreeItem);
                        budgetTable.getSelectionModel().select(row);
                    }
                }
            }
        });

        actionColumn.setCellFactory(new Callback<TreeTableColumn<BudgetItemViewModel, String>, TreeTableCell<BudgetItemViewModel, String>>() {
            @Override
            public TreeTableCell<BudgetItemViewModel, String> call(TreeTableColumn<BudgetItemViewModel, String> p) {
                ButtonTreeTableCell cell = new ButtonTreeTableCell("");

                final EventHandler<ActionEvent> addHandler = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        BudgetItem budgetItem = new BudgetItem("New Item", BigDecimal.ZERO);
                        TreeItem<BudgetItemViewModel> treeItem = cell.getTreeTableRow().getTreeItem();
                        if (treeItem.getValue() == revenuesRoot.getValue()) {
                            commandInvoker.invoke(new AddBudgetItem(month.getValue(), budgetItem, AddBudgetItem.Type.REVENUE));
                        } else if (treeItem.getValue() == expensesRoot.getValue()) {
                            commandInvoker.invoke(new AddBudgetItem(month.getValue(), budgetItem, AddBudgetItem.Type.EXPENSE));
                        }

                        // Select the newly created item
                        // https://www.mkyong.com/java8/java-8-streams-filter-examples/
                        treeItem.setExpanded(true);
                        TreeItem<BudgetItemViewModel> newTreeItem = treeItem.getChildren().stream().filter(item -> {
                            return ((TreeItem<BudgetItemViewModel>) item).getValue().getModel() == budgetItem;
                        }).findFirst().orElse(null);
                        int row = budgetTable.getRow(newTreeItem);
                        budgetTable.getSelectionModel().select(row);
                    }
                };

                final EventHandler<ActionEvent> deleteHandler = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        TreeItem<BudgetItemViewModel> treeItem = cell.getTreeTableRow().getTreeItem();
                        TreeItem<BudgetItemViewModel> parentTreeItem = treeItem.getParent();
                        BudgetItem selectedItem = ((BudgetItemViewModel) treeItem.getValue()).getModel();
                        if (month.getValue().getRevenues().contains(selectedItem)) {
                            commandInvoker.invoke(new RemoveBudgetItem(month.getValue(), selectedItem, RemoveBudgetItem.Type.REVENUE));
                            budgetTable.getSelectionModel().select(parentTreeItem);
                        } else if (month.getValue().getExpenses().contains(selectedItem)) {
                            commandInvoker.invoke(new RemoveBudgetItem(month.getValue(), selectedItem, RemoveBudgetItem.Type.EXPENSE));
                            budgetTable.getSelectionModel().select(parentTreeItem);
                        }
                    }
                };

                cell.setShowButtonCondition(new Callback<ButtonTreeTableCell, Boolean>() {
                    @Override
                    public Boolean call(ButtonTreeTableCell cell) {
                        TreeItem treeItem = cell.getTreeTableRow().getTreeItem();
                        if ((treeItem == revenuesRoot || treeItem == expensesRoot) && !monthViewModel.getValue().getIsClosed()) {
                            cell.button.setText("+");
                            cell.button.setOnAction(addHandler);
                            return true;
                        } else if (treeItem != null && (treeItem.getParent() == revenuesRoot || treeItem.getParent() == expensesRoot) && !monthViewModel.getValue().getIsClosed()) {
                            cell.button.setText("\u00D7");
                            cell.button.setOnAction(deleteHandler);
                            return true;
                        }

                        return false;
                    }
                });

                return cell;
            }
        });

        // Edit item amount when pressing ENTER
        budgetTable.addEventHandler(KeyEvent.KEY_PRESSED, new TreeTableViewEnterPressedHandler(budgetTable));

        budgetTableRoot.getChildren().addAll(revenuesRoot, expensesRoot, adjustmentsRoot, debtRepaymentsTreeItem, investmentOutflowsTreeItem, netIncomeTargetTreeItem, openingBalanceTreeItem, openingSurplusTreeItem);
        budgetTableRoot.setExpanded(true);
        budgetTable.setEditable(true);
        budgetTable.setRoot(budgetTableRoot);

        // Disable Drag & Drop on headers
        // http://stackoverflow.com/questions/22202782/how-to-prevent-tableview-from-doing-tablecolumn-re-order-in-javafx-8
        budgetTable.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) {
                TableHeaderRow header = (TableHeaderRow) budgetTable.lookup("TableHeaderRow");
                header.reorderingProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        header.setReordering(false);
                    }
                });
            }
        });

        month.addListener(event -> {
            budgetTable.getSelectionModel().clearSelection();
        });
    }

    protected void setupTotalsTable() {
        TableColumn nameColumn = (TableColumn) totalsTable.getColumns().get(0);
        TableColumn amountColumn = (TableColumn) totalsTable.getColumns().get(1);

        nameColumn.prefWidthProperty().bind(totalsTable.widthProperty().multiply(0.7).subtract(28));
        amountColumn.prefWidthProperty().bind(totalsTable.widthProperty().multiply(0.3).add(24));
        nameColumn.setResizable(false);
        amountColumn.setResizable(false);

        amountColumn.setCellFactory(new Callback<TableColumn<BudgetItemViewModel, BigDecimal>, TableCell<BudgetItemViewModel, BigDecimal>>() {
            StringConverter<BigDecimal> converter;

            @Override
            public TableCell<BudgetItemViewModel, BigDecimal> call(TableColumn<BudgetItemViewModel, BigDecimal> param) {
                CurrencyTableCell cell = new CurrencyTableCell(currency.getValue());
                cell.label.textProperty().bind(currency);
                return cell;
            }
        });

        totalsTable.setSelectionModel(null);
    }

    protected void setupClosingTable() {
        TableColumn nameColumn = (TableColumn) closingTable.getColumns().get(0);
        TableColumn amountColumn = (TableColumn) closingTable.getColumns().get(1);

        nameColumn.prefWidthProperty().bind(closingTable.widthProperty().multiply(0.7).subtract(28));
        amountColumn.prefWidthProperty().bind(closingTable.widthProperty().multiply(0.3).add(24));
        nameColumn.setResizable(false);
        amountColumn.setResizable(false);

        amountColumn.setCellFactory(new Callback<TableColumn<BudgetItemViewModel, BigDecimal>, TableCell<BudgetItemViewModel, BigDecimal>>() {
            StringConverter<BigDecimal> converter;

            @Override
            public TableCell<BudgetItemViewModel, BigDecimal> call(TableColumn<BudgetItemViewModel, BigDecimal> param) {
                CurrencyTableCell cell = new CurrencyTableCell(currency.getValue());
                cell.label.textProperty().bind(currency);
                return cell;
            }
        });

        amountColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<BudgetItemViewModel, BigDecimal>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<BudgetItemViewModel, BigDecimal> t) {
                BudgetItem budgetItem = ((BudgetItemViewModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).getModel();
                commandInvoker.invoke(new UpdateBudgetItem(budgetItem, new BudgetItem(budgetItem.getName(), t.getNewValue())));
            }
        });

        // Edit item amount when pressing ENTER
        closingTable.addEventHandler(KeyEvent.KEY_PRESSED, new TableViewEnterPressedHandler(closingTable));

        month.addListener(event -> {
            closingTable.getSelectionModel().clearSelection();
        });
    }

    protected void preventMultipleRowSelection() {
        budgetTable.setOnMouseClicked(event -> {
            closingTable.getSelectionModel().clearSelection();
        });

        closingTable.setOnMouseClicked(event -> {
            budgetTable.getSelectionModel().clearSelection();
        });
    }

    public void refresh() {
        MonthViewModel monthViewModel = monthMapper.mapMonthToMonthViewModel(getMonth());

        this.monthViewModel.set(monthViewModel);
        revenuesRoot.getChildren().clear();
        revenuesRoot.getValue().setAmount(monthViewModel.getTotalRevenues());
        expensesRoot.getChildren().clear();
        expensesRoot.getValue().setAmount(monthViewModel.getTotalExpenses());
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
        openingSurplusTreeItem.setValue(monthViewModel.getOpeningSurplus());

        totalsTable.getItems().clear();
        totalsTable.getItems().addAll(
                monthViewModel.getClosingBalanceTarget(),
                monthViewModel.getEstimatedClosingBalance(),
                monthViewModel.getClosingSurplus()
        );
        closingTable.getItems().clear();
        closingTable.getItems().addAll(monthViewModel.getClosingBalance());

        // Set the date on the label
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
        dateLabel.setText(dateFormat.format(monthViewModel.calendar.getTime()));

        // Set the closing checkbox value
        closeMonthCheckBox.setSelected(monthViewModel.getIsClosed());
        if (!month.getValue().getIsClosed()) {
            closeMonthCheckBox.setDisable(!monthLogicServices.isMonthClosable(month.getValue()));
        } else {
            closeMonthCheckBox.setDisable(!monthLogicServices.isMonthOpenable(month.getValue()));
        }

        // Enable/Disable month closing button
        copyToNext.setDisable(!monthLogicServices.isMonthCloneable(month.getValue()));
        
        // Enable / disable tables based on the "Month Closed" checkbox
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

    @Override
    public Month getMonth() {
        return this.month.get();
    }

    @Override
    public void setMonth(Month month) {
        this.month.set(month);
    }

    public String getCurruency() {
        return this.currency.get();
    }

    public void setCurrency(String currency) {
        this.currency.setValue(currency);
    }

    // <editor-fold defaultstate="collapsed" desc="Callbacks">
    private Callback<Month, Month> onMonthCopyCallback;

    @Override
    public void onMonthCopy(Callback<Month, Month> callback) {
        this.onMonthCopyCallback = callback;
    }

    private void callOnMonthCopyCallback(Month month) {
        if (onMonthCopyCallback != null) {
            onMonthCopyCallback.call(month);
        }
    }
    // </editor-fold>
}
