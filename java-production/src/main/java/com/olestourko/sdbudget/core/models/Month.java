package com.olestourko.sdbudget.core.models;

import java.io.Serializable;

/**
 *
 * @author oles
 */
public class Month implements Serializable {

    private int id;
    private short number;
    private short year;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public short getNumber() {
        return number;
    }

    public void setNumber(short number) {
        this.number = number;
    }

    public short getYear() {
        return year;
    }

    public void setYear(short year) {
        this.year = year;
    }
}
