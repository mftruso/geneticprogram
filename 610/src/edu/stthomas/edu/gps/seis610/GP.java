package edu.stthomas.edu.gps.seis610;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import de.congrace.exp4j.*;

public class GP {
	static Logger log = (Logger) LoggerFactory.getLogger(GP.class);    
    
	public static void main(String[] args) {
		Settings settings = new Settings();
		boolean searchingForProgram = true;
		Forest forest = new Forest();

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

		forest.setGeneration(1);
		// iterate through generations
		while(searchingForProgram){
//		while(forest.getGeneration() < 3){
			log.info("Beginning Generation: " + forest.getGeneration());
			
			forest.initialize(settings);
			log.debug("Forest size: " + forest.getTrees().size());
			
			forest = evaluateForest(forest, trainingData, trainingDataYValueSum);
			
			//check forest for valid tree
			for(BinaryTree bt : forest.getTrees()){
				if(bt.isValid()){
					log.warn("Valid Program!");
					log.warn(bt.toString());
					searchingForProgram = false;
				}
			}
			
			forest.setGeneration(forest.getGeneration()+1);
		 }
		 
		 log.info("End");

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
	public static Forest evaluateForest(Forest currentForest,
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
		
		// evaluate trees against training data
		for (BinaryTree tree : currentForest.getTrees()) {
			log.info(tree.toString());
			treeSum = calculateValues(tree, trainingData, null);
			log.info("Sum: " + treeSum);
			if (Math.abs(treeSum) == trainingDataYValueSum) {
				tree.setValid(true);
				log.warn("Valid tree! " + tree.toString());
				break;
			} else {
				delta = Math.abs(trainingDataYValueSum - treeSum);
				tree.setDelta(delta);
				log.info("Delta: " + delta);
			}

		}
		
//		log.debug("Tree before/after sort");
//		for(BinaryTree bt : currentForest.getTrees())
//			log.debug(bt.toString());
		//sort trees from largest delta value to smallest delta
		Collections.sort(currentForest.getTrees());
//		log.debug("");
//		for(BinaryTree bt : currentForest.getTrees())
//			log.debug(bt.toString());
		
		
		
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
			log.debug("Tree Indexes: " + crossoverTrees.toString());
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
			log.debug("Mutate index: " + randomTreeIndex);
		}		
		
		
		//perform mutations
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
				 log.debug("Training Data value: " + trainingData[j]);
				calc.setVariable("x", trainingData[j]); 
				log.debug("Caluclated Value: " + calc.calculate());
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
		log.debug("tree1Nodes size: " + tree1Nodes.size());
		tree2Nodes = createTreeArrayList(tree2, tree2Nodes);
		log.debug("tree1Nodes size: " + tree2Nodes.size());
		
		randDepth1 = Randomizer.randomGen(1, tree1Nodes.size()-1);
		randDepth2 = Randomizer.randomGen(1, tree2Nodes.size()-1);
		
		
		//Randomly determine a tree node to be crossedover
		BinaryTree tree1CrossoverPart = tree1Nodes.get(randDepth1);
		tree1CrossoverPart.setCrossover(true);
		BinaryTree tree2CrossoverPart = tree2Nodes.get(randDepth2);
		tree2CrossoverPart.setCrossover(true);
		log.debug("Crossover tree part 1");
		log.debug(tree1CrossoverPart.toString());
		log.debug("Crossover tree part 2");
		log.debug(tree2CrossoverPart.toString());

		log.debug("Trees before crossover: ");
		log.debug(tree1.toString());
		log.debug(tree2.toString());

		doCrossover(tree1, tree2CrossoverPart);
		doCrossover(tree2, tree1CrossoverPart);

		log.debug("Trees after crossover: ");
		log.debug(tree1.toString());
		log.debug(tree2.toString());
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
//		log.debug("Value to mutate: " + treeNodes.get(randDepth).getValue());
		if(treeNodes.get(randDepth).isOperator()){
			newValue = Randomizer.randomOperator();
		} else {
			newValue = Randomizer.randomOperand();
		}
		log.debug("Tree before/after mutation");
		log.debug(tree.toString());
		treeNodes.get(randDepth).setValue(newValue);
		log.debug(tree.toString());
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
				log.debug("Found crossover on right! " + tree.toString());
			} else if(tree.getLeftChild() != null && tree.getLeftChild().isCrossover()){
				tree.getLeftChild().setCrossover(false);
				tree.setLeftChild(treeCrossoverPart);
				log.debug("Found crossover on left! " + tree.toString());
			} else {
					doCrossover(tree.getLeftChild(), treeCrossoverPart);
					doCrossover(tree.getRightChild(), treeCrossoverPart);				
			}
		}

	}

}
