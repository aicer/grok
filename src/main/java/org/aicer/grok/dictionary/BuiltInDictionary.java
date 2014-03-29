package org.aicer.grok.dictionary;

public enum BuiltInDictionary {

  GROK_BASE("grok-patterns"),
  GROK_REDIS("redis"),
  GROK_SYSLOG("linux-syslog"),
  GROK_POSTGRESQL("postgresql"),
  GROK_JAVA("java"),
  GROK_MONGODB("mongo_db");

  private String filePath;

  private String dictionaryName;

  private static final String PATH_PREFIX = "grok_built_in_patterns/";

  BuiltInDictionary(String fileName) {

    this.filePath = PATH_PREFIX + fileName;
    this.dictionaryName = fileName;
  }

  public String getFilePath() {
    return this.filePath;
  }

  public String getDictionaryName() {
    return this.dictionaryName;
  }

  public BuiltInDictionary getDictionaryByName(final String dictionaryName) {

    for(BuiltInDictionary dictionary : BuiltInDictionary.values()) {

      if (dictionary.getDictionaryName().equals(dictionaryName)) {
        return dictionary;
      }
    }

    return null;
  }

}
