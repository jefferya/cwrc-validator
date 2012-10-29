package cwrcxmlval.lib;

public class File  implements java.io.Serializable 
{
	private static final long serialVersionUID = -6247290934326030482L;
	
    private String mContent;
    private String mSchemaUrl;
    private String mSchemaType;

    public File() 
    {
    }
	
    public File(String Content, String SchemaUrl, String SchemaType)
    {
    	this.mContent = Content;
    	this.mSchemaUrl = SchemaUrl;
    	this.mSchemaType = SchemaType;
    }
    
    public String getContent()
    {
    	return this.mContent;
    }
    
    public void setContent(String Content)
    {
    	this.mContent = Content;
    }
    
    public String getSch()
    {
    	return this.mSchemaUrl;
    }
    
    public void setSch(String SchemaUrl)
    {
    	this.mSchemaUrl = SchemaUrl;
    }
    
    public String getType()
    {
    	return mSchemaType;
    }
    
    public void setType(String SchemaType)
    {
    	this.mSchemaType = SchemaType;
    }
}