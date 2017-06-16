package com.olestourko.sdbudget.desktop.controls.handlers;

import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author oles
 */
public class TableViewEnterPressedHandler implements EventHandler<KeyEvent> {

    private TableView targetTable;

    public TableViewEnterPressedHandler(TableView targetTable) {
        this.targetTable = targetTable;
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            if (event.getTarget() == targetTable) {
                                
                int row = targetTable.getSelectionModel().getSelectedIndex();
                TableColumn treeTableColumn = (TableColumn) targetTable.getColumns().get(1);
                
                if (targetTable.getEditingCell() == null) {
                    targetTable.edit(row, treeTableColumn);
                    event.consume();
                }
            }
        }
    }

}
