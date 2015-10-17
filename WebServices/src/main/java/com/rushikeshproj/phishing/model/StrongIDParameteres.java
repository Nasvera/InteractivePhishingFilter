package com.rushikeshproj.phishing.model;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import info.debatty.java.stringsimilarity.QGram;

public class StrongIDParameteres {
	
	private Parameters initialpara;
	private QGram stringSimilarity; 
	
	/*public String protocol;
	public Double freqTermsVSRegistrant;
	public Integer noOfDomainNameCandiInHostnameAndContent;
	public Integer noOfDomainNameCandiInHostname;
	public Boolean hasNumericHostname;
	public Double titleVSRegistrant;
	public Double titleVSDomainNameCandiInHostname;
	public Double freqTermsVSHostDomain;
	public Double copyrightVSRegistrant;
	public Double domainNameCandiInAnchorsVSRegistrant;*/
	
	private static final String IPADDRESS_PATTERN = 
			"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
		  
	    
	
	public StrongIDParameteres(Parameters para) {
		// TODO Auto-generated constructor stub
		this.initialpara = para;
		this.stringSimilarity = new QGram();
	}
	
	public String getprotocol(){
		return this.initialpara.getProtocol();
	}
	
	public Double getfreqTermsVSRegistrant() {
		HashMap<String, Integer> freqterms = initialpara.getMostFrequentTerms();
		int count = 0;
		Double maxrelavance = Double.MAX_VALUE;
		
		String reg = initialpara.getWHOISregistrant();
		
		if (reg==null) {
			maxrelavance = -1.00;
			return maxrelavance;
		}
		
		
		String[] reg_array = reg.split("\\;");
		
		
		
		for (Map.Entry<String, Integer> entry : freqterms.entrySet()) {
			/*System.out.println("Key : " + entry.getKey() 
                                      + " Value : " + entry.getValue());*/
			if (count>9) {
				break;
			}
			else {
				//Double temp_dist;
				String term = entry.getKey();
				//System.out.println("Term is :: " + term);
				Double distance = stringSimilarity.distance(term, reg_array[0]);
				if (reg_array.length >1) {
					distance = Math.min(distance , stringSimilarity.distance(term, reg_array[1]));
				}
				maxrelavance = Math.min(maxrelavance, distance);
			}
			count++;
		}
		if (maxrelavance==Double.MAX_VALUE) {
			maxrelavance = -1.00;
		}
		return maxrelavance;
	}
	
	public Integer getnoOfDomainNameCandiInHostnameAndContent() throws MalformedURLException {
		Elements images = initialpara.getImageURL();
		Elements forms = initialpara.getFormURL();
		Elements hrefs = initialpara.getHrefURL();
		String hostname = initialpara.getHostName();
		HashMap<String, Integer> freqterms = initialpara.getMostFrequentTerms();
		List<String> copyright = initialpara.getCopyRight();
		String title = initialpara.getTitleofWebpage();
		
		Integer noOfDomainsNameCandidateInHostname = 0;
		
		String[] part_of_domains = hostname.split("\\.");
		String domainName = part_of_domains[part_of_domains.length - 2] + "." + part_of_domains[part_of_domains.length - 1];
		
		for (Element image : images) { 
			if (image.tagName().equals("img")) {
				String urlstring = image.attr("abs:src");
				if(!urlstring.isEmpty()) {
					URL urlofimage = new URL(urlstring);
					part_of_domains = urlofimage.getHost().split("\\.");
					if (part_of_domains.length<2) {
						noOfDomainsNameCandidateInHostname++;
					}
					else {
						String domainOfImage = part_of_domains[part_of_domains.length - 2] + "." + part_of_domains[part_of_domains.length - 1];
						if (domainName.equals(domainOfImage)) {
							noOfDomainsNameCandidateInHostname++;
						}
					}
				}
			}
		}
		
		for (Element href : hrefs) {
			String urlstring = href.attr("abs:href");
			if (!urlstring.isEmpty()) {
				URL urlofhrefs = new URL(urlstring);
				part_of_domains = urlofhrefs.getHost().split("\\.");
				if (part_of_domains.length<2) {
					noOfDomainsNameCandidateInHostname++;
				}
				else {
					String domainOfImage = part_of_domains[part_of_domains.length - 2] + "." + part_of_domains[part_of_domains.length - 1];
					if (domainName.equals(domainOfImage)) {
						noOfDomainsNameCandidateInHostname++;
					}
				}
				
			}
		}
		
		for (Element form : forms) {
			if (!form.attr("action").isEmpty()) {
				if (form.attr("action").matches("^(https?|ftp)://.*$")) {
					URL urlofforms = new URL(form.attr("action"));
					part_of_domains = urlofforms.getHost().split("\\.");
					String domainOfImage = part_of_domains[part_of_domains.length - 2] + "." + part_of_domains[part_of_domains.length - 1];
					if (domainName.equals(domainOfImage)) {
						noOfDomainsNameCandidateInHostname++;
					}
				}
				else {
					noOfDomainsNameCandidateInHostname++;
				}
			}
		}
		
		if (freqterms.containsKey(domainName)) {
			noOfDomainsNameCandidateInHostname += freqterms.get(domainName); 
		}
		
		
		int lastIndex = 0;
		int count = 0;
		while(lastIndex != -1){

		    lastIndex = title.indexOf(hostname,lastIndex);

		    if(lastIndex != -1){
		        count ++;
		        lastIndex += hostname.length();
		    }
		}
		noOfDomainsNameCandidateInHostname += count;
		
		if (copyright.size()>1) {
			for (String holder : copyright) {
				if (holder.equals(hostname)) {
					noOfDomainsNameCandidateInHostname++;
				}
			}
		}
		
		return noOfDomainsNameCandidateInHostname;
	}
	
