package vn.vna.erivampir;

import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@ToString
public class ServerConfig {

    public static final String ERI_VERSION = "V1.0 - By VNA - 2022";

    public static final String CFG_DISCORD_BOT_TOKEN = "token";
    public static final String CFG_MONGODB_URI       = "mongo-uri";
    public static final String CFG_ERIBOT_PREFIX     = "bot-prefix";

    private static final Collection<String> allCfg;

    private static ServerConfig instance;

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
        try {
            String parg = arg.substring(arg.indexOf('=') + 1);
            configurations.put(cfg, parg);
        } catch (IndexOutOfBoundsException iobex) {
            logger.error("Parse argument to " + cfg + "Error");
        }
    }

    public String getConfiguration(String key) {
        return configurations.get(key);
    }

    public static ServerConfig getInstance() {
        if (Objects.isNull(instance)) {
            instance = new ServerConfig();
        }
        return instance;
    }

}
