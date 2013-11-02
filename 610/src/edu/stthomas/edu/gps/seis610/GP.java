package edu.stthomas.edu.gps.seis610;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import de.congrace.exp4j.*;


public class GP {
	
	public static void main(String[] args) {
		Settings settings = new Settings();
		boolean searchingForProgram = true;
		System.out.println("Program started!");
		
		//readin Training Data from file
		System.out.println("Reading Training Data");
		int trainingData[] = new int[9];
		String inputString;
		int i = 0;
		FileInputStream fstream;
		try {
			fstream = new FileInputStream("TrainingData.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			while ((inputString = br.readLine()) != null) {
				trainingData[i] = Integer.parseInt(inputString);
				i++;
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
		
		double trainingDataYValueSum = Math.abs(calculateValues(null,trainingData, settings.getStringEquation()));
		
		//iterate through generations
		Forest firstForest = new Forest();
		iterateGenerations(firstForest,trainingData, trainingDataYValueSum, settings);
		
		

	}
	
	/**
	 * The recursive method for creating new generations
	 * 
	 * @param currentForest
	 * @param trainingData
	 * @param trainingDataYValueSum
	 * @param settings
	 * @return
	 */
	public static Forest iterateGenerations(Forest currentForest, int[] trainingData, double trainingDataYValueSum, Settings settings){
		currentForest.initialize(settings);	
		
		//evaluate trees
		for(BinaryTree tree : currentForest.getTrees()){
			double treeSum = calculateValues(tree, trainingData, null);
			if(Math.abs(treeSum) == trainingDataYValueSum){
				tree.setValid(true);
				System.out.println("Valid tree! " + tree.toString());
				break;
			} else {
				//do crossovers and mutations
				
				iterateGenerations(currentForest, trainingData, trainingDataYValueSum, settings);
			}
			
		}
		
		return currentForest;
	}
	
	/**
	 * Takes an array of training data x values, and returns the sum of the caluclated y values
	 * 
	 * @param equation
	 * @param trainingData
	 * @return
	 */
	public static double calculateValues(BinaryTree tree, int[] trainingData, String targetFunctionString){
		double valuesSum = 0;
		Calculable calc;
		try {
			if(tree == null){
				calc = new ExpressionBuilder(targetFunctionString).withVariableNames("x").build();
			} else {
				calc = new ExpressionBuilder(tree.toString()).withVariableNames("x").build();
			}
			
			
			for(int j=0; j< trainingData.length; j++){
//				System.out.println(trainingData[j]);
				calc.setVariable("x",trainingData[j]);
			    System.out.println(calc.calculate());
			    valuesSum += calc.calculate();
			}
			
		} catch (UnknownFunctionException e) {
			if(tree != null){
				tree.setValid(false);
			}
//			e.printStackTrace();
		} catch (UnparsableExpressionException e) {
			if(tree != null){
				tree.setValid(false);
			}
//			e.printStackTrace();
		} catch (Exception e){
			if(tree != null){
				tree.setValid(false);
			}
		}
		
		return valuesSum;
	}

}
