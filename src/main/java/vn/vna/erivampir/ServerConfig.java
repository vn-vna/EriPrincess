package vn.vna.erivampir;

import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@ToString
public class ServerConfig {

    public static final  String             CFG_DISCORD_BOT_TOKEN = "token";
    public static final  String             CFG_MONGODB_URI       = "mongo-uri";
    public static final  String             CFG_ERIBOT_PREFIX     = "bot-prefix";
    private static final Collection<String> allCfg;

    static {
        allCfg = new ArrayList<>();
        allCfg.add(CFG_MONGODB_URI);
        allCfg.add(CFG_DISCORD_BOT_TOKEN);
        allCfg.add(CFG_ERIBOT_PREFIX);
    }

    public Map<String, String> configurations;
    public Logger              logger = LoggerFactory.getLogger(ServerConfig.class);

    public ServerConfig() {
        configurations = new HashMap<>();
    }

    void parseArgs(String[] args) {
        for (String arg : args) {
            for (String cfg : allCfg) {
                if (arg.startsWith("--" + cfg)) {
                    try {
                        parseSpecArg(cfg, arg);
                    } catch (IllegalArgumentException iaex) {
                        logger.error(iaex.getMessage());
                    }
                    break;
                }
            }
        }
    }

    void parseSpecArg(String cfg, String arg) {
        String[] parg = arg.split("=");
        if (parg.length != 2) {
            configurations.put(cfg, "");
            throw new IllegalArgumentException("Value passed in to " + cfg + " is NOT valid");
        }
        configurations.put(cfg, parg[1]);
    }

    public String getConfiguration(String key) {
        return configurations.get(key);
    }

    public Logger getLogger() {
        return logger;
    }
}
