package vn.vna.eri.v2.schema;

import lombok.*;
import vn.vna.eri.v2.utils.JsonClass;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AuthorizeToken extends JsonClass {
  public static final String PERMOBJ_ADMIN = "admin";
  public static final String PERMOBJ_SERVICE = "service";
  public static final String PERMOBJ_DISCORD = "discord";
  public static final String PERMOBJ_CONFIG = "config";

  public static final String PERMISSION_READ = "read";
  public static final String PERMISSION_WRITE = "write";
  public static final String PERMISSION_DELETE = "delete";

  public static final String PERMSTR_PERM_COMBINATION = ",";
  public static final String PERMSTR_PERM_SEPARATOR = ":";

  private String token;
  private String permissions;

  @Getter
  @Setter
  @AllArgsConstructor
  @EqualsAndHashCode
  public static class TokenPermission {

    private Boolean admin;
    private PermissionStatus service;
    private PermissionStatus discord;
    private PermissionStatus config;
    public TokenPermission() {
      admin = false;
      config = new PermissionStatus(false, false, false);
      service = new PermissionStatus(false, false, false);
      discord = new PermissionStatus(false, false, false);
    }

    public static TokenPermission parseTokenString(String str) {
      TokenPermission tok = new TokenPermission();

      String[] permStrings = str.split(" ");
      for (var permString : permStrings) {
        if (PERMOBJ_ADMIN.equals(permString)) {
          tok.setAdmin(true);
          continue;
        }

        var permToken = permString.split(PERMSTR_PERM_SEPARATOR);
        if (permToken.length != 2) {
          break;
        }

        switch (permToken[0]) {
          case PERMOBJ_CONFIG -> {
            tok.setConfig(PermissionStatus.parsePermissionStatus(permToken[1]));
          }
          case PERMOBJ_SERVICE -> {
            tok.setService(PermissionStatus.parsePermissionStatus(permToken[1]));
          }
          case PERMOBJ_DISCORD -> {
            tok.setDiscord(PermissionStatus.parsePermissionStatus(permToken[1]));
          }
          default -> {
          }
        }
      }

      return tok;
    }

    @Override
    public String toString() {
      StringBuffer builder = new StringBuffer();

      if (admin) {
        builder.append(PERMOBJ_ADMIN).append(" ");
      }

      String configPermStr = this.config.isDisabled() ? ""
          : (PERMOBJ_CONFIG + PERMSTR_PERM_SEPARATOR + this.config.toString() + " ");
      String servicePermStr = this.service.isDisabled() ? ""
          : (PERMOBJ_SERVICE + PERMSTR_PERM_SEPARATOR + this.service.toString() + " ");
      String discordPermStr = this.discord.isDisabled() ? ""
          : (PERMOBJ_DISCORD + PERMSTR_PERM_SEPARATOR + this.discord.toString() + " ");

      builder.append(configPermStr).append(servicePermStr).append(discordPermStr);

      return builder.toString().strip();
    }
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @EqualsAndHashCode
  public static class PermissionStatus {
    private Boolean read;
    private Boolean write;
    private Boolean delete;

    public static PermissionStatus parsePermissionStatus(String str) {
      PermissionStatus status = new PermissionStatus(false, false, false);

      String[] tokens = str.split(PERMSTR_PERM_COMBINATION);
      for (var token : tokens) {
        switch (token) {
          case PERMISSION_READ -> status.setRead(true);
          case PERMISSION_WRITE -> status.setWrite(true);
          case PERMISSION_DELETE -> status.setDelete(true);
        }
      }

      return status;
    }

    public Boolean isDisabled() {
      return !read && !write && !delete;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();

      if (read) {
        sb.append(PERMISSION_READ).append(PERMSTR_PERM_COMBINATION);
      }
      if (write) {
        sb.append(PERMISSION_WRITE).append(PERMSTR_PERM_COMBINATION);
      }
      if (delete) {
        sb.append(PERMISSION_DELETE).append(PERMSTR_PERM_COMBINATION);
      }

      return sb.isEmpty() ? "" : sb.substring(0, sb.length() - 1);
    }
  }
}
