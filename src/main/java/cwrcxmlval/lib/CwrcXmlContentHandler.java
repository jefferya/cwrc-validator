package cwrcxmlval.lib;

import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author mpm1
 */
public class CwrcXmlContentHandler extends DefaultHandler{

    private Stack<CwrcPath> path = new Stack<CwrcPath>();
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        
        String name = localName;
        String id = null;
        int total = attributes.getLength();
        
        for(int index = 0; index < total; ++index){
            if(attributes.getLocalName(index).toUpperCase().equals("ID")){
                id = attributes.getValue(index);
                break;
            }
        }
        
        path.push(new CwrcPath(name, id));
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        
        path.pop();
    }
    
    public CwrcPath getLastParent(){
        for(int index = path.size() - 1; index >= 0; --index){
            CwrcPath parent = path.elementAt(index);
            if(parent.getId() != null){
                return parent;
            }
        }
        
        return null;
    }
    
    public String getPath(){
        StringBuilder builder = new StringBuilder();
        
        for(CwrcPath parent : path){
            builder.append(parent.name);
            
            if(parent.id != null){
                builder.append("[id=\"");
                builder.append(parent.getId());
                builder.append("\"]");
            }
            
            builder.append("/");
        }
        
        if(builder.length() > 0){
            return builder.substring(0, builder.length() - 1);
        }
        
        return null;
    }
    
    public class CwrcPath {
        private String name, id;
        
        public CwrcPath(String name, String id){
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }
    }
}
