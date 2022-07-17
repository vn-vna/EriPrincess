package vn.vna.eri.test;

import org.junit.Assert;
import org.junit.Test;
import vn.vna.eri.v2.schema.CSPermissionStatus;
import vn.vna.eri.v2.schema.CSPermissions;

public class PermissionStringTest {

  @Test
  public void testToString1() {
    String expected = "";
    Assert.assertEquals(expected, new CSPermissionStatus().toString());
  }

  @Test
  public void testToString2() {
    String expected = "api:read discord:read,write";
    CSPermissions perm = new CSPermissions();
    perm.getApi().setRead(true);
    perm.getApi().setWrite(false);
    perm.getDiscord().setRead(true);
    perm.getDiscord().setWrite(true);
    Assert.assertEquals(expected, perm.toString());
  }

  @Test
  public void testToString3() {
    String expected = "api:read";
    CSPermissions perm = new CSPermissions();
    perm.getApi().setRead(true);
    perm.getApi().setWrite(false);
    perm.getDiscord().setRead(false);
    perm.getDiscord().setWrite(false);
    Assert.assertEquals(expected, perm.toString());
  }

  @Test
  public void testParseAndToString1() {
    String expected = "read,write";
    String source = "write,read";

    CSPermissionStatus ps = new CSPermissionStatus();
    ps.importConfigString(source);

    Assert.assertEquals(expected, ps.toConfigString());
  }

  @Test
  public void testParseAndToString2() {
    String expected = "api:read,write discord:read";
    String source = "discord:read api:write,read,,";

    CSPermissions perm = new CSPermissions();
    perm.importFromConfigString(source);

    Assert.assertEquals(expected, perm.toString());
  }


}
