package com.olestourko.sdbudget.desktop.controls;

import com.olestourko.sdbudget.desktop.models.BudgetItemViewModel;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import javafx.scene.control.Label;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.util.Callback;
import javafx.util.converter.BigDecimalStringConverter;

public class CurrencyTreeTableCell extends TextFieldTreeTableCell<BudgetItemViewModel, BigDecimal> {

    public final Label label = new Label();
    protected Callback<CurrencyTreeTableCell, Boolean> callback;
    protected DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

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
        if (item != null) {
            this.setText(decimalFormat.format(item));
        }

        if (item != null) {
            setGraphic(label);
        } else {
            setGraphic(null);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        this.setText(decimalFormat.format(this.getItem()));

        if (this.getItem() != null) {
            setGraphic(label);
        } else {
            setGraphic(null);
        }
    }
}
