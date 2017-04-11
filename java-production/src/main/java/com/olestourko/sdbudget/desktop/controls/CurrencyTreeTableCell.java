package com.olestourko.sdbudget.desktop.controls;

import com.olestourko.sdbudget.desktop.models.BudgetItemViewModel;
import java.math.BigDecimal;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.util.Callback;
import javafx.util.converter.BigDecimalStringConverter;

public class CurrencyTreeTableCell extends TextFieldTreeTableCell<BudgetItemViewModel, BigDecimal> {

    public final Label label = new Label();
    protected Callback<CurrencyTreeTableCell, Boolean> callback;

    public CurrencyTreeTableCell() {
        super(new BigDecimalStringConverter());
    }

    public CurrencyTreeTableCell(String currencySymbol) {
        super(new BigDecimalStringConverter());
        label.setText(currencySymbol);
    }

    @Override
    public void updateItem(BigDecimal item, boolean empty) {
        super.updateItem(item, empty);
        this.setText(new BigDecimalStringConverter().toString(item));

        if (item != null) {
            setGraphic(label);
        } else {
            setGraphic(null);
        }

    }
}
