package vn.vna.eri.v2.event.discord;

import java.util.Objects;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vna.eri.v2.configs.CFGlobalConfig;
import vn.vna.eri.v2.configs.helper.ConfigTargetLoadStage;
import vn.vna.eri.v2.services.SVDiscord;

@NoArgsConstructor
public class EMServiceEvent
  extends ListenerAdapter {
  private static final Logger   logger = LoggerFactory.getLogger(EMMessageEvent.class);
  private static EMServiceEvent instance;

  public static EMServiceEvent getInstance() {
    synchronized (EMServiceEvent.class) {
      if (Objects.isNull(EMServiceEvent.instance)) {
        EMServiceEvent.instance = new EMServiceEvent();
      }
    }
    return EMServiceEvent.instance;
  }

  @Override
  public void onReady(ReadyEvent event) {
    logger.info("Logged in as [{}] successfully", SVDiscord.getInstance().getSelfUser().getAsTag());
    super.onReady(event);
    CFGlobalConfig.getInstance().invokeUpdateAtStage(ConfigTargetLoadStage.DISCORD_SERVICE_READY);
    CMDTemplate.loadCommands();
  }

}
