package vn.vna.erivampir.discord.msgcmd.ncmd;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import vn.vna.erivampir.discord.msgcmd.CommandTemplate;
import vn.vna.erivampir.utilities.DiscordUtilities;
import vn.vna.erivampir.utilities.SauceNAOSearcher;

import java.util.Objects;

import static vn.vna.erivampir.utilities.SauceNAOSearcher.SauceNAOResult;
import static vn.vna.erivampir.utilities.SauceNAOSearcher.SauceNAOResult.SauceNAOResultData;

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
                message
                    .editMessage("There are multiple embed found, eri will only find result for the first one")
                    .queue();
            }
            new Thread(() -> {
                String         messageImgURL = event.getMessage().getAttachments().get(0).getUrl();
                SauceNAOResult result        = SauceNAOSearcher.requestSauceNAOSearch(messageImgURL);

                boolean statusOk    = result.getHeader().getStatus() == 0;
                int     resultCount = result.getHeader().getResultsReturned();

                if (statusOk && resultCount > 0) {
                    SauceNAOResultData bestMatch   = result.getResults().get(0);
                    StringBuilder      linkBuilder = new StringBuilder();

                    for (var imgurl : bestMatch.getData().getExtUrls()) {
                        linkBuilder.append(imgurl).append("\n");
                    }

                    String similarity = result.getResults().get(0).getHeader().getSimilarity();
                    String imgTitle   = Objects.isNull(bestMatch.getHeader().getIndexName()) ? "NO TITLE" : bestMatch.getHeader().getIndexName();
                    String imgLinks   = linkBuilder.toString();

                    EmbedBuilder embedBuilder = DiscordUtilities.getEriEmbedBuilder()
                        .setTitle("Best match result")
                        .setDescription("API Provider: SauceNAO")
                        .setImage(bestMatch.getHeader().getThumbnail())
                        .addField("Match ratio", similarity + "%", false)
                        .addField("Image title", imgTitle, false)
                        .addField("Image Link", imgLinks, false);

                    MessageBuilder messageBuilder = new MessageBuilder()
                        .setContent("Here U are ~~")
                        .setEmbeds(embedBuilder.build());

                    message
                        .editMessage(messageBuilder.build())
                        .queue();
                } else {
                    message
                        .editMessage("No result :<")
                        .queue();
                }
            }, "FindImage-" + event.getMessage().getId()).start();
        }));
    }
}
