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
import javafx.scene.control.CheckMenuItem;

public class Sdbudget extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        final BudgetInjector budgetInjector = DaggerBudgetInjector.create();
        final Budget budget = budgetInjector.budget();
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

        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/desktop/fxml/MainScene.fxml"));
        AnchorPane mainRoot = mainLoader.load();
        MainController mainController = mainLoader.getController();
        mainRoot.getChildren().addAll(oneMonthRoot);
        Scene mainScene = new Scene(mainRoot);
        mainScene.getStylesheets().add("/desktop/styles/Styles.css");
        
        mainController.mainMenu.getMenus().get(0).getItems().get(0).setOnAction(event -> {
            CheckMenuItem menu = (CheckMenuItem) mainController.mainMenu.getMenus().get(0).getItems().get(0);
            if(menu.isSelected()) {
                mainRoot.getChildren().remove(oneMonthRoot);
                mainRoot.getChildren().add(threeMonthRoot);
                stage.setWidth(920);
            } else {
                mainRoot.getChildren().remove(threeMonthRoot);
                mainRoot.getChildren().add(oneMonthRoot);
                stage.setWidth(400);
            }
        });
        
        stage.setTitle("S/D Budget");
        stage.setWidth(400);
        stage.setHeight(580);
        stage.setScene(mainScene);
        stage.show();

        oneMonthController.scratchpadViewButton.setOnAction(event -> {
            mainRoot.getChildren().remove(oneMonthRoot);
            mainRoot.getChildren().add(scratchPadRoot);
        });

        scratchpadController.budgetViewButton.setOnAction(event -> {
            mainRoot.getChildren().remove(scratchPadRoot);
            mainRoot.getChildren().add(oneMonthRoot);
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
