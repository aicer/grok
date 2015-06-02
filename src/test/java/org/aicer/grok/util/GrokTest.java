package org.aicer.grok.util;

import org.aicer.grok.dictionary.GrokDictionary;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class GrokTest {

    private static final String TESTSTRING = "This is a test";
    private GrokDictionary grokDictionary;

    @Before
    public void setUp() throws Exception {
        grokDictionary = new GrokDictionary();
        grokDictionary.addBuiltInDictionaries();
        grokDictionary.bind();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testExtractNamedGroups() throws Exception {
        Grok grok = grokDictionary.compileExpression("%{WORD:testName}");
        Map<String, String> stringStringMap = grok.extractNamedGroups(TESTSTRING);
        assertThat(stringStringMap,is(notNullValue()));
        assertThat(stringStringMap.keySet(), is(not(empty())));
    }

}