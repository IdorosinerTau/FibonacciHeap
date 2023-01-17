/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap
{
	private HeapNode min = null;
	private HeapNode first = null;
	private int size = 0;
	private int numOfTrees = 0;
	private static int cuts = 0;
	private static int links = 0;
	private static int nonMarked = 0;
	
	public FibonacciHeap() {}
	
	public FibonacciHeap(HeapNode node) {
		this.min = node;
		this.first = node;
		this.numOfTrees = 1;
		this.size = (int) Math.pow(2, node.getRank());
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
	
	
	public int findNumOfTrees() {
		//An empty heap has 0 trees
		if (isEmpty())
    		return 0;
    	
    	HeapNode node = getFirst();
    	int numOfTrees = 1;
    	//counting the number of trees until we return to the same one
    	while (node.getNext() != getFirst()) {
    		node = node.getNext();
    		numOfTrees += 1;
    	}
    	return numOfTrees;
	}
	
   /**
    * public boolean isEmpty()
    *
    * Returns true if and only if the heap is empty.
    *   
    */
    public boolean isEmpty()
    {
    	return getSize() == 0; // if the size is 0 the heap is empty
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
	    	newNode.setNext(getFirst());
	    	newNode.setPrev(getFirst().getPrev());
	    	getFirst().getPrev().setNext(newNode);
	    	getFirst().setPrev(newNode);
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
    	return newNode; // should be replaced by student code
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
     	FibonacciHeap new_fibTree = consolidate();
     	setFirst(new_fibTree.getFirst());
     	setNumOfTrees(new_fibTree.getNumOfTrees());
    	return; // should be replaced by student code
     	
    }

    private void removeMin() {
    	if (getMin().getChild() == null) {
    		getMin().getPrev().setNext(getMin().getNext());
    		getMin().getNext().setPrev(getMin().getPrev());
    	}
    	
    	else {
    		getMin().getPrev().setNext(getMin().getChild());
    		getMin().getChild().getPrev().setNext(getMin().getNext());
    		getMin().getNext().setPrev(getMin().getChild().getPrev());
    		getMin().getChild().setPrev(getMin().getPrev());
    	}
    	
    	if (getMin() == getFirst()) {
    		if (getFirst() == getFirst().getNext())
    			setFirst(getFirst().getChild());
    		else
    			setFirst(getFirst().getNext());
    	}
    		
    	
    	HeapNode node = getFirst();
    	while (node.getNext() != getFirst()) {
    		node.setParent(null);
    		if (node.getMark()) {
    			node.setMark(false);
    			setNonMarked(getNonMarked() - 1);
    		}
    		node = node.getNext();
    	}
    	setMin(searchMin());
    	setSize(getSize() - 1);
    	setNonMarked(getNonMarked() - 1);
    }
    
    private HeapNode searchMin() {
    	//no min key if there are no keys
    	if (isEmpty())
    		return null;
    	//going over the parents, if we find a smaller value, saving it as the new minimum
    	HeapNode node = getFirst();
    	HeapNode minNode = node;
    	int minKey = node.getKey();
    	while (node.getNext() != getFirst()) {
    		node = node.getNext();
    		if (node.getKey() < minKey) {
    			minKey = node.getKey();
    			minNode = node;
    		}
    	}
    	return minNode;
    }
    
    private FibonacciHeap consolidate() {
    	HeapNode[] heapsArr = new HeapNode[(int) Math.ceil(Math.log(getSize())/Math.log(2))];
    	HeapNode node = getFirst();
    	while (node.getNext() != getFirst()) {
    		if (heapsArr[node.getRank()] != null) {
    			HeapNode new_binTree = link(node,heapsArr[node.getRank()]);
    			heapsArr[node.getRank()] = null;
    			while (heapsArr[new_binTree.getRank()] != null) {
    				int index = new_binTree.getRank();
    				new_binTree = link(new_binTree,heapsArr[new_binTree.getRank()]);
        			heapsArr[index] = null;
    			}
    			heapsArr[new_binTree.getRank()] = new_binTree;
    		}
    		else {
    			heapsArr[node.getRank()] = node;
    		}
    		node = node.getNext();
    	}
    	
    	FibonacciHeap new_fibTree = new FibonacciHeap();
    	for (HeapNode binTree : heapsArr) {
    		if (binTree != null)
    			if (new_fibTree.isEmpty())
    				new_fibTree.setFirst(binTree);
    			new_fibTree.meld(new FibonacciHeap(binTree));
    	}
    	new_fibTree.setMin(getMin());
    	new_fibTree.setSize(getSize());
    	new_fibTree.setNumOfTrees(new_fibTree.findNumOfTrees());
    	return new_fibTree;
    }
    
    
    
    private HeapNode link(HeapNode x, HeapNode y) {
    	if (x.getKey() > y.getKey()) {
    		HeapNode tmp = x;
    		x = y;
    		y = tmp;
    	}
    	
    	y.getPrev().setNext(y.getNext());
		y.getNext().setPrev(y.getPrev());
		
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
		y.setParent(x);
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
    	return getMin();// should be replaced by student code
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Melds heap2 with the current heap.
    *
    */
    public void meld (FibonacciHeap heap2)
    {
    	if (isEmpty()) {
    		setMin(heap2.getMin());
    		setFirst(heap2.getFirst());
    		setSize(heap2.getSize());
    		setNumOfTrees(heap2.getNumOfTrees());
    		return;
    	}
    	getFirst().getPrev().setNext(heap2.getFirst());
    	heap2.getFirst().getPrev().setNext(getFirst());
    	HeapNode heap2_last = heap2.getFirst().getPrev();
    	heap2.getFirst().setPrev(getFirst().getPrev());
    	getFirst().setPrev(heap2_last);
    	if (getMin().getKey() > heap2.getMin().getKey())
    		setMin(heap2.getMin());
    	setSize(getSize() + heap2.getSize());
    	setNumOfTrees(getNumOfTrees() + heap2.getNumOfTrees());
    	return; // should be replaced by student code   		
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
    	
    	int[] arr = new int[(int) Math.ceil(Math.log(getSize())/Math.log(2))];
    	
    	HeapNode node = this.first;
    	arr[node.getRank()]++;
    	while (node.getNext() != this.first) {
    		node = node.getNext();
    		arr[node.getRank()]++;
    	}
        return arr; //	 to be replaced by student code
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
    	
    	int hefresh = x.getKey() - this.min.getKey()+1;
    	this.decreaseKey(x, hefresh);
    	this.deleteMin();
    	return;
    }
   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta)
    {    
    	x.setKey(delta);
    	if (x.getParent().getKey() < x.key)
    		cascadeCut(x);
    	return; // should be replaced by student code
    }
    
    public void cascadeCut(HeapNode x) {
		x = cutAndInsertHeap(x);
		incCuts();
		if (x.getParent() != null) {
			if (x.getMark() == false) {
				x.getParent().setMark(true);
				setNonMarked(getNonMarked() - 1);
			}
			else
				cascadeCut(x);
		}
    }

    
    public HeapNode cutAndInsertHeap(HeapNode x) {
    	if (x.getParent().getChild() == x) {
    		x.getParent().setRank(x.getParent().getRank() - 1);
    		if (x.getNext() != x)
    			x.getParent().setChild(x.getNext());
    		else
    			x.getParent().setChild(null);
		}
    	HeapNode parent = x.getParent();
    	x.setParent(null);
    	if (x.getMark()) {
    		x.setMark(false);
    		setNonMarked(getNonMarked() - 1);
    	}
    	x.getNext().setPrev(x.getPrev());
    	x.getPrev().setNext(x.getNext());
    	x.setNext(getFirst());
    	x.setPrev(getFirst().getPrev());
    	getFirst().getPrev().setNext(x);
    	getFirst().setPrev(x);
    	if (getMin().getKey() > x.getKey())
    		setMin(x);
    	setFirst(x);
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
    {    
        return getNumOfTrees() + 2*(getSize() - getNonMarked()); // should be replaced by student code
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
    	boolean flag = true;
    	int[] arr = new int[k];
    	//if k==0, no keys should return
    	if (k==0) {
    		return arr; 
    	}
    	//if we have an empty heap, no keys should return
    	if (H.isEmpty()) {
    		return arr; 
    	}
    	//making a heap with the candidates keys
    	FibonacciHeap candidateHeap = new FibonacciHeap();
    	//finding the key with the smallest value
    	HeapNode curMin = H.findMin();
    	//inserting a node with the same key value to the new heap
    	HeapNode newMin = candidateHeap.insert(curMin.getKey());
    	//connecting the 2 nodes
    	newMin.setpointerKMin(curMin);
    	//running over the keys, deleting a key once inserted in arr 
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
    	
    	
    	
    	
    	
    	
        return arr; // should be replaced by student code
    	
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
    	private HeapNode pointerKmin; //used in kMin, pointer towards the node with the same key in the other heap. 
    	
    
    	public HeapNode(int key) {
    		this.key = key;
    		this.pointerKmin = null; //for Kmin	
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
    		this.pointerKmin = node;		//setting a pointer
    	}
    	public HeapNode getpointerKMin() {
    		return this.pointerKmin;       //getting  the pointer
    	}
    	
    }
}
