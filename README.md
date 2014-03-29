## Grok Library ##

A Java library for extracting structured data from unstructured data

This library was inspired by the logstash inteceptor or filter available here

http://logstash.net/docs/1.4.0/filters/grokdiscovery

This grok library comes with pre-defined patterns 

https://github.com/aicer/grok/tree/master/src/main/resources/grok_built_in_patterns

However, you can also create your own custom named patterns.

### SYNTAX ###

The syntax for the patterns are as follows

```
%{PATTERN_NAME:NAMED_GROUP_IN_RESULT}

```

For example, the following pattern

```
%{EMAIL:username} %{USERNAME:password} %{INT:yearOfBirth}
``` 

will extract an email address, password and year of birth from the following string

```
55BB778 - ekpo.israel@example.net secret123 4439 Valid Data Stream
```

The PATTERN_NAME has to be defined in the dictionary and the group names, username, password and yearOfBirth will be used to retrieve the values from the extraction results.


Patterns can be loaded in 4 ways by invoking the following methods on the dictionary object.

### GrokDictionary.addBuiltInDictionaries() ###

This loads all the built in dictionaries from the class path

### GrokDictionary.addDictionary(File) ###

```java

final GrokDictionary dictionary = new GrokDictionary();

// Load the built-in dictionaries
dictionary.addBuiltInDictionaries();

// Add custom pattern
dictionary.addDictionary(new File(patternDirectoryOrFilePath));

// Resolve all expressions loaded
dictionary.bind();

```

Here custom patterns can be loaded into the dictionary by passing in a File object representing the directory where the patterns are stored

### GrokDictionary.addDictionary(InputStream)

Here custom patterns can be loaded into the dictionary by passing in an inpustream containing the named expressions

### GrokDictionary.addDictionary(Reader) 

Here a custom pattern can be added by passing a reader contain the named pattern

```java

final GrokDictionary dictionary = new GrokDictionary();

// Load the built-in dictionaries
dictionary.addBuiltInDictionaries();

// Add custom pattern
dictionary.addDictionary(new StringReader("DOMAINTLD [a-zA-Z]+"));
dictionary.addDictionary(new StringReader("EMAIL %{NOTSPACE}@%{WORD}\.%{DOMAINTLD}"));

// Resolve all expressions loaded
dictionary.bind();

```



### Example of How to Use The Library ###

```java

public final class GrokStage {

  private static final void displayResults(final Map<String, String> results) {
    if (results != null) {
      for(Map.Entry<String, String> entry : results.entrySet()) {
        System.out.println(entry.getKey() + "=" + entry.getValue());
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

```
