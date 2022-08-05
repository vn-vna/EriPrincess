package vn.vna.eri.test;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import vn.vna.eri.v2.utils.UTStringParser;

public class StringParserTest {

  @Test
  public void TestNormal() {
    String       source = "this is a template command";
    List<String> result = UTStringParser.parseCommand(source);
    Assert.assertArrayEquals(result.toArray(),
      new String[] { "this", "is", "a", "template", "command" });
  }

  @Test
  public void TestContainsQuote() {
    String       source = "command arg \"this is a param\"";
    List<String> result = UTStringParser.parseCommand(source);
    Assert.assertArrayEquals(result.toArray(),
      new String[] { "command", "arg", "this is a param" });
  }

  @Test
  public void TestContainsMultipleQuote() {
    String       source = "command arg \"this is a param\" arg2 \"an other param\"";
    List<String> result = UTStringParser.parseCommand(source);
    Assert.assertArrayEquals(result.toArray(),
      new String[] { "command", "arg", "this is a param", "arg2", "an other param" });
  }

}
