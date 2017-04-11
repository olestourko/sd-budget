package com.olestourko.sdbudget.desktop.controls;

import com.olestourko.sdbudget.desktop.models.BudgetItemViewModel;
import java.math.BigDecimal;
import javafx.scene.control.Label;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.BigDecimalStringConverter;

public class CurrencyTableCell extends TextFieldTableCell<BudgetItemViewModel, BigDecimal> {

    public final Label label = new Label();
    protected Callback<CurrencyTableCell, Boolean> callback;

    public CurrencyTableCell() {
        super(new BigDecimalStringConverter());
    }

    public CurrencyTableCell(String currencySymbol) {
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
