package conversion;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class writes all the paper records
 * into the model.
 * 
 * Since there seems to be no more suitable
 * vocabulary set for the paper dataset,
 * I'm using DC_11.
 * @author Haoyuan Huang
 *
 */
public class XMLToRDFUtils {
	
	public static final int MAX_RETRIEVE = Integer.MAX_VALUE;
	
	public static Model parse (String pathToFile, Model model) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(pathToFile);

			NodeList nodeList = document.getDocumentElement().getChildNodes();
			int elementCount = 0;
			for (int i = 0; i < nodeList.getLength() && elementCount < MAX_RETRIEVE; i ++) {
				Node node = nodeList.item(i);
				if (node instanceof Element) {
					elementCount++;
					Resource articleResource = model.createResource();
					StringBuilder detailDescription = new StringBuilder();
					StringBuilder yearMonth = new StringBuilder();
					
					// Get metadata stored as attributes
					detailDescription.append( "mdate: " + node.getAttributes().getNamedItem("mdate").getNodeValue() );
					detailDescription.append( ", key: " + node.getAttributes().getNamedItem("key")  .getNodeValue() );

					// Get all other metadata stored as child nodes
					NodeList childList = node.getChildNodes();
					for (int j = 0; j < childList.getLength(); j ++) {
						Node cNode = childList.item(j);
						if (cNode instanceof Element) {
							String content = cNode.getLastChild().
									getTextContent().trim();
							switch (cNode.getNodeName()) {
							   case  "author"    :    
								   articleResource.addProperty(DC_11.creator, content); break;
							   case  "editor"    :
								   articleResource.addProperty(DC_11.contributor, content); break;
							   case  "title"     :   
								   articleResource.addProperty(DC_11.title, content); break;
							   case  "booktitle" :
								   articleResource.addProperty(DC_11.subject, content); break;
							   case  "year"      : 
							   case  "month"     : 
								   yearMonth.append(cNode.getNodeName() + ": " + content + ";"); break;
							   case  "pages"     : 
							   case  "address"   : 
							   case  "journal"   : 
							   case  "volume"    : 
							   case  "number"    : 
							   case  "ee"        : 
							   case  "cdrom"     : 
							   case  "cite"      :   
							   case  "note"      :
							   case  "crossref"  :
							   case  "isbn"      :
							   case  "series"    :
							   case  "school"    :
							   case  "chapter"   :
								   detailDescription.append(cNode.getNodeName() + ": " + content + ";"); break;
							   case  "url"       :  
								   articleResource.addProperty(DC_11.identifier, content); break;
							   case  "publisher" :
								   articleResource.addProperty(DC_11.publisher, content); break;
							}				
						}
					}
					articleResource.addProperty(DC_11.date, yearMonth.toString());
					articleResource.addProperty(DC_11.description, detailDescription.toString());
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
			return null;
		}
		return model;
	}
}
