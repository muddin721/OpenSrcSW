package simpleIR.main;

import java.io.File;
import java.util.HashMap;

import org.snu.ids.kkma.index.KeywordList;

public class kuir {

	public static void main(String[] args) {
		
		if(args.length != 2) {
			System.out.println("인자가 올바르지 않습니다.");
			
			return;
		}
		
		switch(args[0]) {
		case "-c": 			
		{
			//HTML 파일을 불러들여 data로 저장합니다.
			File[] files = makeCollection.findHTMLFiles(args[1]);
			IRElement[] data = new IRElement[files.length]; 

			for(int i = 0; i < files.length; i++) {
				data[i] = makeCollection.readHTMLFile(files[i]);
			}
			
			makeCollection.writeXMLFile("./result/collection.xml", data);
		}
			break;
		case "-k":
		{
			IRElement[] data = makeCollection.readXMLFile(args[1]);
			
			KeywordList[] kl = new KeywordList[data.length];
			
			for(int i = 0; i < data.length; i++) {
				kl[i] = makeKeyword.extractKeyword(data[i].body);
			} 
  
			for(int i = 0; i < data.length; i++) { 
				data[i].body = makeKeyword.makeKLString(kl[i]);
			}
			
			makeCollection.writeXMLFile("./result/index.xml", data);
		}
			break;
			
		case "-i":  
		{
			IRElement[] data = makeCollection.readXMLFile(args[1]);
		
			KeywordList[] kl = new KeywordList[data.length];
		
			for(int i = 0; i < data.length; i++) {
				kl[i] = makeKeyword.parseKLString(data[i].body);
			} 
			
			HashMap<String, Inverted> hashMap = indexer.createHashMap(kl);
			
			for ( String key : hashMap.keySet() ) {
			    System.out.printf("key : %s\n%s", key, hashMap.get(key).toString());
			}
			
			indexer.writeHashMap("./result/index.post", hashMap); 			
		}
			break;
			
		default:
			System.out.println("인자가 올바르지 않습니다."); 
			return;
		}
	}

}
