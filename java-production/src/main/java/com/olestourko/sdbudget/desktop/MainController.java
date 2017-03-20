package com.olestourko.sdbudget.desktop;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *
 * @author oles
 */
public class MainController implements Initializable {

    @FXML
    public MenuBar mainMenu;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainMenu.getMenus().get(0).setOnAction(event -> {
            throw new NotImplementedException();
        });
    }

}
