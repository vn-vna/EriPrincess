package vn.vna.eri.v2.utils.helper;

import lombok.Getter;

@Getter
public enum MessageMentionType {
  MENTION_MEMBER("@"),
  MENTION_ROLE("@&"),
  MENTION_CHANNEL_TEXT("#");

  MessageMentionType(String prefix) {
    this.prefix = prefix;
  }

  String prefix;
}
