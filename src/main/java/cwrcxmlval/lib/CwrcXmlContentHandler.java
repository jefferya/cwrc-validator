package cwrcxmlval.lib;

import java.io.IOException;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author mpm1
 */
public class CwrcXmlContentHandler extends DefaultHandler {

    private Stack<CwrcPath> path = new Stack<CwrcPath>();
    private Locator locator;
    private CwrcXmlInputStream reader;

    @Override
    public void setDocumentLocator(Locator locator) {
        super.setDocumentLocator(locator);
        this.locator = locator;
    }

    public void setReader(CwrcXmlInputStream reader) {
        this.reader = reader;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        String name = localName;
        String id = null;
        int total = attributes.getLength();

        for (int index = 0; index < total; ++index) {
            String attr = attributes.getLocalName(index);
            if (attr.toUpperCase().equals("ID")) {
                id = attributes.getValue(index);
            }
        }

        CwrcPath cwrcPath = null;
        try{
            cwrcPath = path.peek();
            cwrcPath.pushElement(localName);
        }catch(EmptyStackException ex){
            
        }
                
        cwrcPath = new CwrcPath(name, id);
        path.push(cwrcPath);

        super.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        path.pop();
    }

    private String getLastElement(int line, int column) {
        String lineString = null;
        try {
            lineString = reader.getLine(line);
        } catch (IOException ex) {
            Logger.getLogger(CwrcXmlContentHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lineString;
    }

    public CwrcPath getElement(SAXParseException e) {

        String lastElement = getLastElement(e.getLineNumber(), e.getColumnNumber());
        
        int index = -1;
        for(index = e.getColumnNumber() - 2; index > -1; --index){
            if(lastElement.charAt(index) == '<'){
                break;
            }
        }
        
        if(index > -1){
            String test = lastElement.substring(index + 1);
            String element = test;
            int var = test.indexOf(' ');
            
            // Remove closing triangular bracket
            int check = test.indexOf('>');
            if(var < 0){
                if(check > -1){
                    var = check;
                }
            }else if(check > -1 && check < var){
                var = check;
            }
            
            // Find the element name.
            if(var > 0){
                element = test.substring(0, var);
                
                /*if(element.startsWith("/")){
                    element = element.substring(1);
                }*/
            }
            
            // Find the element ID.
            String id = null;
            int idIndex = test.indexOf("ID=");
            if(idIndex < 0){
                idIndex = test.indexOf("id=");
            }
            
            if(idIndex > -1){
                id = test.substring(idIndex + 4);
                id = id.substring(0, id.indexOf("\""));
            }
            
            return new CwrcPath(element, id);
        }

        return null;
    }

    public CwrcPath getLastParent() {
        for (int index = path.size() - 1; index >= 0; --index) {
            CwrcPath parent = path.elementAt(index);
            if (parent.getId() != null) {
                return parent;
            }
        }

        return null;
    }

    public String getPath(CwrcPath lastPath) {
        StringBuilder builder = new StringBuilder();
        Map<String,Integer> elementList = new HashMap<String,Integer>();
        
        for (CwrcPath parent : path) {
            builder.append(parent.name);

            if (parent.id != null) {
                builder.append("[id=\"");
                builder.append(parent.getId());
                builder.append("\"]");
            }else if(elementList.containsKey(parent.name)){
                Integer value = elementList.get(parent.name);
                
                if(value > -1){
                    builder.append("[");
                    builder.append(value.toString());
                    builder.append("]");
                }
            }

            builder.append("/");
            elementList = parent.getElementList();
        }

        if (lastPath != null && !lastPath.name.startsWith("/")) {            
            builder.append(lastPath.name);

            if (lastPath.id != null) {
                builder.append("[id=\"");
                builder.append(lastPath.getId());
                builder.append("\"]");
            }else {
                Integer value = 0;
                
                if(elementList.containsKey(lastPath.name)){
                    value = elementList.get(lastPath.name);
                    value = value + 1;
                }
                
                if(value > -1){
                    builder.append("[");
                    builder.append(value.toString());
                    builder.append("]");
                }
            }
        }

        return builder.toString();
    }

    public class CwrcPath {

        private String name, id;
        private Attributes attributes;
        private Map<String, Integer> elementList = new HashMap<String, Integer>();

        public CwrcPath(String name, String id) {
            this.name = name;
            this.id = id;
            //this.attributes = attributes;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }
        
        public Map<String, Integer> getElementList(){
            return Collections.unmodifiableMap(elementList);
        }
        
        public int pushElement(String elementName){
            if(elementList.containsKey(elementName)){
                Integer value = elementList.get(elementName);
                ++value;
                elementList.put(elementName, value);
                return value;
            }else{
                elementList.put(elementName, 0);
            }
            
            return 0;
        }
    }
}
