package vn.vna.eri.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import vn.vna.eri.v2.schema.DCAuthorizeToken;

public class PermissionStringTest {
  @Test
  public void SmallPermStr1() {
    String expect = "read,write";
    DCAuthorizeToken.PermissionStatus p = new DCAuthorizeToken.PermissionStatus(true, true, false);
    assertEquals(expect, p.toString());
  }

  @Test
  public void SmallPermStr2() {
    String expect = "read";
    DCAuthorizeToken.PermissionStatus p = new DCAuthorizeToken.PermissionStatus(true, false, false);
    assertEquals(expect, p.toString());
  }

  @Test
  public void SmallPermStr3() {
    String expect = "write,delete";
    DCAuthorizeToken.PermissionStatus p = new DCAuthorizeToken.PermissionStatus(false, true, true);
    assertEquals(expect, p.toString());
  }

  @Test
  public void SmallPermStr4() {
    String expect = "delete";
    DCAuthorizeToken.PermissionStatus p = new DCAuthorizeToken.PermissionStatus(false, false, true);
    assertEquals(expect, p.toString());
  }

  @Test
  public void SmallPermStr5() {
    String expect = "";
    DCAuthorizeToken.PermissionStatus p = new DCAuthorizeToken.PermissionStatus(false, false, false);
    assertEquals(expect, p.toString());
  }

  @Test
  public void FullPermStr1() {
    String expect = "admin config:read,write service:read,delete";
    DCAuthorizeToken.TokenPermission tok = new DCAuthorizeToken.TokenPermission();
    tok.setAdmin(true);
    tok.getConfig().setRead(true);
    tok.getConfig().setWrite(true);
    tok.getService().setRead(true);
    tok.getService().setDelete(true);
    assertEquals(expect, tok.toString());
  }

  @Test
  public void FullPermStr2() {
    String expect = "config:read service:read discord:read";
    DCAuthorizeToken.TokenPermission tok = new DCAuthorizeToken.TokenPermission();
    tok.getConfig().setRead(true);
    tok.getService().setRead(true);
    tok.getDiscord().setRead(true);
    assertEquals(expect, tok.toString());
  }

  @Test
  public void FullPermStr3() {
    String expect = "config:read,write service:read,delete discord:delete";
    DCAuthorizeToken.TokenPermission tok = new DCAuthorizeToken.TokenPermission();
    tok.getConfig().setRead(true);
    tok.getConfig().setWrite(true);
    tok.getService().setRead(true);
    tok.getService().setDelete(true);
    tok.getDiscord().setDelete(true);
    assertEquals(expect, tok.toString());
  }

  @Test
  public void PermParserTest1() {
    String expect = "config:read,write service:read,delete discord:delete";
    DCAuthorizeToken.TokenPermission tok = DCAuthorizeToken.TokenPermission.parseTokenString(expect);
    assertEquals(expect, tok.toString());
  }

  @Test
  public void PermParserTest2() {
    String expect = "config:read,write service:read,delete";
    String actual = "service:read,delete config:read,write";
    DCAuthorizeToken.TokenPermission tok = DCAuthorizeToken.TokenPermission.parseTokenString(actual);
    assertEquals(expect, tok.toString());
  }

  @Test
  public void PermParserTest3() {
    String expect = "admin";
    DCAuthorizeToken.TokenPermission tok = DCAuthorizeToken.TokenPermission.parseTokenString(expect);
    assertEquals(expect, tok.toString());
  }

  @Test
  public void PermParserTest4() {
    String expect = "admin config:read,write";
    String actual = "admin config:read,write service discord";
    DCAuthorizeToken.TokenPermission tok = DCAuthorizeToken.TokenPermission.parseTokenString(actual);
    assertEquals(expect, tok.toString());
  }

}
