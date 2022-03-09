package vn.vna.erivampir.cli;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import picocli.CommandLine;
import vn.vna.erivampir.EriServer;
import vn.vna.erivampir.EriServerConfig;
import vn.vna.erivampir.utilities.StuffUtilities;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommandLineService implements Runnable {

    private static CommandLineService instance;
    private final  Scanner            serviceScanner;
    private final  Thread             cliThread;
    private final  Logger             logger = LoggerFactory.getLogger(CommandLineService.class);
    private final  Set<CommandLine>   commandLines;
    private        boolean            serviceAlive;

    public CommandLineService() {
        serviceAlive   = true;
        serviceScanner = new Scanner(System.in);
        cliThread      = new Thread(this, "eri-cli");
        commandLines   = new HashSet<>();
        loadCommands();
    }

    public static CommandLineService getInstance() {
        if (Objects.isNull(instance)) {
            instance = EriServer.getAppContext().getBean(CommandLineService.class);
        }
        return instance;
    }

    public boolean isServiceAlive() {
        return serviceAlive;
    }

    public void setServiceAlive(boolean serviceAlive) {
        this.serviceAlive = serviceAlive;
    }

    public void loadCommands() {
        commandLines.add(new CommandLine(new CLICommandExit()));
        commandLines.add(new CommandLine(new CLICommandTerminate()));
    }

    public void awake() {
        cliThread.start();
    }

    @Override
    public void run() {
        if ("true".equals(EriServerConfig.getInstance().getConfiguration(EriServerConfig.CFG_DISABLE_CLI))) {
            return;
        }

        logger.info("Eri CLI Service Start");
        String line;
//      Print banner
//        try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ListenerAdapter.class.getResourceAsStream("/cli-banner"))));
//            String         str;
//            while (!Objects.isNull(str = reader.readLine())) {
//                System.out.println(str);
//            }
//            reader.close();
//        } catch (IOException ioex) {
//            ioex.printStackTrace();
//        }
//      Listening to CLI
        while (serviceAlive) {
            System.out.println();
            line = serviceScanner.nextLine();

            if (line.startsWith("$")) {
                String trimmedLine = StuffUtilities.trimMultiSpaceString(line);
                int    firstSpace  = trimmedLine.indexOf(' ');

                int    slicer  = firstSpace == -1 ? trimmedLine.length() : firstSpace;
                String command = trimmedLine.substring(1, slicer);
                String[] args = Arrays.stream(trimmedLine.substring(slicer).split(" "))
                    .filter(s -> !"".equals(s))
                    .collect(Collectors.toList())
                    .toArray(String[]::new);

                for (CommandLine cli : commandLines) {
                    if (cli.getCommandName().equals(command)) {
                        cli.execute(args);
                    }
                }
            }
        }
    }

    public @interface EriCLI {
    }
}
