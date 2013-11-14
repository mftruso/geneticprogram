package edu.stthomas.edu.gps.seis610;


public class Settings {
	private static int maxTreeHeight;
	private static int populationSize;
	private static double treesToCrossover;
	private static double treesToMutate;
	private static double percentageOfTreesToSurvive;
	private static String stringEquation;

	public Settings(){
		maxTreeHeight = 15;
		populationSize = 50;
		treesToCrossover = 0.2;
		treesToMutate = 0.1;
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

	public double getTreesToCrossover() {
		return treesToCrossover;
	}

	public void setTreesToCrossover(double treesToCrossover) {
		this.treesToCrossover = treesToCrossover;
	}

	public double getTreesToMutate() {
		return treesToMutate;
	}

	public void setTreesToMutate(double treesToMutate) {
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
