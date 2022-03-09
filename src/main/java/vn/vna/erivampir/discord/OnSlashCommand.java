package vn.vna.erivampir.discord;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import vn.vna.erivampir.discord.SlashCommand;
import vn.vna.erivampir.discord.slash.PingSlashCommand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class OnSlashCommand extends ListenerAdapter {

    Collection<SlashCommand> slashCommands;

    public OnSlashCommand() {
        slashCommands = new ArrayList<>();
        slashCommands.add(new PingSlashCommand());
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        super.onSlashCommand(event);

        for (SlashCommand slashCommand : slashCommands) {
            if (Objects.isNull(slashCommand)) {
                continue;
            }
            if (slashCommand.matchCommand(event)) {
                slashCommand.invoke(event);
            }
        }
    }
}
