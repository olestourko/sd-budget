package com.olestourko.sdbudget.desktop;

import com.olestourko.sdbudget.core.commands.AddBudgetItem;
import com.olestourko.sdbudget.core.commands.CommandInvoker;
import com.olestourko.sdbudget.core.commands.CopyMonth;
import com.olestourko.sdbudget.core.commands.RemoveBudgetItem;
import com.olestourko.sdbudget.core.commands.SetMonthClosed;
import com.olestourko.sdbudget.core.commands.UpdateBudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.core.services.MonthCalculationServices;
import com.olestourko.sdbudget.desktop.controllers.MainController;
import com.olestourko.sdbudget.desktop.controllers.OneMonthController;
import com.olestourko.sdbudget.desktop.controllers.ScratchpadController;
import com.olestourko.sdbudget.desktop.controllers.ThreeMonthController;
import com.olestourko.sdbudget.desktop.models.Budget;
import java.util.Optional;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.inject.Inject;

/**
 *
 * @author oles
 */
public class Frontend {

    protected static final int ONE_MONTH_WIDTH = 400;
    protected static final int THREE_MONTH_WIDTH = 920;
    protected static final int SCRATCHPAD_WIDTH = 400;
    protected static final int DEFAULT_HEIGHT = 600;
    protected static final String baseTitle = "SDBudget 0.1.5b3";
    protected static final String thumbUri = "/desktop/images/thumb.png";

    protected double lastOneMonthWidth;
    protected double lastThreeMonthWidth;
    protected double lastScratchpadWidth;

    protected final Budget budget;
    protected final MonthCalculationServices monthCalculationServices;
    protected final MonthRepository monthRepository;
    protected final MainController mainController;
    protected final OneMonthController oneMonthController;
    protected final ThreeMonthController threeMonthController;
    protected final ScratchpadController scratchpadController;
    protected final CommandInvoker commandInvoker;

    protected Stage stage;
    protected Node currentRoot;
    protected Node mainControllerRoot;
    protected Node oneMonthControllerRoot;
    protected Node threeMonthControllerRoot;
    protected Node scratchpadControllerRoot;

    @Inject
    public Frontend(
            Budget budget,
            MonthCalculationServices monthCalculationServices,
            MonthRepository monthRepository,
            MainController mainController,
            OneMonthController oneMonthController,
            ThreeMonthController threeMonthController,
            ScratchpadController scratchpadController,
            CommandInvoker commandInvoker
    ) {
        this.budget = budget;
        this.monthCalculationServices = monthCalculationServices;
        this.monthRepository = monthRepository;
        this.mainController = mainController;
        this.oneMonthController = oneMonthController;
        this.threeMonthController = threeMonthController;
        this.scratchpadController = scratchpadController;
        this.commandInvoker = commandInvoker;
    }

