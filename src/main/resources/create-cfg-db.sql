CREATE TABLE IF NOT EXISTS ERICONFIG (
        _CFG_KEY            VARCHAR(30) NOT NULL PRIMARY KEY,
        _CFG_VALUE          VARCHAR(100),
        _CFG_MODIFIED       TIMESTAMP
);
