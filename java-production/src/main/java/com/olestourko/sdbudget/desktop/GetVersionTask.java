package com.olestourko.sdbudget.desktop;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import javafx.concurrent.Task;
import org.json.JSONObject;

/**
 *
 * @author oles
 */
public class GetVersionTask extends Task<String> {

    @Override
    protected String call() throws Exception {       
        URL sdbudget = new URL("https://www.sdbudget.com/current-version");
        URLConnection connection = sdbudget.openConnection();
        connection.setRequestProperty("Content-Type", "application/json");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
        
        String result = "";
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            result += inputLine;
        }
        in.close();
        JSONObject json = new JSONObject(result);
        return (String) json.get("version");
    }

}
