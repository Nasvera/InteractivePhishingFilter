package com.rushikeshproj.phishing.model;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.json.simple.parser.JSONParser;
import org.json.JSONException;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import info.debatty.java.stringsimilarity.QGram;

public class IsPhishing {
	
	
	private static final String IPADDRESS_PATTERN = 
			"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	
	
	

	Double FrequentTermsvsRegistrant ;
	Integer NOofDomainNamecandidatesinHostnameandContent; 
	Integer NOofDomainNamecandidatesinHostname; 
	Boolean NumericHostname ;
	Double TitlevsRegistrant;
	Double TitlevsDomainNamecandidatesinHostname;
	Double FrequentTermsvsHostDomain; 
	Double CopyrightvsRegistrant;
	Double DomainNamecandidatesinAnchorsvsRegistrant;
	Boolean isPhishing;
	
	public Boolean isPhishing(String websiteurl) throws IOException, URISyntaxException {
		// TODO Auto-generated method stub
		String url = websiteurl;//"http://examples.javacodegeeks.com/core-java/crypto/encrypt-decrypt-string-with-des/";
		WebpageRead pageread = new WebpageRead();
		pageread.getUrlSource(url);
		String webpagesource = pageread.getWebPage();
		
		if (webpagesource==null) {
			System.out.println("URL is not working. Please try with different URL");
			return false;
		}
		
		System.out.println("Web page source is  :: " + webpagesource);
		
		Parameters website = new Parameters(url);
		website.findMostFrequentTerms(webpagesource);
		HashMap<String, Integer> freqTerms = website.getMostFrequentTerms();
		/*for (Map.Entry<String, Integer> entry : freqTerms.entrySet()) {
			System.out.println("Key : " + entry.getKey() 
                                      + " Value : " + entry.getValue());
			//i++;
		}*/
		
		
		website.findTitleofURL(webpagesource);
		String titleofurl = website.getTitleofWebpage();
		//System.out.println("Title of the url is :: " + titleofurl);
		
		website.findFormURLsofWebpage(webpagesource);
		Elements pageurls = website.getFormURL();
		/*for (Element form : pageurls) {
            System.out.println(form.attr("action"));
        }*/
		
		website.findImageURLofWebpage(webpagesource);
		Elements imageurls = website.getImageURL();
		/*for (Element image : imageurls) { 
			if (image.tagName().equals("img")) {
				System.out.println(image.attr("abs:src"));
			}
		}*/
		
		website.findhrefURLofWebpage(webpagesource);
		Elements hrefs = website.getHrefURL();
		/*for (Element href : hrefs) {
            System.out.println(href.attr("abs:href"));
        }*/
		
		website.findregistrant(url);
		String registrant = website.getWHOISregistrant();
		//System.out.println("Registrant Name :: " + registrant);
		
		website.findNameServerDomain(url);
		String nameserverdomain = website.getWHOISnameServerDomain();
		//System.out.println("Name Server domain is :: " + nameserverdomain);
		
		website.findcopyrightHolder(webpagesource);
		List<String> copyrightholders = website.getCopyRight();
		/*if (copyrightholders.size()>1) {
			for (String holder : copyrightholders) {
				System.out.println(holder);
			}
		}*/
		
		
		website.findEmbeddedDomain(url);
		List<String> embeddedDomains = website.getEmbeddedDoamins();
		/*if (embeddedDomains.size() > 1) {
			for (String domain : embeddedDomains) {
				System.out.println(domain);
			}
		}*/
		
		website.findEmbeddedDomaininHostname(url);
		String hostname = website.getHostName();
		Boolean hasdomaininHostname = website.getHasdomainsinhostname();
		/*System.out.println("Hostname is :: " + hostname);
		System.out.println("Has domains embedded in hostname :: " + hasdomaininHostname);*/
		
		
		StrongIDParameteres para1 = new StrongIDParameteres(website);
		
		/*System.out.println("Value :: " + para1.getprotocol());
		System.out.println("Value :: " + para1.getfreqTermsVSRegistrant());
		System.out.println("Value :: " + para1.getnoOfDomainNameCandiInHostnameAndContent());
		System.out.println("Value :: " + para1.getnoOfDomainNameCandiInHostname());
		System.out.println("Value :: " + para1.isNumericdomainName());
		System.out.println("Value :: " + para1.getTextRelavanceTitleVSRegistrant());
		System.out.println("Value :: " + para1.getTextRelavanceTitleVSHostanme());
		System.out.println("Value :: " + para1.getTextRelavanceFreqTermsVShostdomain());
		System.out.println("Value :: " + para1.getTextRelavanceCopyRightVSRegistrant());
		System.out.println("Value :: " + para1.getTextRelavanceAnchorsVSRegistrant());*/
		
		this.protocol = para1.getprotocol();
		this.FrequentTermsvsRegistrant = para1.getfreqTermsVSRegistrant(); 
		this.NOofDomainNamecandidatesinHostnameandContent = para1.getnoOfDomainNameCandiInHostnameAndContent(); 
		this.NOofDomainNamecandidatesinHostname  = para1.getnoOfDomainNameCandiInHostname();
		this.NumericHostname  = para1.isNumericdomainName();
		this.TitlevsRegistrant  = para1.getTextRelavanceTitleVSRegistrant();
		this.TitlevsDomainNamecandidatesinHostname = para1.getTextRelavanceTitleVSHostanme(); 
		this.FrequentTermsvsHostDomain  = para1.getTextRelavanceFreqTermsVShostdomain();
		this.CopyrightvsRegistrant  = para1.getTextRelavanceCopyRightVSRegistrant();
		this.DomainNamecandidatesinAnchorsvsRegistrant = para1.getTextRelavanceAnchorsVSRegistrant();
		
		String written_content1 = 
			    protocol + "," 
			  + FrequentTermsvsRegistrant + ","
			  + NOofDomainNamecandidatesinHostnameandContent + ","
			  + NOofDomainNamecandidatesinHostname + ","
			  + NumericHostname + ","
			  + TitlevsRegistrant + ","
			  + TitlevsDomainNamecandidatesinHostname + ","
			  + FrequentTermsvsHostDomain + ","
			  + CopyrightvsRegistrant + ","
			  + DomainNamecandidatesinAnchorsvsRegistrant + ","
			  + "?\n";
		System.out.println(written_content1);
		
		FileWrite obj1 = new FileWrite();
		obj1.arfffilewriter(written_content1,"/Users/rushikesh/Documents/workspace/Test_Rushikesh/src/main/resources/phishing_pred.arff");
		
		MachineLearningAlgo ladTree = new MachineLearningAlgo();
		String result = ladTree.predictresult("/Users/rushikesh/Documents/workspace/Test_Rushikesh/src/main/resources/phishing_f.arff", "/Users/rushikesh/Documents/workspace/Test_Rushikesh/src/main/resources/phishing_pred.arff");
		System.out.println("Value is :: " + result);
		this.isPhishing = Boolean.valueOf(result);
		
		return true;
		
//		System.out.println("Result is :: " + result);
//		System.out.println("Do you agree (y/n)?");
//		
//		BufferedReader br = 
//                new BufferedReader(new InputStreamReader(System.in));
//		
//		String input;
//		
//		written_content1 =  "\n" 
//				  + protocol + "," 
//				  + FrequentTermsvsRegistrant + ","
//				  + NOofDomainNamecandidatesinHostnameandContent + ","
//				  + NOofDomainNamecandidatesinHostname + ","
//				  + NumericHostname + ","
//				  + TitlevsRegistrant + ","
//				  + TitlevsDomainNamecandidatesinHostname + ","
//				  + FrequentTermsvsHostDomain + ","
//				  + CopyrightvsRegistrant + ","
//				  + DomainNamecandidatesinAnchorsvsRegistrant + ",";
//		
//		input = br.readLine();
//		if (input.equals("y")) {
//			written_content1 += result;
//			obj1.updatearffFileWriter(written_content1, "phishing_f.arff");
//		}
//		else {
//			written_content1 += !(Boolean.valueOf(result));
//			obj1.updatearffFileWriter(written_content1, "phishing_f.arff");
//		}
		
		/*String csvFile = "/Users/rushikesh/Documents/workspace/JavaSrc/bin/Data/verified_online-parte_non_phishing.csv";///Users/rushikesh/Documents/workspace/JavaSrc/src/Data/verified_online-2.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		String written_content = "";
		String written_content_2 = "";
		int count = 0;
		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

			        // use comma as separator
				String[] attribute = line.split(cvsSplitBy);

				//System.out.println("Url is " + attribute[1]);
				String url = attribute[1];
				System.out.println(url);
				url = url.replaceAll("\\s+","");
				WebpageRead pageread = new WebpageRead();
				pageread.getUrlSource(url);
				String webpagesource = pageread.getWebPage();
				
				if (webpagesource==null) {
					//System.out.println(url);
					System.out.println("Not Working");
				}
				else {
					Parameters website = new Parameters(url);
					website.findMostFrequentTerms(webpagesource);
					website.findcopyrightHolder(webpagesource);
					website.findEmbeddedDomain(url);
					website.findEmbeddedDomaininHostname(url);
					website.findFormURLsofWebpage(webpagesource);
					website.findhrefURLofWebpage(webpagesource);
					website.findImageURLofWebpage(webpagesource);
					website.findNameServerDomain(url);
					website.findregistrant(url);
					website.findTitleofURL(webpagesource);
					
					StrongIDParameteres para1 = new StrongIDParameteres(website);
					String protocol = para1.getprotocol();
					Double FrequentTermsvsRegistrant = para1.getfreqTermsVSRegistrant(); 
					Integer NOofDomainNamecandidatesinHostnameandContent = para1.getnoOfDomainNameCandiInHostnameAndContent(); 
					Integer NOofDomainNamecandidatesinHostname  = para1.getnoOfDomainNameCandiInHostname();
					Boolean NumericHostname  = para1.isNumericdomainName();
					Double TitlevsRegistrant  = para1.getTextRelavanceTitleVSRegistrant();
					Double TitlevsDomainNamecandidatesinHostname = para1.getTextRelavanceTitleVSHostanme(); 
					Double FrequentTermsvsHostDomain  = para1.getTextRelavanceFreqTermsVShostdomain();
					Double CopyrightvsRegistrant  = para1.getTextRelavanceCopyRightVSRegistrant();
					Double DomainNamecandidatesinAnchorsvsRegistrant = para1.getTextRelavanceAnchorsVSRegistrant(); 
					String phising  = "false";
					
					String written_content1 = 
											    protocol + "," 
											  + FrequentTermsvsRegistrant + ","
											  + NOofDomainNamecandidatesinHostnameandContent + ","
											  + NOofDomainNamecandidatesinHostname + ","
											  + NumericHostname + ","
											  + TitlevsRegistrant + ","
											  + TitlevsDomainNamecandidatesinHostname + ","
											  + FrequentTermsvsHostDomain + ","
											  + CopyrightvsRegistrant + ","
											  + DomainNamecandidatesinAnchorsvsRegistrant + ","
											  + phising + "\n";
					
					String written_content_21 =       url + ","
											  +	protocol + "," 
											  + FrequentTermsvsRegistrant + ","
											  + NOofDomainNamecandidatesinHostnameandContent + ","
											  + NOofDomainNamecandidatesinHostname + ","
											  + NumericHostname + ","
											  + TitlevsRegistrant + ","
											  + TitlevsDomainNamecandidatesinHostname + ","
											  + FrequentTermsvsHostDomain + ","
											  + CopyrightvsRegistrant + ","
											  + DomainNamecandidatesinAnchorsvsRegistrant + ","
											  + phising + "\n";
					//System.out.println(url);
					System.out.println(written_content);
					written_content += written_content1;
					written_content_2 += written_content_21;
					count++;
				}
				
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		FileWrite obj1 = new FileWrite();
		obj1.arfffilewriter(written_content,"phishing.arff");
		obj1.arfffilewriter(written_content_2, "phishingdatawithurl.arff");
		
		System.out.println("Done");*/

//		Pattern pattern;
//	    Matcher matcher; 
//		pattern = Pattern.compile(IPADDRESS_PATTERN);
//		String hostname = "28.176.167.107";
//		matcher = pattern.matcher(hostname);
//		System.out.println( matcher.matches());
//		
//		
		
	}

	
	String protocol;
	public String getProtocol() {
		return protocol;
	}

