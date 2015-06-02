package org.aicer.grok.util;

import org.aicer.grok.dictionary.GrokDictionary;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GrokIT {
    @DataPoint
    public static final String rawDataLine1 = "1234567 - israel.ekpo@massivelogdata.net cc55ZZ35 1789 Hello Grok";
    @DataPoint
    public static final String rawDataLine2 = "98AA541 - israel-ekpo@israelekpo.com mmddgg22 8800 Hello Grok";
    @DataPoint
    public static final String rawDataLine3 = "55BB778 - ekpo.israel@example.net secret123 4439 Valid Data Stream";

    private GrokDictionary dictionary;

    @Before
    public void setUp() throws Exception {
        dictionary = new GrokDictionary();

        // Load the built-in dictionaries
        dictionary.addBuiltInDictionaries();

        // Resolve all expressions loaded
        dictionary.bind();

        // Take a look at how many expressions have been loaded
        System.out.println("Dictionary Size: " + dictionary.getDictionarySize());
        assertThat(dictionary.getDictionarySize(), is(equalTo(91)));
    }

    @Test
    public void testTestData() throws Exception {

        final String expression = "%{EMAIL:username} %{USERNAME:password} %{INT:yearOfBirth}";


        Grok compiledPattern = dictionary.compileExpression(expression);

        Map<String, String> map = compiledPattern.extractNamedGroups(rawDataLine1);
        assertThat(map, hasEntry("username", "israel.ekpo@massivelogdata.net"));
        assertThat(map, hasEntry("password", "cc55ZZ35"));
        assertThat(map, hasEntry("yearOfBirth", "1789"));
        map = compiledPattern.extractNamedGroups(rawDataLine2);
        assertThat(map, hasEntry("username", "israel-ekpo@israelekpo.com"));
        assertThat(map, hasEntry("password", "mmddgg22"));
        assertThat(map, hasEntry("yearOfBirth", "8800"));
        map = compiledPattern.extractNamedGroups(rawDataLine3);
        assertThat(map, hasEntry("username", "ekpo.israel@example.net"));
        assertThat(map, hasEntry("password", "secret123"));
        assertThat(map, hasEntry("yearOfBirth", "4439"));
    }
}
