package com.olestourko.sdbudget.desktop;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class GetVersionService extends Service<String> {

    @Override
    protected Task<String> createTask() {
        return new GetVersionTask();
    }

}
