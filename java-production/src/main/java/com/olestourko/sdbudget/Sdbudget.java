package com.olestourko.sdbudget;

import com.olestourko.sdbudget.desktop.controllers.OneMonthController;
import com.olestourko.sdbudget.desktop.controllers.ThreeMonthController;
import com.olestourko.sdbudget.desktop.controllers.ScratchpadController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.olestourko.sdbudget.desktop.models.Month;
import javafx.scene.layout.AnchorPane;
import com.olestourko.sdbudget.desktop.dagger.DaggerBudgetInjector;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.core.models.Budget;
import com.olestourko.sdbudget.desktop.controllers.MainController;
import java.util.Calendar;
import com.olestourko.sdbudget.desktop.dagger.BudgetInjector;
import java.sql.*;
import javafx.scene.control.CheckMenuItem;

public class Sdbudget extends Application {

    private AnchorPane currentRoot;

    @Override
    public void start(Stage stage) throws Exception {
        final BudgetInjector budgetInjector = DaggerBudgetInjector.create();
        final Budget budget = budgetInjector.budget();

        startDatabase();

        //Populate the month repository
        MonthRepository monthRepository = budgetInjector.monthRepository();
        for (int i = 0; i < 12; i++) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, i);
            monthRepository.putMonth(new Month(cal));
        }
        budget.setCurrentMonth(monthRepository.getMonth(Calendar.getInstance()));

        OneMonthController oneMonthController = budgetInjector.oneMonthController().get();
        FXMLLoader oneMonthLoader = new FXMLLoader(getClass().getResource("/desktop/fxml/BudgetScene_OneMonth.fxml"));
        oneMonthLoader.setController(oneMonthController);
        AnchorPane oneMonthRoot = oneMonthLoader.load();
        oneMonthController.load();

        ThreeMonthController threeMonthController = budgetInjector.threeMonthController().get();
        FXMLLoader threeMonthLoader = new FXMLLoader(getClass().getResource("/desktop/fxml/BudgetScene_ThreeMonth.fxml"));
        threeMonthLoader.setController(threeMonthController);
        AnchorPane threeMonthRoot = threeMonthLoader.load();
        threeMonthController.load();

        ScratchpadController scratchpadController = budgetInjector.scratchpadController().get();
        FXMLLoader scratchpadLoader = new FXMLLoader(getClass().getResource("/desktop/fxml/ScratchpadScene.fxml"));
        scratchpadLoader.setController(scratchpadController);
        AnchorPane scratchPadRoot = scratchpadLoader.load();
        scratchpadController.load();

        MainController mainController = budgetInjector.mainController().get();
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/desktop/fxml/MainScene.fxml"));
        mainLoader.setController(mainController);
        AnchorPane mainRoot = mainLoader.load();

        currentRoot = oneMonthRoot;
        mainController.contentContainer.getChildren().addAll(currentRoot); // Set the month view
        Scene mainScene = new Scene(mainRoot);
        mainScene.getStylesheets().add("/desktop/styles/Styles.css");

        mainController.mainMenu.getMenus().get(0).getItems().get(0).setOnAction(event -> {
            CheckMenuItem menu = (CheckMenuItem) mainController.mainMenu.getMenus().get(0).getItems().get(0);
            if (menu.isSelected()) {
                currentRoot = threeMonthRoot;
                if (!mainController.contentContainer.getChildren().contains(scratchPadRoot)) {
                    mainController.contentContainer.getChildren().remove(oneMonthRoot);
                    mainController.contentContainer.getChildren().add(threeMonthRoot);
                    stage.setWidth(920);
                }
            } else {
                currentRoot = oneMonthRoot;
                if (!mainController.contentContainer.getChildren().contains(scratchPadRoot)) {
                    mainController.contentContainer.getChildren().remove(threeMonthRoot);
                    mainController.contentContainer.getChildren().add(oneMonthRoot);
                    stage.setWidth(400);
                }
            }
        });

        stage.setTitle("S/D Budget");
        stage.setWidth(400);
        stage.setHeight(580);
        stage.setScene(mainScene);
        stage.show();

        mainController.scratchpadViewButton.setOnAction(event -> {
            if (!mainController.contentContainer.getChildren().contains(scratchPadRoot)) {
                mainController.scratchpadViewButton.setText("Budget");
                mainController.contentContainer.getChildren().clear();
                mainController.contentContainer.getChildren().add(scratchPadRoot);
            } else {
                mainController.scratchpadViewButton.setText("Scratchpad");
                mainController.contentContainer.getChildren().clear();
                mainController.contentContainer.getChildren().add(currentRoot);
            }
        });
    }

    private void startDatabase() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        Connection connection = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
        ResultSet tables = connection.getMetaData().getTables(null, "PUBLIC", "%", null);
        while (tables.next()) {
            System.out.println(tables.getString(3));
        }
        connection.close();
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
