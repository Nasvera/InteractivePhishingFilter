package com.rushikeshproj.phishing.model;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

public class WebpageRead {
	
	private String webPageCode; 
	
	public void getUrlSource(String url) throws IOException {
		
		if (this.checkLiveURL(url)) {
			URL yahoo = new URL(url);
	        URLConnection yc = yahoo.openConnection();
	        BufferedReader in = new BufferedReader(new InputStreamReader(
	                yc.getInputStream(), "UTF-8"));
	        String inputLine;
	        StringBuilder a = new StringBuilder();
	        while ((inputLine = in.readLine()) != null)
	            a.append(inputLine);
	        in.close();
	        
	        this.webPageCode = a.toString();
		}
		else {
			//System.out.println("Exception Passed ");
			this.webPageCode = null;
		}
    }
	
	private Boolean checkLiveURL(String url) {
		URL u = null;
		int code = 0;
		try {
			u = new URL (url);
			HttpURLConnection huc = null;
			huc = ( HttpURLConnection )  u.openConnection ();
			huc.setRequestMethod ("GET");
			huc.connect () ;
			code = huc.getResponseCode();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//System.out.println("URL Malformed");
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//System.out.println("Something going wrong");
			return false;
		} 
		//System.out.println("Response Code is  :: " + code);
		if (code==200) {
			return true;
		}
		return false;
	}
	
	public String getWebPage() {
		return this.webPageCode;
	}
}
