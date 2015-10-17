package com.rushikeshproj.phishing.model;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parameters {

	private static final String USER_AGENT = "Mozilla/5.0";
	private static final String WHOISAPI = 
										   //"https://www.whoisxmlapi.com/whoisserver/WhoisService?username=harry2015&password=12345678&outputFormat=json&domainName=";	
										   "http://www.whoisxmlapi.com/whoisserver/WhoisService?outputFormat=json&domainName=";
	
	private HashMap<String, Integer> mostFrequentTerms = new HashMap<>();
	private String TitleofWebpage = null;
	private Elements formURL = new Elements();
	private Elements imageURL = new Elements();
	private Elements hrefURL = new Elements();
	private List<String> copyRight = new ArrayList<>();
	private String WHOISregistrant = new String();
	private String WHOISnameServerDomain = new String();
	private List<String> embeddedDoamins = new ArrayList<>();
	private String hostName = null;
	private Boolean hasdomainsinhostname = false;
	private static JSONObject whoisjson;
	private String protocol = null;
	
	public Parameters(String weburl) {
		try {
			getWHOISsJsobObject(weburl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
 	
	public void findMostFrequentTerms(String webpageText) {
 		webpageText = Jsoup.parse(webpageText).text();
 		String[] words = webpageText.split("\\s+");
 		System.out.println("Lenght of words array :: " + words.length);
		for (String word : words) {
			if (!this.mostFrequentTerms.containsKey(word)) {
				this.mostFrequentTerms.put(word, 1);
			}
			else {
				this.mostFrequentTerms.put(word, this.mostFrequentTerms.get(word) + 1);
			}
		}
		
		this.mostFrequentTerms = (HashMap<String, Integer>) sortByComparator(this.mostFrequentTerms);
	}
	
	
 	
 	public void findTitleofURL(String webpageText) {
		Document doc = Jsoup.parse(webpageText);
		this.TitleofWebpage = doc.title();
	}
	
	
 	
 	public void findFormURLsofWebpage(String webpageText) {
		Document doc = Jsoup.parse(webpageText);
		this.formURL = doc.select("form");
        
		/*for (Element form : formURL) {
            print(" * Form actions: <%s> ", form.attr("action"));
        }*/
	}
	
	
 	
 	public void findImageURLofWebpage(String webpageText) {
		Document doc = Jsoup.parse(webpageText);
		this.imageURL = doc.select("[src]");
        
        /*for (Element src : imageURL) {
            if (src.tagName().equals("img"))
                print(" * %s: <%s> %sx%s (%s)",
                        src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
                        trim(src.attr("alt"), 20));
            else
                print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
        }*/
	}
	
	
 	
 	public void findhrefURLofWebpage(String webpageText) {
 		Document doc = Jsoup.parse(webpageText);
 		this.hrefURL = doc.select("a[href]");
		
 		/*for (Element link : hrefURL) {
            print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
        }*/
	}
	
	
 	
 	public void findcopyrightHolder(String webpageText) {
 		webpageText = Jsoup.parse(webpageText).text();
 		String[] words = webpageText.split("\\s+");
 		int index = 0;
 		this.copyRight.add("nothing");
 		for (String word : words) {
			if (word.equals("©") || word.equals("�")) {
				List<String> stringList = new ArrayList<String>(Arrays.asList(words));
				index = stringList.contains("©") ? Arrays.asList(words).indexOf("©") : Arrays.asList(words).indexOf("�"); 
				//Arrays.asList(words).indexOf("©");
				this.copyRight.add(words[index+1]);
		        if (words.length > (index + 2)) {
		        	this.copyRight.add(words[index+2]);
		        }
		        if (words.length > (index + 3)) {
		        	this.copyRight.add(words[index+3]);
		        }
		        if (words.length > (index + 4)) {
		        	this.copyRight.add(words[index+3]);
		        }
			}
		}
	}
	
	
 	
 	public void findregistrant(String weburl) {
		try {
			//getWHOISsJsobObject(weburl);
			if (whoisjson==null) {
				WHOISregistrant = null;
			}
			else {
				if (whoisjson.has("registrant")) {
					JSONObject registrant = whoisjson.getJSONObject("registrant");
					if (registrant.has("name")) {
						WHOISregistrant = registrant.getString("name") + ';';
					}
					
					if (registrant.has("organization")) {
						WHOISregistrant = WHOISregistrant + registrant.getString("organization");
					}
				}
				else if (whoisjson.has("registryData")) {
					JSONObject regdata = whoisjson.getJSONObject("registryData");
					if (regdata.has("registrant")) {
						JSONObject registrant = regdata.getJSONObject("registrant");
						if(registrant.has("name")) {
							WHOISregistrant = registrant.getString("name") + ';';
						}
						if (registrant.has("organization")) {
							WHOISregistrant = WHOISregistrant + registrant.getString("organization");
						}
					}
					else {
						WHOISregistrant = null;
					}
				}
				else {
					WHOISregistrant = null;
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
 	
 	public void findNameServerDomain(String weburl) {
 		try {
			//getWHOISsJsobObject(weburl);
			if (whoisjson==null) {
				WHOISnameServerDomain = null;
			}
			else {
				if (whoisjson.has("registryData")){
					JSONObject registryData = whoisjson.getJSONObject("registryData");
					if (registryData.has("nameServers")) {
						JSONObject nameServers = registryData.getJSONObject("nameServers");
						if (nameServers.has("hostNames")) {
							JSONArray hostnames = nameServers.getJSONArray("hostNames");
							WHOISnameServerDomain = hostnames.optString(0);
						}
						else {
							WHOISnameServerDomain = null;
						}
					}
					else {
						WHOISnameServerDomain = null;
					}
				}
				else {
					WHOISnameServerDomain = null;
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
 	
 	public void findEmbeddedDomain(String weburl) throws MalformedURLException {
 		int noofdomains = 1;
 		this.embeddedDoamins.add("nothing");
 		URL uri = new URL(weburl);
 		String path = uri.getPath();
 		//System.out.println("Path is  :: " + path);
 		String[] listofpaths = path.split("\\/");
 		for (String paths : listofpaths) {
 			if (!paths.isEmpty()) {
 				if ((paths.indexOf('.'))>0) {
 					this.embeddedDoamins.add(paths);
 					noofdomains++;
 				}
 			}
 		}
	}
	
 	public void findEmbeddedDomaininHostname(String weburl) throws URISyntaxException, MalformedURLException{
 		URL uri = new URL(weburl);
 		this.hostName = uri.getHost();
 		String[] listofhostname = this.hostName.split("\\.");
 		if (listofhostname.length > 5) {
 			this.hasdomainsinhostname = true;
 		}
 		URL url = new URL(weburl);
 		this.protocol = url.getProtocol();
 	}
 	
 	
 	public HashMap<String, Integer> getMostFrequentTerms() {
		return mostFrequentTerms;
	}



	public String getTitleofWebpage() {
		return TitleofWebpage;
	}



	public Elements getFormURL() {
		return formURL;
	}



	public Elements getImageURL() {
		return imageURL;
	}



	public Elements getHrefURL() {
		return hrefURL;
	}



	public List<String> getCopyRight() {
		return copyRight;
	}



	public String getWHOISregistrant() {
		return WHOISregistrant;
	}



	public String getWHOISnameServerDomain() {
		return WHOISnameServerDomain;
	}



	public List<String> getEmbeddedDoamins() {
		return embeddedDoamins;
	}



	public static JSONObject getWhoisjson() {
		return whoisjson;
	}


	public String getHostName() {
		return hostName;
	}



	public Boolean getHasdomainsinhostname() {
		return hasdomainsinhostname;
	}


	
	public String getProtocol() {
		return protocol;
	}

	
	
	private Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap) {

		// Convert Map to List
		List<Map.Entry<String, Integer>> list = 
			new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1,
                                           Map.Entry<String, Integer> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			
			}
		});

		// Convert sorted map back to a Map
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, Integer> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
	public static void printMap(Map<String, Integer> map) {
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			System.out.println("Key : " + entry.getKey() 
                                      + " Value : " + entry.getValue());
		}
	}
	
	private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }

    
    private static void getWHOISsJsobObject(String url) throws IOException {
    	
    	String requestURL = WHOISAPI + url;
		
		URL obj = new URL(requestURL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		//System.out.println("\nSending 'GET' request to URL : " + url);
		//System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		JSONObject myObject;
		
		try {
			myObject = new JSONObject(response.toString());
			
			if (myObject.has("dataError")|| myObject.has("ErrorMessage")) {
				whoisjson = null;
			}
			else {
				whoisjson = myObject.getJSONObject("WhoisRecord");
			}
			
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			//System.out.println("Here");
			e.printStackTrace();
		}
    }
}
