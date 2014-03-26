grok
====

A Java library for extracting structured data from unstructured data

```java

public final class GrokStage {

  public static void main(String[] args) {

    final String rawDataLine1 = "1234567 - israel.ekpo@massivelogdata.net cc55ZZ35 1789 Hello Grok";
    final String rawDataLine2 = "1234567 - big.data@massivelogdata.com cc55ZZ35 2014 Welcome Grokker";
    
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

    // Extracting data from first instance of raw data
    Map<String, String> results1 = grok.extractNamedGroups(rawDataLine1);

    if (results1 != null) {
      for(Map.Entry<String, String> entry : results1.entrySet()) {
        System.out.println(entry.getKey() + "=" + entry.getValue());
      }
    }
    
    // Extracting data from second instance of raw data
    Map<String, String> results2 = grok.extractNamedGroups(rawDataLine2);
    
    if (results2 != null) {
      for(Map.Entry<String, String> entry : results2.entrySet()) {
        System.out.println(entry.getKey() + "=" + entry.getValue());
      }
    }
  }
}

```
