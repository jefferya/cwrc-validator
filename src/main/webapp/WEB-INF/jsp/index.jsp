<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
   <%@page contentType="text/html" pageEncoding="UTF-8"%>
   <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
      "http://www.w3.org/TR/html4/loose.dtd">
  
   <html>
       <head>
           <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
           <link rel=StyleSheet href="standard.css" type="text/css"/>
       </head>
       <body>
       		<h1>CWRC XML Validator API</h1>
       		<h2>Conditions:</h2>
       		<ul>
       			<li>Schema should be specified with a publicly accessible URL</li>
       			<li>Entity declarations should either be merged within the XML document or should be referred to using publicly accessible URLs</li> 
       		</ul>
       		
       		<h2>Synopsis:</h2>
       		<h4>POST</h4>
				Submit an HTTP POST request with following variables: <br />
				<span class="code">sch</span>: URL to schema<br />
				<span class="code">type</span>: Schema Type<br />		
				<span class="code">content</span>: XML content to be validated<br />
       		<h4>GET</h4>
       		<div class="code">[Base URL]/validate.html?sch=[Schema URL]&type=[Schema type]&doc=[Document URL]</div>
      			
       		<h2>Output:</h2>
       		<div class="code">
       			&lt;validation-result&gt;<br/>
       			&nbsp;&nbsp;&nbsp;&lt;status&gt;pass|fail&lt;/status&gt;<br/>
       			&nbsp;&nbsp;&nbsp;&lt;error&gt;<br/>
       			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;line&gt;integer&lt;/line&gt;<br/>
       			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;column&gt;integer&lt;/column&gt;<br/>
       			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;message&gt;string&lt;/message&gt;<br/>
       			&nbsp;&nbsp;&nbsp;&lt;/error&gt;<br/>
       			&nbsp;&nbsp;&nbsp;&lt;warning&gt;<br/>
       			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;line&gt;integer&lt;/line&gt;<br/>
       			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;column&gt;integer&lt;/column&gt;<br/>
       			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;message&gt;string&lt;/message&gt;<br/>
       			&nbsp;&nbsp;&nbsp;&lt;/warning&gt;<br/>
       			&nbsp;&nbsp;&nbsp;... <br/>
       			&lt;/validation-result&gt;
       		</div>
       		<div>
       			Note that column number specifies end position of element tag containing an error, not the exact position of the error.<br/>
       			For example in a case of an incorrect attribute, the column number tells us where the end of the element tag which contains the attribute, not the position of the attribute.<br/>
       		</div>
       		
       		<h2>Try It:</h2>
       		<h3>POST</h3>
           	<%
	       		String type = "RNG_XML";
           		String schema_url = "http://dev.onlineresearchcanada.ca/orlando-bio-tight/schemas/biography-test_10-07-16-CAPS.rng";
           		String document_url = "http://dev.onlineresearchcanada.ca/orlando-bio-tight/elinor_glyn-bio.xml";
           		String s = "validate.html?sch=" + schema_url + "&type=" + type + "&doc=" + document_url; 
           	%>

   			<form action="validate.html" target="_blank" method="post">
   				Schema URL: <input type="text" name="sch" value = "<% out.print(schema_url);%>" style="width:680px;"/> <br />
   				Schema Type: <input type="text" name="type" value = "<% out.print(type);%>" style="width:100px;"/> <br />
   				Document Content: <br/ >
   				<textarea name="content" style="width:100%;height:480px">
   					<% 
   						java.net.URL url = new java.net.URL(document_url);
   						java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(url.openStream()));
   						String inputLine;
   						while ((inputLine = in.readLine()) != null)
   							out.println(inputLine);
   						in.close();   					
   					%>
   				</textarea><br />
   				<input type="submit" />
   			</form>
       		
       		<h3>GET</h3>
       		
       		<h4>Valid Documents with Orlando Tight Schemas:</h4>

           	<%
	       		type = "RNG_XML";
           		schema_url = "http://dev.onlineresearchcanada.ca/orlando-bio-tight/schemas/biography-test_10-07-16-CAPS.rng";
           		document_url = "http://dev.onlineresearchcanada.ca/orlando-bio-tight/elinor_glyn-bio.xml";
           		s = "validate.html?sch=" + schema_url + "&type=" + type + "&doc=" + document_url; 
           	%>
           	
      		<table>
           		<tr><td>Schema</td><td><a href="<% out.print(schema_url);%>" target="_balnk"><% out.print(schema_url);%></a></td></tr>
           		<tr><td>Document</td><td><a href="<% out.print(document_url);%>" target="_balnk"><% out.print(document_url);%></a></td></tr>
           		<tr><td>Test</td><td><a href="<% out.print(s);%>" target="_balnk"><% out.print(s);%></a></td></tr>
       		</table>
           	
           	
           	<%
	       		type = "RNG_XML";
           		schema_url = "http://dev.onlineresearchcanada.ca/orlando-bio-tight/schemas/biography-test_10-07-16-CAPS.rng";
           		document_url = "http://dev.onlineresearchcanada.ca/orlando-bio-tight/gladys_henrietta_schutze-bio.xml";
           		s = "validate.html?sch=" + schema_url + "&type=" + type + "&doc=" + document_url; 
           	%>
           	
      		<table>
           		<tr><td>Schema</td><td><a href="<% out.print(schema_url);%>" target="_balnk"><% out.print(schema_url);%></a></td></tr>
           		<tr><td>Document</td><td><a href="<% out.print(document_url);%>" target="_balnk"><% out.print(document_url);%></a></td></tr>
           		<tr><td>Test</td><td><a href="<% out.print(s);%>" target="_balnk"><% out.print(s);%></a></td></tr>
       		</table>
           	
           
           	<%
	       		type = "RNG_XML";
           		schema_url = "http://dev.onlineresearchcanada.ca/orlando-bio-tight/schemas/biography-test_10-07-16-CAPS.rng";
           		document_url = "http://dev.onlineresearchcanada.ca/orlando-bio-tight/rosita_forbes-bio.xml";
           		s = "validate.html?sch=" + schema_url + "&type=" + type + "&doc=" + document_url; 
           	%>
           	
      		<table>
           		<tr><td>Schema</td><td><a href="<% out.print(schema_url);%>" target="_balnk"><% out.print(schema_url);%></a></td></tr>
           		<tr><td>Document</td><td><a href="<% out.print(document_url);%>" target="_balnk"><% out.print(document_url);%></a></td></tr>
           		<tr><td>Test</td><td><a href="<% out.print(s);%>" target="_balnk"><% out.print(s);%></a></td></tr>
       		</table>
           	
			
			<h4>Valid Documents with Orlando Looser Schemas:</h4>
			
           	<%
	       		type = "RNG_COMP";
           		schema_url = "http://dev.onlineresearchcanada.ca/orlando-looser-schemas/schemas/orlando_bio_and_writing.rnc";
           		document_url = "http://dev.onlineresearchcanada.ca/orlando-looser-schemas/margaret_laurence_entry.xml";
           		s = "validate.html?sch=" + schema_url + "&type=" + type + "&doc=" + document_url; 
           	%>
      		<table>
           		<tr><td>Schema</td><td><a href="<% out.print(schema_url);%>" target="_balnk"><% out.print(schema_url);%></a></td></tr>
           		<tr><td>Document</td><td><a href="<% out.print(document_url);%>" target="_balnk"><% out.print(document_url);%></a></td></tr>
           		<tr><td>Test</td><td><a href="<% out.print(s);%>" target="_balnk"><% out.print(s);%></a></td></tr>
       		</table>
   
 			<h4>Invalid Documents with Orlando Looser Schemas:</h4>
			           	
           	<%
	       		type = "RNG_COMP";
           		schema_url = "http://dev.onlineresearchcanada.ca/orlando-looser-schemas/schemas/orlando_bio_and_writing.rnc";
           		document_url = "http://dev.onlineresearchcanada.ca/orlando-looser-schemas/lady_mary_montagu_entry.xml";
           		s = "validate.html?sch=" + schema_url + "&type=" + type + "&doc=" + document_url; 
           	%>
      		<table>
           		<tr><td>Schema</td><td><a href="<% out.print(schema_url);%>" target="_balnk"><% out.print(schema_url);%></a></td></tr>
           		<tr><td>Document</td><td><a href="<% out.print(document_url);%>" target="_balnk"><% out.print(document_url);%></a></td></tr>
           		<tr><td>Test</td><td><a href="<% out.print(s);%>" target="_balnk"><% out.print(s);%></a></td></tr>
       		</table>
   
            	
          	<%
	       		type = "RNG_COMP";
           		//schema_url = "http://dev.onlineresearchcanada.ca/orlando-looser-schemas/schemas/orlando_writing_transitional_medium_rare_upper_v04.rnc";
           		//schema_url = "http://dev.onlineresearchcanada.ca/orlando-looser-schemas/schemas/orlando_biograph_transitional_medium_rare_upper_v04.rnc";
	       		schema_url = "http://dev.onlineresearchcanada.ca/orlando-looser-schemas/schemas/orlando_bio_and_writing.rnc";
           		document_url = "http://dev.onlineresearchcanada.ca/orlando-looser-schemas/lady_morgan_sydney_owenson_entry.xml";
           		s = "validate.html?sch=" + schema_url + "&type=" + type + "&doc=" + document_url; 
           	%>
           	
      		<table>
           		<tr><td>Schema</td><td><a href="<% out.print(schema_url);%>" target="_balnk"><% out.print(schema_url);%></a></td></tr>
           		<tr><td>Document</td><td><a href="<% out.print(document_url);%>" target="_balnk"><% out.print(document_url);%></a></td></tr>
           		<tr><td>Test</td><td><a href="<% out.print(s);%>" target="_balnk"><% out.print(s);%></a></td></tr>
       		</table>
   
           	<%
	       		type = "RNG_COMP";
           		schema_url = "http://dev.onlineresearchcanada.ca/orlando-looser-schemas/schemas/orlando_bio_and_writing.rnc";
           		document_url = "http://dev.onlineresearchcanada.ca/orlando-looser-schemas/l_s_bevington_entry.xml";
           		s = "validate.html?sch=" + schema_url + "&type=" + type + "&doc=" + document_url; 
           	%>
      		<table>
           		<tr><td>Schema</td><td><a href="<% out.print(schema_url);%>" target="_balnk"><% out.print(schema_url);%></a></td></tr>
           		<tr><td>Document</td><td><a href="<% out.print(document_url);%>" target="_balnk"><% out.print(document_url);%></a></td></tr>
           		<tr><td>Test</td><td><a href="<% out.print(s);%>" target="_balnk"><% out.print(s);%></a></td></tr>
       		</table>
 
       </body>
   </html>