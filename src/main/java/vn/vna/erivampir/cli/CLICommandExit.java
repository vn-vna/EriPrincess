package vn.vna.erivampir.cli;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@SuppressWarnings("unused")
@CommandLineService.EriCLI
@CommandLine.Command(name = "exit", description = "Terminate the CLI thread")
public class CLICommandExit implements Callable<Integer> {

    @CommandLine.Option(names = { "-h", "--help" }, usageHelp = true, description = "Show help")
    private Boolean showHelp;

    @Override
    public Integer call() throws Exception {
        CommandLineService.getInstance().setServiceAlive(false);
        System.out.println("Command line interface is exiting");
        return 0;
    }
}