	public Double getFrequentTermsvsRegistrant() {
		return FrequentTermsvsRegistrant;
	}

	public Integer getNOofDomainNamecandidatesinHostnameandContent() {
		return NOofDomainNamecandidatesinHostnameandContent;
	}

	public Integer getNOofDomainNamecandidatesinHostname() {
		return NOofDomainNamecandidatesinHostname;
	}

	public Boolean getNumericHostname() {
		return NumericHostname;
	}

	public Double getTitlevsRegistrant() {
		return TitlevsRegistrant;
	}

	public Double getTitlevsDomainNamecandidatesinHostname() {
		return TitlevsDomainNamecandidatesinHostname;
	}

	public Double getFrequentTermsvsHostDomain() {
		return FrequentTermsvsHostDomain;
	}

	public Double getCopyrightvsRegistrant() {
		return CopyrightvsRegistrant;
	}

	public Double getDomainNamecandidatesinAnchorsvsRegistrant() {
		return DomainNamecandidatesinAnchorsvsRegistrant;
	}

	public Boolean getIsPhishing() {
		return isPhishing;
	}

	
	
	
	public void setFrequentTermsvsRegistrant(Double frequentTermsvsRegistrant) {
		FrequentTermsvsRegistrant = frequentTermsvsRegistrant;
	}

