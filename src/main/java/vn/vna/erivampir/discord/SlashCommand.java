package vn.vna.erivampir.discord;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public interface SlashCommand {

    boolean matchCommand(SlashCommandEvent event);

    void invoke(SlashCommandEvent event);
}
