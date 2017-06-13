package com.olestourko.sdbudget.desktop.controllers;

import com.olestourko.sdbudget.core.commands.AddBudgetItem;
import com.olestourko.sdbudget.core.commands.CommandInvoker;
import com.olestourko.sdbudget.core.commands.ICommand;
import com.olestourko.sdbudget.core.commands.ICommandCallback;
import com.olestourko.sdbudget.core.commands.RemoveBudgetItem;
import com.olestourko.sdbudget.core.commands.SetMonthClosed;
import com.olestourko.sdbudget.core.commands.UpdateBudgetItem;
import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.desktop.controllers.IScratchpad;
import com.olestourko.sdbudget.desktop.controls.ButtonTableCell;
import com.olestourko.sdbudget.desktop.controls.CurrencyTableCell;
import com.olestourko.sdbudget.desktop.controls.handlers.TableViewEnterPressedHandler;
import com.olestourko.sdbudget.desktop.models.BudgetItemViewModel;
import com.olestourko.sdbudget.desktop.models.MonthViewModel;
import com.olestourko.sdbudget.desktop.mappers.MonthMapper;
import com.olestourko.sdbudget.desktop.models.Budget;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
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
import javafx.scene.control.Button;
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

public class ScratchpadController implements Initializable, IScratchpad {

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
    private TableColumn actionColumn;
    @FXML
    private TextField nameField;
    @FXML
    private TextField amountField;
    @FXML
    private Button addTransactionButton;

    private final BudgetItemViewModel totalAdjustments = new BudgetItemViewModel("Total Adjustments", BigDecimal.ZERO);
    private final MonthMapper monthMapper;
    private final CommandInvoker commandInvoker;
    private final Budget budget;
    private final SimpleObjectProperty<Month> month = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<MonthViewModel> monthViewModel = new SimpleObjectProperty<>();
    private final String currency;

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
    public ScratchpadController(Budget budget, String currency, CommandInvoker commandInvoker) {
        this.monthMapper = Mappers.getMapper(MonthMapper.class);
        this.budget = budget;
        this.currency = currency;
        this.commandInvoker = commandInvoker;

        // Register command listeners
        ICommandCallback commandHandler = new ICommandCallback() {
            @Override
            public void handle(ICommand command) {
                refresh();
            }
        };
        commandInvoker.addListener(UpdateBudgetItem.class, commandHandler);
        commandInvoker.addListener(AddBudgetItem.class, commandHandler);
        commandInvoker.addListener(RemoveBudgetItem.class, commandHandler);
        commandInvoker.addListener(SetMonthClosed.class, commandHandler);
    }

