package edu.stthomas.edu.gps.seis610;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.omg.CORBA.INITIALIZE;

import de.congrace.exp4j.*;

public class GP {

	public static void main(String[] args) {
		Settings settings = new Settings();
		boolean searchingForProgram = true;
		System.out.println("Program started!");

		// readin Training Data from file
		System.out.println("Reading Training Data");
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
				trainingData, settings.getStringEquation()));

		// iterate through generations
		Forest firstForest = new Forest();
		// while(searchingForProgram){
		iterateGenerations(firstForest, trainingData, trainingDataYValueSum,
				settings);

		// }

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
	public static Forest iterateGenerations(Forest currentForest,
			int[] trainingData, double trainingDataYValueSum, Settings settings) {
		currentForest.initialize(settings);

		// evaluate trees
		for (BinaryTree tree : currentForest.getTrees()) {
			double treeSum = calculateValues(tree, trainingData, null);
			if (Math.abs(treeSum) == trainingDataYValueSum) {
				tree.setValid(true);
				System.out.println("Valid tree! " + tree.toString());
				break;
			}

		}

		crossover(currentForest.getTrees().get(0), currentForest.getTrees().get(1));

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

		int randDepth1 = Randomizer.randomGen(0, tree1.getMaxDepth());
		int randDepth2 = Randomizer.randomGen(0, tree2.getMaxDepth());
		List<BinaryTree> tree1Nodes = new ArrayList<BinaryTree>();
		List<BinaryTree> tree2Nodes = new ArrayList<BinaryTree>();
		
		//Assign each tree node to an ArrayList
		tree1Nodes = createTreeArrayList(tree1, tree1Nodes);
//		System.out.println("tree1Nodes size: " + tree1Nodes.size());
		tree2Nodes = createTreeArrayList(tree2, tree2Nodes);
//		System.out.println("tree1Nodes size: " + tree2Nodes.size());
		
		//Randomly determine a tree node to be crossedover
		BinaryTree tree1CrossoverPart = tree1Nodes.get(randDepth1);
		tree1CrossoverPart.setCrossover(true);
		BinaryTree tree2CrossoverPart = tree2Nodes.get(randDepth2);
		tree2CrossoverPart.setCrossover(true);
//		System.out.println("Crossover tree part 1");
//		System.out.println(tree1CrossoverPart.toString());
//		System.out.println("Crossover tree part 2");
//		System.out.println(tree2CrossoverPart.toString());

//		System.out.println("Trees before crossover: ");
//		System.out.println(tree1.toString());
//		System.out.println(tree2.toString());
		
		doCrossover(tree1, tree2CrossoverPart);
		doCrossover(tree2, tree1CrossoverPart);

//		System.out.println("Trees after crossover: ");
//		System.out.println(tree1.toString());
//		System.out.println(tree2.toString());
	}

	private static List<BinaryTree> createTreeArrayList(BinaryTree tree, List<BinaryTree> nodeList) {
		if(tree != null){
			nodeList.add(tree);
			createTreeArrayList(tree.getLeftChild(), nodeList);
			createTreeArrayList(tree.getRightChild(), nodeList);
		}
		return nodeList;
	}

	public static void doCrossover(BinaryTree tree, BinaryTree treeCrossoverPart) {
		if (tree != null) {
			if (tree.getRightChild() != null && tree.getRightChild().isCrossover()) {
				tree.setRightChild(treeCrossoverPart);
			} else if(tree.getLeftChild() != null && tree.getLeftChild().isCrossover()){
				tree.setLeftChild(treeCrossoverPart);
			}else {
				doCrossover(tree.getLeftChild(), treeCrossoverPart);
				doCrossover(tree.getRightChild(), treeCrossoverPart);
			}
		}

	}

}
