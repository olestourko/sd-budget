package com.olestourko.sdbudget;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
//For data
import com.olestourko.sdbudget.models.BudgetItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.converter.DoubleStringConverter;

public class Sdbudget extends Application {

    final TableView table = new TableView();
    final ObservableList<BudgetItem> data = FXCollections.observableArrayList(
            new BudgetItem("Revenues", 2000),
            new BudgetItem("Expenses", 1000)
    );

    @Override
    public void start(Stage stage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));

        Scene scene = new Scene(new Group());
        scene.getStylesheets().add("/styles/Styles.css");
        stage.setTitle("S/D Budget");
        stage.setWidth(270);
        stage.setHeight(450);

        table.setEditable(true);

        TableColumn name = new TableColumn("name");
        name.setCellValueFactory(
                new PropertyValueFactory<BudgetItem, String>("name")
        );
        //This draws the textfield when editing a table cell
        name.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn amount = new TableColumn("amount");
        amount.setCellValueFactory(
                new PropertyValueFactory<BudgetItem, String>("amount")
        );
        amount.setCellFactory(TextFieldTableCell.<BudgetItem, Double>forTableColumn(new DoubleStringConverter()));

        table.setItems(data);
        table.getColumns().addAll(name, amount);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(table);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();

    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
