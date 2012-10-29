package cwrcxmlval.lib;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class CwrcXmlErrorHandler extends DefaultHandler 
{
	private CwrcValidationReport myErrorReport;
	
	public CwrcXmlErrorHandler(CwrcValidationReport errorReport)
	{
		myErrorReport = errorReport;
	}
	
	public void warning(SAXParseException e) throws SAXException 
	{
		myErrorReport.addValidationError(CwrcValidationReport.TYPE_WARNING, e);
	}

	public void error(SAXParseException e) throws SAXException
	{
		myErrorReport.addValidationError(CwrcValidationReport.TYPE_ERROR, e);
	}
	 
	public void fatalError(SAXParseException e) throws SAXException
	{
		myErrorReport.addValidationError(CwrcValidationReport.TYPE_ERROR, e);
		throw new SAXException("A fatal error occurred.");	
	}
}