    public void load() {
        nameColumn.prefWidthProperty().bind(scratchpadTable.widthProperty().multiply(0.7).subtract(28));
        amountColumn.prefWidthProperty().bind(scratchpadTable.widthProperty().multiply(0.3));
        actionColumn.prefWidthProperty().set(24);

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
            public void handle(TableColumn.CellEditEvent<BudgetItemViewModel, String> event) {
                BudgetItem item = ((BudgetItemViewModel) event.getTableView().getItems().get(event.getTablePosition().getRow())).getModel();
                commandInvoker.invoke(new UpdateBudgetItem(item, new BudgetItem(event.getNewValue(), item.getAmount())));
                scratchpadTable.getSelectionModel().select(event.getTablePosition().getRow());
            }
        });

        amountColumn.setCellFactory(new Callback<TableColumn<BudgetItemViewModel, BigDecimal>, TableCell<BudgetItemViewModel, BigDecimal>>() {
            StringConverter<BigDecimal> converter;

            @Override
            public TableCell<BudgetItemViewModel, BigDecimal> call(TableColumn<BudgetItemViewModel, BigDecimal> param) {
                CurrencyTableCell cell = new CurrencyTableCell(currency);
                return cell;
            }
        });
        amountColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<BudgetItemViewModel, BigDecimal>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<BudgetItemViewModel, BigDecimal> event) {
                BudgetItem item = ((BudgetItemViewModel) event.getTableView().getItems().get(event.getTablePosition().getRow())).getModel();
                commandInvoker.invoke(new UpdateBudgetItem(item, new BudgetItem(item.getName(), event.getNewValue())));
                scratchpadTable.getSelectionModel().select(event.getTablePosition().getRow());
            }
        });

        actionColumn.setCellFactory(new Callback<TableColumn<BudgetItemViewModel, String>, TableCell<BudgetItemViewModel, String>>() {
            @Override
            public TableCell<BudgetItemViewModel, String> call(TableColumn<BudgetItemViewModel, String> p) {
                ButtonTableCell cell = new ButtonTableCell("\u00D7");

                final EventHandler<ActionEvent> deleteHandler = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        deleteItem(((BudgetItemViewModel) cell.getTableRow().getItem()).getModel());
                    }
                };

                cell.button.setOnAction(deleteHandler);
                cell.setShowButtonCondition(new Callback<ButtonTableCell, Boolean>() {
                    @Override
                    public Boolean call(ButtonTableCell cell) {
                        return cell.getTableRow().getItem() != null && !monthViewModel.getValue().getIsClosed();
                    }
                });

                return cell;
            }
        });

        //Set up the totals table
        totalsTable.getItems().add(totalAdjustments);
        ((TableColumn) totalsTable.getColumns().get(0)).prefWidthProperty().bind(totalsTable.widthProperty().multiply(0.7).subtract(28));
        ((TableColumn) totalsTable.getColumns().get(1)).prefWidthProperty().bind(totalsTable.widthProperty().multiply(0.3).add(24));

        ((TableColumn) totalsTable.getColumns().get(1)).setCellFactory(new Callback<TableColumn<BudgetItemViewModel, BigDecimal>, TableCell<BudgetItemViewModel, BigDecimal>>() {
            StringConverter<BigDecimal> converter;

            @Override
            public TableCell<BudgetItemViewModel, BigDecimal> call(TableColumn<BudgetItemViewModel, BigDecimal> param) {
                CurrencyTableCell cell = new CurrencyTableCell(currency);
                return cell;
            }
        });

        // Disable selection on the totals table
        totalsTable.setSelectionModel(null);

        //Set the date on the label
        if (this.month != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
            periodDate.setText(dateFormat.format(this.monthViewModel.get().calendar.getTime()));
        }

        // Remove adjusments when DELETE key is pressed
        scratchpadTable.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.DELETE)) {
                    deleteItem(((BudgetItemViewModel) scratchpadTable.getSelectionModel().getSelectedItem()).getModel());
                }
            }
        });

        // Edit item amount when pressing ENTER
        scratchpadTable.addEventHandler(KeyEvent.KEY_PRESSED, new TableViewEnterPressedHandler(scratchpadTable));

        // Disable Drag & Drop on headers
        // http://stackoverflow.com/questions/22202782/how-to-prevent-tableview-from-doing-tablecolumn-re-order-in-javafx-8
        scratchpadTable.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) {
                TableHeaderRow header = (TableHeaderRow) scratchpadTable.lookup("TableHeaderRow");
                header.reorderingProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        header.setReordering(false);
                    }
                });
            }
        });
    }

    private void deleteItem(BudgetItem item) {
        if (month.getValue().getIsClosed()) {
            return;
        }

        int deletedRow = scratchpadTable.getItems().indexOf(item);
        commandInvoker.invoke(new RemoveBudgetItem(month.getValue(), item, RemoveBudgetItem.Type.ADJUSTMENT));

        if (scratchpadTable.getItems().size() >= deletedRow + 1) {
            scratchpadTable.getSelectionModel().select(deletedRow);
        } else {
            scratchpadTable.getSelectionModel().select(deletedRow - 1);
        }
    }

    public void handleAddTransactionButtonAction(ActionEvent event) {
        String name = nameField.getText();
        BigDecimal amount = new BigDecimal(amountField.getText());
        BudgetItem item = new BudgetItem(name, amount);
        commandInvoker.invoke(new AddBudgetItem(month.getValue(), item, AddBudgetItem.Type.ADJUSTMENT));
        nameField.setText("");
        amountField.setText("");
    }

    public Month getMonth() {
        return month.getValue();
    }

    public void setMonth(Month month) {
        MonthViewModel monthViewModel = monthMapper.mapMonthToMonthViewModel(month);
        this.month.set(month);
        this.monthViewModel.set(monthViewModel);

        // Set the date on the label
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
        periodDate.setText(dateFormat.format(this.monthViewModel.get().calendar.getTime()));

        scratchpadTable.getItems().removeListener(listChangeListener);
        scratchpadTable.setItems(monthViewModel.getAdjustments());
        scratchpadTable.getItems().addListener(listChangeListener);
        totalAdjustments.setAmount(month.getTotalAdjustments());
    }

    public void refresh() {
        monthMapper.updateMonthViewModelFromMonth(month.getValue(), monthViewModel.getValue());
        scratchpadTable.refresh();
        totalsTable.refresh();

        if (month.getValue().getIsClosed()) {
            scratchpadTable.setEditable(false);
            addTransactionButton.setDisable(true);
            nameField.setDisable(true);
            amountField.setDisable(true);
        } else {
            scratchpadTable.setEditable(true);
            addTransactionButton.setDisable(false);
            nameField.setDisable(false);
            amountField.setDisable(false);
        }
    }
}
