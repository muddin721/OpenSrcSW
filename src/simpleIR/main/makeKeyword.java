package simpleIR.main;

import java.util.ArrayList;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;

public class makeKeyword { 
	public static KeywordList extractKeyword(String body) {
		if(body.isBlank()) {
			System.err.println("문자열이 비어있습니다.");
			return null;
		}
		
		KeywordExtractor ke = new KeywordExtractor();
		
		return ke.extractKeyword(body, true);
	}
	
	public static String makeKLString(KeywordList kl) {   
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
	
	public static KeywordList parseKLString(String str) {
		KeywordList kl = new KeywordList(new ArrayList<Keyword>());
		
		String[] elements = str.split("#");
		 
		for(String element : elements) {
			if(element.isBlank()) continue;
			
			String[] value = element.split(":");
			
			Keyword keyword = new Keyword();
			keyword.setString(value[0]);
			keyword.setCnt(Integer.parseInt(value[1]));
			kl.add(keyword);
		}
		
		return kl;
	}
}
