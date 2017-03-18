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
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.BigDecimalStringConverter;
import javafx.event.ActionEvent;

/**
 *
 * @author oles
 */
public class MonthControl extends AnchorPane {

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

    private final SimpleObjectProperty<Month> month = new SimpleObjectProperty<Month>();

    public MonthControl() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/desktop/fxml/BudgetMonthComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
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
                MonthControl.this.fireEvent(new ActionEvent());
            }
        });

        // Update the tables when the month is changed
        this.monthProperty().addListener(event -> {
            Month month = this.getMonth();
            budgetTable.setItems(FXCollections.observableArrayList(
                    month.revenues,
                    month.expenses,
                    month.adjustments,
                    month.netIncomeTarget,
                    month.openingBalance
            ));
            totalsTable.getItems().clear();
            totalsTable.getItems().addAll(month.closingBalanceTarget, month.estimatedClosingBalance, month.surplus);
            closingTable.getItems().clear();
            closingTable.getItems().addAll(month.closingBalance);

            //Set the date on the label
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
            periodDate.setText(dateFormat.format(month.calendar.getTime()));
            MonthControl.this.fireEvent(new ActionEvent());
        });

        //Set up the closing table
        TableColumn closingTableAmountColumn = (TableColumn) closingTable.getColumns().get(1);
        closingTableAmountColumn.setCellFactory(TextFieldTableCell.<BudgetItem, BigDecimal>forTableColumn(new BigDecimalStringConverter()));
        closingTableAmountColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<BudgetItem, BigDecimal>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<BudgetItem, BigDecimal> t) {
                BudgetItem budgetItem = (BudgetItem) t.getTableView().getItems().get(t.getTablePosition().getRow());
                budgetItem.setAmount(t.getNewValue());
            }
        });
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
