/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olestourko.sdbudget.core.models;

/**
 *
 * @author oles
 */
abstract public class Model {
    private int id;
    
    public final int getId() {
        return id;
    }

    public final void setId(int id) {
        this.id = id;
    }
}
