package kr.tj.sample.redis.stream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple Test.
 */
public class MyTest extends TestCase {
  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public MyTest(String testName) {
    super( testName );
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite() {
    return new TestSuite( MyTest.class );
  }

  /**
   * Rigourous Test :-)
   */
  public void testIt() {
    Main.main(null);
  }
}
