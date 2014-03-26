package org.aicer.grok.util;

import java.io.File;
import java.util.Map;

import org.aicer.grok.dictionary.GrokDictionary;
import org.aicer.grok.dictionary.Pattern;

import com.google.code.regexp.MatchResult;
import com.google.code.regexp.Matcher;

/**
 *
 * @author Israel Ekpo <israel@aicer.org>
 *
 */
public final class Grok {

  private final Pattern compiledPattern;

  /**
   * Constructor
   */
  public Grok(final Pattern compiledPattern) {
     this.compiledPattern = compiledPattern;
  }

  /**
   * Extracts named groups from the raw data
   *
   * @param rawData
   * @return A map of group names mapped to thier extracted values
   */
  public Map<String, String> extractNamedGroups(final CharSequence rawData) {

    Matcher matcher = compiledPattern.matcher(rawData);

    if (matcher.find()) {

      MatchResult r = matcher.toMatchResult();

      if (r != null && r.namedGroups() != null) {
        return r.namedGroups();
      }
    }

    return null;
  }

  public static void main(String[] args) {

    final String rawDataLine = "1234567 - israel.ekpo@massivelogdata.net cc55ZZ35 1789 Hello Grok";

    final String expression = "%{EMAIL:username} %{USERNAME:password} %{INT:yearOfBirth}";

    // Directory where the Grok pattern files are stored
    final String patternsDirectory  = args[0];

    final File grokPatterns = new File(patternsDirectory);

    final GrokDictionary dictionary = new GrokDictionary();

    dictionary.addDictionary(grokPatterns);

    dictionary.bind();

    System.out.println("Dictionary Size: " + dictionary.getDictionarySize());

    final String digested = dictionary.digestExpression(expression);

    System.out.println("Digested : " + digested);
    System.out.println("Haystack : " + rawDataLine);

    Pattern compiledPattern = dictionary.compileExpression(expression);

    Grok grok = new Grok(compiledPattern);

    Map<String, String> results = grok.extractNamedGroups(rawDataLine);

    if (results != null) {
      for(Map.Entry<String, String> entry : results.entrySet()) {
        System.out.println(entry.getKey() + "=" + entry.getValue());
      }
    }
  }
}
