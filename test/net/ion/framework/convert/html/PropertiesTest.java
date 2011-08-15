package net.ion.framework.convert.html;

import junit.framework.TestCase;

import java.io.IOException;
import java.io.File;

import net.ion.framework.convert.html.CleanerProperties;
import net.ion.framework.convert.html.HtmlCleaner;
import net.ion.framework.convert.html.SimpleXmlSerializer;
import net.ion.framework.convert.html.TagNode;
import net.ion.framework.convert.html.XPatherException;

/**
 * Testing node manipulation after cleaning.
 */
public class PropertiesTest extends TestCase {

    private HtmlCleaner cleaner;
    private CleanerProperties properties;

    protected void setUp() throws Exception {
        cleaner = new HtmlCleaner();
        properties = cleaner.getProperties();
    }

    public void testProperties() throws XPatherException, IOException {
        properties.setNamespacesAware(false);

        properties.setAdvancedXmlEscape(true);
        assertTrue( getXmlString().indexOf("<div>&amp;&quot;&apos;&lt;&gt;</div>") >= 0 );
        properties.setAdvancedXmlEscape(false);
        assertTrue( getXmlString().indexOf("<div>&amp;amp;&amp;quot;&amp;apos;&amp;lt;&amp;gt;</div>") >= 0 );

        properties.setAdvancedXmlEscape(true);
        properties.setTransResCharsToNCR(true);
        assertTrue( getXmlString().indexOf("<div>1.&#38;&#34;&#39;&#60;&#62;</div>") >= 0 );
        assertTrue( getXmlString().indexOf("<div>2.&#38;&#34;&#39;&#60;&#62;</div>") >= 0 );
        properties.setTransResCharsToNCR(false);
        assertTrue( getXmlString().indexOf("<div>1.&amp;&quot;&apos;&lt;&gt;</div>") >= 0 );
        assertTrue( getXmlString().indexOf("<div>2.&amp;&quot;&apos;&lt;&gt;</div>") >= 0 );
        properties.setAdvancedXmlEscape(false);

        properties.setUseCdataForScriptAndStyle(true);
        assertTrue( getXmlString().indexOf("<script><![CDATA[var x=y&&z;]]></script>") >= 0 );
        assertTrue( getXmlString().indexOf("<style><![CDATA[.test{font-size:10;}]]></style>") >= 0 );
        properties.setUseCdataForScriptAndStyle(false);
        assertTrue( getXmlString().indexOf("<script>var x=y&amp;&amp;z;</script>") >= 0 );
        assertTrue( getXmlString().indexOf("<style>.test{font-size:10;}</style>") >= 0 );

        properties.setTranslateSpecialEntities(true);
        String specialHtmlEntities = "<div>"+ new String(new char[] {244,8240, 215,376, 8364})+"</div>";
        assertTrue( getXmlString().indexOf(specialHtmlEntities) >= 0 );
        properties.setTranslateSpecialEntities(false);
        assertTrue( getXmlString().indexOf(specialHtmlEntities) < 0 );

        String unicodeCharString = "<div>"+ new String(new char[] {352, 8224,8249})+"</div>";
        properties.setRecognizeUnicodeChars(true);
        assertTrue( getXmlString().indexOf(unicodeCharString) >= 0 );
        properties.setRecognizeUnicodeChars(false);
        assertTrue( getXmlString().indexOf(unicodeCharString) < 0 );
        assertTrue( getXmlString().indexOf("<div>&amp;#352;&amp;#8224;&amp;#8249;</div>") >= 0 );

        properties.setOmitUnknownTags(true);
        assertTrue( getXmlString().indexOf("<mytag>content of unknown tag</mytag>") < 0 );
        assertTrue( getXmlString().indexOf("content of unknown tag") >= 0 );
        properties.setOmitUnknownTags(false);
        assertTrue( getXmlString().indexOf("<mytag>content of unknown tag</mytag>") >= 0 );

        properties.setOmitUnknownTags(false);
        properties.setTreatUnknownTagsAsContent(true);
        assertTrue( getXmlString().indexOf("&lt;mytag&gt;content of unknown tag&lt;/mytag&gt;") >= 0 );
        properties.setTreatUnknownTagsAsContent(false);
        assertTrue( getXmlString().indexOf("<mytag>content of unknown tag</mytag>") >= 0 );

        properties.setOmitDeprecatedTags(true);
        assertTrue( getXmlString().indexOf("<u>content of deprecated tag</u>") < 0 );
        assertTrue( getXmlString().indexOf("content of deprecated tag") >= 0 );
        properties.setOmitDeprecatedTags(false);
        assertTrue( getXmlString().indexOf("<u>content of deprecated tag</u>") >= 0 );

        properties.setOmitDeprecatedTags(false);
        properties.setTreatDeprecatedTagsAsContent(true);
        assertTrue( getXmlString().indexOf("&lt;u&gt;content of deprecated tag&lt;/u&gt;") >= 0 );
        properties.setTreatDeprecatedTagsAsContent(false);
        assertTrue( getXmlString().indexOf("<u>content of deprecated tag</u>") >= 0 );

        properties.setOmitComments(false);
        assertTrue( getXmlString().indexOf("<!--my comment-->") >= 0 );
        properties.setOmitComments(true);
        assertTrue( getXmlString().indexOf("<!--my comment-->") < 0 );

        properties.setOmitXmlDeclaration(false);
        assertTrue( getXmlString().indexOf("<?xml version=\"1.0\"") >= 0 );
        properties.setOmitXmlDeclaration(true);
        assertTrue( getXmlString().indexOf("<?xml version=\"1.0\"") < 0 );

        properties.setOmitDoctypeDeclaration(false);
        assertTrue( getXmlString().indexOf("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">") >= 0 );
        properties.setOmitDoctypeDeclaration(true);
        assertTrue( getXmlString().indexOf("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">") < 0 );

        properties.setOmitHtmlEnvelope(true);
        assertTrue( getXmlString().indexOf("<html><head>") < 0 );
        assertTrue( getXmlString().indexOf("</body></html>") < 0 );
        properties.setOmitHtmlEnvelope(false);
        assertTrue( getXmlString().indexOf("<html><head>") >= 0 );
        assertTrue( getXmlString().indexOf("</body></html>") >= 0 );

        properties.setUseEmptyElementTags(true);
        assertTrue( getXmlString().indexOf("<a href=\"index.php\" />") >= 0 );
        properties.setUseEmptyElementTags(false);
        assertTrue( getXmlString().indexOf("<a href=\"index.php\"></a>") >= 0 );
        assertTrue( getXmlString().indexOf("<br />") >= 0 );

        properties.setAllowMultiWordAttributes(false);
        assertTrue( getXmlString().indexOf("<div att=\"a b c\">") < 0 );
        assertTrue( getXmlString().indexOf("<div att=\"a\" b=\"b\" c=\"c\">") >= 0 );
        properties.setAllowMultiWordAttributes(true);
        assertTrue( getXmlString().indexOf("<div att=\"a b c\">") >= 0 );

        properties.setAllowHtmlInsideAttributes(true);
        assertTrue( getXmlString().indexOf("<a title=\"&lt;b&gt;Title&lt;b&gt; is here\">LINK 1</a>") >= 0 );
        properties.setAllowHtmlInsideAttributes(false);
        assertTrue( getXmlString().indexOf("<a title=\"&lt;b&gt;Title&lt;b&gt; is here\">LINK 1</a>") < 0 );
        assertTrue( getXmlString().indexOf("<a title=\"\"><b>Title<b> is here&quot;&gt;LINK 1</b></b></a>") >= 0 );

        properties.setIgnoreQuestAndExclam(true);
        assertTrue( getXmlString().indexOf("&lt;!INSTRUCTION1 id=&quot;aaa&quot;&gt;") < 0 );
        assertTrue( getXmlString().indexOf("&lt;?INSTRUCTION2 id=&quot;bbb&quot;&gt;") < 0 );
        properties.setIgnoreQuestAndExclam(false);
        assertTrue( getXmlString().indexOf("&lt;!INSTRUCTION1 id=&quot;aaa&quot;&gt;") >= 0 );
        assertTrue( getXmlString().indexOf("&lt;?INSTRUCTION2 id=&quot;bbb&quot;&gt;") >= 0 );

        properties.setNamespacesAware(true);
        assertTrue( getXmlString().indexOf("<my:tag id=\"xxx\" xmlns:my=\"my\">aaa</my:tag>") >= 0 );
        properties.setNamespacesAware(false);
        assertTrue( getXmlString().indexOf("<html") >= 0 );
        assertTrue( getXmlString().indexOf("<tag id=\"xxx\">aaa</tag>") >= 0 );

        properties.setOmitComments(false);
        assertTrue( getXmlString().indexOf("<!-- comment with == - hyphen -->") >= 0 );
        properties.setHyphenReplacementInComment("*");
        assertTrue( getXmlString().indexOf("<!-- comment with ** - hyphen -->") >= 0 );

        properties.setPruneTags("div,mytag");
        assertTrue( getXmlString().indexOf("<div") < 0 );
        assertTrue( getXmlString().indexOf("<mytag") < 0 );
        properties.setPruneTags("");
        assertTrue( getXmlString().indexOf("<div") >= 0 );
        assertTrue( getXmlString().indexOf("<mytag") >= 0 );

        assertTrue( getXmlString().indexOf("<input checked=\"checked\" />") >= 0 );
        properties.setBooleanAttributeValues("empty");
        assertTrue( getXmlString().indexOf("<input checked=\"\" />") >= 0 );
        properties.setBooleanAttributeValues("true");
        assertTrue( getXmlString().indexOf("<input checked=\"true\" />") >= 0 );
        properties.setBooleanAttributeValues("selft");
        assertTrue( getXmlString().indexOf("<input checked=\"checked\" />") >= 0 );
    }

    private String getXmlString() throws IOException {
        TagNode node = cleaner.clean( new File("src/test/resources/test4.html"), "UTF-8" );
        return new SimpleXmlSerializer(properties).getAsString(node);
    }

}