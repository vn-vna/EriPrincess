package vn.vna.eri.v2.event.discord.helper;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.vna.eri.v2.event.discord.CMDDiscordCommand;

@Getter
@Setter
@AllArgsConstructor
public class ExecutionInfo {

  private CMDDiscordCommand command;
  private Integer depth;
  private CMDDiscordCommand rootCommand;

}