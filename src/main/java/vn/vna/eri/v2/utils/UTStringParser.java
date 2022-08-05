package vn.vna.eri.v2.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import vn.vna.eri.v2.utils.helper.MessageMention;
import vn.vna.eri.v2.utils.helper.MessageMentionType;

public class UTStringParser {

  public static Optional<MessageMention> parseMention(String quote) {
    if (quote.startsWith("<") && quote.endsWith(">")) {
      quote = quote.substring(1, quote.length() - 1);
      for (MessageMentionType mt : MessageMentionType.values()) {
        if (quote.startsWith(mt.getPrefix())) {
          String id = quote.substring(mt.getPrefix().length());
          return Optional.of(new MessageMention(id, mt));
        }
      }
    }

    return Optional.empty();
  }

  public static List<String> parseCommand(String str) {
    List<String> result = new ArrayList<>();
    String[]     spArr  = str.split("\"");
    for (int i = 0; i < spArr.length; ++i) {
      if (i % 2 == 0) {
        Collections.addAll(result, spArr[i].split(" "));
      } else {
        Collections.addAll(result, spArr[i]);
      }
    }

    return result.stream()
      .filter((s) -> Objects.nonNull(s) && !"".equals(s)).toList();
  }

}
