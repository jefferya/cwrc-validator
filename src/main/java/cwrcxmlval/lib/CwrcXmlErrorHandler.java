package cwrcxmlval.lib;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class CwrcXmlErrorHandler extends DefaultHandler {

    private CwrcValidationReport myErrorReport;
    private CwrcXmlContentHandler contentHandler;

    public CwrcXmlErrorHandler(CwrcValidationReport errorReport, CwrcXmlContentHandler contentHandler) {
        myErrorReport = errorReport;
        this.contentHandler = contentHandler;
    }

    @Override
    public void warning(SAXParseException e) throws SAXException {
        CwrcXmlContentHandler.CwrcPath element = contentHandler.getElement(e);

        myErrorReport.addValidationError(CwrcValidationReport.TYPE_WARNING, e, element, contentHandler.getPath(element));
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        CwrcXmlContentHandler.CwrcPath element = contentHandler.getElement(e);
 
        // Check message for xmlns error
        if(e.getMessage().startsWith("attribute \"xmlns\" not allowed here;")){
            return;
        }
        
        myErrorReport.addValidationError(CwrcValidationReport.TYPE_WARNING, e, element, contentHandler.getPath(element));
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        CwrcXmlContentHandler.CwrcPath element = contentHandler.getElement(e);

        myErrorReport.addValidationError(CwrcValidationReport.TYPE_WARNING, e, element, contentHandler.getPath(element));

        throw new SAXException("A fatal error occurred.");
    }
}
