package com.rushikeshproj.phishing.model;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import weka.core.Instances;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.LADTree;

public class MachineLearningAlgo {

	public String predictresult(String datafile, String predictedfile) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub

		// load model
		/*Classifier cls = (Classifier) weka.core.SerializationHelper
				.read(rootPath + "train2.model");*/
		
		System.out.println("File path :: " + datafile);
		
		Classifier cls = new LADTree();
		//Classifier cls = new LADTree();
		 
		InputStream is = getClass().getResourceAsStream(datafile);
		
		 // train
		 Instances inst = new Instances( new BufferedReader( new FileReader(datafile) ));
		 inst.setClassIndex(inst.numAttributes() - 1);
		 try {
			cls.buildClassifier(inst);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		 
		is = getClass().getResourceAsStream(predictedfile); 
		// predict instance class values
		Instances originalTrain = new Instances(
				new BufferedReader(
						new FileReader(predictedfile))); // load
																																											// or
																																											// predict
		originalTrain.setClassIndex(originalTrain.numAttributes() - 1);
		// which instance to predict class value
		int s1 = 0;

		// perform your prediction
		double value = -1.00;
		try {
			value = cls.classifyInstance(originalTrain.instance(s1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// get the prediction percentage or distribution
		double[] percentage = null;
		try {
			percentage = cls.distributionForInstance(originalTrain
					.instance(s1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// get the name of the class value
		String prediction = originalTrain.classAttribute().value((int) value);

		//System.out.println("The predicted value of instance "+ Integer.toString(s1) + ": " + prediction);

		// Format the distribution
		String distribution = "";
		for (int i = 0; i < percentage.length; i = i + 1) {
			if (i == value) {
				distribution = distribution + "*"
						+ Double.toString(percentage[i]) + ",";
			} else {
				distribution = distribution + Double.toString(percentage[i])
						+ ",";
			}
		}
		distribution = distribution.substring(0, distribution.length() - 1);

		//System.out.println("Distribution:" + distribution);
		return prediction;

	}

}
