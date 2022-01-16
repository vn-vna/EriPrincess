package vn.vna.erivampir;

import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@ToString
public class EriServerConfig {

    public static final String ERI_VERSION = "V1.0 - By VNA - 2022";

    public static final String CFG_DISCORD_BOT_TOKEN = "token";
    public static final String CFG_MONGODB_URI       = "mongo-uri";
    public static final String CFG_ERIBOT_PREFIX     = "bot-prefix";
    public static final String CFG_SAUCENAO_APIKEY   = "saucenao-apikey";

    private static final Collection<String> cliArgsProps;

    private static EriServerConfig instance;

    static {
        cliArgsProps = new ArrayList<>();
        cliArgsProps.add(CFG_MONGODB_URI);
        cliArgsProps.add(CFG_DISCORD_BOT_TOKEN);
        cliArgsProps.add(CFG_ERIBOT_PREFIX);
        cliArgsProps.add(CFG_SAUCENAO_APIKEY);
    }

    public Map<String, String> configurations;
    public Logger              logger = LoggerFactory.getLogger(EriServerConfig.class);

    public EriServerConfig() {
        configurations = new HashMap<>();
    }

    public static EriServerConfig getInstance() {
        if (Objects.isNull(instance)) {
            instance = new EriServerConfig();
        }
        return instance;
    }

    void parseArgs(String[] args) {
        for (String arg : args) {
            for (String cfg : cliArgsProps) {
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

}
