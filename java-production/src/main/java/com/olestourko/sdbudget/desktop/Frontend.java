package com.olestourko.sdbudget.desktop;

import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.desktop.controllers.MainController;
import com.olestourko.sdbudget.desktop.controllers.OneMonthController;
import com.olestourko.sdbudget.desktop.controllers.ScratchpadController;
import com.olestourko.sdbudget.desktop.controllers.ThreeMonthController;
import com.olestourko.sdbudget.desktop.mappers.BudgetItemMapper;
import com.olestourko.sdbudget.desktop.models.Budget;
import com.olestourko.sdbudget.desktop.models.BudgetItemViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioMenuItem;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.inject.Inject;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author oles
 */
public class Frontend {

    protected static final int ONE_MONTH_WIDTH = 400;
    protected static final int THREE_MONTH_WIDTH = 920;
    protected static final int SCRATCHPAD_WIDTH = 400;
    protected static final int DEFAULT_HEIGHT = 600;

    protected double lastOneMonthWidth;
    protected double lastThreeMonthWidth;
    protected double lastScratchpadWidth;

    protected final Budget budget;
    protected final MonthRepository monthRepository;
    protected final MainController mainController;
    protected final OneMonthController oneMonthController;
    protected final ThreeMonthController threeMonthController;
    protected final ScratchpadController scratchpadController;

    protected Stage stage;
    protected Node currentRoot;
    protected Node mainControllerRoot;
    protected Node oneMonthControllerRoot;
    protected Node threeMonthControllerRoot;
    protected Node scratchpadControllerRoot;

    @Inject
    public Frontend(
            Budget budget,
            MonthRepository monthRepository,
            MainController mainController,
            OneMonthController oneMonthController,
            ThreeMonthController threeMonthController,
            ScratchpadController scratchpadController
    ) {
        this.budget = budget;
        this.monthRepository = monthRepository;
        this.mainController = mainController;
        this.oneMonthController = oneMonthController;
        this.threeMonthController = threeMonthController;
        this.scratchpadController = scratchpadController;
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

        stage.setTitle("SDBudget");
        stage.setWidth(ONE_MONTH_WIDTH);
        stage.setHeight(DEFAULT_HEIGHT);
        stage.setScene(mainScene);
        stage.show();

        mainController.scratchpadViewButton.setOnAction(event -> {
            if (!mainController.contentContainer.getChildren().contains(scratchpadControllerRoot)) {
                switchToScratchpadView();
            } else {
                mainController.scratchpadViewButton.setText("Scratchpad");
                mainController.contentContainer.getChildren().clear();
                if (currentRoot == oneMonthControllerRoot) {
                    switchToOneMonthView();
                } else {
                    switchToThreeMonthView();
                }
            }
        });

        // Link Scratchpad to other views
        scratchpadController.onAdjustmentAdded(new Callback<BudgetItemViewModel, Month>() {
            @Override
            public Month call(BudgetItemViewModel itemViewModel) {
                Month month = Frontend.this.scratchpadController.getMonth();
                BudgetItemMapper mapper = Mappers.getMapper(BudgetItemMapper.class);
                BudgetItem item = mapper.mapBudgetItemViewModelToBudgetItem(itemViewModel);
                month.getAdjustments().add(item);
                Frontend.this.scratchpadController.setMonth(month);
                // Update the other controllers
                Frontend.this.oneMonthController.refresh();
                Frontend.this.threeMonthController.refresh();
                return month;
            }
        });

        scratchpadController.onAdjustmentRemoved(new Callback<BudgetItemViewModel, Month>() {
            @Override
            public Month call(BudgetItemViewModel itemViewModel) {
                Month month = Frontend.this.scratchpadController.getMonth();
                month.getAdjustments().remove(itemViewModel.getModel());
                Frontend.this.scratchpadController.setMonth(month);
                // Update the other controllers
                Frontend.this.oneMonthController.refresh();
                Frontend.this.threeMonthController.refresh();
                return month;
            }
        });

        scratchpadController.onAdjustmentModified(new Callback<BudgetItemViewModel, Month>() {
            @Override
            public Month call(BudgetItemViewModel itemViewModel) {
                Month month = Frontend.this.scratchpadController.getMonth();
                BudgetItemMapper mapper = Mappers.getMapper(BudgetItemMapper.class);
                mapper.updateBudgetItemFromBudgetItemViewModel(itemViewModel.getModel(), itemViewModel);
                Frontend.this.scratchpadController.setMonth(month);
                // Update the other controllers
                Frontend.this.oneMonthController.refresh();
                Frontend.this.threeMonthController.refresh();
                return month;
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
        mainController.contentContainer.getChildren().clear();
        if (!mainController.contentContainer.getChildren().contains(oneMonthControllerRoot)) {
            mainController.contentContainer.getChildren().add(oneMonthControllerRoot);
        }
        stage.setWidth(Math.max(lastOneMonthWidth, ONE_MONTH_WIDTH));
    }

    protected void switchToThreeMonthView() {
        rememberCurrentWidth();
        currentRoot = threeMonthControllerRoot;
        mainController.threeMonthViewMenuItem.setSelected(true);
        mainController.contentContainer.getChildren().clear();
        if (!mainController.contentContainer.getChildren().contains(threeMonthControllerRoot)) {
            mainController.contentContainer.getChildren().add(threeMonthControllerRoot);
        }
        stage.setWidth(Math.max(lastThreeMonthWidth, THREE_MONTH_WIDTH));
    }

    protected void switchToScratchpadView() {
        rememberCurrentWidth();
        mainController.scratchpadViewMenuItem.setSelected(true);
        mainController.scratchpadViewButton.setText("Budget");
        mainController.contentContainer.getChildren().clear();
        mainController.contentContainer.getChildren().add(scratchpadControllerRoot);
        stage.setWidth(Math.max(lastScratchpadWidth, SCRATCHPAD_WIDTH));
    }
}
