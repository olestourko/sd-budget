package com.olestourko.sdbudget.desktop.controls;

import com.olestourko.sdbudget.desktop.models.BudgetItemViewModel;
import javafx.scene.control.Button;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;

public class ButtonTreeTableCell extends TextFieldTreeTableCell<BudgetItemViewModel, String> {

    public final Button button = new Button();
    protected Callback<ButtonTreeTableCell, Boolean> callback;

    public ButtonTreeTableCell() {
        super(new DefaultStringConverter());
    }

    public ButtonTreeTableCell(String buttonText) {
        super(new DefaultStringConverter());
        button.setText(buttonText);
    }

    public void setShowButtonCondition(Callback<ButtonTreeTableCell, Boolean> callback) {
        this.callback = callback;
    }
    
    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        this.setText(item);
        setGraphic(null);

        if (callback != null && callback.call(this)) {
            setGraphic(button);
        } else {
            setGraphic(null);
        }
    }
}
