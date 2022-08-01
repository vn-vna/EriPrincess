package vn.vna.eri.v2.configs;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vna.eri.v2.configs.helper.LangPackEnum;

@Getter
@Setter
public class CFLangPack {

  public static final LangPackEnum DEFAULT_LANG_PACK        = LangPackEnum.EN_US;
  public static final String       SECTION_CMD              = "cmd";
  public static final String       SECTION_TEMPLATE         = "template";
  private static final String      LANG_LIST_PATHNAME       = "/lang-list.ini";
  private static final String      LANGPACK_SOURCE_PROPNAME = "source";

  private static CFLangPack instance;
  private static Logger     logger = LoggerFactory.getLogger(CFLangPack.class);

  Map<String, Ini> langPacks;

  public static CFLangPack getInstance() {
    synchronized (CFLangPack.class) {
      if (Objects.isNull(CFLangPack.instance)) {
        CFLangPack.instance = new CFLangPack();
      }
    }
    return CFLangPack.instance;
  }

  public void loadLangPacks() {
    logger.info("Starting load language pack from resources");
    this.langPacks = new HashMap<>();
    try {
      InputStream packListStream = CFLangPack.class
          .getResourceAsStream(LANG_LIST_PATHNAME);

      Ini langList = new Ini(packListStream);

      langList.entrySet().stream().forEach((entry) -> {
        String  sectionName = entry.getKey();
        Section sectionData = entry.getValue();
        String  source      = sectionData.get(LANGPACK_SOURCE_PROPNAME);
        try {
          InputStream packStream = CFLangPack.class.getResourceAsStream(source);
          this.langPacks.put(sectionName, new Ini(packStream));
        } catch (Exception ex) {
          logger.error(
              "Can't load lang pack {} from source {} due to error: {}",
              sectionName,
              source,
              ex.getMessage());
        }
      });
    } catch (Exception ex) {
      logger.error("Can't load language pack due to error: {}", ex.getMessage());
    }
  }

  public Optional<Ini> getLangPack(LangPackEnum pack) {
    return this.getLangPack(pack.getName());
  }

  public Optional<Ini> getLangPack(String packName) {
    return Optional.ofNullable(this.langPacks.get(packName));
  }

  public Optional<String> getString(String pack, String section, String key) {
    Ini langPack = this.getLangPack(pack)
        .orElse(this.getLangPack(DEFAULT_LANG_PACK).get());

    return Optional.ofNullable(langPack.get(section, key));
  }

  public Optional<String> getString(String section, String key) {
    Ini langPack = this.getLangPack(DEFAULT_LANG_PACK).get();
    return Optional.ofNullable(langPack.get(section, key));
  }

  public Optional<String> getString(LangPackEnum pack, String section, String key) {
    return this.getString(pack.getName(), section, key);
  }

}
