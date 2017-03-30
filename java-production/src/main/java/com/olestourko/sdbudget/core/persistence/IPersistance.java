package com.olestourko.sdbudget.core.persistence;

/**
 *
 * @author oles
 */
public interface IPersistance<T> {
    public T create();
    public T find(int id);
    public void store(T model);
    public void delete(T model);
}