    public void load(Stage stage) throws Exception {
        this.stage = stage;

        // <editor-fold defaultstate="collapsed" desc="Load FXML and initialize controller">
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/desktop/fxml/MainScene.fxml"));
        mainLoader.setController(mainController);
        mainControllerRoot = mainLoader.load();

        FXMLLoader oneMonthLoader = new FXMLLoader(getClass().getResource("/desktop/fxml/BudgetScene_OneMonth.fxml"));
        oneMonthLoader.setController(oneMonthController);
        oneMonthControllerRoot = oneMonthLoader.load();
        oneMonthController.load();

        FXMLLoader threeMonthLoader = new FXMLLoader(getClass().getResource("/desktop/fxml/BudgetScene_ThreeMonth.fxml"));
        threeMonthLoader.setController(threeMonthController);
        threeMonthControllerRoot = threeMonthLoader.load();
        threeMonthController.load();

        FXMLLoader scratchpadLoader = new FXMLLoader(getClass().getResource("/desktop/fxml/ScratchpadScene.fxml"));
        scratchpadLoader.setController(scratchpadController);
        scratchpadControllerRoot = scratchpadLoader.load();
        scratchpadController.load();
        // </editor-fold>

        this.currentRoot = oneMonthControllerRoot;
        mainController.contentContainer.getChildren().addAll(currentRoot); // Set the month view
        Scene mainScene = new Scene((Parent) mainControllerRoot);
        mainScene.getStylesheets().add("/desktop/styles/css/styles.css");

        // Register handler for save menu item
        mainController.mainMenu.getMenus().get(0).getItems().get(0).setOnAction(event -> {
            monthRepository.storeMonths();
        });

        // Register handler for undo menu item
        mainController.undoMenuItem.setOnAction(event -> {
            if (commandInvoker.canUndo()) {
                commandInvoker.undo();
            }
        });

        // Register handler for view switching menu item
        mainController.oneMonthViewMenuItem.setOnAction(event -> {
            RadioMenuItem menuItem = (RadioMenuItem) event.getSource();
            if (menuItem.isSelected()) {
                switchToOneMonthView();
            }
        });

        mainController.threeMonthViewMenuItem.setOnAction(event -> {
            RadioMenuItem menuItem = (RadioMenuItem) event.getSource();
            if (menuItem.isSelected()) {
                switchToThreeMonthView();
            }
        });
        mainController.scratchpadViewMenuItem.setOnAction(event -> {
            RadioMenuItem menuItem = (RadioMenuItem) event.getSource();
            if (menuItem.isSelected()) {
                switchToScratchpadView();
            }
        });

        stage.setTitle(baseTitle);
        stage.getIcons().add(new Image(getClass().getResourceAsStream(thumbUri)));
        stage.setWidth(ONE_MONTH_WIDTH);
        stage.setHeight(DEFAULT_HEIGHT);
        stage.setScene(mainScene);
        stage.show();

        mainController.scratchpadViewButton.setOnAction(event -> {
            if (!mainController.contentContainer.getChildren().contains(scratchpadControllerRoot)) {
                switchToScratchpadView();
            } else {
                mainController.contentContainer.getChildren().clear();
                if (currentRoot == oneMonthControllerRoot) {
                    switchToOneMonthView();
                } else {
                    switchToThreeMonthView();
                }
            }
        });

        // Register command listeners
        commandInvoker.addListener(UpdateBudgetItem.class, command -> {
            Month month = monthRepository.getFirst();
            monthCalculationServices.recalculateMonths(month);
        }, 9);
        commandInvoker.addListener(AddBudgetItem.class, command -> {
            Month month = ((AddBudgetItem) command).getMonth();
            monthCalculationServices.recalculateMonths(month);
        }, 9);
        commandInvoker.addListener(RemoveBudgetItem.class, command -> {
            Month month = ((RemoveBudgetItem) command).getMonth();
            monthCalculationServices.recalculateMonths(month);
        }, 9);
        commandInvoker.addListener(SetMonthClosed.class, command -> {
            Month month = ((SetMonthClosed) command).getMonth();
            monthCalculationServices.recalculateMonths(month);
        }, 9);
        commandInvoker.addListener(CopyMonth.class, command -> {
            Month month = monthRepository.getFirst();
            monthCalculationServices.recalculateMonths(month);
        }, 9);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {

                //http://code.makery.ch/blog/javafx-dialogs-official/
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle(baseTitle + " - Exit");
                alert.setHeaderText("Do you want to save any changes?");
                alert.setContentText(null);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(getClass().getResourceAsStream(thumbUri)));

                ButtonType saveAndExitButton = new ButtonType("Save & Exit");
                ButtonType exitButton = new ButtonType("Exit");
                ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(saveAndExitButton, exitButton, cancelButton);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == saveAndExitButton) {
                    monthRepository.storeMonths();
                } else if (result.get() == exitButton) {

                } else if (result.get() == cancelButton) {
                    event.consume();
                }
            }
        });
    }

    protected void rememberCurrentWidth() {
        if (mainController.contentContainer.getChildren().contains(oneMonthControllerRoot)) {
            lastOneMonthWidth = stage.getWidth();
        } else if (mainController.contentContainer.getChildren().contains(threeMonthControllerRoot)) {
            lastThreeMonthWidth = stage.getWidth();
        } else if (mainController.contentContainer.getChildren().contains(scratchpadController)) {
            lastScratchpadWidth = stage.getWidth();
        }
    }

    protected void switchToOneMonthView() {
        rememberCurrentWidth();
        currentRoot = oneMonthControllerRoot;
        mainController.oneMonthViewMenuItem.setSelected(true);
        mainController.scratchpadViewButton.setText("Scratchpad");
        mainController.contentContainer.getChildren().clear();
        if (!mainController.contentContainer.getChildren().contains(oneMonthControllerRoot)) {
            oneMonthController.refresh();
            mainController.contentContainer.getChildren().add(oneMonthControllerRoot);
        }
        stage.setWidth(Math.max(lastOneMonthWidth, ONE_MONTH_WIDTH));
    }

    protected void switchToThreeMonthView() {
        rememberCurrentWidth();
        currentRoot = threeMonthControllerRoot;
        mainController.threeMonthViewMenuItem.setSelected(true);
        mainController.scratchpadViewButton.setText("Scratchpad");
        mainController.contentContainer.getChildren().clear();
        if (!mainController.contentContainer.getChildren().contains(threeMonthControllerRoot)) {
            threeMonthController.refresh();
            mainController.contentContainer.getChildren().add(threeMonthControllerRoot);
        }
        stage.setWidth(Math.max(lastThreeMonthWidth, THREE_MONTH_WIDTH));
    }

    protected void switchToScratchpadView() {
        rememberCurrentWidth();
        mainController.scratchpadViewMenuItem.setSelected(true);
        mainController.scratchpadViewButton.setText("Budget");
        mainController.contentContainer.getChildren().clear();
        scratchpadController.refresh();
        mainController.contentContainer.getChildren().add(scratchpadControllerRoot);
        stage.setWidth(Math.max(lastScratchpadWidth, SCRATCHPAD_WIDTH));
    }
}
