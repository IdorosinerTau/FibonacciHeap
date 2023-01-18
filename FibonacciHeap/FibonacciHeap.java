package FibonacciHeap;
/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap
{
	private HeapNode min;
	private HeapNode first;
	private int size;
	private int numOfTrees;
	private static int cuts;
	private static int links;
	private static int nonMarked;
	private int HeapNonMarked;
	
	public FibonacciHeap() {
		this.min = null;
		this.first = null;
		this.size = 0;
		this.numOfTrees = 0;
		this.HeapNonMarked = 0;
	}
	
	
	public HeapNode getMin() {
		return this.min;
	}
	public HeapNode getFirst() {
		return this.first;
	}
	public int getSize() {
		return this.size;
	}
	public int getNumOfTrees() {
		return this.numOfTrees;
	}
	public static int getCuts() {
		return FibonacciHeap.cuts;
	}
	public static int getLinks() {
		return FibonacciHeap.links;
	}
	public static int getNonMarked() {
		return FibonacciHeap.nonMarked;
	}
	public int getHeapNonMarked() {
		return this.HeapNonMarked;
	}
	
	public void setMin(HeapNode min) {
		this.min = min;
	}
	public void setFirst(HeapNode first) {
		this.first = first;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public void setNumOfTrees(int numOfTrees) {
		this.numOfTrees = numOfTrees;
	}
	public void incCuts() {
		FibonacciHeap.cuts++;
	}
	public void incLinks() {
		FibonacciHeap.links++;
	}
	public void setNonMarked(int nonMarked) {
		FibonacciHeap.nonMarked = nonMarked;
	}
	public void setHeapNonMarked(int nonMarked) {
		this.HeapNonMarked = nonMarked;
	}
	
	
	
   /**
    * public boolean isEmpty()
    *
    * Returns true if and only if the heap is empty.
    *   
    */
    public boolean isEmpty()
    {
    	return getSize() == 0;
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
    * The added key is assumed not to already belong to the heap.  
    * 
    * Returns the newly created node.
    */
    public HeapNode insert(int key)
    {   
    	HeapNode newNode = new HeapNode(key);
    	
    	if (!isEmpty()) {
    		//insert node to leftmost space in the heap
	    	newNode.setNext(getFirst());
	    	newNode.setPrev(getFirst().getPrev());
	    	getFirst().getPrev().setNext(newNode);
	    	getFirst().setPrev(newNode);
	    	//update min pointer if needed
	    	if (getMin().getKey() > key)
	    		setMin(newNode);
    	}
    	else {
    		newNode.setNext(newNode);
	    	newNode.setPrev(newNode);
	    	setMin(newNode);
    	}
    	
    	setFirst(newNode);
    	setSize(getSize() + 1);
    	setNumOfTrees(getNumOfTrees() + 1);
    	setNonMarked(getNonMarked() + 1);
    	setHeapNonMarked(getHeapNonMarked() + 1);
    	return newNode;
    }

   /**
    * public void deleteMin()
    *
    * Deletes the node containing the minimum key.
    *
    */
    public void deleteMin()
    {
    	removeMin();
    	consolidate();
    }

    /**
     * private void removeMin()
     *
     * Removes the node containing the minimum key from the heap (updating all pointers and fields).
     *
     */
    private void removeMin() {
    	if (this.isEmpty())
    		return;
    	
    	
    	if (this.getNumOfTrees() == 1) {
    		setFirst(getFirst().getChild());
    	}
    	
    	//there is more than one tree in the heap
    	else {
    		//if this.min == this.first update pointer of this.first
        	if (getMin() == getFirst()) {
    			if(getMin().getChild() != null)
        			setFirst(getMin().getChild());
        		else
        			setFirst(getFirst().getNext());
        	}
        	
        	//if this.min has children connect this.min direct descendants with the other roots
    		if(getMin().getChild() != null) {
    			getMin().getNext().setPrev(getMin().getChild().getPrev());
    			getMin().getChild().getPrev().setNext(getMin().getNext());
    			getMin().getChild().setPrev(getMin().getPrev());
    			getMin().getPrev().setNext(getMin().getChild());
    		}
    		//connect this.min.next with this.min.prev
    		else {
    			getMin().getNext().setPrev(getMin().getPrev());
    			getMin().getPrev().setNext(getMin().getNext());
    		}
    	}
    	
    	//if this.min has children, iterate over this.min direct descendants and cancel all their marks and set their parent to null
		HeapNode node = getMin().getChild();
    	for (int i = 0; i < getMin().getRank(); i++) {
    		node.setParent(null);
    		if (node.getMark()) {
    			node.setMark(false);
    			setNonMarked(getNonMarked() + 1);
    			setHeapNonMarked(getHeapNonMarked() + 1);
    		}
    		setNumOfTrees(getNumOfTrees() + 1);
    		node = node.getNext();
    	}
    	
    	setSize(getSize() - 1);
    	setNonMarked(getNonMarked() - 1);
    	setHeapNonMarked(getHeapNonMarked() - 1);
    }
    
    /**
     * private void consolidate()
     *
     * Consolidates the heap like we learned in class
     *
     */
    private void consolidate() {
    	//create array with the length of the highest rank possible in a heap containing this.size nodes 
    	HeapNode[] heap_arr = new HeapNode[(int) (Math.log(getSize() + 1)/ Math.log(2)) + 3];
    	
    	HeapNode node = getFirst();
    	HeapNode tmp = null;

    	//iterate over all trees in the heap
    	while (tmp != getFirst()) {
    		tmp = node.getNext();
    		
    		//break all ties of the tree from the heap
    		node.setNext(node);
    		node.setPrev(node);
    		
    		int index = node.getRank();
    		
    		//put the tree in the array if there isn't a tree with the same rank in there already
    		if(heap_arr[index] == null) 
    			heap_arr[index] = node;
    		
    		//if there was already a tree in the array with the same rank, start consolidating
    		else {
    			while(heap_arr[index] != null) {
    				node = link(node, heap_arr[index]);
    				heap_arr[index] = null;
    				index++;
    			}
    			heap_arr[index] = node;
    		}
    		
    		node = tmp;
    	}
    	
    	//clear heap (without erasing trackers) 
    	setFirst(null);
    	setMin(null);
    	
    	//meld the trees in the array to the heap
    	int num_of_trees = 0;
    	for (HeapNode root : heap_arr) {
    		if (root != null) {
    			meldNode(root);
    			num_of_trees++;
    		}
    	}
    	
    	setNumOfTrees(num_of_trees);  
    }
       
    /**
     * private HeapNode link(HeapNode x, HeapNode y)
     *
     * Links the tree which has x as it's root with the tree which has y as it's root 
     * Returns the root of the new tree
     * 
     */
    private HeapNode link(HeapNode x, HeapNode y) {
    	//if y.key is smaller than x.key swap x and y
    	if (x.getKey() > y.getKey()) {
    		HeapNode tmp = x;
    		x = y;
    		y = tmp;
    	}
    	
    	//link both trees like we learned in class
    	y.setParent(x);
    	
    	if (x.getChild() == null) {
    		y.setNext(y);
    		y.setPrev(y);
    	}
    	else {
    		y.setNext(x.getChild());
    		x.getChild().getPrev().setNext(y);
    		y.setPrev(x.getChild().getPrev());
    		x.getChild().setPrev(y);
    	}
    	
    	x.setChild(y);
		x.setRank(x.getRank() + 1);
		
		incLinks();
    	return x;
    }
    
   /**
    * public HeapNode findMin()
    *
    * Returns the node of the heap whose key is minimal, or null if the heap is empty.
    *
    */
    public HeapNode findMin()
    {
    	if (isEmpty())
    		return null;
    	return getMin();
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Melds heap2 with the current heap.
    *
    */
    public void meld (FibonacciHeap heap2)
    {
    	if (heap2.isEmpty())
    		return;
    	if (getFirst() == null) {
    		setMin(heap2.getMin());
    		setFirst(heap2.getFirst());
    	}
    	else {
    		//set this.last.next to heap2.first
	    	getFirst().getPrev().setNext(heap2.getFirst());
	    	//set heap2.last.next to this.first
	    	heap2.getFirst().getPrev().setNext(getFirst());
	    	//keep pointer to heap2.last
	    	HeapNode heap2_last = heap2.getFirst().getPrev();
	    	//set heap2.first.prev to this.last
	    	heap2.getFirst().setPrev(getFirst().getPrev());
	    	//set this.first.prev to heap2.last
	    	getFirst().setPrev(heap2_last);
	    	//update min pointer if needed
			if(getMin().getKey() > heap2.getMin().getKey())
				setMin(heap2.getMin());
    	}
    	
    	setSize(getSize() + heap2.getSize());
    	setNumOfTrees(getNumOfTrees() + heap2.getNumOfTrees());
    	setHeapNonMarked(getHeapNonMarked() + heap2.getHeapNonMarked());
    	return; // should be replaced by student code   		
    }
    
    
    /**
     * public void meldNode (HeapNode node)
     *
     * Melds node to the end of the current heap.
     *
     */
    public void meldNode(HeapNode node) {
    	//if the heap is "empty" then insert node to heap as it's only tree
    	if (getFirst() == null) {
    		node.setNext(node);
    		node.setPrev(node);
    		setMin(node);
    		setFirst(node);
    	}
    	
    	//meld node to the end of the heap (rightmost space)
    	else {
    		node.setNext(getFirst());
    		node.setPrev(getFirst().getPrev());
    		node.getNext().setPrev(node);
    		node.getPrev().setNext(node);
    		if(getMin().getKey() > node.getKey())
    			setMin(node);
    	}
    }

   /**
    * public int size()
    *
    * Returns the number of elements in the heap.
    *   
    */
    public int size()
    {
    	return getSize(); // should be replaced by student code
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
    * (Note: The size of of the array depends on the maximum order of a tree.)  
    * 
    */
    public int[] countersRep()
    {
    	if (isEmpty())
    		return new int[0];
    	
    	int[] arr = new int[(int) Math.ceil(Math.log(getSize())/Math.log(2)) + 1];
    	
    	//iterate over all trees in the heap
    	HeapNode node = this.first;
    	arr[node.getRank()]++;
    	while (node.getNext() != this.first) {
    		node = node.getNext();
    		arr[node.getRank()]++;
    	}
        return arr;
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap.
	* It is assumed that x indeed belongs to the heap.
    *
    */
    public void delete(HeapNode x) 
    {    
    	if (this.min == x) {
    		this.deleteMin();
    		return;
    	}
    	
    	//decrease key so that it becomes the new min of the heap and then delete it
    	int delta = x.getKey() - this.min.getKey() + 1;
    	this.decreaseKey(x, delta);  	
    	this.deleteMin();
    	return;
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    * 
    */
    public void decreaseKey(HeapNode x, int delta)
    {   
    	//decrease x.key by delta
    	x.setKey(x.getKey() - delta);
    	
    	//update min pointer if needed
    	if (x.getKey() < getMin().getKey())
			setMin(x);
    	
    	//if x is not a root
    	if (x.getParent() != null) {
    		
    		//if x.key < x.parent.key start cascade cut process like we learned in class
	    	if (x.getParent().getKey() > x.getKey())
	    		cascadeCut(x);		
    	}
    }
    
    /**
     * private void cascadeCut(HeapNode x)
     *
     * Cascade cut process like we learned in class
     * 
     */
    private void cascadeCut(HeapNode x) {
    	
    	//if x is not a root of a tree
    	if (x.getParent() != null) {
			HeapNode x_parent = cutAndInsertHeap(x);
			incCuts();
			
			//if prev(x.parent) is not a root
			if (x_parent.getParent() != null) {
				
				//if prev(x.parent) is unmarked then mark it
				if (x_parent.getMark() == false) {
					x_parent.setMark(true);
					setNonMarked(getNonMarked() - 1);
					setHeapNonMarked(getHeapNonMarked() - 1);
				}
				
				//continue cascade cut process
				else
					cascadeCut(x_parent);
			}
    	}
    }

    /**
     * private HeapNode cutAndInsertHeap(HeapNode x)
     *
     * Cut the subtree of x from it's parent tree and insert it as a new tree to leftmost space in the heap
     * Return the parent of prev(x)
     * 
     */
    private HeapNode cutAndInsertHeap(HeapNode x) {
    	
    	//update the rank of x.parent
    	x.getParent().setRank(x.getParent().getRank() - 1);
    	
    	//if x.parent's child pointer is x then update it
    	if (x.getParent().getChild() == x) {
    		if (x.getNext() != x)
    			x.getParent().setChild(x.getNext());
    		else
    			x.getParent().setChild(null);
		}
    	
    	//maintain pointer to x.parent
    	HeapNode parent = x.getParent();
    	
    	//cut x from it's parent's subtree and 
    	x.setParent(null);
    	x.getNext().setPrev(x.getPrev());
    	x.getPrev().setNext(x.getNext());
    	x.setNext(getFirst());
    	x.setPrev(getFirst().getPrev());
    	
    	//insert x's subtree to the leftmost space in the heap
    	getFirst().getPrev().setNext(x);
    	getFirst().setPrev(x);
    	setFirst(x);
    	
    	//make sure that x's mark is false as it is a root
    	if (x.getMark()) {
    		x.setMark(false);
    		setNonMarked(getNonMarked() + 1);
    		setHeapNonMarked(getHeapNonMarked() + 1);
    	}
	
    	setNumOfTrees(getNumOfTrees() + 1);
    	return parent;
    }
    
   /**
    * public int nonMarked() 
    *
    * This function returns the current number of non-marked items in the heap
    */
    public int nonMarked() 
    {    
        return getNonMarked(); // should be replaced by student code
    }

   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * 
    * In words: The potential equals to the number of trees in the heap
    * plus twice the number of marked nodes in the heap. 
    */
    public int potential() 
    {   int numberOfMarked = getSize() - getHeapNonMarked();
        return getNumOfTrees() + (2 * numberOfMarked); // should be replaced by student code
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the
    * run-time of the program. A link operation is the operation which gets as input two
    * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
    * tree which has larger value in its root under the other tree.
    */
    public static int totalLinks()
    {    
    	return getLinks(); // should be replaced by student code
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the
    * run-time of the program. A cut operation is the operation which disconnects a subtree
    * from its parent (during decreaseKey/delete methods). 
    */
    public static int totalCuts()
    {    
    	return getCuts(); // should be replaced by student code
    }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
    * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
    *  
    * ###CRITICAL### : you are NOT allowed to change H. 
    */
    public static int[] kMin(FibonacciHeap H, int k)
    {   
    	int[] arr = new int[k];
    	
    	//if k==0, no keys should return
    	if (k==0)
    		return arr; 
    	
    	//if we have an empty heap, no keys should return
    	if (H.isEmpty())
    		return arr; 
    	
    	//initializing a heap that contains the min candidates
    	FibonacciHeap candidateHeap = new FibonacciHeap();
    	
    	//finding the key with the smallest value
    	HeapNode curMin = H.findMin();
    	
    	//inserting a node with the same key value to the new heap
    	HeapNode newMin = candidateHeap.insert(curMin.getKey());
    	
    	//connecting the 2 nodes
    	newMin.setpointerKMin(curMin);
    	
    	//iterating over the keys, deleting a key once inserted in arr 
    	for(int i = 0; i < k; i++) {
    		arr[i] = curMin.getKey();
    		candidateHeap.deleteMin();

    		//inserting the child of the just deleted parent, if it exists	
    		if (curMin.getChild() != null){
    			HeapNode childOfCurMin = curMin.getChild();
    			HeapNode pointerForChild = childOfCurMin.getNext();
    			while (pointerForChild != null) {
    				HeapNode curadded = candidateHeap.insert(pointerForChild.getKey());
    				
    				//connecting between the nodes
    				curadded.setpointerKMin(pointerForChild);
    				if(pointerForChild.getNext() == pointerForChild) {
    					break;
    				}
    				if(pointerForChild.getNext()== childOfCurMin) {
    					break;
    				}
    				pointerForChild = pointerForChild.getNext();
    				
    			}	
    		}
    		curMin = candidateHeap.findMin().getpointerKMin(); //updating curmin 
    		
    	}
   	
        return arr;
    	
    }  
    
   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in another file. 
    *  
    */
    public static class HeapNode{

    	public int key;
    	private int rank = 0;
    	private boolean mark = false;
    	private HeapNode child = null;
    	private HeapNode next = null;
    	private HeapNode prev = null;
    	private HeapNode parent = null;
    	private HeapNode pointerKmin = null;
    	
    	public HeapNode(int key) {
    		this.key = key;
    	}
    	public HeapNode() {}
    	
    	public int getKey() {
    		return this.key;
    	}
    	public int getRank() {
    		return this.rank;
    	}
    	public boolean getMark() {
    		return this.mark;
    	}
    	public HeapNode getChild() {
    		return this.child;
    	}
    	public HeapNode getNext() {
    		return this.next;
    	}
    	public HeapNode getPrev() {
    		return this.prev;
    	}
    	public HeapNode getParent() {
    		return this.parent;
    	}
    	public HeapNode getpointerKMin() {
    		return this.pointerKmin;
    	}
    	
    	
    	public void setKey(int key) {
    		this.key = key;
    	}
    	public void setRank(int rank) {
    		this.rank = rank;
    	}
    	public void setMark(boolean mark) {
    		this.mark = mark;
    	}
    	public void setChild(HeapNode child) {
    		this.child = child;
    	}
    	public void setNext(HeapNode next) {
    		this.next = next;
    	}
    	public void setPrev(HeapNode prev) {
    		this.prev = prev;
    	}
    	public void setParent(HeapNode parent) {
    		this.parent = parent;
    	}
    	public void setpointerKMin(HeapNode node) {
    		this.pointerKmin = node;
    	}
    }
}
