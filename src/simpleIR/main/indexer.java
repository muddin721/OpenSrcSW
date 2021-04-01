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
		
		sb.append("[ ");
		for(int i = 0; i < list.size(); i++) {
			sb.append(list.get(i).toString());
			
			if(i != list.size() - 1) {
				sb.append(", ");
			}
		}
		sb.append(" ]");
		return sb.toString();
	}
}

class InvertedElement implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2688140763939423573L;
	public int id;
	public float weight;
	
	public InvertedElement(int _id, float _weight) {
		id = _id;
		weight = _weight;
	} 
	
	@Override
	public String toString() {
		return String.format("%d : %.2f", id, weight); 
	}
}

public class indexer{

	public static int find(Keyword term, KeywordList doc) {
		for(int i = 0; i < doc.size(); i++) {
			if(doc.get(i).getString().equals(term.getString())) {
				return i;
			}
		}
		return -1;
	}
	
	public static float getTF(Keyword term, KeywordList doc) { 
		int index = find(term, doc);
		
		if (index != -1) return doc.get(index).getCnt();
		else return 0.0f;
	}
	
	public static float getDF(Keyword term, KeywordList[] docs) {
		int result = 0;
		for(int i = 0; i < docs.length; i++) {
			if(find(term, docs[i]) != -1) {
				result++;
			} 
		} 
		return (float)Math.log(docs.length / (double)result);
	}
	
	public static float getWeight(Keyword term, int id, KeywordList[] docs) {
		float tf = getTF(term, docs[id]);
		float df = getDF(term, docs);
		
		return tf * df;
	}
	
	public static HashMap<String, Inverted> createHashMap(final KeywordList[] kl){
		var result = new HashMap<String, Inverted>(); 

		KeywordList total = new KeywordList(new ArrayList<Keyword>()); 
		for(int i = 0; i < kl.length; i++) {  
			total.addAll(kl[i]);
		}
		
		for(int i = 0; i < total.size(); i++) {
			Inverted inverted = new Inverted();
			
			Keyword keyword = total.get(i); 
			
			for(int j = 0; j < kl.length; j++) { 
				float weight = getWeight(keyword, j, kl);
				if(weight != 0.0f) { 
					inverted.list.add(new InvertedElement(j, weight)); 
				}
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
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, Inverted> readHashMap(String dir){
		HashMap<String, Inverted> result = null;
		
		try { 
			FileInputStream fileStream = new FileInputStream(dir);
			
			ObjectInputStream objectOutputStream = new ObjectInputStream(fileStream);
		
			result = (HashMap<String, Inverted>)objectOutputStream.readObject();
			
			objectOutputStream.close(); 
		}
		catch(IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) { 
			e.printStackTrace();
		}
		
		return result;
	}
}