package cwrcxmlval.lib;

import java.io.IOException;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.util.StringUtils;
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
        StringBuilder builder = new StringBuilder("/");
        StringBuilder elementString = null;
        Map<String,Integer> elementList = new HashMap<String,Integer>();
        
        for (CwrcPath parent : path) {
            elementString = new StringBuilder(parent.name);
            

            if (parent.id != null) {
                elementString.append("[id=\"");
                elementString.append(parent.getId());
                elementString.append("\"]");
            }else if(elementList.containsKey(parent.name)){
                Integer value = elementList.get(parent.name);
                
                if(value > -1){
                    elementString.append("[");
                    elementString.append(value + 1);
                    elementString.append("]");
                }
            }

            builder.append(elementString);
            builder.append("/");
            elementList = parent.getElementList();
        }

        if (lastPath != null && !lastPath.name.startsWith("/")) { 
            StringBuilder lastString = new StringBuilder(lastPath.name);

            if (lastPath.id != null) {
                lastString.append("[id=\"");
                lastString.append(lastPath.getId());
                lastString.append("\"]");
            }else {
                Integer value = 0;
                
                if(elementList.containsKey(lastPath.name)){
                    value = elementList.get(lastPath.name);
                    value = value + 1;
                }
                
                if(value > -1){
                    lastString.append("[");
                    lastString.append(value + 1);
                    lastString.append("]");
                }
            }
            
            // JCA & MRB: Tue 20-Jan-2015: added lastString and elementString not equal to
            // to null to statement below as a workaround for the Java null pointer exception
            // error that was being thrown if those variables were null
            if(lastString != null && elementString != null && lastString.toString().compareTo(elementString.toString()) != 0){
                builder.append(lastString);
            }else{
                builder.deleteCharAt(builder.length() - 1);
            }
        }else{
            builder.deleteCharAt(builder.length() - 1);
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

        @Override
        public boolean equals(Object o) {
            if(o instanceof CwrcPath){
                CwrcPath test = (CwrcPath)o;
                if(this.name.compareTo(test.name) == 0){
                    if(this.id != null){
                        if(test.id != null && this.id.compareTo(test.id) == 0){
                            return true;
                        }
                    }else if(test.id == null){
                        return true;
                    }
                }
            }
            
            return false;
        }
    }
}
