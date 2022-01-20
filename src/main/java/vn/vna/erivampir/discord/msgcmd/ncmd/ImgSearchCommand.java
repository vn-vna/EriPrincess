package vn.vna.erivampir.discord.msgcmd.ncmd;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import vn.vna.erivampir.EriServerConfig;
import vn.vna.erivampir.discord.msgcmd.CommandTemplate;
import vn.vna.erivampir.utilities.SauceNAOSearcher;

import java.util.Objects;

@SuppressWarnings("unused")
@CommandTemplate.NormalCommand
public class ImgSearchCommand extends CommandTemplate {
    public ImgSearchCommand() {
        super("search-img", "Send me an image, i'll find it for U <3.");
    }

    @Override
    public void invoke(String[] commands, MessageReceivedEvent event) {
        event.getChannel().sendMessage("Parsing request...").queue((message -> {
            if (event.getMessage().getAttachments().isEmpty()) {
                message.editMessage("What are U looking for?").queue();
                return;
            }
            int nreq = event.getMessage().getAttachments().size();
            if (nreq > 1) {
                message.editMessage("There are multiple embed found, eri will only find result for the first one").queue();
            }
            new Thread(() -> {
                SauceNAOSearcher.SauceNAOResult result = SauceNAOSearcher.requestSauceNAOSearch(event.getMessage().getAttachments().get(0).getUrl());
                if (result.getHeader().getStatus() == 0 && result.getHeader().getResultsReturned() > 0) {
                    var           bestMatch   = result.getResults().get(0);
                    StringBuilder linkBuilder = new StringBuilder();

                    for (var imgurl : bestMatch.getData().getExtUrls()) {
                        linkBuilder.append(imgurl).append("\n");
                    }

                    EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setAuthor("Eri Vampir Shirone")
                        .setTitle("Best match result")
                        .setDescription("API Provider: SauceNAO")
                        .setThumbnail(bestMatch.getHeader().getThumbnail())
                        .addField("Match ratio", result.getResults().get(0).getHeader().getSimilarity() + "%", false)
                        .addField("Image title", Objects.isNull(bestMatch.getHeader().getIndexName()) ? "NO TITLE" : bestMatch.getHeader().getIndexName(), false)
                        .addField("Image Link", linkBuilder.toString(), false)
                        .setFooter(EriServerConfig.ERI_VERSION);

                    MessageBuilder messageBuilder = new MessageBuilder()
                        .setContent("Here U are ~~")
                        .setEmbeds(embedBuilder.build());

                    message.editMessage(messageBuilder.build()).queue();
                } else {
                    message.editMessage("No result :<").queue();
                }
            }, "FindImage-" + event.getMessage().getId()).start();
        }));
    }
}
