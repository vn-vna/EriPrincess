package vn.vna.erivampir.cli;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Service;
import vn.vna.erivampir.EriServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Scanner;

@Service
public class CommandLineService {

    private static CommandLineService instance;
    private final  Scanner            serviceScanner;
    private        boolean            serviceAlive;

    public CommandLineService() {
        serviceAlive   = true;
        serviceScanner = new Scanner(System.in);
    }

    public static CommandLineService getInstance() {
        if (Objects.isNull(instance)) {
            instance = EriServer.getAppContext().getBean(CommandLineService.class);
        }
        return instance;
    }

    public void awake() {
        String line;
        // Print banner
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ListenerAdapter.class.getResourceAsStream("/cli-banner"))));
            String         str    = "";
            while (!Objects.isNull(str = reader.readLine())) {
                System.out.println(str);
            }
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
        // Listen to CLI
        while (serviceAlive) {
            System.out.println();
            line = serviceScanner.nextLine();
            if (".exit".equals(line)) {
                serviceAlive = false;
            }
        }
    }
}
