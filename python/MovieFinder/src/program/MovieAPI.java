package program;

import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
  
public class MovieAPI {

	public static String parseJSON(String json) throws ParseException {
		StringBuilder sb = new StringBuilder();
		
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject)jsonParser.parse(json);
		JSONArray infoArray = (JSONArray)jsonObject.get("items");
		for(int i = 0; i < infoArray.size(); i++) {
			sb.append("=item"+i+"=============================\n");
			JSONObject itemObject = (JSONObject)infoArray.get(i);
			sb.append("title:\t"+itemObject.get("title") + "\n");
			sb.append("subtitle:\t"+itemObject.get("subtitle") + "\n");
			sb.append("director:\t"+itemObject.get("director") + "\n");
			sb.append("actor:\t"+itemObject.get("actor") + "\n");
			sb.append("userRating:\t"+itemObject.get("userRating") + "\n");
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("영화 검색어를 입력해주세요 : ");
		
		String query = scanner.nextLine();
		 
		NaverAPI naverAPI = new NaverAPI("M3DxQhOjos46uEEXXdC4", "aLvU2Rb4ec");
		try {
			System.out.println(parseJSON(naverAPI.getMovieData(query)));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		scanner.close();
	}

}
