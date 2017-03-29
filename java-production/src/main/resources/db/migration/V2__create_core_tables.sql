CREATE TABLE sdbudget.month (
    id INT NOT NULL,
    number SMALLINT,
    year SMALLINT,
    CONSTRAINT PK_Month PRIMARY KEY (id)
);

CREATE TABLE sdbudget.budget_item (
    id INT NOT NULL,
    amount DECIMAL,
    name VARCHAR(255),
    CONSTRAINT PK_Budget_Item PRIMARY KEY (id)
);