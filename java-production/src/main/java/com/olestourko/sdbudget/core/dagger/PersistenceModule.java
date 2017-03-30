package com.olestourko.sdbudget.core.dagger;

import com.olestourko.sdbudget.core.persistence.MonthPersistence;
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
@Module
public class PersistenceModule {

    @Provides
    @Singleton
    DSLContext createDSLContext() {
        /*
        I really need to find an alternative to wrapping this in a try catch and ignoring exceptions.
        Also, that connection needs to get closed at some point.
         */
        try {
            Class.forName("org.h2.Driver");
            String url = "jdbc:h2:~/test";
            String userName = "sa";
            Connection connection = DriverManager.getConnection(url, userName, "");
            DSLContext create = DSL.using(connection, SQLDialect.H2);
            return create;
        } catch (Exception e) {
            return null;
        }
    }
}
