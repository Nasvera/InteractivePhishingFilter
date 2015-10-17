package com.rushikeshproj.phishing.model;
import info.debatty.java.stringsimilarity.QGram;

public class QGramDistance {
	
	public Double findQGramDistanceofStrings(String s1, String s2) {
		QGram stringSimilarity = new QGram();
		double distance = stringSimilarity.distance(s1, s2);
		//System.out.println("String similarity is ::: " + distance);
		return distance;
	}
}
