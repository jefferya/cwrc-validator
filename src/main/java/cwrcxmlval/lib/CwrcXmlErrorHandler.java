package cwrcxmlval.lib;

import cwrcxmlval.lib.CwrcXmlContentHandler.CwrcPath;
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
        CwrcXmlContentHandler.CwrcPath parent = contentHandler.getLastParent();

        myErrorReport.addValidationError(CwrcValidationReport.TYPE_WARNING, e,
                parent != null ? parent.getName() : null, parent != null ? parent.getId() : null,
                contentHandler.getPath());
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        CwrcXmlContentHandler.CwrcPath parent = contentHandler.getLastParent();

        // Check message for xmlns error
        if(e.getMessage().matches("attribute \"xmlns.*\" not allowed here;.*")){
            return;
        }
        
        myErrorReport.addValidationError(CwrcValidationReport.TYPE_ERROR, e,
                parent != null ? parent.getName() : null, parent != null ? parent.getId() : null,
                contentHandler.getPath());
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        CwrcXmlContentHandler.CwrcPath parent = contentHandler.getLastParent();

        myErrorReport.addValidationError(CwrcValidationReport.TYPE_ERROR, e,
                parent != null ? parent.getName() : null, parent != null ? parent.getId() : null,
                contentHandler.getPath());

        throw new SAXException("A fatal error occurred.");
    }
}
