package edu.stthomas.edu.gps.seis610;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import de.congrace.exp4j.*;

public class GP {
	private static Logger log = Logger.getLogger("InfoLogger");
	  
	public static void main(String[] args) {
		Settings settings = new Settings();
		boolean searchingForProgram = true;
		Forest forest = new Forest();
		int generation = 1;
		log.info("Program started!");

		// readin Training Data from file
		log.info("Reading Training Data");
		int trainingData[] = new int[9];
		String inputString;
		int i = 0;
		FileInputStream fstream;
		try {
			fstream = new FileInputStream("TrainingData.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					fstream));
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

		double trainingDataYValueSum = Math.abs(calculateValues(null,
				trainingData, Settings.getStringEquation()));

		
		// iterate through generations
		while(searchingForProgram){
//		while(generation < 3){
			System.out.println("Beginning Generation: " + generation); 
			forest.initialize(settings);
//			 System.out.println("Forest size: " + forest.getTrees().size());
			 forest = iterateGenerations(forest, trainingData, trainingDataYValueSum);
			
			for(BinaryTree bt : forest.getTrees()){
				if(bt.isValid()){
					System.out.println("Valid Program!");
					System.out.println(bt.toString());
					searchingForProgram = false;
				} else {
					
				}
			}
			generation++;
		 }
		 
		 System.out.println("End");

	}

	/**
	 * The  method for creating new generations
	 * 
	 * @param currentForest
	 * @param trainingData
	 * @param trainingDataYValueSum
	 * @param settings
	 * @return
	 */
	public static Forest iterateGenerations(Forest currentForest,
			int[] trainingData, double trainingDataYValueSum) {
		
		
		double delta;
		double treeSum;
		int populationSize = Settings.getPopulationSize();
		double treesToKill = Math.ceil(populationSize * (1-Settings.getPercentageOfTreesToSurvive()));
		double treesToCrossover = Math.ceil(populationSize) * (Settings.getTreesToCrossover());
		double treesToMutate = Math.ceil(populationSize) * (Settings.getTreesToMutate());
		int[] crossoverTrees = new int[(int) treesToCrossover];
		int randomTreeIndex;
		int[] mutateTrees = new int[(int) treesToMutate];
		
		
		//number of trees to crossover should always be even
		if(treesToCrossover%2 == 1){
			treesToCrossover++;
		}
		
		// evaluate trees
		for (BinaryTree tree : currentForest.getTrees()) {
			treeSum = calculateValues(tree, trainingData, null);

			if (Math.abs(treeSum) == trainingDataYValueSum) {
				tree.setValid(true);
				System.out.println("Valid tree! " + tree.toString());
				break;
			} else {
				delta = Math.abs(trainingDataYValueSum - treeSum);
				tree.setDelta(delta);
			}

		}
//		System.out.println("Tree before/after sort");
//		for(BinaryTree bt : currentForest.getTrees())
//			System.out.println(bt.toString());
		//sort trees from largest delta value to smallest delta
		Collections.sort(currentForest.getTrees());
//		System.out.println("");
//		for(BinaryTree bt : currentForest.getTrees())
//			System.out.println(bt.toString());
		
		//remove treesfrom the bottom of the list
		for(int i = 0; i < treesToKill; i++ ){
			currentForest.getTrees().remove(populationSize-1-i);
		} 

		//add random list indexes to an array to determine which trees to crossover
		for(int j = 0; j < crossoverTrees.length; j++){
			randomTreeIndex = Randomizer.randomGen(0, currentForest.getTrees().size());
			
			//make sure the index has not been selected already
			for(int k = 0; k < crossoverTrees.length; k++){
				if(randomTreeIndex == crossoverTrees[k]){
					randomTreeIndex = Randomizer.randomGen(0, currentForest.getTrees().size());
					k = 0;
				}
			}
		
			crossoverTrees[j] = randomTreeIndex;
		}
		
		//perform crossovers in pairs
		for(int m = 0; m < crossoverTrees.length; m = m + 2){
			crossover(currentForest.getTrees().get(m), currentForest.getTrees().get(m+1));
		}
		
		//add random list indexes to an array to determine which trees to mutate
		for(int j = 0; j < mutateTrees.length; j++){
			randomTreeIndex = Randomizer.randomGen(0, currentForest.getTrees().size());
			
			//make sure the index has not been selected already in either mutate index list or crossover index list
			for(int k = 0; k < crossoverTrees.length; k++){
				if(randomTreeIndex == crossoverTrees[k] ){
					randomTreeIndex = Randomizer.randomGen(0, currentForest.getTrees().size());
					k = 0;
				} else {
					for(int n = 0; n < mutateTrees.length; n++){
						if(randomTreeIndex == mutateTrees[n]){
							randomTreeIndex = Randomizer.randomGen(0, currentForest.getTrees().size());
							n = 0;
						}
					}
				}
			}
		
			mutateTrees[j] = randomTreeIndex;
//			System.out.println("Mutate: " + randomTreeIndex);
		}		
		
		for(int p = 0; p < mutateTrees.length-1; p++){
			mutate(currentForest.getTrees().get(p));
		}
		

		return currentForest;
	}

	/**
	 * Takes an array of training data x values or a tree and returns the sum of
	 * the calculated y values
	 * 
	 * @param equation
	 * @param trainingData
	 * @return
	 */
	public static double calculateValues(BinaryTree tree, int[] trainingData,
			String targetFunctionString) {
		double valuesSum = 0;
		Calculable calc;
		try {
			if (tree == null) {
				calc = new ExpressionBuilder(targetFunctionString)
						.withVariableNames("x").build();
			} else {
				calc = new ExpressionBuilder(tree.toString())
						.withVariableNames("x").build();
			}

			for (int j = 0; j < trainingData.length; j++) {
				// System.out.println(trainingData[j]);
				calc.setVariable("x", trainingData[j]);
				// System.out.println(calc.calculate());
				valuesSum += calc.calculate();
			}

		} catch (UnknownFunctionException e) {
			if (tree != null) {
				tree.setValid(false);
			}
			// e.printStackTrace();
		} catch (UnparsableExpressionException e) {
			if (tree != null) {
				tree.setValid(false);
			}
			// e.printStackTrace();
		} catch (Exception e) {
			if (tree != null) {
				tree.setValid(false);
			}
		}

		return valuesSum;
	}
	
	
	/**
	 * Takes two BinaryTree and swaps a portion of each tree 
	 * 
	 * @param tree1
	 * @param tree2
	 */
	public static void crossover(BinaryTree tree1, BinaryTree tree2) {

		int randDepth1;
		int randDepth2;
		List<BinaryTree> tree1Nodes = new ArrayList<BinaryTree>();
		List<BinaryTree> tree2Nodes = new ArrayList<BinaryTree>();
		
		//Assign each tree node to an ArrayList
		tree1Nodes = createTreeArrayList(tree1, tree1Nodes);
//		System.out.println("tree1Nodes size: " + tree1Nodes.size());
		tree2Nodes = createTreeArrayList(tree2, tree2Nodes);
//		System.out.println("tree1Nodes size: " + tree2Nodes.size());
		
		randDepth1 = Randomizer.randomGen(1, tree1Nodes.size()-1);
		randDepth2 = Randomizer.randomGen(1, tree2Nodes.size()-1);
		
		
		//Randomly determine a tree node to be crossedover
		BinaryTree tree1CrossoverPart = tree1Nodes.get(randDepth1);
		tree1CrossoverPart.setCrossover(true);
		BinaryTree tree2CrossoverPart = tree2Nodes.get(randDepth2);
		tree2CrossoverPart.setCrossover(true);
		System.out.println("Crossover tree part 1");
		System.out.println(tree1CrossoverPart.toString());
		System.out.println("Crossover tree part 2");
		System.out.println(tree2CrossoverPart.toString());

		System.out.println("Trees before crossover: ");
		System.out.println(tree1.toString());
		System.out.println(tree2.toString());

		doCrossover(tree1, tree2CrossoverPart);
		doCrossover(tree2, tree1CrossoverPart);

		System.out.println("Trees after crossover: ");
		System.out.println(tree1.toString());
		System.out.println(tree2.toString());
	}
	
	/**
	 * Mutate one value in a tree
	 * 
	 * @param tree
	 */
	private static void mutate(BinaryTree tree){
		int randDepth;
		String newValue = "";
		List<BinaryTree> treeNodes = new ArrayList<BinaryTree>();
		
		treeNodes = createTreeArrayList(tree, treeNodes);
		randDepth = Randomizer.randomGen(0, treeNodes.size()-1);
//		System.out.println("Value to mutate: " + treeNodes.get(randDepth).getValue());
		if(treeNodes.get(randDepth).isOperator()){
			newValue = Randomizer.randomOperator();
		} else {
			newValue = Randomizer.randomOperand();
		}
		System.out.println("Tree before/after mutation");
		System.out.println(tree.toString());
		treeNodes.get(randDepth).setValue(newValue);
		System.out.println(tree.toString());
	}
	
	/**
	 * Recursive method to build an array list of tree nodes
	 * 
	 * @param tree
	 * @param nodeList
	 * @return
	 */
	private static List<BinaryTree> createTreeArrayList(BinaryTree tree, List<BinaryTree> nodeList) {
		if(tree != null){
			nodeList.add(tree);
			createTreeArrayList(tree.getLeftChild(), nodeList);
			createTreeArrayList(tree.getRightChild(), nodeList);
		}
		return nodeList;
	}
	
	/**
	 * recursively searches a BinaryTree for the node marked for crossover then replaces that node 
	 * with a different tree
	 *  
	 * @param tree
	 * @param treeCrossoverPart
	 */
	public static void doCrossover(BinaryTree tree, BinaryTree treeCrossoverPart) {
		if (tree != null) {
			if (tree.getRightChild() != null && tree.getRightChild().isCrossover()) {				
				tree.getRightChild().setCrossover(false);
				tree.setRightChild(treeCrossoverPart);
				System.out.println("Found crossover on right! " + tree.toString());
			} else if(tree.getLeftChild() != null && tree.getLeftChild().isCrossover()){
				tree.getLeftChild().setCrossover(false);
				tree.setLeftChild(treeCrossoverPart);
				System.out.println("Found crossover on left! " + tree.toString());
			} else {
					doCrossover(tree.getLeftChild(), treeCrossoverPart);
					doCrossover(tree.getRightChild(), treeCrossoverPart);				
			}
		}

	}

}
