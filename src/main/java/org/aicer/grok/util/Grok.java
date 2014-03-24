package org.aicer.grok.util;

import java.io.File;
import java.util.Map;

import org.aicer.grok.dictionary.GrokDictionary;

import com.google.code.regexp.MatchResult;
import com.google.code.regexp.Matcher;
import com.google.code.regexp.Pattern;

public final class Grok {

  public Grok() {

  }

  public static void main(String[] args) {

    final String rawDataLine = "1234567 - israel.ekpo@massivelogdata.net cc55ZZ35 1789 Whats going on?";

    final String expression = "%{NOTSPACE:username} %{USERNAME:password} %{INT:yearOfBirth}";

    // Directory where the Grok pattern files are stored
    final String patternsDirectory  = args[0];

    final File grokPatterns = new File(patternsDirectory);

    final GrokDictionary dictionary = new GrokDictionary();

    dictionary.load(grokPatterns);

    System.out.println("Dictionary Size: " + dictionary.getDictionarySize());

    final String digested = dictionary.digestExpression(expression);

    System.out.println("Digested : " + digested);
    System.out.println("Haystack : " + rawDataLine);

    boolean run = true;

    if (run) {
      Pattern compiledPattern = dictionary.compileExpression(expression);
      Matcher matcher = compiledPattern.matcher(rawDataLine);


      System.out.println("Number of matches: " + matcher.groupCount());

      if (matcher.find()) {
        MatchResult r = matcher.toMatchResult();

        for(Map.Entry<String, String> group : r.namedGroups().entrySet()) {
           System.out.println(group.getKey() + " = " + group.getValue());
        }
      }

    }
  }
}
