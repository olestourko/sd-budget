package com.olestourko.sdbudget.desktop.controls.handlers;

import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author oles
 */
public class TreeTableViewEnterPressedHandler implements EventHandler<KeyEvent> {

    private TreeTableView targetTable;

    public TreeTableViewEnterPressedHandler(TreeTableView targetTable) {
        this.targetTable = targetTable;
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            if (event.getTarget() == targetTable) {
                int row = targetTable.getRow((TreeItem) targetTable.getSelectionModel().getSelectedItem());
                TreeTableColumn treeTableColumn = (TreeTableColumn) targetTable.getColumns().get(1);
                if (targetTable.getEditingCell() == null) {
                    targetTable.edit(row, treeTableColumn);
                    event.consume();
                }
            }
        }
    }

}
