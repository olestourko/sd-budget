package com.olestourko.sdbudget;

import com.olestourko.sdbudget.desktop.BudgetSceneController;
import com.olestourko.sdbudget.desktop.ScratchpadSceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.olestourko.sdbudget.desktop.models.Month;
import javafx.scene.layout.AnchorPane;
import com.olestourko.sdbudget.desktop.dagger.DaggerBudgetInjector;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.core.models.Budget;
import java.util.Calendar;
import com.olestourko.sdbudget.desktop.dagger.BudgetInjector;

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

        BudgetSceneController budgetSceneController = budgetInjector.budgetSceneController().get();
        FXMLLoader budgetSceneLoader = new FXMLLoader(getClass().getResource("/desktop/fxml/BudgetScene.fxml"));
        budgetSceneLoader.setController(budgetSceneController);
        AnchorPane budgetRoot = budgetSceneLoader.load();
        budgetSceneController.load();

        ScratchpadSceneController scratchpadSceneController = budgetInjector.scratchpadSceneController().get();
        FXMLLoader scratchpadSceneLoader = new FXMLLoader(getClass().getResource("/desktop/fxml/ScratchpadScene.fxml"));
        scratchpadSceneLoader.setController(scratchpadSceneController);
        AnchorPane scratchPadRoot = scratchpadSceneLoader.load();
        ScratchpadSceneController scratchPadSceneController = scratchpadSceneLoader.getController();
        scratchPadSceneController.load();

        FXMLLoader mainSceneLoader = new FXMLLoader(getClass().getResource("/desktop/fxml/MainScene.fxml"));
        AnchorPane mainRoot = mainSceneLoader.load();
        mainRoot.getChildren().addAll(budgetRoot);
        Scene mainScene = new Scene(mainRoot);
        mainScene.getStylesheets().add("/desktop/styles/Styles.css");

        stage.setTitle("S/D Budget");
        stage.setWidth(380);
        stage.setHeight(580);
        stage.setScene(mainScene);
        stage.show();

        budgetSceneController.scratchpadViewButton.setOnAction(event -> {
            mainRoot.getChildren().remove(budgetRoot);
            mainRoot.getChildren().add(scratchPadRoot);
        });

        scratchPadSceneController.budgetViewButton.setOnAction(event -> {
            mainRoot.getChildren().remove(scratchPadRoot);
            mainRoot.getChildren().add(budgetRoot);
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
