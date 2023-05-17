CREATE TABLE balance (
    id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    account_id VARCHAR(36) NOT NULL,
    value DOUBLE NOT NULL,
    UNIQUE INDEX balance_accountId(account_id)
)