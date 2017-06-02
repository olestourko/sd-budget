package com.olestourko.sdbudget.core.dagger;

import com.olestourko.sdbudget.core.persistence.BudgetItemPersistence;
import com.olestourko.sdbudget.core.persistence.MonthPersistence;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import dagger.Module;
import dagger.Provides;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.inject.Singleton;
import org.cfg4j.provider.ConfigurationProvider;
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
    DSLContext createDSLContext(ConfigurationProvider configurationProvider) {
        /*
        I really need to find an alternative to wrapping this in a try catch and ignoring exceptions.
        Also, that connection needs to get closed at some point.
         */
        try {
            Class.forName("org.h2.Driver");
            String dbPathName = configurationProvider.getProperty("db_pathname", String.class);
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
    MonthRepository monthRepository(MonthPersistence monthPersistence, BudgetItemPersistence budgetItemPersistence) {
        return new MonthRepository(monthPersistence, budgetItemPersistence);
    }
}
