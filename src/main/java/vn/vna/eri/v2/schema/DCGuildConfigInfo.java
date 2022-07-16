package vn.vna.eri.v2.schema;

import com.google.gson.annotations.SerializedName;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.vna.eri.v2.utils.UTJsonClass;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DCGuildConfigInfo extends UTJsonClass {
  @SerializedName("id")
  private String guildId;
  @SerializedName("joined_datetime")
  private Instant joinedDateTime;

  public DCGuildConfigInfo(String guildId) {
    this.guildId = guildId;
    this.joinedDateTime = Instant.now();
  }
}
