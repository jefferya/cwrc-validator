package cwrcxmlval.lib;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.*;

public class CwrcValidationReport {

    private Document myReport;
    int myErrorCount;
    public static String TYPE_ERROR = "error";
    public static String TYPE_WARNING = "warning";

    public CwrcValidationReport() {
        myReport = CreateReport();
        myErrorCount = 0;
    }

    protected Document CreateReport() {
        try {
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element root = doc.createElement("validation-result");
            root.appendChild(doc.createElement("status"));

            doc.appendChild(root);
            return doc;
        } catch (Exception e) {
            return null;
        }
    }

    public Document GetReport() {
        Element root = myReport.getDocumentElement();
        Element status_element = (Element) root.getElementsByTagName("status").item(0);
        if (status_element.getChildNodes().getLength() == 0) {
            String status = "pass";
            if ((root.getElementsByTagName(TYPE_ERROR).getLength() > 0) || (root.getElementsByTagName(TYPE_WARNING).getLength() > 0)) {
                status = "fail";
            }

            status_element.appendChild(myReport.createTextNode(status));
        }

        return myReport;
    }

    private Element createElementWithTextNode(String elementTag, int textContent) {
        Element element = myReport.createElement(elementTag);
        element.appendChild(myReport.createTextNode(Integer.toString(textContent)));
        return element;
    }

    private Element createElementWithTextNode(String elementTag, String textContent) {
        Element element = myReport.createElement(elementTag);
        element.appendChild(myReport.createTextNode(textContent));
        return element;
    }

    public void addValidationError(String type, SAXParseException e, String parent, String parentId, String path) throws SAXException {
        Element entry = myReport.createElement(type);

        //Adding line number
        entry.appendChild(this.createElementWithTextNode("line", e.getLineNumber()));

        //Adding column number
        entry.appendChild(this.createElementWithTextNode("column", e.getColumnNumber()));

        //Adding the error message
        entry.appendChild(this.createElementWithTextNode("message", e.getMessage()));

        // Adding the parent elements id
        if (parent != null) {
            entry.appendChild(this.createElementWithTextNode("parent", parent));
            entry.appendChild(this.createElementWithTextNode("parentId", parentId));
        }

        if (path != null) {
            entry.appendChild(this.createElementWithTextNode("path", path));
        }

        //Adding the error entry to the document root.
        myReport.getDocumentElement().appendChild(entry);

        ++myErrorCount;

        if (myErrorCount >= 100) {
            throw new SAXException("Too many errors");
        }
    }

    public void addGeneralError(String type, String messageText) {
        Element entry = myReport.createElement(type);

        //Adding the error message
        entry.appendChild(this.createElementWithTextNode("message", messageText));

        //Adding the error entry to the document root.
        myReport.getDocumentElement().appendChild(entry);

        ++myErrorCount;

    }
}
