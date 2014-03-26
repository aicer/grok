package org.aicer.grok.dictionary;

public class Pattern extends com.google.code.regexp.Pattern {

  protected Pattern(String regex, int flags) {
    super(regex, flags);
  }

  /**
   * Compiles the given regular expression into a pattern
   *
   * @param regex the expression to be compiled
   * @return the pattern
   */
  public static Pattern compile(String regex) {
      return new Pattern(regex, 0);
  }
}
