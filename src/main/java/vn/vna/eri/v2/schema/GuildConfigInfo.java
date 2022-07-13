package vn.vna.eri.v2.schema;

import com.google.gson.annotations.SerializedName;
import lombok.*;
import vn.vna.eri.v2.utils.JsonClass;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GuildConfigInfo extends JsonClass {
  @SerializedName("id")
  private String guildId;
  @SerializedName("joined_datetime")
  private Instant joinedDateTime;
}
