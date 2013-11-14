package edu.stthomas.edu.gps.seis610;


import java.util.ArrayList;
import java.util.List;

public class Forest {
	private List<BinaryTree> trees = new ArrayList<BinaryTree>();
	private int generation;
	
	public List<BinaryTree> getTrees() {
		return trees;
	}
	public void setTrees(List<BinaryTree> trees) {
		this.trees = trees;
	}
	public int getGeneration() {
		return generation;
	}
	public void setGeneration(int generation) {
		this.generation = generation;
	}
	
	public void initialize(Settings settings){
//		List<BinaryTree> treelist = new ArrayList<BinaryTree>();
		int treeDepth;
		
		//create the tree list
		for(int i = getTrees().size(); i < settings.getPopulationSize(); i++){

			BinaryTree newtree = new BinaryTree(Randomizer.randomOperator());
			newtree.setLevel(0);
			
			//random select tree depth
			treeDepth = Randomizer.randomGen(2, settings.getMaxTreeHeight());
//			System.out.println("Creating tree with depth of: " + treeDepth);
			newtree.setMaxDepth(treeDepth);
			
			buildTree(newtree, treeDepth, 0);
			
//			System.out.println("tree creation finished");
			System.out.println( newtree.toString() );
//			newtree.print();
			getTrees().add(newtree);
		}
	}
	

	
	
	
	public static void buildTree (BinaryTree root, Integer targetLevel, Integer countLevel){ // Each child of a tree is a root of its subtree.
//	    System.out.println("traversing tree for level " + targetLevel);
//	    System.out.println("level count at: " + countLevel);
//		System.out.println("traversing direction " + direction);
		if(countLevel < targetLevel-1) {
			
			if(root.isOperator()){
				if(countLevel == targetLevel-2){
					//always end with two operands
					root.setLeftChild( new BinaryTree(Randomizer.randomOperand()));
					root.getLeftChild().setLevel(countLevel);
					root.setRightChild( new BinaryTree(Randomizer.randomOperand()));
					root.getRightChild().setLevel(countLevel);
					buildTree(root.getLeftChild(), targetLevel, countLevel +1);
					buildTree(root.getRightChild(), targetLevel, countLevel +1);
				}  else {
					//create either two operands or one operand and one operator
					int option = Randomizer.randomGen(0, 1);
					if(option == 0){
						//two operators
						root.setLeftChild( new BinaryTree(Randomizer.randomOperator()));
						root.getLeftChild().setLevel(countLevel);
						root.setRightChild( new BinaryTree(Randomizer.randomOperator()));
						root.getRightChild().setLevel(countLevel);
						buildTree(root.getLeftChild(), targetLevel, countLevel +1);
						buildTree(root.getRightChild(), targetLevel, countLevel +1);
					} else {
						//one operator and one operand
						root.setLeftChild( new BinaryTree(Randomizer.randomOperator()));
						root.getLeftChild().setLevel(countLevel);
						root.setRightChild( new BinaryTree(Randomizer.randomOperand()));
						root.getRightChild().setLevel(countLevel);
						buildTree(root.getLeftChild(), targetLevel, countLevel +1);
						buildTree(root.getRightChild(), targetLevel, countLevel +1);
					}	
				}
			} else {
				//end
			}

	} else {
		return;
	}
} 

	
}