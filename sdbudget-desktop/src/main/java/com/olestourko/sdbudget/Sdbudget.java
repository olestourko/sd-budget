package com.olestourko.sdbudget;

import com.olestourko.sdbudget.core.models.Month;
import javafx.application.Application;
import javafx.stage.Stage;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.desktop.models.Budget;
import com.olestourko.sdbudget.desktop.dagger.DaggerDesktopComponent;
import com.olestourko.sdbudget.desktop.models.factories.MonthFactory;
import com.olestourko.sdbudget.desktop.services.VersionComparisonService;
import com.olestourko.sdbudget.desktop.Frontend;
import com.olestourko.sdbudget.desktop.GetVersionService;
import com.olestourko.sdbudget.desktop.persistence.MonthRepositoryPersistence;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.List;
import org.flywaydb.core.Flyway;
import javafx.application.Application.Parameters;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import com.olestourko.sdbudget.desktop.dagger.DesktopComponent;

public class Sdbudget extends Application {

    private boolean ALWAYS_MIGRATE = true;
    private GetVersionService getVersionService; // Prevent garbage collection on service

    @Override
    public void start(Stage stage) throws Exception {
        // Create configuration file if it doesn't exist
        File configFile = new File("./configuration.yaml");
        if (!configFile.exists()) {
            InputStream link = (getClass().getResourceAsStream("/configuration/configuration.yaml"));
            Files.copy(link, configFile.getAbsoluteFile().toPath());
        };

        final DesktopComponent desktopComponent = DaggerDesktopComponent.builder().build();
        final Budget budget = desktopComponent.budget().get();
        final Frontend frontend = desktopComponent.frontend().get();
        final Configuration configuration = desktopComponent.configuration();

        // Migrate DB if the migrate flag is set
        Parameters parameters = getParameters();
        List<String> unnamedParamers = parameters.getUnnamed();
        if (unnamedParamers.contains("migrate") || ALWAYS_MIGRATE) {
            String dbPathName = configuration.getDbPathname();
            String url = "jdbc:h2:" + dbPathName;
            String userName = "sdbudget";
            Flyway flyway = new Flyway();
            flyway.setDataSource(url, userName, "");
            flyway.migrate();
        }

        // Populate the month repository
        MonthRepository monthRepository = desktopComponent.monthRepository();
        MonthRepositoryPersistence monthRepositoryPersistence = desktopComponent.monthRepositoryPersistence();
        MonthFactory monthFactory = desktopComponent.monthFactory();
        monthRepositoryPersistence.fetchMonths(monthRepository);
        Calendar calendar = Calendar.getInstance();
        Month month = monthRepository.getMonth((short) calendar.get(Calendar.MONTH), (short) calendar.get(Calendar.YEAR));
        if (month == null) {
            month = monthFactory.createCurrentMonth();
            monthRepository.putMonth(month);
        }

        for (int i = 0; i < 2; i++) {
            if (monthRepository.getNext(month) == null) {
                monthRepository.putMonth(monthFactory.createNextMonth(month));
            }
            month = monthRepository.getNext(month);
        }

        desktopComponent.monthServices().recalculateMonths(monthRepository.getFirst());
        budget.setCurrentMonth(monthRepository.getMonth((short) calendar.get(Calendar.MONTH), (short) calendar.get(Calendar.YEAR)));

        if (configuration.getCheckVersion()) {
            stage.setOnShown(value -> {
                // Get the latest version number from sdbudget.com
                getVersionService = new GetVersionService();
                getVersionService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        String latestVersion = (String) t.getSource().getValue();
                        if (new VersionComparisonService().compare(configuration.getVersion(), latestVersion) == VersionComparisonService.Status.OLDVERSION) {
                            Alert alert = new Alert(AlertType.INFORMATION);
                            alert.getDialogPane().getStylesheets().add("/desktop/styles/css/styles.css");
                            alert.setTitle("Update Available");
                            alert.setHeaderText("A new version of SDBudget is available!");
                            alert.setContentText("Version " + latestVersion + " is now available.\nGet it at www.sdbudget.com.");
                            alert.showAndWait();
                        }
                    }
                });
                getVersionService.start();
            });
        }

        frontend.load(stage);
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
