/*
 * Copyright 2014 American Institute for Computing Education and Research Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.aicer.grok.util;

import com.google.common.base.Strings;
import org.aicer.grok.dictionary.GrokDictionary;
import org.joni.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Israel Ekpo <israel@aicer.org>
 *
 */
public final class Grok {

    private final Regex compiledPattern;
    private final static Logger LOGGER = LoggerFactory.getLogger(Grok.class);

  /**
   * Constructor
   */
  public Grok(final Regex compiledPattern) {
     this.compiledPattern = compiledPattern;
  }

  /**
   * Extracts named groups from the raw data
   *
   * @param rawData String to match pattern against
   * @return A map of group names mapped to their extracted values or null if there are no matches
   */
  public Map<String, String> extractNamedGroups(final CharSequence rawData) {
      Map<String, String> namedGroups = new HashMap<>();
      Matcher matcher = compiledPattern.matcher(rawData.toString().getBytes());
      if (matcher.search(0, rawData.length(), Option.DEFAULT) != -1) {
          Region region = matcher.getEagerRegion();

          for (Iterator<NameEntry> entry = compiledPattern.namedBackrefIterator(); entry.hasNext(); ) {
              NameEntry e = entry.next();
              int backRef = e.getBackRefs()[0];
              int start = region.beg[backRef];
              int end = region.end[backRef];
              CharSequence charSequence = rawData.subSequence(start, end);
              String name = new String(e.name).substring(e.nameP, e.nameEnd);
              LOGGER.debug("{} = {}", name, charSequence);
              namedGroups.put(name, charSequence.toString());
          }
          return namedGroups;
      }

      return null;
  }

  private static final void displayResults(final Map<String, String> results) {
    if (results != null) {
      for(Map.Entry<String, String> entry : results.entrySet()) {
          System.out.println(Strings.padEnd(entry.getKey(), 11, ' ') + " = " + entry.getValue());
      }
    }
  }

  public static void main(String[] args) {

    final String rawDataLine1 = "1234567 - israel.ekpo@massivelogdata.net cc55ZZ35 1789 Hello Grok";
    final String rawDataLine2 = "98AA541 - israel-ekpo@israelekpo.com mmddgg22 8800 Hello Grok";
    final String rawDataLine3 = "55BB778 - ekpo.israel@example.net secret123 4439 Valid Data Stream";

    final String expression = "%{EMAIL:username} %{USERNAME:password} %{INT:yearOfBirth}";

    final GrokDictionary dictionary = new GrokDictionary();

    // Load the built-in dictionaries
    dictionary.addBuiltInDictionaries();

    // Resolve all expressions loaded
    dictionary.bind();

    // Take a look at how many expressions have been loaded
    System.out.println("Dictionary Size: " + dictionary.getDictionarySize());

    Grok compiledPattern = dictionary.compileExpression(expression);

    displayResults(compiledPattern.extractNamedGroups(rawDataLine1));
    displayResults(compiledPattern.extractNamedGroups(rawDataLine2));
    displayResults(compiledPattern.extractNamedGroups(rawDataLine3));
  }
}
