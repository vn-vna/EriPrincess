package vn.vna.eri.v2.schema;

import com.google.gson.annotations.SerializedName;
import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.vna.eri.v2.configs.helper.LangPackEnum;
import vn.vna.eri.v2.utils.UTJsonClass;

@Data
@NoArgsConstructor
public class DCGuildConfig implements UTJsonClass {

  @SerializedName("id")
  private String guildId;
  @SerializedName("joined_datetime")
  private Instant joinedDateTime;
  @SerializedName("language")
  private String language;
  @SerializedName("tz")
  private Integer timeZone;

  public DCGuildConfig(String guildId) {
    this.guildId = guildId;
    this.joinedDateTime = Instant.now();
    this.language = LangPackEnum.EN_US.getName();
    this.timeZone = 7;
  }

}
