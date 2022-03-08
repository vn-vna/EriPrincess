CREATE TABLE IF NOT EXISTS DiscordGuild (
        _INDEX                  SERIAL NOT NULL PRIMARY KEY,
        _GUILD_ID               VARCHAR(20) NOT NULL,
        _GUILD_REGISTER_DATE    TIMESTAMP NOT NULL,
        _GUILD_GMT              INT4 DEFAULT 7 NOT NULL,
        _ENABLE_GM              BOOLEAN,
        _ENABLE_GT              BOOLEAN,
        _ENABLE_LM              BOOLEAN
);
