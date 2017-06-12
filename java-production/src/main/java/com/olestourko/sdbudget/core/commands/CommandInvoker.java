package com.olestourko.sdbudget.core.commands;

import javax.inject.Inject;

/**
 *
 * @author oles
 */
public class CommandInvoker {

    @Inject
    public CommandInvoker() {

    }

    public void invoke(ICommand command) {
        command.execute();
    }
}
