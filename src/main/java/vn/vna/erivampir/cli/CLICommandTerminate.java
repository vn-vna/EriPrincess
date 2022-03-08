package vn.vna.erivampir.cli;

import picocli.CommandLine;

import java.util.Objects;
import java.util.concurrent.Callable;

@SuppressWarnings("unused")
@CommandLineService.EriCLI
@CommandLine.Command(name = "terminate", description = "Terminate the server")
public class CLICommandTerminate implements Callable<Integer> {

    @CommandLine.Option(names = {"-t", "--delay"}, description = "Delay terminate application")
    private Integer delay;
    @CommandLine.Option(names = {"-h", "--help"}, usageHelp = true, description = "Show help")
    private Boolean showHelp;

    @Override
    public Integer call() throws Exception {

        if (Objects.isNull(delay)) delay = 0;

        System.out.printf("Process will be terminated after %dms", delay);

        new Thread(() -> {
            try {
                Thread.sleep(delay);
                Runtime.getRuntime().exit(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        return 0;
    }

}
