package javaTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
 
 
public class getSnippet { 
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		if(args.length == 4) {
			if(!(args[0].equals("-f") && args[2].equals("-q"))) {
				return;
			}
			
			String filePath = args[1];
	
			String fileContents = read(new File("./" + filePath));
			
			String[] input = args[3].split(" ");
			
			String[] contents = fileContents.split("\n");
			
			int[] count = new int[contents.length];
			
			var set = new ArrayList<HashSet<String>>();
			
			for(int i = 0; i < contents.length; i++) {
				if(contents[i].isBlank()) {
					break;
				}
				
				count[i] = 0;
				
				String[] key = contents[i].split(" ");
				
				set.add(new HashSet<String>());
				
				for(int j = 0; j < key.length; j++) {
					set.get(i).add(key[i]);
				}
				
				for(int j = 0; j < input.length; j++) {
					if(set.get(i).contains(input[j])) { 
						count[i]++;
					}
				}
			}
			
			int maxIndex = 0;
			for(int i = 0; i < count.length; i++) {
				if(count[maxIndex] <= count[i]) {
					maxIndex = i;
				}
			}
			 
			System.out.println(contents[maxIndex]);
		}
	}
	
	public static String read(File file) { 
		StringBuilder sb = new StringBuilder(); 
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF8"))) {    
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			sb.append(reader.readLine());
		}
		catch(FileNotFoundException ex) {
				System.err.println("파일을 찾을 수 없습니다.");
		}
		catch (IOException e) {
			e.printStackTrace();
		}  
		
		return sb.toString();
	}
}
