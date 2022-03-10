package vn.vna.erivampir.discord.msgcmd;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import picocli.CommandLine;
import vn.vna.erivampir.discord.msgcmd.CommandTemplate;
import vn.vna.erivampir.utilities.DiscordUtilities;

import java.util.Arrays;
import java.util.concurrent.Callable;

@CommandTemplate.NormalCommand
public class ScheduleMessageCommand extends CommandTemplate {

    public ScheduleMessageCommand() {
        super("schedule", "Configure schedule messages");
    }


    @Override
    public void invoke(String[] commands, MessageReceivedEvent event) {
        new CommandLine(new ScheduleManagerCLI(commands, event)).execute(commands);
    }

    @CommandLine.Command(name = "schedule")
    private static class ScheduleManagerCLI implements Callable<Integer> {

        public static final String commandExampleHelp = """
            ```
            schedule <target>(s) [-option][=value](s)
            Example:
            schedule gm gn -e -c=#channel1
            ```
            """;

        public static final String commandTargetsHelp = """
            ```
            gm morning: Good morning message schedule
            gn night:   Good night message schedule
            lm lonely:  Lonely schedule message
            ```
            """;

        public static final String commandOptionsHelp = """
            ```
            -c --channel=<channel> specific channel
            -e --enable            enable targets
            -d --disable           disable targets
            -h --help              show this message
            ```
            """;

        private final MessageReceivedEvent messageReceivedEvent;
        private final String[]             commandArgs;

        @CommandLine.Option(names = {"-c", "--channel"})
        private String channel;

        @CommandLine.Option(names = {"-e", "--enable"})
        private boolean callEnable;

        @CommandLine.Option(names = {"-d", "--disable"})
        private boolean callDisable;

        @CommandLine.Option(names = {"-h", "--help"})
        private boolean callHelp;

        @CommandLine.Parameters
        private String[] targets;

        public ScheduleManagerCLI(String[] commands, MessageReceivedEvent event) {
            messageReceivedEvent = event;
            commandArgs          = commands;
        }

        @Override
        public Integer call() throws Exception {
            MessageBuilder messageBuilder = new MessageBuilder();
            EmbedBuilder   embedBuilder   = DiscordUtilities.getEriEmbedBuilder();

            if (commandArgs.length == 1 || callHelp) {
                embedBuilder
                    .setTitle("Schedule message manager")
                    .setDescription(commandExampleHelp)
                    .addField("Command targets", commandTargetsHelp, false)
                    .addField("Command options", commandOptionsHelp, false);

                messageBuilder
                    .setEmbeds(embedBuilder.build());

                messageReceivedEvent
                    .getMessage()
                    .reply(messageBuilder.build())
                    .mentionRepliedUser(false)
                    .queue();

                return 0;
            }

            if (targets.length == 1) {
                embedBuilder
                    .setTitle("Schedule message manager")
                    .addField("Invalid Argument", "No target specified", false);

                messageBuilder
                    .setEmbeds(embedBuilder.build());

                messageReceivedEvent
                    .getMessage()
                    .reply(messageBuilder.build())
                    .mentionRepliedUser(false)
                    .queue();

                return 0;
            }

            return 0;
        }

    }

}
