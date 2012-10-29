package cwrcxmlval.lib;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.google.common.base.CharMatcher;


public class CwrcXmlValidator
{
	public Document ValidateDocInUrl(String schemaUrl, String documentUrl, String schemaType)
	{
		StreamSource document = new StreamSource(documentUrl);
		return Validate(schemaUrl, document, schemaType);
	}

	public Document ValidateDocContent(String schemaUrl, String documentContent, String schemaType)
	{
		documentContent = CharMatcher.WHITESPACE.trimFrom(documentContent);
		StringReader reader = new StringReader(documentContent);
		StreamSource document = new StreamSource(reader);
		return Validate(schemaUrl, document, schemaType);
	}
	
	public Document Validate(String schemaUrl, StreamSource document, String schemaType)
	{
		CwrcValidationReport error_report = new CwrcValidationReport();
		CwrcXmlErrorHandler error_handler = new CwrcXmlErrorHandler(error_report);
		try 
		{
	        // 1. Specify you want a factory for RELAX NG
			// ==========================================
			
			if(schemaType.equals("RNG_XML"))
			{
		        // For XML-based Relax NG Schema
		        System.setProperty(SchemaFactory.class.getName() + ":" + XMLConstants.RELAXNG_NS_URI, "com.thaiopensource.relaxng.jaxp.XMLSyntaxSchemaFactory");
			}
			else if(schemaType.equals("RNG_COMP"))
			{
				// For compact form of Relax NG schema
		        System.setProperty(SchemaFactory.class.getName() + ":" + XMLConstants.RELAXNG_NS_URI, "com.thaiopensource.relaxng.jaxp.CompactSyntaxSchemaFactory");
			}
	        
	        // Build the schema factory
	        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.RELAXNG_NS_URI);
			
	        // 2. Compile the schema
	        // =====================
	        URL schemaLocation = new URL(schemaUrl);
	        Schema schema = factory.newSchema(schemaLocation);
	        
	        // 3. Get a validator from the schema
	        // =================================
	        Validator validator = schema.newValidator();
	        validator.setErrorHandler(error_handler);
	        
	        
	        // 4. Validate the document
	        // ========================
	        validator.validate(document);
	        
		}
		catch (MalformedURLException e) 
		{
			error_report.addGeneralError(CwrcValidationReport.TYPE_ERROR, e.getMessage());
			return error_report.GetReport();
		}
		catch (SAXException e) 
		{
			return error_report.GetReport();
		}
		catch (IOException e) 
		{
			error_report.addGeneralError(CwrcValidationReport.TYPE_ERROR, e.getMessage());
			return error_report.GetReport();
		}		
		
		return error_report.GetReport();  		
	}
	
	
}
