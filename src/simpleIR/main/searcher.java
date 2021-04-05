package simpleIR.main;

import java.util.*;

import org.snu.ids.kkma.index.*;

public class searcher {
	public static void CalcSim(String dir1, String dir2, String query) {
		 var col = makeCollection.readXMLFile(dir1);
		 var data = indexer.readHashMap(dir2);
		 KeywordList kl = makeKeyword.extractKeyword(query);
 
		 class Result implements Comparable<Result>{
			 public String title;
			 public float dot;
			 
			 public Result(String _title) {
				 this(_title, 0.0f);
			 }
			 
			 public Result(String _title, float _dot){
				 title = _title;
				 dot = _dot;
			 }
			 
			@Override
			public int compareTo(Result o) {
				// TODO Auto-generated method stub
				return -Float.compare(dot, o.dot);
			}
		 } 
		 
		 var result = new ArrayList<Result>();
		 
		 for(int i = 0; i < col.length; i++) {
			 result.add(new Result(col[i].title));
			 
			 float a = 0, b = 0;
			 
			 for(Keyword keyword : kl) {  
				 if(data.containsKey(keyword.getString())) {
					 Inverted inverted = data.get(keyword.getString());
					 
					 for(var element : inverted.list) {
						 if(element.id == i) { 
							 result.get(i).dot += 1f * element.weight;
							 
							 a += element.weight * element.weight;
							 b += 1 * 1f;
							 
							 break;
						 }
					 }
				 } 
			 }
			 
			 float p = (float) (Math.sqrt(a) *  Math.sqrt(b));
			 
			 if(p!= 0) {
				 result.get(i).dot /= p;
			 }
		 }  
		 
		 Collections.sort(result);
		 
		 for(int i = 0; i < Math.min(col.length, 3); i++) {
			 System.out.printf("title : %s, result : %.2f\n", result.get(i).title, result.get(i).dot);
		 }
	}
}