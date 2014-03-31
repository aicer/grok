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
package org.aicer.grok.dictionary;

/**
 * An Enum representing built-in dictionaries in the library
 *
 * @author Israel Ekpo <israel@aicer.org>
 *
 */
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
