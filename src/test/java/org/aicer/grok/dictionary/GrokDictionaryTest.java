package org.aicer.grok.dictionary;

import org.aicer.grok.util.Grok;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
public class GrokDictionaryTest {

    private GrokDictionary grokDictionary;

    @Before
    public void setUp() throws Exception {
        grokDictionary = new GrokDictionary();

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetRegexDictionary() throws Exception {

    }

    @Test
    public void testBind() throws Exception {
        grokDictionary.bind();
        assertThat(grokDictionary.getRegexDictionary().keySet(), hasSize(0));

        grokDictionary.addBuiltInDictionaries();
        grokDictionary.bind();
        assertThat(grokDictionary.getRegexDictionary().keySet(), hasSize(91));
    }

    @Test
    public void testCompileExpression() throws Exception {
        grokDictionary.addBuiltInDictionaries();
        grokDictionary.bind();
        Grok grok = grokDictionary.compileExpression("%{WORD:test}");
        assertThat(grok,is(not(nullValue())));
    }

    @Test
    public void testDigestExpression() throws Exception {
        grokDictionary.addBuiltInDictionaries();
        grokDictionary.bind();
        String digestExpression = grokDictionary.digestExpression("%{WORD:test}");
        assertThat(digestExpression,is(equalTo("(?<test>\\b\\w+\\b)")));

    }
}