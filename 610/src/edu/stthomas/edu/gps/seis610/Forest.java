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
		List<BinaryTree> treelist = new ArrayList<BinaryTree>();
		int treeDepth;
		
		//create the tree list
		for(int i = 0; i < settings.getPopulationSize(); i++){
			//build out each tree
			//initialize newtree
			BinaryTree newtree = new BinaryTree(Randomizer.randomOperatorOrOperand());
			newtree.setLevel(0);
			//random select tree depth
			treeDepth = Randomizer.randomGen(1, settings.getMaxTreeHeight());
			System.out.println("Creating tree with depth of: " + treeDepth);
			buildTree(newtree, treeDepth, 0);
//			System.out.println("tree creation finished");
			System.out.println( newtree.toString() );
//			newtree.print();
			treelist.add(newtree);
		}
		setTrees(treelist);
	}
	
//	public static BinaryTree traverseTree (BinaryTree root, Integer level){ // Each child of a tree is a root of its subtree.
//	    System.out.println("traversing tree for level " + level);
//		int direction = Randomizer.randomLeftRight();
//		if(direction == 0){
//			if (root.getLeftChild() != null && root.getLeftChild().getLevel() != level){
//		        traverseTree (root.getLeftChild(), level);
//		    } else {
//		    	return root.getLeftChild();
//		    }
//		} else {
//			if (root.getRightChild() != null && root.getRightChild().getLevel() != level){
//		        traverseTree (root.getRightChild(), level);
//		    } else {
//		    	return root.getRightChild();
//		    }
//		}
//	    
//	    return null;
//	}
	
	
	
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
					//create a left and right nodes
					root.setLeftChild( new BinaryTree(Randomizer.randomOperatorOrOperand()));
					root.getLeftChild().setLevel(countLevel);
					root.setRightChild( new BinaryTree(Randomizer.randomOperatorOrOperand()));
					root.getRightChild().setLevel(countLevel);
					buildTree(root.getLeftChild(), targetLevel, countLevel +1);
					buildTree(root.getRightChild(), targetLevel, countLevel +1);
				}
			} else {
				if(countLevel == targetLevel-2){
					//always end with two operands
					root.setLeftChild( new BinaryTree(Randomizer.randomOperand()));
					root.getLeftChild().setLevel(countLevel);
					root.setRightChild( new BinaryTree(Randomizer.randomOperand()));
					root.getRightChild().setLevel(countLevel);
					buildTree(root.getLeftChild(), targetLevel, countLevel +1);
					buildTree(root.getRightChild(), targetLevel, countLevel +1);
				} else {
					int lfb = Randomizer.randomLeftRightBoth();
					if(lfb == 0){
						root.setLeftChild(new BinaryTree(Randomizer.randomOperatorOrOperand()));
						root.getLeftChild().setLevel(countLevel);
						buildTree(root.getLeftChild(), targetLevel, countLevel+1);
					} else if(lfb == 1){
						root.setRightChild( new BinaryTree(Randomizer.randomOperatorOrOperand()));
						root.getRightChild().setLevel(countLevel);
						buildTree(root.getRightChild(), targetLevel, countLevel+1);
					} else {
						root.setLeftChild( new BinaryTree(Randomizer.randomOperatorOrOperand()));
						root.getLeftChild().setLevel(countLevel);
						root.setRightChild( new BinaryTree(Randomizer.randomOperatorOrOperand()));
						root.getRightChild().setLevel(countLevel);
						buildTree(root.getLeftChild(), targetLevel, countLevel +1);
						buildTree(root.getRightChild(), targetLevel, countLevel +1);
					}
				}
				
			}

	} else {
		return;
	}
} 

	
}