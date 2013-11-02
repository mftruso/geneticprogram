package edu.stthomas.edu.gps.seis610;


public class Settings {
	private static int maxTreeHeight;
	private static int populationSize;
	private static int treesToCrossover;
	private static int treesToMutate;
	private static double percentageOfTreesToSurvive;
	private static String stringEquation;

	public Settings(){
		maxTreeHeight = 5;
		populationSize = 100;
		treesToCrossover = 2;
		treesToMutate = 1;
		setPercentageOfTreesToSurvive(0.8);
		setStringEquation("(((x*x)-1)/2)");
	}

	public static int getMaxTreeHeight() {
		return maxTreeHeight;
	}

	public void setMaxTreeHeight(int maxTreeHeight) {
		this.maxTreeHeight = maxTreeHeight;
	}

	public static int getPopulationSize() {
		return populationSize;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	public int getTreesToCrossover() {
		return treesToCrossover;
	}

	public void setTreesToCrossover(int treesToCrossover) {
		this.treesToCrossover = treesToCrossover;
	}

	public int getTreesToMutate() {
		return treesToMutate;
	}

	public void setTreesToMutate(int treesToMutate) {
		this.treesToMutate = treesToMutate;
	}

	public static double getPercentageOfTreesToSurvive() {
		return percentageOfTreesToSurvive;
	}

	public static void setPercentageOfTreesToSurvive(
			double percentageOfTreesToSurvive) {
		Settings.percentageOfTreesToSurvive = percentageOfTreesToSurvive;
	}

	public static String getStringEquation() {
		return stringEquation;
	}

	public static void setStringEquation(String stringEquation) {
		Settings.stringEquation = stringEquation;
	}
}
