CREATE TABLE sdbudget.month_debt_repayments (
    id INT NOT NULL AUTO_INCREMENT,
    month_id INT NOT NULL,
    budget_item_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (month_id) REFERENCES month(id),
    FOREIGN KEY (budget_item_id) REFERENCES budget_item(id)
);

CREATE TABLE sdbudget.month_investment_outflows (
    id INT NOT NULL AUTO_INCREMENT,
    month_id INT NOT NULL,
    budget_item_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (month_id) REFERENCES month(id),
    FOREIGN KEY (budget_item_id) REFERENCES budget_item(id)
);