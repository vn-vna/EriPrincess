package vn.vna.eri.v2.configs;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import org.ini4j.Ini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vna.eri.v2.configs.helper.LangPackEnum;

@Getter
@Setter
public class CFLangPack {

  public static final LangPackEnum DEFAULT_LANG_PACK = LangPackEnum.EN_US;
  public static final String       SECTION_CMD       = "cmd";
  public static final String       SECTION_TEMPLATE  = "template";

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
      Optional.of(Objects.requireNonNull(CFLangPack.class.getResource("/lang")).toURI())
          .ifPresent((uri) -> {
            File   folder        = new File(uri);
            List<File> langPackFiles = Arrays.stream(Objects.requireNonNull(folder.listFiles()))
                .filter((file) -> file.isFile() && file.getName().endsWith(".ini")).toList();

            logger.info("Found {} [ini] file from [lang] folder", langPackFiles.size());

            for (File file : langPackFiles) {
              try {
                String fileName = file.getName();
                String langPackName = fileName.substring(0, fileName.length() - 4);
                Ini ini         = new Ini(file);
                this.langPacks.put(langPackName, ini);
              } catch (IOException ioex) {
                logger.error("Create ini loader for {} failed due to error: {}", file.getName(),
                    ioex.getMessage());
              }
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
