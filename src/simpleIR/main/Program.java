package simpleIR.main; 

import java.io.*;
import java.util.Scanner;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.snu.ids.kkma.index.*;

class HTMLElement{
	public String title = null;
	public String body = null;
	
	public HTMLElement() {
		super();
	}
}

public class Program {
  
	public static String extractKeyword(String body) {  
		KeywordExtractor ke = new KeywordExtractor();
		
		KeywordList kl = ke.extractKeyword(body, true);
		
		 StringBuilder sb = new StringBuilder();
		 
		for(int i = 0; i < kl.size(); i++) {
			Keyword kwrd = kl.get(i);
			
			sb.append(kwrd.getString());
			sb.append(":"); 
			sb.append(kwrd.getCnt()); 
			sb.append("#");
		}
		
		return sb.toString();
	}
	
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
	  
	public static HTMLElement readHTMLFile(File file) {
		HTMLElement result = new HTMLElement();
		
		try {  
				 Scanner scanner = new Scanner(file);
				 
				 StringBuilder sb = new StringBuilder();
				 
				 while(scanner.hasNextLine()) { 
					 String str = scanner.nextLine().trim();
					  
					 if(str.startsWith("<title>")) {
						 result.title = str.replace("<title>", "").replace("</title>", "");
					 }
					 else if(str.startsWith("<p>")){
						 sb.append(str.replace("<p>", "").replace("</p>", ""));
						 sb.append('\n');
					 }
				 }
				 
				 result.body = sb.toString();
				 
				 scanner.close();
		}
		catch(FileNotFoundException ex) {
				System.out.println("파일을 찾을 수 없습니다.");
		}  
		
		return result;
	}
	
	public static void addXMLElements(Document doc, HTMLElement[] data) { 
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
	
	public static void writeXMLFile(String dir, String filename, final HTMLElement[] data) {
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
			
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new FileOutputStream(dir + "/" + filename + ".xml"));
			
			trans.transform(source, result);
			//
			
		}
		catch(FileNotFoundException ex) {
			System.out.println("파일을 찾을 수 없습니다.");
		}
		catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("경로를 입력해주세요 : ");
		Scanner scanner = new Scanner(System.in);
		
		String dir = scanner.nextLine();
		
		//HTML 파일을 불러들여 data로 저장합니다.
		File[] files = findHTMLFiles(dir);
		HTMLElement[] data = new HTMLElement[files.length]; 
		

		for(int i = 0; i < files.length; i++) {
			data[i] = readHTMLFile(files[i]);
		}
		
		writeXMLFile(dir, "collection", data);
		
		for(int i = 0; i < files.length; i++) {
			data[i].body = extractKeyword(data[i].body);
		}
		
		writeXMLFile(dir, "index", data);
		
		System.out.println("XML 파일이 생성되었습니다.");
		
		scanner.close();
	}

}
