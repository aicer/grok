grok
====

A Java library for extracting structured data from unstructured data

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
