package com.olestourko.sdbudget;

import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.core.persistence.MonthPersistence;
import com.olestourko.sdbudget.desktop.controllers.OneMonthController;
import com.olestourko.sdbudget.desktop.controllers.ThreeMonthController;
import com.olestourko.sdbudget.desktop.controllers.ScratchpadController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.desktop.models.Budget;
import com.olestourko.sdbudget.desktop.controllers.MainController;
import java.util.Calendar;
import java.util.ArrayList;
import javafx.scene.control.RadioMenuItem;
import com.olestourko.sdbudget.core.dagger.CoreComponent;
import com.olestourko.sdbudget.core.dagger.DaggerCoreComponent;
import com.olestourko.sdbudget.desktop.dagger.BudgetComponent;
import com.olestourko.sdbudget.desktop.dagger.DaggerBudgetComponent;

public class Sdbudget extends Application {

    private AnchorPane currentRoot;

    @Override
    public void start(Stage stage) throws Exception {
        final CoreComponent coreComponent = DaggerCoreComponent.builder().build();
        final BudgetComponent budgetComponent = DaggerBudgetComponent.builder().coreComponent(coreComponent).build();
        final Budget budget = budgetComponent.budget();
        final MonthPersistence monthPersistence = coreComponent.monthPersistenceProvider().get();
        
        //Populate the month repository
        MonthRepository monthRepository = coreComponent.monthRepository();
        ArrayList<Month> months = monthPersistence.getAllMonths();
        if (months.size() == 0) {
            for (int i = 0; i < 12; i++) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.add(Calendar.MONTH, i);
                cal.set(Calendar.YEAR, 2017);
                Month month = new Month();
                month.setNumber((short) cal.get(Calendar.MONTH));
                month.setYear((short) cal.get(Calendar.YEAR));
                monthRepository.putMonth(month);
            }
            monthRepository.storeMonths();
        } else {
            monthRepository.fetchMonths();
        }

        budget.setCurrentMonth(monthRepository.getMonth((short) 0, (short) 2017));

        OneMonthController oneMonthController = budgetComponent.oneMonthController().get();
        FXMLLoader oneMonthLoader = new FXMLLoader(getClass().getResource("/desktop/fxml/BudgetScene_OneMonth.fxml"));
        oneMonthLoader.setController(oneMonthController);
        AnchorPane oneMonthRoot = oneMonthLoader.load();
        oneMonthController.load();

        ThreeMonthController threeMonthController = budgetComponent.threeMonthController().get();
        FXMLLoader threeMonthLoader = new FXMLLoader(getClass().getResource("/desktop/fxml/BudgetScene_ThreeMonth.fxml"));
        threeMonthLoader.setController(threeMonthController);
        AnchorPane threeMonthRoot = threeMonthLoader.load();
        threeMonthController.load();

        ScratchpadController scratchpadController = budgetComponent.scratchpadController().get();
        FXMLLoader scratchpadLoader = new FXMLLoader(getClass().getResource("/desktop/fxml/ScratchpadScene.fxml"));
        scratchpadLoader.setController(scratchpadController);
        AnchorPane scratchPadRoot = scratchpadLoader.load();
        scratchpadController.load();

        MainController mainController = budgetComponent.mainController().get();
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/desktop/fxml/MainScene.fxml"));
        mainLoader.setController(mainController);
        AnchorPane mainRoot = mainLoader.load();

        currentRoot = oneMonthRoot;
        mainController.contentContainer.getChildren().addAll(currentRoot); // Set the month view
        Scene mainScene = new Scene(mainRoot);
        mainScene.getStylesheets().add("/desktop/styles/Styles.css");

        // Register handler for save menu item
        mainController.mainMenu.getMenus().get(0).getItems().get(0).setOnAction(event -> {
            coreComponent.monthRepository().storeMonths();
        });

        // Register handler for view switching menu item
        mainController.oneMonthViewMenuItem.setOnAction(event -> {
            RadioMenuItem menuItem = (RadioMenuItem) event.getSource();
            if (menuItem.isSelected()) {
                currentRoot = oneMonthRoot;
                if (!mainController.contentContainer.getChildren().contains(scratchPadRoot)) {
                    mainController.contentContainer.getChildren().remove(threeMonthRoot);
                    mainController.contentContainer.getChildren().add(oneMonthRoot);
                    stage.setWidth(400);
                }
            }
        });

        mainController.threeMonthViewMenuItem.setOnAction(event -> {
            RadioMenuItem menuItem = (RadioMenuItem) event.getSource();
            if (menuItem.isSelected()) {
                currentRoot = threeMonthRoot;
                if (!mainController.contentContainer.getChildren().contains(scratchPadRoot)) {
                    mainController.contentContainer.getChildren().remove(oneMonthRoot);
                    mainController.contentContainer.getChildren().add(threeMonthRoot);
                    stage.setWidth(920);
                }

            }
        });

        stage.setTitle("SDBudget");
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
