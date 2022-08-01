package vn.vna.eri.v2.utils;

import java.util.Optional;
import vn.vna.eri.v2.utils.helper.MessageMention;
import vn.vna.eri.v2.utils.helper.MessageMentionType;

public class UTMentionParser {

  public static Optional<MessageMention> parseMention(String quote) {
    if (quote.startsWith("<") && quote.endsWith(">")) {
      quote = quote.substring(1, quote.length() - 2);
      for (MessageMentionType mt : MessageMentionType.values()) {
        if (quote.startsWith(mt.getPrefix())) {
          String id = quote.substring(mt.getPrefix().length());
          return Optional.of(new MessageMention(id, mt));
        }
      }
    }

    return Optional.empty();
  }

}
