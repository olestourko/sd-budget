package com.olestourko.sdbudget.desktop.controls;

import com.olestourko.sdbudget.desktop.models.BudgetItem;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TreeTableCell;
import javafx.util.Callback;

public class ButtonTreeTableCell extends TreeTableCell<BudgetItem, String> {

    public final Button button = new Button();
    protected Callback<ButtonTreeTableCell, Boolean> callback;

    public ButtonTreeTableCell() {
        super();
    }

    public ButtonTreeTableCell(String buttonText) {
        super();
        button.setText(buttonText);
        button.setPadding(new Insets(0, 2, 0, 2));
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
