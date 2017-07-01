package com.olestourko.sdbudget.core.repositories;

import com.olestourko.sdbudget.core.models.Month;
import java.util.List;

/**
 *
 * @author oles
 */
public interface IMonthRepository {

    public List<Month> getMonths();
    
    public void putMonth(Month month);
    
    public Month getMonth(short number, short year);

    public Month getPrevious(Month month);

    public Month getNext(Month month);
    
    public Month getFirst();
}
