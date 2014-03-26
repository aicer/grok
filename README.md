grok
====

A Java library for extracting structured data from unstructured data

```java

public final class GrokStage {

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

```
