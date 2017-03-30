CREATE TABLE sdbudget.month_revenues (
    id INT NOT NULL AUTO_INCREMENT,
    month_id INT NOT NULL,
    budget_item_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (month_id) REFERENCES month(id),
    FOREIGN KEY (budget_item_id) REFERENCES budget_item(id)
);

CREATE TABLE sdbudget.month_expenses (
    id INT NOT NULL AUTO_INCREMENT,
    month_id INT NOT NULL,
    budget_item_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (month_id) REFERENCES month(id),
    FOREIGN KEY (budget_item_id) REFERENCES budget_item(id)
);

CREATE TABLE sdbudget.month_adjustments (
    id INT NOT NULL AUTO_INCREMENT,
    month_id INT NOT NULL,
    budget_item_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (month_id) REFERENCES month(id),
    FOREIGN KEY (budget_item_id) REFERENCES budget_item(id)
);