	public Integer getnoOfDomainNameCandiInHostname() throws MalformedURLException {
		Elements images = initialpara.getImageURL();
		Elements forms = initialpara.getFormURL();
		Elements hrefs = initialpara.getHrefURL();
		String hostname = initialpara.getHostName();
		
		Integer noOfDomainsNameCandidateInHostname = 0;
		
		String[] part_of_domains = hostname.split("\\.");
		String domainName = part_of_domains[part_of_domains.length - 2] + "." + part_of_domains[part_of_domains.length - 1];
		
		for (Element image : images) { 
			if (image.tagName().equals("img")) {
				String urlstring = image.attr("abs:src");
				if(!urlstring.isEmpty()) {
					URL urlofimage = new URL(urlstring);
					part_of_domains = urlofimage.getHost().split("\\.");
					if (part_of_domains.length<2) {
						noOfDomainsNameCandidateInHostname++;
					}
					else {
						String domainOfImage = part_of_domains[part_of_domains.length - 2] + "." + part_of_domains[part_of_domains.length - 1];
						if (domainName.equals(domainOfImage)) {
							noOfDomainsNameCandidateInHostname++;
						}
					}
				}
			}
		}
		
		for (Element href : hrefs) {
			String urlstring = href.attr("abs:href");
			if (!urlstring.isEmpty()) {
				URL urlofhrefs = new URL(urlstring);
				part_of_domains = urlofhrefs.getHost().split("\\.");
				
				if (part_of_domains.length<2) {
					noOfDomainsNameCandidateInHostname++;
				}
				else {
					String domainOfImage = part_of_domains[part_of_domains.length - 2] + "." + part_of_domains[part_of_domains.length - 1];
					if (domainName.equals(domainOfImage)) {
						noOfDomainsNameCandidateInHostname++;
					}
				}
			}
		}
		
		for (Element form : forms) {
			if (!form.attr("action").isEmpty()) {
				if (form.attr("action").matches("^(https?|ftp)://.*$")) {
					URL urlofforms = new URL(form.attr("action"));
					part_of_domains = urlofforms.getHost().split("\\.");
					if (part_of_domains.length<2) {
						noOfDomainsNameCandidateInHostname++;
					}
					else {
						String domainOfImage = part_of_domains[part_of_domains.length - 2] + "." + part_of_domains[part_of_domains.length - 1];
						if (domainName.equals(domainOfImage)) {
							noOfDomainsNameCandidateInHostname++;
						}
					}
				}
				else {
					noOfDomainsNameCandidateInHostname++;
				}
			}
		}
		return noOfDomainsNameCandidateInHostname;
	}
	
