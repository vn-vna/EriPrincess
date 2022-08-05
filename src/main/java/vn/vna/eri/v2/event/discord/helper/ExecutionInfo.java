package vn.vna.eri.v2.event.discord.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.vna.eri.v2.event.discord.CMDTemplate;

@Getter
@Setter
@AllArgsConstructor
public class ExecutionInfo {

  private CMDTemplate command;
  private Integer     depth;
  private CMDTemplate rootCommand;

}
