package vn.vna.eri.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import vn.vna.eri.v2.schema.AuthorizeToken;

public class PermissionStringTest {
  @Test
  public void SmallPermStr1() {
    String expect = "read,write";
    AuthorizeToken.PermissionStatus p = new AuthorizeToken.PermissionStatus(true, true, false);
    assertEquals(expect, p.toString());
  }

  @Test
  public void SmallPermStr2() {
    String expect = "read";
    AuthorizeToken.PermissionStatus p = new AuthorizeToken.PermissionStatus(true, false, false);
    assertEquals(expect, p.toString());
  }

  @Test
  public void SmallPermStr3() {
    String expect = "write,delete";
    AuthorizeToken.PermissionStatus p = new AuthorizeToken.PermissionStatus(false, true, true);
    assertEquals(expect, p.toString());
  }

  @Test
  public void SmallPermStr4() {
    String expect = "delete";
    AuthorizeToken.PermissionStatus p = new AuthorizeToken.PermissionStatus(false, false, true);
    assertEquals(expect, p.toString());
  }

  @Test
  public void SmallPermStr5() {
    String expect = "";
    AuthorizeToken.PermissionStatus p = new AuthorizeToken.PermissionStatus(false, false, false);
    assertEquals(expect, p.toString());
  }

  @Test
  public void FullPermStr1() {
    String expect = "admin config:read,write service:read,delete";
    AuthorizeToken.TokenPermission tok = new AuthorizeToken.TokenPermission();
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
    AuthorizeToken.TokenPermission tok = new AuthorizeToken.TokenPermission();
    tok.getConfig().setRead(true);
    tok.getService().setRead(true);
    tok.getDiscord().setRead(true);
    assertEquals(expect, tok.toString());
  }

  @Test
  public void FullPermStr3() {
    String expect = "config:read,write service:read,delete discord:delete";
    AuthorizeToken.TokenPermission tok = new AuthorizeToken.TokenPermission();
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
    AuthorizeToken.TokenPermission tok = AuthorizeToken.TokenPermission.parseTokenString(expect);
    assertEquals(expect, tok.toString());
  }

  @Test
  public void PermParserTest2() {
    String expect = "config:read,write service:read,delete";
    String actual = "service:read,delete config:read,write";
    AuthorizeToken.TokenPermission tok = AuthorizeToken.TokenPermission.parseTokenString(actual);
    assertEquals(expect, tok.toString());
  }

  @Test
  public void PermParserTest3() {
    String expect = "admin";
    AuthorizeToken.TokenPermission tok = AuthorizeToken.TokenPermission.parseTokenString(expect);
    assertEquals(expect, tok.toString());
  }

  @Test
  public void PermParserTest4() {
    String expect = "admin config:read,write";
    String actual = "admin config:read,write service discord";
    AuthorizeToken.TokenPermission tok = AuthorizeToken.TokenPermission.parseTokenString(actual);
    assertEquals(expect, tok.toString());
  }

}
