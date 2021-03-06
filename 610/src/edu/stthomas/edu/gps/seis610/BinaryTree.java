package edu.stthomas.edu.gps.seis610;


/**
 * A BinaryTree consists of "nodes"--each "node" is itself a BinaryTree.
 * Each node has a parent (unless it is the root), may have a left child,
 * and may have a right child. This class implements loop-free binary trees,
 * allowing shared subtrees.
 * 
 * @author David Matuszek
 * @version Jan 25, 2004
 */
public class BinaryTree implements Comparable<BinaryTree> {
    /**
     * The value (data) in this node of the binary tree; may be of
     * any object type.
     */
    public Object value;
    private BinaryTree leftChild;
    private BinaryTree rightChild;
    private BinaryTree parent;
    private boolean crossover;
    private int maxDepth;
    private boolean operator;
    private boolean valid;
    private double delta;


	public boolean isCrossover() {
		return crossover;
	}

	public void setCrossover(boolean crossover) {
		this.crossover = crossover;
	}

	public int getMaxDepth() {
		return maxDepth;
	}

	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	/**
     * Constructor for BinaryTree.
     * 
     * @param value The value to be placed in the root.
     * @param leftChild The left child of the root (may be null).
     * @param rightChild The right child of the root (may be null).
     */
    public BinaryTree(Object value, BinaryTree leftChild, BinaryTree rightChild) {
        this.value = value;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    /**
     * Constructor for a BinaryTree leaf node (that is, with no children).
     * 
     * @param value The value to be placed in the root.
     */
    public BinaryTree(Object value) {
        this(value, null, null);
    }

    
    /**
     * Getter method for the value in this BinaryTree node.
     * 
     * @return The value in this node.
     */
    public Object getValue() {
        return value;
    }
    
    /**
     * Getter method for left child of this BinaryTree node.
     * 
     * @return The left child (<code>null</code> if no left child).
     */
    public BinaryTree getLeftChild() {
        return leftChild;
    }
    
    /**
     * Getter method for right child of this BinaryTree node.
     * 
     * @return The right child (<code>null</code> if no right child).
     */
    public BinaryTree getRightChild() {
        return rightChild;
    }

    /**
     * Sets the left child of this BinaryTree node to be the
     * given subtree. If the node previously had a left child,
     * it is discarded. Throws an <code>IllegalArgumentException</code>
     * if the operation would cause a loop in the binary tree.
     * 
     * @param subtree The node to be added as the new left child.
     * @throws IllegalArgumentException If the operation would cause
     *         a loop in the binary tree.
     */
    public void setLeftChild(BinaryTree subtree) throws IllegalArgumentException {
        if (contains(subtree, this)) {
            throw new IllegalArgumentException(
                "Subtree " + this +" already contains " + subtree);
//        	System.out.println("already contains " + subtree);
        }
        leftChild = subtree;
    }

    /**
     * Sets the right child of this BinaryTree node to be the
     * given subtree. If the node previously had a right child,
     * it is discarded. Throws an <code>IllegalArgumentException</code>
     * if the operation would cause a loop in the binary tree.
     * 
     * @param subtree The node to be added as the new right child.
     * @throws IllegalArgumentException If the operation would cause
     *         a loop in the binary tree.
     */
    public void setRightChild(BinaryTree subtree) throws IllegalArgumentException {
        if (contains(subtree, this)) {
            throw new IllegalArgumentException(
                    "Subtree " + this +" already contains " + subtree);
//        	System.out.println("already contains " + subtree);
        }
        rightChild = subtree;
    }

    public BinaryTree getParent() {
		return parent;
	}

	public void setParent(BinaryTree parent) {
		this.parent = parent;
	}

	/**
     * Sets the value in this BinaryTree node.
     * 
     * @param value The new value.
     */
    public void setValue(Object value) {
        this.value = value;
    }
    
    /**
     * Tests whether this node is a leaf node.
     * 
     * @return <code>true</code> if this BinaryTree node has no children.
     */
    public boolean isLeaf() {
        return leftChild == null && rightChild == null;
    }
    
    /**
     * Tests whether this BinaryTree is equal to the given object.
     * To be considered equal, the object must be a BinaryTree,
     * and the two binary trees must have equal values in their
     * roots, equal left subtrees, and equal right subtrees.
     * 
     * @return <code>true</code> if the binary trees are equal.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        if (o == null || !(o instanceof BinaryTree)) {
            return false;
        }
        BinaryTree otherTree = (BinaryTree) o;
        return equals(value, otherTree.value)
            && equals(leftChild, otherTree.getLeftChild())
            && equals(rightChild, otherTree.getRightChild());
    }
    
    /**
     * Tests whether its two arguments are equal.
     * This method simply checks for <code>null</code> before
     * calling <code>equals(Object)</code> so as to avoid possible
     * <code>NullPointerException</code>s.
     * 
     * @param x The first object to be tested.
     * @param y The second object to be tested.
     * @return <code>true</code> if the two objects are equal.
     */
    private boolean equals(Object x, Object y) {
        if (x == null) return y == null;
        return x.equals(y);
    }

    /**
     * Tests whether the <code>tree</code> argument contains within
     * itself the <code>targetNode</code> argument.
     * 
     * @param tree The root of the binary tree to search.
     * @param targetNode The node to be searched for.
     * @return <code>true</code> if the <code>targetNode</code> argument can
     *        be found within the binary tree rooted at <code>tree</code>.
     */
    protected boolean contains(BinaryTree tree, BinaryTree targetNode) {
        if (tree == null)
            return false;
        if (tree == targetNode)
            return true;
        return contains(targetNode, tree.getLeftChild())
            || contains(targetNode, tree.getRightChild());
    }
    
    
    
    
    /**
     * Returns a String representation of this BinaryTree.
     * 
     * @see java.lang.Object#toString()
     * @return A String representation of this BinaryTree.
     */
    public String toString() {
        if (isLeaf()) {
            return value.toString();
        }
        else {
            String root, left = "null", right = "null";
            root = value.toString();
            if (getLeftChild() != null) {
                left = getLeftChild().toString();
            } else {
            	left = "0";
            }
            if (getRightChild() != null) {
                right = getRightChild().toString();
            } else {
            	right = "0";
            }
//            return root + " (" + left + ", " + right + ")";
              return "( " + left + " " + root + " " + right + " )";
        }
    }
    
    /**
     * Computes a hash code for the complete binary tree rooted
     * at this BinaryTree node.
     * 
     * @return A hash code for the binary tree with this root.
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        int result = value.hashCode();
        if (leftChild != null) {
            result += 3 * leftChild.hashCode();
        }
        if (rightChild != null) {
            result += 7 * leftChild.hashCode();
        }
        return result;
    }
    
    /**
     * Prints the binary tree rooted at this BinaryTree node.
     */
    public void print() {
        print(this, 0);
    }
    
    private void print(BinaryTree root, int indent) {
        for (int i = 0; i < indent; i++) {
            System.out.print("   ");
        }
        if (root == null) {
            System.out.println("null");
            return;
        }
        System.out.println(root.value);
        if (root.isLeaf()) return;
        print(root.leftChild, indent + 1);
        print(root.rightChild, indent + 1);
    }

	public boolean isOperator() {
		String value = (String) getValue();
		if(value.equals("+") || value.equals("-") || value.equals("*") || value.equals("/")){
			operator = true;
		} else {
			operator = false;
		}
		return operator;
	}

	public void setOperator(boolean operator) {
		this.operator = operator;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
    
    /**
     * Delta value is the sum of the training values subtracted by the sum of this tree's values
     * @return
     */
	public double getDelta() {
		return delta;
	}

	public void setDelta(double delta) {
		this.delta = delta;
	}

	@Override
	public int compareTo(BinaryTree tree1) {
		if(tree1.getDelta() > getDelta()){
			return -1;
		} else if(tree1.getDelta() == getDelta()){
			return 0;
		} else {
			return 1;
		}
	}
	

 
}
