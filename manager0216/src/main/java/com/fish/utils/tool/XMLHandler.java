package com.fish.utils.tool;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * xml解析句柄
 *
 * @author Host-0222
 */
public class XMLHandler extends DefaultHandler
{
    //
    private Map<String, String> xmlMap;
    private String key;

    /**
     * 进行解析XML
     *
     * @param protocolXML
     * @throws Exception
     */
    public static XMLHandler parse(String protocolXML) throws Exception
    {
        try
        {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            XMLHandler dh = new XMLHandler();
            saxParser.parse(new InputSource(new StringReader(protocolXML)), dh);
            return dh;
        } catch (Exception e)
        {
            throw e;
        }
    }

    public Map<String, String> getXmlMap()
    {
        return xmlMap;
    }

    /**
     * 接收文档开始的通知
     */
    public void startDocument() throws SAXException
    {
        xmlMap = new HashMap<String, String>();
    }

    /**
     * 接收元素开始的通知
     *
     * @param uri        - 名称空间 URI，如果元素没有任何名称空间 URI，或者没有正在执行名称空间处理，则为空字符串。
     * @param localName  - 本地名称（不带前缀），如果没有正在执行名称空间处理，则为空字符串。
     * @param qName      - 限定的名称（带有前缀），如果限定的名称不可用，则为空字符串。
     * @param attributes - 附加到元素的属性。如果没有属性，则它将是空的 Attributes 对象。
     */
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes)
    {
        key = qName;
    }

    /**
     * @param ch     - 字符。
     * @param start  - 字符数组中的开始位置。
     * @param length - 从字符数组中使用的字符数。
     */
    public void characters(char[] ch, int start, int length)
            throws SAXException
    {
        String content = new String(ch, start, length);
        if (key != null && length > 0 && !content.equals("\n"))
        {
            xmlMap.put(key, content);
        }

    }

    /**
     *
     */
    public void endDocument() throws SAXException
    {

    }

    /**
     * @param uri       - 名称空间 URI，如果元素没有任何名称空间 URI，或者没有正在执行名称空间处理，则为空字符串。
     * @param localName - 本地名称（不带前缀），如果没有正在执行名称空间处理，则为空字符串。
     * @param qName     - 限定的名称（带有前缀），如果限定的名称不可用，则为空字符串。
     */
    public void endElement(String uri, String localName, String qName)
            throws SAXException
    {
        key = null;
    }
}
