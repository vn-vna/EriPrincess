package vn.vna.eri.v2.schema;

import java.time.Instant;

import com.google.gson.annotations.SerializedName;

import vn.vna.eri.v2.utils.JsonClass;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
