 package PACK;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
@WebServlet("/READINGDATA_FROM_DATABASE_AND_CONVERTING_TOXML")
public class READINGDATA_FROM_DATABASE_AND_CONVERTING_TOXML extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		try { 
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    Document doc = builder.newDocument();
		    Element results = doc.createElement("rplayground");
		    doc.appendChild(results);
		    Class.forName("org.postgresql.Driver");
			 Connection con=DriverManager.getConnection("jdbc:postgresql://38.242.196.35:35777/extio_db","extio_user","7Aw$S(5+7aKF0R6p");
	        ResultSet rs = con.createStatement().executeQuery("select * from playground");
            ResultSetMetaData rsmd = rs.getMetaData();
		    int colCount = rsmd.getColumnCount();
             while (rs.next()) {
		      Element row = doc.createElement("DATA_RECORD");
		      results.appendChild(row);
		      for (int i = 1; i <= colCount; i++) {
		        String columnName = rsmd.getColumnName(i);
		        Object value = rs.getObject(i);
		        Element node = doc.createElement(columnName);
		        node.appendChild(doc.createTextNode(value.toString()));
		        row.appendChild(node); 
		      }
		    }
		    DOMSource domSource = new DOMSource(doc);
		    TransformerFactory tf = TransformerFactory.newInstance();
		    Transformer transformer = tf.newTransformer();
		    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		    transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
		    StringWriter sw = new StringWriter();
		    StreamResult sr = new StreamResult(sw);
		    transformer.transform(domSource, sr);

		    out.println(sw.toString());

		    con.close();
		    rs.close();}
		catch(Exception e) {e.printStackTrace();}
		  }
		}

		/*SELECT CONCAT('<playground>',
'<DATA_RECORD>',
'<EQUIP_ID>', equip_id , '</EQUIP_ID>',
'<TYPE>', type, '</TYPE>',
'<COLOR>', color, '</COLOR>',
'<LOCATION>', location , '</LOCATION>',
'<INSTALL_DATE>', install_date , '</INSTALL_DATE>',
'</DATA_RECORD>') ,
'</playground>'playground_xml
FROM playground  ;*/