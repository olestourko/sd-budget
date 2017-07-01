package com.olestourko.sdbudget;

/**
 *
 * @author oles
 */
public class Configuration {

    protected String version;
    protected String dbPathname;
    protected String currency;
    protected boolean checkVersion;
    
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDbPathname() {
        return dbPathname;
    }

    public void setDbPathname(String dbPathname) {
        this.dbPathname = dbPathname;
    }

    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public boolean getCheckVersion() {
        return checkVersion;
    }
    
    public void setCheckVersion(boolean checkVersion) {
        this.checkVersion = checkVersion;
    }
}
