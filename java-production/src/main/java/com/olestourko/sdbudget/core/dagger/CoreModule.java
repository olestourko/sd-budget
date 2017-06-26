/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olestourko.sdbudget.core.dagger;

import com.olestourko.sdbudget.Configuration;
import com.olestourko.sdbudget.core.commands.CommandInvoker;
import dagger.Module;
import dagger.Provides;
import java.nio.file.Paths;
import java.util.Arrays;
import javax.inject.Singleton;
import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.context.environment.Environment;
import org.cfg4j.source.context.environment.ImmutableEnvironment;
import org.cfg4j.source.context.filesprovider.ConfigFilesProvider;
import org.cfg4j.source.files.FilesConfigurationSource;

/**
 *
 * @author oles
 */
@Singleton
@Module
public class CoreModule {

    @Singleton
    @Provides
    public Configuration configuration() {
        // Specify which files to load. Configuration from both files will be merged.
        ConfigFilesProvider configFilesProvider = () -> Arrays.asList(Paths.get("configuration.yaml"));
        Environment environment = new ImmutableEnvironment("./");

        // Use local files as configuration store
        ConfigurationSource source = new FilesConfigurationSource(configFilesProvider);

        // Create provider
        ConfigurationProvider configurationProvider = new ConfigurationProviderBuilder()
                .withConfigurationSource(source)
                .withEnvironment(environment)
                .build();

        // Copy configuration into a POJO
        Configuration configuration = new Configuration();
        configuration.setVersion("0.3.0");

        try {
            configuration.setDbPathname(configurationProvider.getProperty("db_pathname", String.class));
        } catch (Exception exception) {
            configuration.setDbPathname("~/sdbudget");
        }

        try {
            configuration.setCurrency(configurationProvider.getProperty("currency", String.class));
        } catch (Exception exception) {
            configuration.setCurrency("$");
        }

        try {
            configuration.setCheckVersion(configurationProvider.getProperty("check_version", Boolean.class));
        } catch (Exception exception) {
            configuration.setCheckVersion(true);
        }

        return configuration;
    }

    @Singleton
    @Provides
    public CommandInvoker commandInvoker() {
        return new CommandInvoker();
    }
}
