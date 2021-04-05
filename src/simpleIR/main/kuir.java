package simpleIR.main;

import java.io.File;
import java.util.*;

import org.snu.ids.kkma.index.KeywordList;

public class kuir {
	
	public static void parseArgument(String arg, List<String> list) {

		int sIndex = 0;
		boolean quotes = false;
		
		for(int i = 0; i < arg.length(); i++) {
			if(quotes) {
				if(arg.charAt(i) == '\"') {
					if(sIndex != i) {
						list.add(arg.substring(sIndex, i)); 
						
						quotes = false;

						sIndex = i + 1;
					} 
				}
			}
			else {
				if(arg.charAt(i) == '\"') { 
					if(i == 0 || (i > 0 && arg.charAt(i - 1) == ' ')) { 
						quotes = true;
						
						sIndex = i + 1;
					}
				}
				
				if(arg.charAt(i) == ' ' || i == arg.length() - 1) { 
					if(i == arg.length() - 1) i++;
					
					if(sIndex != i) {
						list.add(arg.substring(sIndex, i)); 
					} 

					sIndex = i + 1;
				}	 
			}
		}   
	}
	
	public static void main(String[] args) {
		
		var opts = new ArrayList<String>();
		
		if(args.length != 0) {
			opts.addAll(Arrays.asList(args));
		}
		else {
			System.out.print("인자를 입력해주세요 : ");
			
			Scanner scanner = new Scanner(System.in);
			 
			String arg = scanner.nextLine(); 
			
			parseArgument(arg, opts);
			  
			scanner.close();
		} 
		
		try { 
			switch(opts.get(0)) {
			
			case "-c": 			
			{
				System.out.println("==================================================");
				System.out.println("명령어");
				System.out.println("==================================================");
				System.out.println("-c {HTML 폴더 경로}");
				System.out.println("==================================================");
				
				//HTML 파일을 불러들여 data로 저장합니다.
				File[] files = makeCollection.findHTMLFiles(opts.get(1));
				
				if(files.length == 0) {
					System.out.println("파일을 찾을 수 없습니다. 올바른 경로를 입력해주세요!");
					break;
				}
				
				IRElement[] data = new IRElement[files.length]; 

				for(int i = 0; i < files.length; i++) {
					data[i] = makeCollection.readHTMLFile(files[i]);
				}
				
				makeCollection.writeXMLFile("./result/collection.xml", data);
				
				break;
			}
			
			case "-k":
			{
				System.out.println("==================================================");
				System.out.println("명령어");
				System.out.println("==================================================");
				System.out.println("-k {collection.xml 경로}");
				System.out.println("==================================================");
				
				IRElement[] data = makeCollection.readXMLFile(opts.get(1));
				
				KeywordList[] kl = new KeywordList[data.length];
				
				for(int i = 0; i < data.length; i++) {
					kl[i] = makeKeyword.extractKeyword(data[i].body);
				} 
	  
				for(int i = 0; i < data.length; i++) { 
					data[i].body = makeKeyword.makeKLString(kl[i]);
				}
				
				makeCollection.writeXMLFile("./result/index.xml", data);

				break;
			}
			
			case "-i":  
			{
				System.out.println("==================================================");
				System.out.println("명령어");
				System.out.println("==================================================");
				System.out.println("-i {index.xml 경로}");
				System.out.println("==================================================");
				
				IRElement[] data = makeCollection.readXMLFile(opts.get(1));
			
				KeywordList[] kl = new KeywordList[data.length];
			
				for(int i = 0; i < data.length; i++) {
					kl[i] = makeKeyword.parseKLString(data[i].body);
				} 
				
				HashMap<String, Inverted> hashMap = indexer.createHashMap(kl);
				
				for ( String key : hashMap.keySet() ) {
				    System.out.printf("%s : %s\n", key, hashMap.get(key).toString());
				}
				
				indexer.writeHashMap("./result/index.post", hashMap); 			
				
				break;
			}   
				
			case "-s":  
			{   
				System.out.println("==================================================");
				System.out.println("명령어");
				System.out.println("==================================================");
				System.out.println("-s {폴더(파일) 경로} -q \"쿼리\"");
				System.out.println("==================================================");
				if(opts.get(2).equals("-q")) { 
					File file = new File(opts.get(1));
					
					String path;
					
					if(file.isDirectory()) {
						path = file.getPath();
					}
					else {
						path = file.getParent();
					}
					
					System.out.println(path);
					searcher.CalcSim(path + "/collection.xml", path + "/index.post", opts.get(3));
				} 
				
				break;
			} 
			
			default:
				System.err.println("인자가 올바르지 않습니다.");  
			}
		}
		catch(IndexOutOfBoundsException e) { 
			System.err.println("인자 수가 너무 많거나 적습니다.");  
		}
		
	}

}