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
import javafx.event.EventHandler;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.BigDecimalStringConverter;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
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
    private TreeItem<BudgetItem> budgetTableRoot = new TreeItem<>(new BudgetItem("Budget", BigDecimal.ZERO));
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
    
    public MonthControl() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/desktop/fxml/MonthControl.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
        nameColumn.setCellValueFactory(new Callback<CellDataFeatures<BudgetItem, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<BudgetItem, String> p) {
                // p.getValue() returns the TreeItem<Person> instance for a particular TreeTableView row,
                // p.getValue().getValue() returns the Person instance inside the TreeItem<Person>
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
//        amountColumn.setCellFactory(TextFieldTableCell.<BudgetItem, BigDecimal>forTableColumn(new BigDecimalStringConverter()));        
        // This is a callback for edits
        amountColumn.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<BudgetItem, BigDecimal>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<BudgetItem, BigDecimal> t) {
                BudgetItem budgetItem = t.getTreeTablePosition().getTreeItem().getValue();
//                BudgetItem budgetItem = (BudgetItem) t.getTableView().getItems().get(t.getTablePosition().getRow());
                budgetItem.setAmount(t.getNewValue());
                MonthControl.this.fireEvent(new ActionEvent());
            }
        });

        budgetTableRoot.setExpanded(true);
        budgetTable.setRoot(budgetTableRoot);

        // Update the tables when the month is changed
        this.monthProperty().addListener(event -> {
            Month month = this.getMonth();
            budgetTableRoot.getChildren().clear();
            budgetTableRoot.getChildren().add(new TreeItem<>(month.revenues));
            budgetTableRoot.getChildren().add(new TreeItem<>(month.expenses));
            budgetTableRoot.getChildren().add(new TreeItem<>(month.adjustments));
            budgetTableRoot.getChildren().add(new TreeItem<>(month.netIncomeTarget));
            budgetTableRoot.getChildren().add(new TreeItem<>(month.openingBalance));

//            budgetTable.setItems(FXCollections.observableArrayList(
//                    month.revenues,
//                    month.expenses,
//                    month.adjustments,
//                    month.netIncomeTarget,
//                    month.openingBalance
//            ));
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
