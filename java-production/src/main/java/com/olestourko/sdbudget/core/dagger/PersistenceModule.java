package com.olestourko.sdbudget.core.dagger;

import com.olestourko.sdbudget.Configuration;
import com.olestourko.sdbudget.desktop.persistence.BudgetItemPersistence;
import com.olestourko.sdbudget.desktop.persistence.MonthPersistence;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.desktop.persistence.MonthRepositoryPersistence;
import dagger.Module;
import dagger.Provides;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.inject.Singleton;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

/**
 *
 * @author oles
 */
//@CoreApplicationScope
@Singleton
@Module
public class PersistenceModule {

    @Provides
    DSLContext createDSLContext(Configuration configuration) {
        /*
        I really need to find an alternative to wrapping this in a try catch and ignoring exceptions.
        Also, that connection needs to get closed at some point.
         */
        try {
            Class.forName("org.h2.Driver");

            String dbPathName = configuration.getDbPathname();
            String url = "jdbc:h2:" + dbPathName;
            String userName = "sdbudget";
            Connection connection = DriverManager.getConnection(url, userName, "");
            DSLContext create = DSL.using(connection, SQLDialect.H2);
            return create;
        } catch (Exception e) {
            return null;
        }
    }

    @Provides
    @Singleton
    MonthRepository monthRepository() {
        return new MonthRepository();
    }

    @Provides
    @Singleton
    public MonthRepositoryPersistence monthRepositoryPersistence(
            MonthPersistence monthPersistence,
            BudgetItemPersistence budgetItemPersistence
    ) {
        return new MonthRepositoryPersistence(monthPersistence, budgetItemPersistence);
    }
}
