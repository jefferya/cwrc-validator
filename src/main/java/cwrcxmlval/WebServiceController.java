package cwrcxmlval;

import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import cwrcxmlval.lib.*;
import org.w3c.dom.Document;

@Controller
public class WebServiceController 
{
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public void index()
    {
    	//TODO: Give some end-user documentation on how to use the service in the index.jsp file.
    }
	
    @RequestMapping(value = "/validate", method = RequestMethod.GET)
    public void validate(@RequestParam String sch, @RequestParam String doc, @RequestParam String type, Model model)
    {
    	//Instantiate CWRC XML validator
    	CwrcXmlValidator validator = new CwrcXmlValidator();
    	
    	//Instantiate a Validator object
    	//CwrcValidator validator = CwrcValidatorFactory.NewValidator("CwrcJavaXmlValidator");
    	
    	//Do the validation and obtain results.
    	Document report = validator.ValidateDocInUrl(sch, doc, type);
    	
    	//Exporting the validation-result DOM object into a string.
    	String xml;
    	try
    	{
	    	TransformerFactory factory = TransformerFactory.newInstance();
	    	Transformer transformer = factory.newTransformer();
	    	
	    	StringWriter writer = new StringWriter();
	    	StreamResult result = new StreamResult(writer);
	    	Source source = new DOMSource(report.getDocumentElement());
	    	transformer.transform(source, result);
	    	writer.close();
	    	xml = writer.toString();    	
		}
    	catch (Exception e) 
    	{
    		xml = "<validation-result><status>fail</status><error><message>" + e.getMessage() + "</message></error></validation-result>";
    	}
    	
    	//Export results for printing
        model.addAttribute("result", xml);
    }
    
    @RequestMapping(value="/validate", method=RequestMethod.POST)
    public void file(File file, BindingResult resultx, HttpServletRequest request, Model model)
    {
    	
    	//Instantiate CWRC XML validator
    	CwrcXmlValidator validator = new CwrcXmlValidator();
    	
    	//Instantiate a Validator object
    	//CwrcValidator validator = CwrcValidatorFactory.NewValidator("CwrcJavaXmlValidator");
    	
    	//Do the validation and obtain results.
    	Document report = validator.ValidateDocContent(file.getSch(), file.getContent(), file.getType());
    	
    	//Exporting the validation-result DOM object into a string.
    	String xml;
    	try
    	{
	    	TransformerFactory factory = TransformerFactory.newInstance();
	    	Transformer transformer = factory.newTransformer();
	    	
	    	StringWriter writer = new StringWriter();
	    	StreamResult result = new StreamResult(writer);
	    	Source source = new DOMSource(report.getDocumentElement());
	    	transformer.transform(source, result);
	    	writer.close();
	    	xml = writer.toString();    	
		}
    	catch (Exception e) 
    	{
    		xml = "<validation-result><status>fail</status><error><message>" + e.getMessage() + "</message></error></validation-result>";
    	}
    	
    	//Export results for printing
        model.addAttribute("result", xml);
    }    
}
