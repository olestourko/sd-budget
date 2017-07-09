package com.olestourko.sdbudget.web;

import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import java.math.BigDecimal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author oles
 */
@Configuration
public class AppConfig {

    @Bean
    public MonthRepository monthRepository() {
        MonthRepository monthRepository = new MonthRepository();
        Month month = new Month();
        month.setNumber((short)7);
        month.setYear((short)2017);
        month.getRevenues().add(new BudgetItem("Salary 1", new BigDecimal(2000.00)));
        month.getRevenues().add(new BudgetItem("Salary 2", new BigDecimal(2000.00)));
        month.getExpenses().add(new BudgetItem("Rent", new BigDecimal(800.00)));
        month.setIsClosed(true);
        monthRepository.putMonth(month);
        return monthRepository;
    }
}