	public Boolean isNumericdomainName() {
		Pattern pattern;
	    Matcher matcher; 
		pattern = Pattern.compile(IPADDRESS_PATTERN);
		String hostname = initialpara.getHostName();
		matcher = pattern.matcher(hostname);
		return matcher.matches();	
	}
	
	public Double getTextRelavanceTitleVSRegistrant() {
		String title = initialpara.getTitleofWebpage();
		String registrant = initialpara.getWHOISregistrant();
		if (registrant==null) {
			return -1.00;
		}
		
		String[] reg_array = registrant.split("\\;");
		
		Double distance = stringSimilarity.distance(title, reg_array[0]);
		if (reg_array.length >1) {
			distance = Math.min(distance, stringSimilarity.distance(title, reg_array[1]));
		}
		return distance;
	}
	
	public Double getTextRelavanceTitleVSHostanme() {
		String title = initialpara.getTitleofWebpage();
		String hostname = initialpara.getHostName();
		
		Double distance = stringSimilarity.distance(title, hostname);
		return distance;
	}
	
	public Double getTextRelavanceFreqTermsVShostdomain() {
		HashMap<String, Integer> freqterms = this.initialpara.getMostFrequentTerms();
		String hostname = initialpara.getHostName();
		
		String[] part_of_domains = hostname.split("\\.");
		String domainName = part_of_domains[part_of_domains.length - 2] + "." + part_of_domains[part_of_domains.length - 1];
		Double maxrelavance = Double.MAX_VALUE;
		int count = 0;
		
		
		for (Map.Entry<String, Integer> entry : freqterms.entrySet()) {
			if (count>9) {
				break;
			}
			else {
				String term = entry.getKey();
				Double distance = stringSimilarity.distance(term, domainName);
				
				maxrelavance = Math.min(maxrelavance, distance);
			}
			count++;
		}
		if (maxrelavance==Double.MAX_VALUE) {
			maxrelavance = -1.00;
		}
		return maxrelavance;
	}
	
	public Double getTextRelavanceCopyRightVSRegistrant() {
		String registrant = initialpara.getWHOISregistrant();
		if (registrant==null) {
			return -1.00;
		}
		
		String[] reg_array = registrant.split("\\;");
		List<String> copyright = initialpara.getCopyRight();
		Double maxrelavance = Double.MAX_VALUE;
		
		
		if (copyright.size()>1) {
			for (String holder : copyright) {
				Double distance = stringSimilarity.distance(holder, reg_array[0]);
				if (reg_array.length >1) {
					distance = Math.min(distance, stringSimilarity.distance(holder, reg_array[1]));
				}
				maxrelavance = Math.min(maxrelavance, distance);
			}
		}
		else {
			maxrelavance = -1.0;
		}
		
		return maxrelavance;
	}
	
	public Double getTextRelavanceAnchorsVSRegistrant() throws MalformedURLException {
		Elements anchors = initialpara.getHrefURL();
		String registrant = initialpara.getWHOISregistrant();
		if (registrant==null) {
			return -1.00;
		}
		
		String[] reg_array = registrant.split("\\;");
		Double maxrelavance = Double.MAX_VALUE;
		
		for (Element href : anchors) {
			String urlstring = href.attr("abs:href");
			if (!urlstring.isEmpty()) {
				URL urlofhrefs = new URL(urlstring);
				String[] part_of_domains = urlofhrefs.getHost().split("\\.");
				String domainOfanchors;
				if (part_of_domains.length<2) {
					String hostname = initialpara.getHostName();
					String[] part_of_domains_1 = hostname.split("\\.");
					String domainName = part_of_domains_1[part_of_domains_1.length - 2] + "." + part_of_domains_1[part_of_domains_1.length - 1];
					domainOfanchors = domainName;
				}
				else {
					domainOfanchors = part_of_domains[part_of_domains.length - 2] + "." + part_of_domains[part_of_domains.length - 1];
				}
				
				Double distance = stringSimilarity.distance(domainOfanchors, reg_array[0]);
				if (reg_array.length >1) {
					distance = Math.min(distance, stringSimilarity.distance(domainOfanchors, reg_array[1]));
				}
				maxrelavance = Math.min(maxrelavance, distance);
			}
		}
		if (maxrelavance==Double.MAX_VALUE) {
			maxrelavance = -1.00;
		}
		return maxrelavance;
	}
}
