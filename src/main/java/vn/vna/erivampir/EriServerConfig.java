package vn.vna.erivampir;

import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@ToString
public class EriServerConfig {

    public static final String ERI_VERSION = "V1.0 - By VNA - 2022";

    public static final String CFG_DISCORD_BOT_TOKEN = "ERI_TOKEN";
    public static final String CFG_MONGODB_URI       = "MONGO_URI";
    public static final String CFG_ERIBOT_PREFIX     = "ERI_PREFIX";
    public static final String CFG_SAUCENAO_APIKEY   = "SAUCENAO_APIKEY";
    public static final String CFG_DISABLE_CLI       = "ERI_DISABLE_CLI";
    public static final String CFG_DATABASE_URL      = "DATABASE_URL";
    public static final String CFG_SPRING_DATASOURCE = "SPRING_DATASOURCE";
    public static final String CFG_SPRING_DBUSER     = "SPRING_DBUSER";
    public static final String CFG_SPRING_DBPWD      = "SPRING_DBPWD";

    private static final Collection<String> cliArgsProps;

    private static EriServerConfig instance;

    static {
        cliArgsProps = new ArrayList<>();
        cliArgsProps.add(CFG_MONGODB_URI);
        cliArgsProps.add(CFG_DISCORD_BOT_TOKEN);
        cliArgsProps.add(CFG_ERIBOT_PREFIX);
        cliArgsProps.add(CFG_SAUCENAO_APIKEY);
        cliArgsProps.add(CFG_DISABLE_CLI);
        cliArgsProps.add(CFG_DATABASE_URL);
        cliArgsProps.add(CFG_SPRING_DATASOURCE);
        cliArgsProps.add(CFG_SPRING_DBUSER);
        cliArgsProps.add(CFG_SPRING_DBPWD);
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

        for (String prop : cliArgsProps) {
            String envVal = System.getenv(prop);
            if (!Objects.isNull(envVal) && !"".equals(envVal)) {
                configurations.put(prop, envVal);
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
