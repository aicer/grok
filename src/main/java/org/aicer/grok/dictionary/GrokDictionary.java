package org.aicer.grok.dictionary;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.aicer.grok.exception.GrokCompilationException;
import org.apache.log4j.Logger;

import com.google.code.regexp.Pattern;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;

public final class GrokDictionary {

  private static final Logger logger = Logger.getLogger(GrokDictionary.class);

  private final Map<String, String> regexDictionary = new HashMap<String, String>();

  public GrokDictionary() {

  }

  /**
   * Loads the Grok dictionary
   *
   * @param file
   * @throws GrokCompilationException if there is a problem
   */
  public void load(final File file) {

    try {
      loadDictionary(file);
    } catch (IOException e) {
      logger.error("Grok Comipilation error ", e);
      throw new GrokCompilationException(e);
    }

    digestExpressions();
  }

  /**
   * Compiles the expression into a pattern
   *
   * This uses the internal dictionary of named regular expressions
   *
   * @param expression
   * @return
   */
  public Pattern compileExpression(final String expression) {

    final String digestedExpression = digestExpression(expression);

    return Pattern.compile(digestedExpression);
  }

  public int getDictionarySize() {
    return this.regexDictionary.size();
  }

  public void display() {

    for(Map.Entry<String, String> entry: regexDictionary.entrySet()) {
      System.out.println(entry.getKey() + entry.getValue());
    }
  }

  private void digestExpressions() {

    boolean wasModified = true;

    while (wasModified) {

      wasModified = false;

      for(Map.Entry<String, String> entry: regexDictionary.entrySet()) {

        String originalExpression = entry.getValue();
        String digestedExpression = digestExpression(originalExpression);
        wasModified = (originalExpression != digestedExpression);

        if (wasModified) {
          entry.setValue(digestedExpression);
          break; // stop the for loop
        }

      }
    }
  }

  /**
   * Digests the original expression into a pure named regex
   *
   * @param originalExpression
   * @return
   */
  public String digestExpression(String originalExpression) {

    final String PATTERN_START = "%{";
    final String PATTERN_STOP = "}";
    final char PATTERN_DELIMITER = ':';

    while(true) {

      int PATTERN_START_INDEX = originalExpression.indexOf(PATTERN_START);
      int PATTERN_STOP_INDEX = originalExpression.indexOf(PATTERN_STOP, PATTERN_START_INDEX + PATTERN_START.length());

      // End the loop is %{ or } is not in the current line
      if (PATTERN_START_INDEX < 0 || PATTERN_STOP_INDEX < 0) {
        break;
      }

      // Grab what's inside %{ }
      String grokPattern = originalExpression.substring(PATTERN_START_INDEX + PATTERN_START.length(), PATTERN_STOP_INDEX);

      // Where is the : character
      int PATTERN_DELIMITER_INDEX = grokPattern.indexOf(PATTERN_DELIMITER);

      String regexName = grokPattern;
      String groupName = null;

      if (PATTERN_DELIMITER_INDEX >= 0) {
        regexName = grokPattern.substring(0, PATTERN_DELIMITER_INDEX);
        groupName = grokPattern.substring(PATTERN_DELIMITER_INDEX + 1, grokPattern.length());
      }

      final String dictionaryValue = regexDictionary.get(regexName);

      if (dictionaryValue == null) {
        throw new GrokCompilationException("Missing value for regex name : " + regexName);
      }

      // Defer till next iteration
      if (dictionaryValue.contains(PATTERN_START)) {
        break;
      }

      String replacement = dictionaryValue;

      // Named capture group
      if (null != groupName) {
        replacement = "(?<" + groupName + ">" + dictionaryValue + ")";
      }

      originalExpression = new StringBuilder(originalExpression).replace(PATTERN_START_INDEX, PATTERN_STOP_INDEX + PATTERN_STOP.length(), replacement).toString();
    }

    return originalExpression;
  }

  private void loadDictionary(final File file) throws IOException {

    if (false == file.exists()) {
      throw new GrokCompilationException("The path specfied could not be found: " + file);
    }

    if (false == file.canRead()) {
      throw new GrokCompilationException("The path specified is not readable" + file);
    }

    if (file.isDirectory()) {

      File[] children = file.listFiles();

      // Cycle through the directory and process all child files or folders
      for (File child : children) {
        loadDictionary(child);
      }

    } else {

      Reader reader = new InputStreamReader(new FileInputStream(file), "UTF-8");

      try {
        loadDictionary(reader);
      } finally {
        Closeables.closeQuietly(reader);
      }
    }

  }


  private void loadDictionary(Reader reader) throws IOException {

    for (String currentFileLine : CharStreams.readLines(reader)) {

      final String currentLine = currentFileLine.trim();

      if (currentLine.length() == 0 || currentLine.startsWith("#")) {
        continue;
      }

      int entryDelimiterPosition = currentLine.indexOf(" ");

      if (entryDelimiterPosition < 0) {
        throw new GrokCompilationException("Dictionary entry (name and value) must be space-delimited: " + currentLine);
      }

      if (entryDelimiterPosition == 0) {
        throw new GrokCompilationException("Dictionary entry must contain a name. " + currentLine);
      }

      final String dictionaryEntryName = currentLine.substring(0, entryDelimiterPosition);
      final String dictionaryEntryValue = currentLine.substring(entryDelimiterPosition + 1, currentLine.length()).trim();

      if (dictionaryEntryValue.length() == 0) {
        throw new GrokCompilationException("Dictionary entry must contain a value: " + currentLine);
      }

      regexDictionary.put(dictionaryEntryName, dictionaryEntryValue);
    }
  }

}
