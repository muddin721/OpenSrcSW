package simpleIR.main;

import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException; 

class IRElement{
	public String title = null;
	public String body = null;
	
	public IRElement() {
		super();
	}
	
	public IRElement(String _title, String _body) {
		title = _title;
		body = _body;
	}
}

public class makeCollection {

	//입력된 경로 하위 폴더에 있는 모든 HTML 파일의 객체를 반환합니다.
	public static File[] findHTMLFiles(String dir) {
		File file = new File(dir);
		
		return file.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".html");
			}
			
		});
	}
	  
	public static IRElement readHTMLFile(File file) {
		IRElement result = new IRElement();
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF8"))) {    
		    
				 StringBuilder sb = new StringBuilder();
				 
				 String line;
				 while ((line = reader.readLine()) != null) {
					 String str = line.trim();
					  
					 if(str.startsWith("<title>")) {
						 result.title = str.replace("<title>", "").replace("</title>", "");
					 }
					 else if(str.startsWith("<p>")){
						 sb.append(str.replace("<p>", "").replace("</p>", ""));
						 sb.append('\n');
					 }
				 } 
				 
				 result.body = sb.toString(); 
		}
		catch(FileNotFoundException ex) {
				System.err.println("파일을 찾을 수 없습니다.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		return result;
	}
	
	public static void addXMLElements(Document doc, IRElement[] data) { 
		Element docs = doc.createElement("docs");
		doc.appendChild(docs);
		
		for(int i = 0; i < data.length; i++) {
			Element docE = doc.createElement("doc");
			docs.appendChild(docE);
			
			docE.setAttribute("id", Integer.toString(i));
			
			Element title = doc.createElement("title");
			title.appendChild(doc.createTextNode(data[i].title));
			
			Element body = doc.createElement("body");
			body.appendChild(doc.createTextNode(data[i].body));
			
			docE.appendChild(title);
			docE.appendChild(body);
		}
	}
	
	public static void writeXMLFile(String dir, final IRElement[] data) {
		try{  
			DocumentBuilderFactory docFac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFac.newDocumentBuilder();
			
			Document doc = docBuilder.newDocument();
			
			//입력된 data를 XML 요소를 추가합니다.
			addXMLElements(doc, data); 
			
			//XML 파일을 저장합니다.
			TransformerFactory transFac = TransformerFactory.newInstance();
			
			Transformer trans = transFac.newTransformer();
			trans.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			
			Path pathToFile = Paths.get(dir);
			Files.createDirectories(pathToFile.getParent());
			
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new FileOutputStream(dir));
			
			trans.transform(source, result);
			 
			System.out.println("XML 파일이 생성되었습니다.");
			
			//
			
		}
		catch(FileNotFoundException ex) {
			System.err.println("파일을 쓸 수 없습니다.");
		}
		catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}

	public static IRElement[] readXMLFile(String dir) {
		
		var list = new ArrayList<IRElement>();
		
		try{  
			DocumentBuilderFactory docFac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFac.newDocumentBuilder();
			
			Document doc = docBuilder.parse(new File(dir));
			
			doc.getDocumentElement().normalize();
			   
			Element root = doc.getDocumentElement();
			
			NodeList nodeList = root.getChildNodes();
			
			for(int i = 0; i < nodeList.getLength(); i++){
				Node node = nodeList.item(i);
				if(node.getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element)node;
					 
					String title = e.getElementsByTagName("title").item(0).getTextContent();
					String body = e.getElementsByTagName("body").item(0).getTextContent();
					
					list.add(new IRElement(title, body));
				}
			}
				
			return list.toArray(new IRElement[list.size()]); 
		}
		catch(NullPointerException ex) {
			System.err.println("파일을 찾을 수 없습니다.");
		}
		catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			System.err.println("올바른 경로를 입력해주세요.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return null;
	}
}
