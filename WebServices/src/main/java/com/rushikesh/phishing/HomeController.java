package com.rushikesh.phishing;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rushikeshproj.phishing.model.FileWrite;
import com.rushikeshproj.phishing.model.IsPhishing;
import com.rushikeshproj.phishing.model.Track;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	/**
	 * Simply selects the home view to render by returning its name.
	 * @throws JSONException 
	 */
	@RequestMapping(value = "/isPhishing", method = RequestMethod.GET,produces ="application/json")
	public @ResponseBody IsPhishing phishinghome(@RequestParam ("websiteUrl") String websiteurl) {
		logger.info("Welcome Phishing home! The name of the website is {}.", websiteurl);
		
		/*String hello = "{\"pin\":\"12345\","
				+ "\"installationId\":\"123456\","
				+ "\"udid\":\"12345\"}";*/
		//JSONObject myObject = new JSONObject(hello);
		
	    IsPhishing newWebsite = new IsPhishing();
		try {
			Boolean isChecked = newWebsite.isPhishing(websiteurl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Track heheh = new Track();
		heheh.setSinger("Rushikesh");
		
		return newWebsite;
	}
	
	@RequestMapping(value = "/isPhishing/update", method=RequestMethod.POST)
	public @ResponseBody Track updatephishingresult(@RequestParam ("websiteUrl") String websiteurl, 
													@RequestParam ("protocol") String protocol,
													@RequestParam ("frequentTermsvsRegistrant") String frequentTermsvsRegistrant,
													@RequestParam ("noofDomainNamecandidatesinHostnameandContent") String noofDomainNamecandidatesinHostnameandContent,
													@RequestParam ("noofDomainNamecandidatesinHostname") String noofDomainNamecandidatesinHostname,
													@RequestParam ("numericHostname") String numericHostname,
													@RequestParam ("titlevsRegistrant") String titlevsRegistrant,
													@RequestParam ("titlevsDomainNamecandidatesinHostname") String titlevsDomainNamecandidatesinHostname,
													@RequestParam ("frequentTermsvsHostDomain") String frequentTermsvsHostDomain,
													@RequestParam ("copyrightvsRegistrant") String copyrightvsRegistrant,
													@RequestParam ("domainNamecandidatesinAnchorsvsRegistrant") String domainNamecandidatesinAnchorsvsRegistrant,
													@RequestParam ("isphishing") String isphishing) {		
		String response = "{\"website\":\"" + websiteurl + "\","
				+ "\"decision\":" + websiteurl + "}";
		logger.info("Url is ::" + websiteurl);
		logger.info("Decision is " + isphishing);
		
		String written_content1 = 
			    protocol + "," 
			  + frequentTermsvsRegistrant + ","
			  + noofDomainNamecandidatesinHostnameandContent + ","
			  + noofDomainNamecandidatesinHostname + ","
			  + numericHostname + ","
			  + titlevsRegistrant + ","
			  + titlevsDomainNamecandidatesinHostname + ","
			  + frequentTermsvsHostDomain + ","
			  + copyrightvsRegistrant + ","
			  + domainNamecandidatesinAnchorsvsRegistrant + ","
			  + isphishing;
		System.out.println("String is :: " + written_content1);
		FileWrite obj1 = new FileWrite();
		Boolean fact = obj1.updatearffFileWriter(written_content1, "/Users/rushikesh/Documents/workspace/Test_Rushikesh/src/main/resources/phishing_f.arff");
		if (fact) {
			System.out.println("File Updated Successfully");
		}
		
		Track hehe = new Track();
		hehe.setSinger("Rushikesh");
		
		return hehe;
	}
	
}
