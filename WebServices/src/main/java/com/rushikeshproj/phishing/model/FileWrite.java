package com.rushikeshproj.phishing.model;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class FileWrite {
	private String fileHeader =    "@relation Phishing.Detection\n\n"
								 + "@attribute protocol {http,https,other}\n"
								 + "@attribute FrequentTermsvsRegistrant integer\n"
								 + "@attribute NOofDomainNamecandidatesinHostnameandContent integer\n"
								 + "@attribute NOofDomainNamecandidatesinHostname integer\n"
								 + "@attribute NumericHostname {true,false}\n"
								 + "@attribute TitlevsRegistrant integer\n"
								 + "@attribute TitlevsDomainNamecandidatesinHostname integer\n"
								 + "@attribute FrequentTermsvsHostDomain integer\n"
								 + "@attribute CopyrightvsRegistrant integer\n"
								 + "@attribute DomainNamecandidatesinAnchorsvsRegistrant integer\n"
								 + "@attribute phising {true,false}\n"
								 + "@data \n";
	
	public void arfffilewriter(String attributes, String fileName) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(fileName, "UTF-8");
		String contentTowrite = fileHeader + attributes;
		System.out.println("Writting File");
		writer.println(contentTowrite);
		//writer.println("The second line");
		writer.close();
	}
	
	public Boolean updatearffFileWriter(String websiteAttributes, String filepath) {
		
		try {
//			File file = new File(filepath);
//			
//			FileWriter fileWritter = new FileWriter(file.getName());
//			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
//			bufferWritter.write(websiteAttributes);
//			bufferWritter.close();
			
			PrintWriter writer = new PrintWriter(new FileWriter(filepath, true));
			String contentTowrite = websiteAttributes;
			//System.out.println("Writting File");
			writer.println(contentTowrite);
			//writer.println("The second line");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Something went wrong. File did not updated successfully.");
			return false;
		}
		return true;
	}
}
