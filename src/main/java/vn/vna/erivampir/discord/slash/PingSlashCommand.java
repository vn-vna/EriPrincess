package vn.vna.erivampir.discord.slash;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import vn.vna.erivampir.discord.SlashCommand;

public class PingSlashCommand implements SlashCommand {

    public static final String COMMAND     = "ping";
    public static final String DESCRIPTION = "ping me";

    @Override
    public boolean matchCommand(SlashCommandEvent event) {
        return "ping".equals(event.getCommandString());
    }

    @Override
    public void invoke(SlashCommandEvent event) {
        event.reply("Pong");
    }
}