	public void setNOofDomainNamecandidatesinHostnameandContent(Integer nOofDomainNamecandidatesinHostnameandContent) {
		NOofDomainNamecandidatesinHostnameandContent = nOofDomainNamecandidatesinHostnameandContent;
	}

	public void setNOofDomainNamecandidatesinHostname(Integer nOofDomainNamecandidatesinHostname) {
		NOofDomainNamecandidatesinHostname = nOofDomainNamecandidatesinHostname;
	}

	public void setNumericHostname(Boolean numericHostname) {
		NumericHostname = numericHostname;
	}

	public void setTitlevsRegistrant(Double titlevsRegistrant) {
		TitlevsRegistrant = titlevsRegistrant;
	}

	public void setTitlevsDomainNamecandidatesinHostname(Double titlevsDomainNamecandidatesinHostname) {
		TitlevsDomainNamecandidatesinHostname = titlevsDomainNamecandidatesinHostname;
	}

	public void setFrequentTermsvsHostDomain(Double frequentTermsvsHostDomain) {
		FrequentTermsvsHostDomain = frequentTermsvsHostDomain;
	}

	public void setCopyrightvsRegistrant(Double copyrightvsRegistrant) {
		CopyrightvsRegistrant = copyrightvsRegistrant;
	}

	public void setDomainNamecandidatesinAnchorsvsRegistrant(Double domainNamecandidatesinAnchorsvsRegistrant) {
		DomainNamecandidatesinAnchorsvsRegistrant = domainNamecandidatesinAnchorsvsRegistrant;
	}

	public void setIsPhishing(Boolean isPhishing) {
		this.isPhishing = isPhishing;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	
	
}
