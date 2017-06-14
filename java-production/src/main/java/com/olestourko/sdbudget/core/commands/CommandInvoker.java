package com.olestourko.sdbudget.core.commands;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.javatuples.Pair;

/**
 *
 * @author oles
 */
public class CommandInvoker {

    private HashMap<Class<?>, List<Pair<ICommandCallback, Integer>>> listeners = new HashMap<>();
    private Deque<ICommand> history = new ArrayDeque<>();

    @Inject
    public CommandInvoker() {

    }

    public void invoke(ICommand command) {
        history.push(command);
        command.execute();
        callListeners(command);
    }

    private void callListeners(ICommand command) {
        if (listeners.containsKey(command.getClass())) {
            // Get sorted listeners
            List<Pair<ICommandCallback, Integer>> relevantListeners = listeners.get(command.getClass()).stream().sorted((pair1, pair2) -> {
                return Integer.compare(pair2.getValue1(), pair1.getValue1());
            }).collect(Collectors.toList());
            for (Pair<ICommandCallback, Integer> pair : relevantListeners) {
                pair.getValue0().handle(command);
            }
        }
    }

    // Default to 0 for priority.
    public void addListener(Class<? extends ICommand> commandClass, ICommandCallback handler) {
        addListener(commandClass, handler, 0);
    }

    // The higher the priority, the sooner the handler is called.
    public void addListener(Class<? extends ICommand> commandClass, ICommandCallback handler, int priority) {
        if (!listeners.containsKey(commandClass)) {
            listeners.put(commandClass, new ArrayList<Pair<ICommandCallback, Integer>>());
        }
        listeners.get(commandClass).add(new Pair<>(handler, priority));
    }

    public boolean canUndo() {
        return history.size() > 0;
    }

    public void undo() {
        ICommand command = history.pop();
        command.undo();
        callListeners(command);
    }
}
