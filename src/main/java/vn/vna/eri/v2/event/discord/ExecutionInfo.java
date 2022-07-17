package vn.vna.eri.v2.event.discord;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExecutionInfo {

  private DiscordCommand command;
  private Integer depth;
  private DiscordCommand rootCommand;

}