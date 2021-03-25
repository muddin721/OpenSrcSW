package simpleIR.main;

import java.io.*;
import java.util.*;
import org.snu.ids.kkma.index.*;

class Inverted implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2061212087328381724L;
	public ArrayList<InvertedElement> list = new ArrayList<InvertedElement>();
	
	@Override
	public String toString() { 
		StringBuilder sb = new StringBuilder();
		for(var element : list) {
			sb.append(element.toString());
		}
		return sb.toString();
	}
}

class InvertedElement implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2688140763939423573L;
	public int id;
	public double weight;
	
	public InvertedElement(int _id, double _weight) {
		id = _id;
		weight = _weight;
	} 
	
	@Override
	public String toString() {
		return String.format("id : %d weight : %.4f\n", id, weight); 
	}
}

public class indexer{

	public static int getTF(Keyword term, KeywordList doc) {

		if(doc.contains(term)) {
			int index = doc.indexOf(term);
			
			return doc.get(index).getCnt();
		}
		else return 0; 
	}
	
	public static int getDF(Keyword term, KeywordList[] docs) {
		int result = 0;
		for(int i = 0; i < docs.length; i++) {
			if(docs[i].contains(term)) {
				result++;
			}
		}
		return result;
	}
	
	public static double getWeight(Keyword term, int id, KeywordList[] docs) {
		double tf = getTF(term, docs[id]);
		double df = getDF(term, docs);
		
		return tf * Math.log((double)docs.length / df);
	}
	
	public static HashMap<String, Inverted> createHashMap(KeywordList[] kl){
		var result = new HashMap<String, Inverted>(); 

		KeywordList total = new KeywordList(new ArrayList<Keyword>());
		
		for(int i = 0; i < kl.length; i++) {  
			total.addAll(kl[i]);
		}
		
		for(int i = 0; i < total.size(); i++) {
			Inverted inverted = new Inverted();
			
			Keyword keyword = total.get(i);
			
			for(int j = 0; j < kl.length; j++) { 
				inverted.list.add(new InvertedElement(j, getWeight(keyword, j, kl)));
			}
			
			result.put(keyword.getString(), inverted);
		}
		
		return result;
	}
	
	public static void writeHashMap(String dir, HashMap<String, Inverted> map){
		
		try { 
			FileOutputStream fileStream = new FileOutputStream(dir);
			
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);
		
			objectOutputStream.writeObject(map);
			
			objectOutputStream.close();
			 
			System.out.println("POST 파일이 생성되었습니다.");
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
	}
}