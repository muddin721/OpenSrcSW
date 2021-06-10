package program;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class NaverAPI {

	private String id, secret;
	
	public String getId() {
		return id;
	}

	public String getSecret() {
		return secret;
	}

	public NaverAPI(String id, String secret) {
		this.id = id;
		this.secret = secret;
	}
	
	public String getMovieData(String query) {
		String result = null;
		
		try { 
			String apiURL = "https://openapi.naver.com/v1/search/movie.json?query=" + URLEncoder.encode(query, "UTF-8");
			  
			HttpURLConnection con = (HttpURLConnection)new URL(apiURL).openConnection();
			
			con.setRequestMethod("GET");
			con.setRequestProperty("X-Naver-Client-Id", id);
			con.setRequestProperty("X-Naver-Client-Secret", secret);
		
			int responseCode = con.getResponseCode();
			
			InputStream inputStream = responseCode == 200 ? con.getInputStream() : con.getErrorStream();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			
			String inputLine;
			StringBuffer response = new StringBuffer();
			while((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}

			br.close();
			
			result = response.toString(); 
		} catch (IOException e) { 
			e.printStackTrace();
		}
			
		return result;
	}
	
}
