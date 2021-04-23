package cwrcxmlval.lib;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.google.common.base.CharMatcher;
import java.io.Reader;
import java.io.StringReader;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class CwrcXmlValidator {

    public Document ValidateDocInUrl(String schemaUrl, String documentUrl, String schemaType) {
        StreamSource document = new StreamSource(documentUrl);
        return Validate(schemaUrl, document.getReader(), schemaType);
    }

    public Document ValidateDocContent(String schemaUrl, String documentContent, String schemaType) {
        documentContent = CharMatcher.whitespace().trimFrom(documentContent);
        //StringReader reader = new StringReader(documentContent);
        //StreamSource document = new StreamSource(reader);
        StringReader document = new StringReader(documentContent);
        return Validate(schemaUrl, document, schemaType);
    }

    public Document Validate(String schemaUrl, Reader document, String schemaType) {
        CwrcValidationReport error_report = new CwrcValidationReport();
        CwrcXmlContentHandler content_handler = new CwrcXmlContentHandler();
        CwrcXmlErrorHandler error_handler = new CwrcXmlErrorHandler(error_report, content_handler);
        
        CwrcXmlInputStream data = new CwrcXmlInputStream(document);
        content_handler.setReader(data);
        
        //content_handler.setReader(document);
        
        try {
            // 1. Specify you want a factory for RELAX NG
            // ==========================================

            if (schemaType.equals("RNG_XML")) {
                // For XML-based Relax NG Schema
                System.setProperty(SchemaFactory.class.getName() + ":" + XMLConstants.RELAXNG_NS_URI, "com.thaiopensource.relaxng.jaxp.XMLSyntaxSchemaFactory");
            } else if (schemaType.equals("RNG_COMP")) {
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
            //Validator validator = schema.newValidator();
            //validator.setErrorHandler(error_handler);

            // 4. Setup the parser
            //====================
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            parserFactory.setValidating(false);
            parserFactory.setNamespaceAware(true);
            parserFactory.setSchema(schema);
            
            SAXParser parser = parserFactory.newSAXParser();
            
            XMLReader reader = parser.getXMLReader();
            reader.setErrorHandler(error_handler);
            reader.setContentHandler(content_handler);
            
            
            
            // 4. Validate the document
            // ========================
            reader.parse(new InputSource(data));

        } catch (MalformedURLException e) {
            error_report.addGeneralError(CwrcValidationReport.TYPE_ERROR, e.getMessage());
            return error_report.GetReport();
        } catch (SAXException e) {
            return error_report.GetReport();
        } catch (IOException e) {
            error_report.addGeneralError(CwrcValidationReport.TYPE_ERROR, e.getMessage());
            return error_report.GetReport();
        } catch (ParserConfigurationException e){
            e.printStackTrace();
        }

        return error_report.GetReport();
    }
}
