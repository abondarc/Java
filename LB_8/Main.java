import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

class NewsArticle {
    private String title;
    private String description;
    private String pubDate;

    public NewsArticle(String title, String description, String pubDate) {
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}

public class XMLParser {

    public static void main(String[] args) {
        String url = "http://naviny.by/rss/alls.xml";

        // Parsing with DOM
        List<NewsArticle> domArticles = parseWithDOM(url);
        System.out.println("DOM Parser Result:");
        for (NewsArticle article : domArticles) {
            System.out.println(article.getTitle());
        }

        // Parsing with SAX
        List<NewsArticle> saxArticles = parseWithSAX(url);
        System.out.println("SAX Parser Result:");
        for (NewsArticle article : saxArticles) {
            System.out.println(article.getTitle());
        }

        // Parsing with StAX
        List<NewsArticle> staxArticles = parseWithStAX(url);
        System.out.println("StAX Parser Result:");
        for (NewsArticle article : staxArticles) {
            System.out.println(article.getTitle());
        }
    }

    public static List<NewsArticle> parseWithDOM(String url) {
        List<NewsArticle> articles = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new URL(url).openStream());

            NodeList itemNodes = document.getElementsByTagName("item");

            for (int i = 0; i < itemNodes.getLength(); i++) {
                Element itemElement = (Element) itemNodes.item(i);
                String title = getElementText(itemElement, "title");
                String description = getElementText(itemElement, "description");
                String pubDate = getElementText(itemElement, "pubDate");

                NewsArticle article = new NewsArticle(title, description, pubDate);
                articles.add(article);
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return articles;
    }

    private static String getElementText(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        Element tagElement = (Element) nodeList.item(0);
        return tagElement.getTextContent();
    }

    public static List<NewsArticle> parseWithSAX(String url) {
        List<NewsArticle> articles = new ArrayList<>();

        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            InputStream inputStream = new URL(url).openStream();
            XMLStreamReader reader = factory.createXMLStreamReader(inputStream);

            NewsArticle article = null;
            String currentElement = "";
            StringBuilder content = new StringBuilder();

            while (reader.hasNext()) {
                int event = reader.next();

                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        currentElement = reader.getLocalName();
                        if (currentElement.equals("item")) {
                            article = new NewsArticle("", "", "");
                        }
                        content.setLength(0);
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        content.append(reader.getText());
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        if (currentElement.equals("item")) {
                            articles.add(article);
                        } else if (currentElement.equals("title")) {
                            article.setTitle(content.toString());
                        } else if (currentElement.equals("description")) {
                            article.setDescription(content.toString());
                        } else if (currentElement.equals("pubDate")) {
                            article.setPubDate(content.toString());
                        }
                        break;
                }
            }
        } catch (XMLStreamException | IOException e) {
           I apologize for the incomplete code. Here's the rest of the program:

```java
            e.printStackTrace();
        }

        return articles;
    }

    public static List<NewsArticle> parseWithStAX(String url) {
        List<NewsArticle> articles = new ArrayList<>();

        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            InputStream inputStream = new URL(url).openStream();
            XMLStreamReader reader = factory.createXMLStreamReader(inputStream);

            NewsArticle article = null;
            String currentElement = "";
            StringBuilder content = new StringBuilder();

            while (reader.hasNext()) {
                int event = reader.next();

                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        currentElement = reader.getLocalName();
                        if (currentElement.equals("item")) {
                            article = new NewsArticle("", "", "");
                        }
                        content.setLength(0);
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        content.append(reader.getText());
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        if (currentElement.equals("item")) {
                            articles.add(article);
                        } else if (currentElement.equals("title")) {
                            article.setTitle(content.toString());
                        } else if (currentElement.equals("description")) {
                            article.setDescription(content.toString());
                        } else if (currentElement.equals("pubDate")) {
                            article.setPubDate(content.toString());
                        }
                        break;
                }
            }
        } catch (XMLStreamException | IOException e) {
            e.printStackTrace();
        }

        return articles;
    }
}