package com.olestourko.sdbudget.desktop;

import com.olestourko.sdbudget.core.commands.AddBudgetItem;
import com.olestourko.sdbudget.core.commands.CommandInvoker;
import com.olestourko.sdbudget.core.commands.CopyMonth;
import com.olestourko.sdbudget.core.commands.ICommand;
import com.olestourko.sdbudget.core.commands.RemoveBudgetItem;
import com.olestourko.sdbudget.core.commands.SetMonthClosed;
import com.olestourko.sdbudget.core.commands.UpdateBudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.core.services.MonthCalculationServices;
import com.olestourko.sdbudget.desktop.controllers.ChartController;
import com.olestourko.sdbudget.desktop.controllers.MainController;
import com.olestourko.sdbudget.desktop.controllers.NMonthController;
import com.olestourko.sdbudget.desktop.controllers.OneMonthController;
import com.olestourko.sdbudget.desktop.controllers.ScratchpadController;
import com.olestourko.sdbudget.desktop.models.Budget;
import com.olestourko.sdbudget.desktop.persistence.MonthRepositoryPersistence;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    protected static final int DEFAULT_WIDTH = 400;
    protected static final int DEFAULT_HEIGHT = 600;
    protected static final String baseTitle = "SDBudget";
    protected static final String thumbUri = "/desktop/images/thumb.png";

    protected final Budget budget;
    protected final MonthCalculationServices monthCalculationServices;
    protected final MonthRepository monthRepository;
    protected final MonthRepositoryPersistence monthRepositoryPersistence;
    protected final MainController mainController;
    protected final OneMonthController oneMonthController;
    protected final NMonthController nMonthController;
    protected final ChartController chartController;
    protected final String version;

    protected final ScratchpadController scratchpadController;
    protected final CommandInvoker commandInvoker;
    ICommand lastCommand = null;

    protected Stage stage;
    protected Node currentRoot;
    protected Node mainControllerRoot;
    protected Node oneMonthControllerRoot;
    protected Node nMonthControllerRoot;
    protected Node scratchpadControllerRoot;
    protected Node chartControllerRoot;

    @Inject
    public Frontend(
            Budget budget,
            MonthCalculationServices monthCalculationServices,
            MonthRepository monthRepository,
            MonthRepositoryPersistence monthRepositoryPersistence,
            MainController mainController,
            OneMonthController oneMonthController,
            NMonthController nMonthController,
            ScratchpadController scratchpadController,
            ChartController chartController,
            CommandInvoker commandInvoker,
            String version
    ) {
        this.budget = budget;
        this.monthCalculationServices = monthCalculationServices;
        this.monthRepository = monthRepository;
        this.monthRepositoryPersistence = monthRepositoryPersistence;
        this.mainController = mainController;
        this.oneMonthController = oneMonthController;
        this.nMonthController = nMonthController;
        this.scratchpadController = scratchpadController;
        this.chartController = chartController;
        this.commandInvoker = commandInvoker;
        this.version = version;
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

        FXMLLoader nMonthLoader = new FXMLLoader(getClass().getResource("/desktop/fxml/BudgetScene_NMonth.fxml"));
        nMonthLoader.setController(nMonthController);
        nMonthControllerRoot = nMonthLoader.load();
        nMonthController.load();

        FXMLLoader scratchpadLoader = new FXMLLoader(getClass().getResource("/desktop/fxml/ScratchpadScene.fxml"));
        scratchpadLoader.setController(scratchpadController);
        scratchpadControllerRoot = scratchpadLoader.load();
        scratchpadController.load();

        FXMLLoader chartLoader = new FXMLLoader(getClass().getResource("/desktop/fxml/ChartScene.fxml"));
        chartLoader.setController(chartController);
        chartControllerRoot = chartLoader.load();

        // </editor-fold>
        this.currentRoot = nMonthControllerRoot;
        mainController.contentContainer.getChildren().addAll(currentRoot); // Set the month view
        Scene mainScene = new Scene((Parent) mainControllerRoot);
        mainScene.getStylesheets().add("/desktop/styles/css/styles.css");

        // Register handler for save menu item
        mainController.saveMenuItem.setOnAction(event -> {
            monthRepositoryPersistence.storeMonths(monthRepository);
            lastCommand = commandInvoker.getLastCommand();
            mainController.saveMenuItem.setDisable(lastCommand == commandInvoker.getLastCommand());
        });

        // Register handler for undo menu item
        mainController.undoMenuItem.setOnAction(event -> {
            commandInvoker.undo();
        });

        // Register handler for redo menu item
        mainController.redoMenuItem.setOnAction(event -> {
            commandInvoker.redo();
        });

        // Register handler for view switching menu item
        mainController.oneMonthViewMenuItem.setOnAction(event -> {
            RadioMenuItem menuItem = (RadioMenuItem) event.getSource();
            if (menuItem.isSelected()) {
                switchToOneMonthView();
            }
        });

        mainController.nMonthViewMenuItem.setOnAction(event -> {
            RadioMenuItem menuItem = (RadioMenuItem) event.getSource();
            if (menuItem.isSelected()) {
                switchToNMonthView();
            }
        });
        mainController.scratchpadViewMenuItem.setOnAction(event -> {
            RadioMenuItem menuItem = (RadioMenuItem) event.getSource();
            if (menuItem.isSelected()) {
                switchToScratchpadView();
            }
        });
        mainController.chartViewMenuItem.setOnAction(event -> {
            RadioMenuItem menuItem = (RadioMenuItem) event.getSource();
            if (menuItem.isSelected()) {
                switchToChartView();
            }
        });

        stage.setTitle(baseTitle + " " + version);
        stage.getIcons().add(new Image(getClass().getResourceAsStream(thumbUri)));
        stage.setWidth(DEFAULT_WIDTH);
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
                    switchToNMonthView();
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

        // Register handlers for binding undo/redo button status and save button status
        List<Class<? extends ICommand>> commandClasses = new ArrayList<>(Arrays.asList(
                UpdateBudgetItem.class,
                AddBudgetItem.class,
                RemoveBudgetItem.class,
                SetMonthClosed.class,
                CopyMonth.class
        ));
        for (Class<? extends ICommand> commandClass : commandClasses) {
            commandInvoker.addListener(commandClass, command -> {
                mainController.saveMenuItem.setDisable(lastCommand == commandInvoker.getLastCommand());
                mainController.undoMenuItem.setDisable(!commandInvoker.canUndo());
                mainController.redoMenuItem.setDisable(!commandInvoker.canRedo());
            });
        }

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (lastCommand == commandInvoker.getLastCommand()) {
                    return;
                }

                //http://code.makery.ch/blog/javafx-dialogs-official/
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle(baseTitle + " - Exit");
                alert.setHeaderText("Do you want to save your changes?");
                alert.setContentText(null);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(getClass().getResourceAsStream(thumbUri)));
                
                /* Add stylesheet */
                alert.getDialogPane().getStylesheets().add("/desktop/styles/css/styles.css");

                ButtonType saveAndExitButton = new ButtonType("Yes");
                ButtonType exitButton = new ButtonType("No");
                ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(saveAndExitButton, exitButton, cancelButton);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == saveAndExitButton) {
                    monthRepositoryPersistence.storeMonths(monthRepository);
                } else if (result.get() == exitButton) {

                } else if (result.get() == cancelButton) {
                    event.consume();
                }
            }
        });
    }

    protected void switchToOneMonthView() {
        currentRoot = oneMonthControllerRoot;
        mainController.oneMonthViewMenuItem.setSelected(true);
        mainController.scratchpadViewButton.setText("Scratchpad");
        mainController.contentContainer.getChildren().clear();
        if (!mainController.contentContainer.getChildren().contains(oneMonthControllerRoot)) {
            oneMonthController.refresh();
            mainController.contentContainer.getChildren().add(oneMonthControllerRoot);
        }
    }

    protected void switchToNMonthView() {
        currentRoot = nMonthControllerRoot;
        mainController.nMonthViewMenuItem.setSelected(true);
        mainController.scratchpadViewButton.setText("Scratchpad");
        mainController.contentContainer.getChildren().clear();
        if (!mainController.contentContainer.getChildren().contains(nMonthControllerRoot)) {
            mainController.contentContainer.getChildren().add(nMonthControllerRoot);
        }
    }

    protected void switchToScratchpadView() {
        mainController.scratchpadViewMenuItem.setSelected(true);
        mainController.scratchpadViewButton.setText("Budget");
        mainController.contentContainer.getChildren().clear();
        scratchpadController.refresh();
        mainController.contentContainer.getChildren().add(scratchpadControllerRoot);
    }

    protected void switchToChartView() {
        mainController.scratchpadViewButton.setText("Budget");
        currentRoot = chartControllerRoot;
        mainController.contentContainer.getChildren().clear();
        mainController.contentContainer.getChildren().add(chartControllerRoot);
    }
}
