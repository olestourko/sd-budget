package com.olestourko.sdbudget.core.commands;

/**
 *
 * @author oles
 */
public interface ICommand {

    void execute();

    void undo();
}
